package me.architetto.fwriv.event.service;

import me.architetto.fwriv.FWRiv;
import me.architetto.fwriv.config.SettingsHandler;
import me.architetto.fwriv.event.PartecipantsManager;
import me.architetto.fwriv.partecipant.PartecipantStatus;
import me.architetto.fwriv.utils.ChatFormatter;
import org.bukkit.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AntiCamperService {

    private static AntiCamperService antiCamperService;

    private List<Integer> taskID;

    private int acDelay;
    private int acDamage;
    private int acFinalDamage;

    private int acY;
    private int acFinalY;
    private int acGrowValue;
    private int acGrowPerion;
    private List<Location> particleEffectPoint;

    private int redCircleRadius;


    private AntiCamperService(){
        if(antiCamperService != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

        this.taskID = new ArrayList<>();
        this.particleEffectPoint = new ArrayList<>();

    }

    public static AntiCamperService getInstance() {
        if(antiCamperService == null) {
            antiCamperService = new AntiCamperService();
        }
        return antiCamperService;
    }

    public void startAntiCamperSystem() {
        SettingsHandler settingsHandler = SettingsHandler.getSettingsHandler();
        EventService eventService = EventService.getInstance();


        this.acDelay = settingsHandler.antiCamperStartDelay;
        this.acDamage = settingsHandler.antiCamperDamage;
        this.acFinalDamage = settingsHandler.antiCamperFinalDamage;
        this.acGrowPerion = settingsHandler.antiCamperGrowPeriod;
        this.acGrowValue = settingsHandler.antiCamperGrowValue;
        this.acY = eventService.getArena().getLowestY();
        this.acFinalY = eventService.getArena().getTower().getBlockY() - settingsHandler.antiCamperRedLineTopTowerDif;
        this.redCircleRadius = settingsHandler.redLineAnimationRadius;

        redLineManager();
        checkPlayersPosition();
        redLineParticleEffect();

    }

    private void redLineManager() {
        EventService eventService = EventService.getInstance();
        BukkitTask bukkitTask =  new BukkitRunnable() {
            @Override
            public void run() {

                acY += acGrowValue;

                if (acY >= acFinalY) {
                    eventService.getArena().getTower().getWorld().playSound(eventService.getArena().getTower(),Sound.ENTITY_LIGHTNING_BOLT_THUNDER,1,1);
                    taskID.remove((Integer) this.getTaskId());
                    acDamage = acFinalDamage;
                    acY = acFinalY;

                    PartecipantsManager.getInstance().getPartecipantsUUID(PartecipantStatus.PLAYING).stream()
                            .map(Bukkit::getPlayer)
                            .filter(Objects::nonNull)
                            .forEach(p -> p.sendMessage(ChatFormatter.formatEventAllert(ChatColor.RED + "!! WARNING !!"
                                    + ChatColor.YELLOW + " Il danno dell'anticamper è aumentato ")));

                    this.cancel();

                }

                particleEffectPoint = getCircle(50);

                PartecipantsManager.getInstance().getPartecipantsUUID(PartecipantStatus.PLAYING).stream()
                        .map(Bukkit::getPlayer)
                        .filter(Objects::nonNull)
                        .forEach(p -> p.sendMessage(ChatFormatter.formatEventAllert("AntiCamper : altezza minima : "
                                + ChatColor.GOLD + acY)));

            }
        }.runTaskTimer(FWRiv.plugin, acDelay, acGrowPerion);
        taskID.add(bukkitTask.getTaskId());


    }

    private void checkPlayersPosition() {

        BukkitTask bukkitTask =  new BukkitRunnable() {
            @Override
            public void run() {

                PartecipantsManager.getInstance().getPartecipantsUUID(PartecipantStatus.PLAYING).stream()
                        .map(Bukkit::getPlayer)
                        .filter(Objects::nonNull)
                        .filter(p -> p.getLocation().getBlockY() < acY)
                        .forEach(p -> p.damage(acDamage));

            }
        }.runTaskTimer(FWRiv.plugin, acDelay,20L);
        taskID.add(bukkitTask.getTaskId());

    }

    private void redLineParticleEffect() {

        BukkitTask bukkitTask = new BukkitRunnable() {

            final Particle.DustOptions dustOptions = new Particle.DustOptions(org.bukkit.Color.fromRGB(200, 0, 0),10);
            final Particle.DustOptions dustOptions2 = new Particle.DustOptions(org.bukkit.Color.fromRGB(255, 255, 51),2);
           boolean deathLineAnimation = false;

            @Override
            public void run(){

                for (Location loc : particleEffectPoint) {

                    if (deathLineAnimation)
                        loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 2, dustOptions);
                    else
                        loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 2, dustOptions2);

                    deathLineAnimation= !deathLineAnimation;

                }
            }
        }.runTaskTimer(FWRiv.plugin, acDelay,10);
        taskID.add(bukkitTask.getTaskId());

    }

    public ArrayList<Location> getCircle(int amount) {
        Location redCircleMidLocation = EventService.getInstance().getArena().getTower().clone();
        redCircleMidLocation.setY(acY);

        World world = redCircleMidLocation.getWorld();

        double increment = (2 * Math.PI) / amount;
        ArrayList<Location> locations = new ArrayList<>();

        for(int i = 0;i < amount; i++) {

            double angle = i * increment;
            double x = redCircleMidLocation.getX() + (redCircleRadius * Math.cos(angle));
            double z = redCircleMidLocation.getZ() + (redCircleRadius * Math.sin(angle));
            locations.add(new Location(world, x, redCircleMidLocation.getY(), z));

        }

        return locations;
    }


    public void stopAnticamperTasks() {
        for (int id : taskID) {
            Bukkit.getScheduler().cancelTask(id);
        }
    }

}
