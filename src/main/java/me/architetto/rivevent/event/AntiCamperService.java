package me.architetto.rivevent.event;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.config.SettingsHandler;
import me.architetto.rivevent.util.ChatFormatter;
import org.bukkit.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
                    taskID.remove((Integer) this.getTaskId());
                    antiCamperDamage += antiCamperDamageBoost;
                    redLineValue = redLineFinalValue;


                    for (UUID u : eventService.getParticipantsPlayers()) {

                        Objects.requireNonNull(Bukkit.getPlayer(u)).sendMessage(ChatFormatter
                                .formatEventAllert(ChatColor.RED + "!! WARNING !!" + ChatColor.YELLOW
                                        + " anti-camper damage boost activated "));

                    }

                    this.cancel();

                }

                particleEffectPoint = getCircle(12,40);

                for (UUID u : eventService.getParticipantsPlayers()) {

                    Objects.requireNonNull(Bukkit.getPlayer(u)).sendMessage(ChatFormatter
                            .formatEventAllert("AntiCamper allert: minimum height " + ChatColor.GOLD + redLineValue));

                }



            }
        }.runTaskTimer(RIVevent.plugin,antiCamperStartDelay,redLineGrowPeriod);
        taskID.add(bukkitTask.getTaskId());


    }

    private void checkPlayersPosition() {

        EventService eventService = EventService.getInstance();
        BukkitTask bukkitTask =  new BukkitRunnable() {
            @Override
            public void run(){

                for (UUID u : eventService.getParticipantsPlayers()) {

                    if (Objects.requireNonNull(Bukkit.getPlayer(u)).getLocation().getBlockY() < redLineValue)
                        Objects.requireNonNull(Bukkit.getPlayer(u)).damage(antiCamperDamage);

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

                /* OLD PARTICLE EFFECT SYSTEM

                location.setY(getRedLineValue());
                Block middle = location.getBlock();

                for (int x = 10; x >= -10; x--) {

                    for (int z = 10; z >= -10; z--) {

                        if (middle.getRelative(x, 0, z).getType().isAir()) {

                            if (z < 10 - 1 && z > -10 + 1 && x < 10 - 1 && x > -10 + 1)
                                continue;

                            if (deathLineAnimation)
                                location.getWorld().spawnParticle(Particle.REDSTONE, middle.getRelative(x, 0, z).getLocation(), 2, dustOptions);
                            else
                                location.getWorld().spawnParticle(Particle.REDSTONE, middle.getRelative(x, 0, z).getLocation(), 1, dustOptions2);


                            if (redLineValue >= startRedLine + 3)
                                location.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, middle.getRelative(x, -1, z).getLocation(), 0, 0,-0.02,0);

                        }
                    }
                }

                deathLineAnimation= !deathLineAnimation;

                */


            }
        }.runTaskTimer(RIVevent.plugin,antiCamperStartDelay,10);
        taskID.add(bukkitTask.getTaskId());

    }

    public ArrayList<Location> getCircle(double radius, int amount) {
        Location redCircleMidLocation = EventService.getInstance().getSummonedArena().getTower();
        redCircleMidLocation.setY(redLineValue);

        World world = redCircleMidLocation.getWorld();

        double increment = (2 * Math.PI) / amount;
        ArrayList<Location> locations = new ArrayList<>();

        for(int i = 0;i < amount; i++) {

            double angle = i * increment;
            double x = redCircleMidLocation.getX() + (radius * Math.cos(angle));
            double z = redCircleMidLocation.getZ() + (radius * Math.sin(angle));
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
