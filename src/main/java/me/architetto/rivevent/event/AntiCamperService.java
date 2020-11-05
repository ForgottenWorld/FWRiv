package me.architetto.rivevent.event;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.config.SettingsHandler;
import me.architetto.rivevent.util.ChatFormatter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AntiCamperService {

    private static AntiCamperService antiCamperService;

    private List<Integer> taskID;

    private int antiCamperStartDelay;
    private int antiCamperDamagePeriod;
    private int antiCamperDamage;
    private int antiCamperDamageBoost;

    private int redLineValue;
    private int redLineFinalValue;
    private int redLineGrowValue;
    private int redLineGrowPeriod;
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
        SettingsHandler settingsHandler = SettingsHandler.getInstance();
        EventService eventService = EventService.getInstance();


        this.antiCamperStartDelay = settingsHandler.antiCamperStartDelay;
        this.antiCamperDamagePeriod = settingsHandler.antiCamperDamagePeriod;
        this.antiCamperDamage = settingsHandler.antiCamperDamage;
        this.antiCamperDamageBoost = settingsHandler.antiCamperDamageBoost;
        this.redLineGrowPeriod = settingsHandler.antiCamperGrowPeriod;
        this.redLineGrowValue = settingsHandler.antiCamperGrowValue;
        this.redLineValue = eventService.getSummonedArena().getLowestY();
        this.redLineFinalValue = eventService.getSummonedArena().getTower().getBlockY() - settingsHandler.antiCamperRedLineTopTowerDif;
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

                redLineValue += redLineGrowValue;

                if (redLineValue >= redLineFinalValue) {
                    eventService.getSummonedArena().getTower().getWorld().playSound(eventService.getSummonedArena().getTower(),Sound.ENTITY_LIGHTNING_BOLT_THUNDER,1,1);
                    taskID.remove((Integer) this.getTaskId());
                    antiCamperDamage += antiCamperDamageBoost;
                    redLineValue = redLineFinalValue;


                    for (UUID u : PlayersManager.getInstance().getActivePlayers()) {

                        Player player = Bukkit.getPlayer(u);
                        if (player != null)
                            player.sendMessage(ChatFormatter.formatEventAllert(ChatColor.RED + "!! WARNING !!"
                                    + ChatColor.YELLOW + " il danno dell'anticamper è aumentato "));

                    }

                    this.cancel();

                }

                particleEffectPoint = getCircle(50);

                for (UUID u : PlayersManager.getInstance().getActivePlayers()) {

                    Player player = Bukkit.getPlayer(u);
                    if (player != null)
                        player.sendMessage(ChatFormatter.formatEventAllert("AntiCamper : altezza minima : "
                                + ChatColor.GOLD + redLineValue));

                }
            }
        }.runTaskTimer(RIVevent.plugin,antiCamperStartDelay,redLineGrowPeriod);
        taskID.add(bukkitTask.getTaskId());


    }

    private void checkPlayersPosition() {

        BukkitTask bukkitTask =  new BukkitRunnable() {
            @Override
            public void run(){

                for (UUID u : PlayersManager.getInstance().getActivePlayers()) {
                    Player p = Bukkit.getPlayer(u);
                    if (p == null)
                        continue;

                    if (p.getLocation().getBlockY() < redLineValue)
                        p.damage(antiCamperDamage);

                }

            }
        }.runTaskTimer(RIVevent.plugin,antiCamperStartDelay,antiCamperDamagePeriod);
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
        }.runTaskTimer(RIVevent.plugin,antiCamperStartDelay,10);
        taskID.add(bukkitTask.getTaskId());

    }

    public ArrayList<Location> getCircle(int amount) {
        Location redCircleMidLocation = EventService.getInstance().getSummonedArena().getTower().clone();
        redCircleMidLocation.setY(redLineValue);

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
