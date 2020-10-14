package me.architetto.rivevent.event;


import com.destroystokyo.paper.Title;
import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.config.SettingsHandler;
import me.architetto.rivevent.event.eventTask.StartCountdown;
import me.architetto.rivevent.util.ChatFormatter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.UUID;

public class MiniGameService {

    private static MiniGameService miniGameService;

    private HashMap<String,Integer> taskIDs; //todo: enum invece di striga..

    private Player cursedPlayer;

    private boolean curseEventFlag = false;
    private boolean deathRaceEventFlag = false;


    private MiniGameService() {

        if(miniGameService != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

        this.taskIDs = new HashMap<>();

    }

    public static MiniGameService getInstance() {

        if(miniGameService == null) {
            miniGameService = new MiniGameService();
        }

        return miniGameService;

    }

    public boolean isMiniGameRunning() {
        return curseEventFlag || deathRaceEventFlag;
    }

    // - CURSE EVENT - //

    public void startCurseEvent(Player sender) {
        EventService eventService = EventService.getInstance();

        if (eventService.getParticipantsPlayers().size() < 2) {
            if (sender != null)
                sender.sendMessage(ChatFormatter.formatErrorMessage("there aren't enough participants to start this event!"));
            return;
        }

        SecureRandom random = new SecureRandom();
        int randomPlayerIndex = random.nextInt(eventService.getParticipantsPlayers().size() - 1);
        cursedPlayer = Bukkit.getPlayer(eventService.getParticipantsPlayers().get(randomPlayerIndex));

        if (cursedPlayer == null){
            sender.sendMessage(ChatFormatter.formatErrorMessage("Error: cursed player choise issue"));
            return;
        }

        curseEventFlag = true;

        cursedPlayer.sendTitle(  ChatColor.MAGIC + "Architetto" + ChatColor.RESET
                + ChatColor.RED + " cursed you!",ChatColor.ITALIC + "hit someone to pass the curse",20,120,20);
        cursedPlayer.getWorld().playSound(cursedPlayer.getLocation(), Sound.ENTITY_GHAST_HURT,5,2);

        for (UUID u : eventService.getParticipantsPlayers()) {

            Player p = Bukkit.getPlayer(u);

            if (p != cursedPlayer)
                p.sendMessage(ChatFormatter.formatEventMessage(ChatColor.DARK_RED + "!! WARNING !!"
                        + ChatColor.RESET + " one of the participants is cursed"));

        }

        curseEventTask();

    }

    public void curseEventTask() {

        EventService eventService = EventService.getInstance();
        BukkitTask bukkitTask = new BukkitRunnable() {

            @Override
            public void run(){

                curseEventFlag = false;

                cursedPlayer.damage(30);
                cursedPlayer.sendMessage(ChatFormatter.formatEventMessage("the curse has come upon you!"));

                for (UUID u : eventService.getParticipantsPlayers()) {

                    Player p = Bukkit.getPlayer(u);
                    p.sendTitle( ChatColor.DARK_RED + cursedPlayer.getDisplayName() + ChatColor.RESET +  "e' morto!",
                            "La maledizione si e' compiuta!",20,60,20);

                }


            }
        }.runTaskLater(RIVevent.plugin,2400L);
        taskIDs.put("CURSE",bukkitTask.getTaskId());

    }

    // - DEATH RACE - //

    public void startDeathRaceEvent(Player sender) {
        EventService eventService = EventService.getInstance();

        if (eventService.getParticipantsPlayers().size() < 2) {
            if (sender != null)
                sender.sendMessage(ChatFormatter.formatErrorMessage("there aren't enough participants to start this event!"));
            return;
        }

        deathRaceEventFlag = true;

        for (UUID u : eventService.getParticipantsPlayers()) {
            Player p  = Bukkit.getPlayer(u);
            p.sendMessage(ChatFormatter.formatEventMessage(ChatColor.YELLOW + "DEATH RACE" + ChatColor.RESET + ": every 60 seconds the lowest location player dies! "));
            p.sendTitle(ChatColor.YELLOW + "DEATH RACE EVENT", "",20,60,20);

        }

        deathRaceRunnable();


    }

    public void deathRaceRunnable() {
        SettingsHandler settingsHandler = SettingsHandler.getInstance();
        EventService eventService = EventService.getInstance();
        //VA SICURAMENTE RIVISTO
        BukkitTask bukkitTask = new BukkitRunnable() {

            @Override
            public void run(){

                StartCountdown timer = new StartCountdown(RIVevent.getPlugin(RIVevent.class),
                        settingsHandler.deathRacePeriod,
                        () -> {},
                        () -> {

                            if (eventService.getParticipantsPlayers().size() <= 1 ) {
                                this.cancel();
                                return;
                            }

                            Player lastPlayer = Bukkit.getPlayer(eventService.getParticipantsPlayers().get(0));
                            for (UUID u : eventService.getParticipantsPlayers()) {
                                if (lastPlayer.getLocation().getBlockY() < Bukkit.getPlayer(u).getLocation().getBlockY())
                                    lastPlayer = Bukkit.getPlayer(u);

                            }
                            lastPlayer.setHealth(0);
                            lastPlayer.sendMessage(ChatFormatter.formatEventMessage("first rule of the" + ChatColor.YELLOW
                                    + "'death reace club'" + ChatColor.RESET + " : don't be the lowest player"));


                        },
                        (t) -> {
                            if( t.getSecondsLeft() <= 10) {

                                if (eventService.getParticipantsPlayers().size() <= 1) {
                                    this.cancel();
                                    return;
                                }

                                for (UUID u : eventService.getParticipantsPlayers()) {
                                    Player p = Bukkit.getPlayer(u);
                                    p.sendTitle(new Title(String.valueOf(t.getSecondsLeft()), "", 1, 18, 1));
                                }

                            }
                        });
                timer.scheduleTimer();



                if (eventService.getParticipantsPlayers().size() <= 1) {
                    deathRaceEventFlag = false;
                    taskIDs.remove("DEATHRACE");
                    this.cancel();
                }


            }
        }.runTaskTimer(RIVevent.plugin,0,settingsHandler.deathRacePeriod);
        taskIDs.put("DEATHRACE",bukkitTask.getTaskId());

    }

    // - - - - - - - - //



    public void stopMiniGameTask(String s) {
        Bukkit.getScheduler().cancelTask(taskIDs.get(s));
    }

    public void stopMiniGameTask() {
        if (taskIDs.isEmpty())
            return;

        for (String s : taskIDs.keySet())
            Bukkit.getScheduler().cancelTask(taskIDs.get(s));

    }






}
