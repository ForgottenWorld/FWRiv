package me.architetto.rivevent.event;

import com.destroystokyo.paper.Title;
import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.arena.Arena;
import me.architetto.rivevent.config.SettingsHandler;
import me.architetto.rivevent.event.eventTask.Countdown;
import me.architetto.rivevent.util.ChatFormatter;
import me.architetto.rivevent.util.Messages;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EventService {

    private static EventService eventService;

    private Arena summonedArena;
    private int arenaSpawnID;

    private List<UUID> participantsPlayers;
    private List<UUID> eliminatedPlayers;

    public List<Block> spawnDoorsList;

    private boolean isRunning = false;
    private boolean isStarted = false;
    private boolean isFinished = false;

    private EventService() {
        if(eventService != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

        this.spawnDoorsList = new ArrayList<>();
        this.participantsPlayers = new ArrayList<>();
        this.eliminatedPlayers = new ArrayList<>();
        this.arenaSpawnID = 0;

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
    public void removeEliminated(UUID u) {
        eliminatedPlayers.remove(u);
    }

    public boolean isRunning() {
        return isRunning;
    }
    public void setRunning(boolean running) {
        isRunning = running;
    }

    public boolean isStarted() {
        return isStarted;
    }
    public void setStarted(boolean started) {
        isStarted = started;
    }

    public boolean isFinished() {
        return isFinished;
    }
    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public List<UUID> getAllPlayerEvent() {
        List<UUID> mergedList = new ArrayList<>(getParticipantsPlayers());
        mergedList.addAll(getEliminatedPlayers());
        return mergedList;
    }

    public void teleportToSpawnPoint(Player player) {

        switch(arenaSpawnID) {
            case 0:
                player.teleport(summonedArena.getSpawn1());
                arenaSpawnID = 1;
                break;
            case 1:
                player.teleport(summonedArena.getSpawn2());
                arenaSpawnID = 2;
                break;
            case 2:
                player.teleport(summonedArena.getSpawn3());
                arenaSpawnID = 3;
                break;
            case 3:
                player.teleport(summonedArena.getSpawn4());
                arenaSpawnID = 0;
        }

    }

    public void startEventTimer() {

        Countdown timer = new Countdown(RIVevent.getPlugin(RIVevent.class),
                10,
                () -> {

                    String matchMessage = ChatFormatter.formatInitializationMessage("the event will start in 10 seconds...");

                    for (UUID u : getParticipantsPlayers()) {
                        Player player = Bukkit.getPlayer(u);
                        if (player != null) {
                            player.sendMessage(matchMessage);
                            equipLoadout(player); //clear inventory,max health, max food, remove potion effects, set loadout

                        }

                    }

                },
                () -> {

                    for (UUID u : getParticipantsPlayers()) {
                        Player p = Bukkit.getPlayer(u);
                        p.sendTitle(new Title(ChatColor.RED + "GO !!!", "", 1, 18, 1));

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

    public void equipLoadout (Player player) {

        player.getInventory().clear();
        player.setHealth(20);
        player.setFoodLevel(20);

        for (PotionEffect potionEffect : player.getActivePotionEffects())
            player.removePotionEffect(potionEffect.getType());

        for (Material material : SettingsHandler.getInstance().startEquipItems.keySet())
            player.getInventory().addItem(new ItemStack(material,SettingsHandler.getInstance().startEquipItems.get(material)));

    }

    public void removePartecipant(UUID u) {

        participantsPlayers.remove(u);
        eliminatedPlayers.add(u);
        Player removedPlayer = Bukkit.getPlayer(u);

        if (removedPlayer != null){
            /*
            removedPlayer.teleport(SettingsHandler.getInstance().respawnLocation);
            removedPlayer.playSound(removedPlayer.getLocation(),Sound.ENTITY_ENDERMAN_TELEPORT,1,1);

             */
            removedPlayer.getInventory().clear();
        }

        if (!isStarted)
            return;

        if (participantsPlayers.size() == 1) {
            Player winner = Bukkit.getPlayer(getParticipantsPlayers().get(0));
            for (UUID uuid : getAllPlayerEvent()) {

                Player partecipants = Bukkit.getPlayer(uuid);
                partecipants.sendTitle("VINCETORE : " + ChatColor.GOLD + winner.getDisplayName(),
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
        isStarted = false;
        isFinished = false;

        participantsPlayers.addAll(eliminatedPlayers);
        eliminatedPlayers.clear();

        for (UUID u : getParticipantsPlayers()) {

            Player player = Bukkit.getPlayer(u);
            if (player == null) continue;

            player.setGameMode(GameMode.SURVIVAL);
            teleportToSpawnPoint(player);
            player.playSound(player.getLocation(),Sound.ENTITY_ENDERMAN_TELEPORT,1,1);
            equipLoadout(player);

            player.sendMessage(ChatFormatter.formatSuccessMessage(Messages.RESTART_CMD_PLAYER_MESSAGE));

        }

        AntiCamperService.getInstance().stopAnticamperTasks();
        RewardService.getInstance().stopRewardTask();
        MiniGameService.getInstance().stopMiniGameTask();


    }

    public void stopEvent() {

        isRunning = false;

        if (isStarted) {
            AntiCamperService.getInstance().stopAnticamperTasks();
            RewardService.getInstance().stopRewardTask();
            MiniGameService.getInstance().stopMiniGameTask();
            isStarted = false;
        }

        isFinished = false;

        this.participantsPlayers.clear();
        this.eliminatedPlayers.clear();

    }



}
