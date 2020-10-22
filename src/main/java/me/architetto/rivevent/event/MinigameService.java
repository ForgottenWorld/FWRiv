package me.architetto.rivevent.event;


import com.destroystokyo.paper.Title;
import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.config.SettingsHandler;
import me.architetto.rivevent.event.eventTask.CountdownMinigame;
import me.architetto.rivevent.util.ChatFormatter;
import me.architetto.rivevent.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class MinigameService{

    private static MinigameService miniGameService;

    private HashMap<String,Integer> taskIDs; //todo: enum invece di striga..

    private Player cursedPlayer;

    private boolean curseEventFlag = false;
    private boolean deathRaceEventFlag = false;


    private MinigameService() {

        if(miniGameService != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

        this.taskIDs = new HashMap<>();

    }

    public static MinigameService getInstance() {

        if(miniGameService == null) {
            miniGameService = new MinigameService();
        }

        return miniGameService;

    }

    public boolean isUniqueMiniGameRunning() {
        return curseEventFlag || deathRaceEventFlag;
    }

    public boolean isCurseEventRunning() {
        return curseEventFlag;
    }
    public Player getCursedPlayer() {
        return cursedPlayer;
    }
    public void setCursedPlayer(Player cursedPlayer) {
        this.cursedPlayer = cursedPlayer;
    }

    // - CURSE EVENT - //

    public void startCurseEvent(Player sender) {
        EventService eventService = EventService.getInstance();

        if (eventService.getParticipantsPlayers().size() < 2) {
            if (sender != null)
                sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.NOT_ENOUGH_PLAYERS));
            return;
        }

        cursedPlayer = Bukkit.getPlayer(EventService.getInstance().getAllPlayerEvent()
                .get(new Random().nextInt(EventService.getInstance().getAllPlayerEvent().size() - 1))) ;

        if (cursedPlayer == null) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Error: cursed player choise issue"));
            return;
        }

        curseEventFlag = true;

        cursedPlayer.sendTitle(  Messages.CURSED_PLAYER_START_TITLE,Messages.CURSED_PLAYER_START_SUBTITLE,20,120,20);
        cursedPlayer.getWorld().playSound(cursedPlayer.getLocation(), Sound.ENTITY_GHAST_HURT,5,2);

        cursedPlayer.spawnParticle(Particle.MOB_APPEARANCE,cursedPlayer.getLocation(),1,0,0,0);

        for (UUID u : eventService.getParticipantsPlayers()) {

            Player p = Bukkit.getPlayer(u);

            if (p != null && p != cursedPlayer)
                p.sendMessage(ChatFormatter.formatEventMessage(Messages.NOT_CURSED_PLAYER_ALLERT));

        }

        curseEventTask();

    }

    public void curseEventTask() {

        EventService eventService = EventService.getInstance();
        BukkitTask bukkitTask = new BukkitRunnable() {

            @Override
            public void run(){

                curseEventFlag = false;

                cursedPlayer.setHealth(0); //Il totem non deve attivarsi in questo caso ?
                cursedPlayer.sendMessage(ChatFormatter.formatEventMessage(Messages.CURSE_MSG3));

                for (UUID u : eventService.getParticipantsPlayers()) {

                    Player p = Bukkit.getPlayer(u);
                    if (p != null){
                        p.sendMessage(ChatFormatter.formatEventMessage(Messages.CURSED_PLAYER_DIE));
                        p.playSound(p.getLocation(),Sound.ENTITY_PARROT_IMITATE_WITCH,1,1); //todo
                    }

                }
                taskIDs.remove("CURSE");


            }
        }.runTaskLater(RIVevent.plugin,SettingsHandler.getInstance().cursePeriod);
        taskIDs.put("CURSE",bukkitTask.getTaskId());

    }

    // - DEATH RACE - //

    public void startDeathRaceEvent(Player sender) {
        EventService eventService = EventService.getInstance();

        if (eventService.getParticipantsPlayers().size() < 2) {
            if (sender != null)
                sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.NOT_ENOUGH_PLAYERS));
            return;
        }

        deathRaceEventFlag = true;

        for (UUID u : eventService.getParticipantsPlayers()) {

            Player p  = Bukkit.getPlayer(u);
            if (p == null)
                return;

            p.sendMessage(ChatFormatter.formatEventMessage(Messages.DEATHRACE_START_MSG));
            p.sendTitle("" ,ChatColor.RED + "DEATH RACE EVENT",20,60,20);

        }

        deathRaceRunnable();

    }

    public Player getLowestPlayer() { //todo testare il fix
        EventService eventService = EventService.getInstance();
        Player lowestPlayer = Bukkit.getPlayer(eventService.getParticipantsPlayers().get(0));
        for (UUID u : eventService.getParticipantsPlayers()) {
            if (lowestPlayer.getLocation().getBlockY() > Bukkit.getPlayer(u).getLocation().getBlockY())
                lowestPlayer = Bukkit.getPlayer(u);
        }
        return lowestPlayer;
    }

    public void deathRaceRunnable() {
        SettingsHandler settingsHandler = SettingsHandler.getInstance();
        EventService eventService = EventService.getInstance();

        CountdownMinigame timer = new CountdownMinigame(RIVevent.getPlugin(RIVevent.class),
                settingsHandler.deathRacePeriod,
                () -> {},
                () -> {

                    Player lastPlayer = getLowestPlayer();
                    lastPlayer.setHealth(0); //IL totem non deve attivarsi in questo caso ?
                    lastPlayer.sendMessage(ChatFormatter.formatEventMessage(Messages.DEATHRACE_DEATH_MSG));

                },
                (t) -> {

                    if (eventService.isFinished()) {
                        Bukkit.getScheduler().cancelTask(taskIDs.get("DEATHRACE"));
                        taskIDs.remove("DEATHRACE");
                        deathRaceEventFlag = false;
                        return;
                    }

                    if(t.getSecondsLeft() <= 10) {

                        for (UUID u : eventService.getParticipantsPlayers()) {
                            Player p = Bukkit.getPlayer(u);
                            if (p != null)
                                p.sendTitle(new Title(String.valueOf(t.getSecondsLeft()), "", 1, 18, 1));
                        }

                    }
                });
        timer.scheduleTimer();
        taskIDs.put("DEATHRACE",timer.getTaskId());

    }


    // - - - - - - - - //

    public void stopMiniGameTask() {

        if (taskIDs.isEmpty())
            return;

        for (String s : taskIDs.keySet())
            Bukkit.getScheduler().cancelTask(taskIDs.get(s));

        this.deathRaceEventFlag = false;
        this.curseEventFlag = false;

    }






}
