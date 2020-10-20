package me.architetto.rivevent.event;


import com.destroystokyo.paper.Title;
import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.config.SettingsHandler;
import me.architetto.rivevent.event.eventTask.CountdownDeathRace;
import me.architetto.rivevent.util.ChatFormatter;
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
                sender.sendMessage(ChatFormatter.formatErrorMessage("there aren't enough participants to start this event!"));
            return;
        }

        cursedPlayer = Bukkit.getPlayer(EventService.getInstance().getAllPlayerEvent()
                .get(new Random().nextInt(EventService.getInstance().getAllPlayerEvent().size() - 1))) ;

        if (cursedPlayer == null) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Error: cursed player choise issue"));
            return;
        }

        curseEventFlag = true;

        cursedPlayer.sendTitle(  ChatColor.MAGIC + "Architetto" + ChatColor.RESET
                + ChatColor.RED + " cursed you!",ChatColor.ITALIC + "hit someone to pass the curse",20,120,20);
        cursedPlayer.getWorld().playSound(cursedPlayer.getLocation(), Sound.ENTITY_GHAST_HURT,5,2);

        cursedPlayer.spawnParticle(Particle.MOB_APPEARANCE,cursedPlayer.getLocation(),1,0,0,0);

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
                    p.sendTitle( ChatColor.DARK_RED + cursedPlayer.getDisplayName() + ChatColor.RESET +  "is dead!",
                            "The curse has been fulfilled!",20,60,20);

                }


            }
        }.runTaskLater(RIVevent.plugin,400L);
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


    public Player getLastPlayer() {
        EventService eventService = EventService.getInstance();
        Player lastPlayer = Bukkit.getPlayer(eventService.getParticipantsPlayers().get(0));
        for (UUID u : eventService.getParticipantsPlayers()) {
            if (lastPlayer.getLocation().getBlockY() < Bukkit.getPlayer(u).getLocation().getBlockY())
                lastPlayer = Bukkit.getPlayer(u);
        }
        return lastPlayer;
    }

    public void deathRaceRunnable() {
        SettingsHandler settingsHandler = SettingsHandler.getInstance();
        EventService eventService = EventService.getInstance();
        //VA SICURAMENTE RIVISTO

        CountdownDeathRace timer = new CountdownDeathRace(RIVevent.getPlugin(RIVevent.class),
                settingsHandler.deathRacePeriod,
                () -> {},
                () -> {

                    Player lastPlayer = getLastPlayer();
                    lastPlayer.setHealth(0);
                    lastPlayer.sendMessage(ChatFormatter.formatEventMessage("first rule of the" + ChatColor.YELLOW
                            + "'death reace club'" + ChatColor.RESET + " : don't be the lowest player"));

                },
                (t) -> {

                    if (eventService.isFinished())
                        Bukkit.getScheduler().cancelTask(taskIDs.get("DEATHRACE"));

                    if(t.getSecondsLeft() <= 10) {

                        for (UUID u : eventService.getParticipantsPlayers()) {
                            Player p = Bukkit.getPlayer(u);
                            p.sendTitle(new Title(String.valueOf(t.getSecondsLeft()), "", 1, 18, 1));
                        }

                    }
                });
        timer.scheduleTimer();
        taskIDs.put("DEATHRACE",timer.getTaskId());

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

        this.deathRaceEventFlag = false;
        this.curseEventFlag = false;

    }






}
