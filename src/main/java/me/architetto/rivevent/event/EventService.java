package me.architetto.rivevent.event;

import com.destroystokyo.paper.Title;
import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.arena.Arena;
import me.architetto.rivevent.event.eventTask.StartCountdown;
import me.architetto.rivevent.util.ChatFormatter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EventService {

    private static EventService eventService;

    private Arena summonedArena;

    private List<UUID> participantsPlayers;
    private List<UUID> eliminatedPlayers;

    public List<Block> spawnDoorsList;

    private boolean isRunning = false;
    private boolean isSetupDone = false;
    private boolean isStarted = false;
    private boolean isFinished = false;

    private EventService() {
        if(eventService != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

        this.spawnDoorsList = new ArrayList<>();
        this.participantsPlayers = new ArrayList<>();
        this.eliminatedPlayers = new ArrayList<>();

    }

    public static EventService getInstance() {
        if(eventService == null) {
            eventService = new EventService();
        }
        return eventService;
    }

    public void initializeEvent(Arena arena) {
        this.summonedArena = arena;
        this.spawnDoorsList = arena.getSpawnDoors();
        this.isRunning = true;
        //chiude eventuali porte aperte
    }

    public Arena getSummonedArena() {
        return summonedArena;
    }
    public void setSummonedArena(Arena arena) {
        summonedArena = arena;
    }

    public List<UUID> getParticipantsPlayers() {
        return new ArrayList<>(participantsPlayers);
    }
    public void addPartecipant(UUID player) {
        participantsPlayers.add(player);
    }

    public List<UUID> getEliminatedPlayers() {
        return new ArrayList<>(eliminatedPlayers);
    }
    public void addEliminated(UUID player) {
        eliminatedPlayers.add(player);
    }

    public boolean isRunning() {
        return isRunning;
    }
    public void setRunning(boolean running) {
        isRunning = running;
    }

    public boolean isSetupDone() {
        return isSetupDone;
    }
    public void setSetupDone(boolean setupStatus) {
        isSetupDone = setupStatus;
    }

    public boolean isStarted() {
        return isStarted;
    }
    public void setStarted(boolean started) {
        isStarted = started;
    }

    public List<UUID> getAllPlayerEvent() {
        List<UUID> mergedList = new ArrayList<>(getParticipantsPlayers());
        mergedList.addAll(getEliminatedPlayers());
        return mergedList;
    }



    public void removePartecipant(UUID u) {

        participantsPlayers.remove(u);
        eliminatedPlayers.add(u);

        if (!isStarted)
            return;

        if (participantsPlayers.size() == 1) {
            Player winner = Bukkit.getPlayer(getParticipantsPlayers().get(0));
            for (UUID uuid : getAllPlayerEvent()) {
                Player p = Bukkit.getPlayer(uuid);
                p.sendTitle(ChatColor.GOLD + winner.getDisplayName(),
                        "",20,100,20);
            }
            AntiCamperService.getInstance().stopAnticamperTasks();
            RewardService.getInstance().stopRewardTask();
            MiniGameService.getInstance().stopMiniGameTask();
            isFinished = true;
            return;
        }

        if (participantsPlayers.isEmpty() && !isFinished) {


            for (UUID uuid : getAllPlayerEvent()) {
                Player p = Bukkit.getPlayer(uuid);
                p.sendMessage(ChatFormatter.formatSuccessMessage("No winner :( ?"));
            }

            AntiCamperService.getInstance().stopAnticamperTasks();
            RewardService.getInstance().stopRewardTask();
            MiniGameService.getInstance().stopMiniGameTask();
        }

    }

    public void setDoorsStatus(boolean status) {

        for(org.bukkit.block.Block block : spawnDoorsList){

            if (!Tag.DOORS.getValues().contains(block.getType())
                    && !Tag.FENCE_GATES.getValues().contains(block.getType())){
                continue;
            }

            BlockData data = block.getBlockData();
            Openable door = (Openable) data;
            door.setOpen(status);
            block.setBlockData(door, true);

        }

    }

    public void restartEvent() {

        isRunning = true;
        isSetupDone = false;
        isStarted = false;
        isFinished = false;

        participantsPlayers.addAll(eliminatedPlayers);
        eliminatedPlayers.clear();

        for (UUID u : getParticipantsPlayers()) {
            Player p = Bukkit.getPlayer(u);
            p.teleport(summonedArena.getSpectator());
            p.setGameMode(GameMode.SURVIVAL);
            p.getInventory().clear();
            p.sendMessage(ChatFormatter.formatSuccessMessage("a new event has been started. You have been added to the participants"));
            p.playSound(p.getLocation(),Sound.ENTITY_ENDERMAN_TELEPORT,1,1);
        }

        AntiCamperService.getInstance().stopAnticamperTasks();
        RewardService.getInstance().stopRewardTask();


    }

    public void stopEvent() {
        isRunning = false;
        isSetupDone = false;

        if (isStarted) {
            AntiCamperService.getInstance().stopAnticamperTasks();
            RewardService.getInstance().stopRewardTask();
            isStarted = false;
        }

        isFinished = false;

        participantsPlayers.clear();
        eliminatedPlayers.clear();



    }

    public void startEventTimer() {

        StartCountdown timer = new StartCountdown(RIVevent.getPlugin(RIVevent.class),
                10,
                () -> {

                    String matchMessage = ChatFormatter.formatInitializationMessage("the event will start in 10 seconds...");

                    for (UUID u : getParticipantsPlayers()) {
                        Player p = Bukkit.getPlayer(u);
                        p.sendMessage(matchMessage);
                    }

                },
                () -> {

                    for (UUID u : getParticipantsPlayers()) {
                        Player p = Bukkit.getPlayer(u);
                        p.sendTitle(new Title(ChatColor.DARK_RED + "GO!", "", 1, 18, 1));

                    }

                    setDoorsStatus(true);
                    setStarted(true);
                    AntiCamperService.getInstance().startAntiCamperSystem();
                    RewardService.getInstance().startRewardSystem();


                },
                (t) -> {
                    if( t.getSecondsLeft() <= 5) {

                        for (UUID u : getParticipantsPlayers()) {
                            Player p = Bukkit.getPlayer(u);
                            p.sendTitle(new Title(String.valueOf(t.getSecondsLeft()), "", 1, 18, 1));
                        }

                    }
                });
        timer.scheduleTimer();
    }


}
