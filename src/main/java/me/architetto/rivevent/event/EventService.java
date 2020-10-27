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
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EventService {

    private static EventService eventService;

    private Arena summonedArena;
    private int spawnID;


    private List<UUID> playersIN;
    private List<UUID> playersOUT;

    public List<Block> spawnDoorsList;

    private boolean isRunning = false;
    private boolean isStarted = false;
    private boolean isFinished = false;

    private boolean isDamageEnabled = false;

    private EventService() {
        if(eventService != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

        this.spawnDoorsList = new ArrayList<>();
        this.playersIN = new ArrayList<>();
        this.playersOUT = new ArrayList<>();
        this.spawnID = 0;

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
        return this.summonedArena;
    }

    public List<UUID> getPlayerIN() {
        return new ArrayList<>(playersIN);
    }
    public void addPartecipant(UUID player) {
        playersIN.add(player);
    }

    public List<UUID> getPlayerOUT() {
        return new ArrayList<>(playersOUT);
    }
    public void addPlayerOUT(UUID player) {
        playersOUT.add(player);
    }
    public void removePlayerOUT(UUID u) {
        playersOUT.remove(u);
    }

    public boolean isRunning() {
        return isRunning;
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

    public boolean isDamageEnabled() { return this.isDamageEnabled; }

    public List<UUID> getEventPlayerList() {

        ArrayList<UUID> list = new ArrayList<>(getPlayerIN());
        list.addAll(getPlayerOUT());

        return list;
    }

    public boolean isPartecipant(UUID u) {   //todo da implementare nel codice
        return playersIN.contains(u);
    }

    public void teleportToSpawnPoint(Player player) {

        switch(spawnID) {
            case 0:
                player.teleport(summonedArena.getSpawn1());
                spawnID = 1;
                break;
            case 1:
                player.teleport(summonedArena.getSpawn2());
                spawnID = 2;
                break;
            case 2:
                player.teleport(summonedArena.getSpawn3());
                spawnID = 3;
                break;
            case 3:
                player.teleport(summonedArena.getSpawn4());
                spawnID = 0;
        }

    }

    public void startEventTimer() {

        Countdown timer = new Countdown(RIVevent.getPlugin(RIVevent.class),
                10,
                () -> {

                    String matchMessage = ChatFormatter.formatInitializationMessage(Messages.START_CD_MSG);
                    setStarted(true);


                    for (UUID u : getPlayerIN()) {
                        Player player = Bukkit.getPlayer(u);
                        if (player != null) {
                            player.sendMessage(matchMessage);
                            equipLoadout(player); //clear inventory,max health, max food, remove potion effects, set loadout

                        }

                    }

                },
                () -> {

                    for (UUID u : getPlayerIN()) {
                        Player p = Bukkit.getPlayer(u);
                        p.sendTitle(new Title(ChatColor.RED + "GO !!!", "", 1, 18, 1));

                    }

                    setDoorsStatus(true);

                    isDamageEnabled = true;
                    AntiCamperService.getInstance().startAntiCamperSystem();
                    RewardService.getInstance().startRewardSystem();


                },
                (t) -> {
                    if( t.getSecondsLeft() <= 5) {

                        for (UUID u : getPlayerIN()) {
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

        playersIN.remove(u);
        playersOUT.add(u);
        Player removedPlayer = Bukkit.getPlayer(u);

        if (removedPlayer != null){
            removedPlayer.getInventory().clear();
        }

        if (!isStarted)
            return;

        if (playersIN.size() == 1) {
            Player winner = Bukkit.getPlayer(getPlayerIN().get(0));
            for (UUID uuid : getEventPlayerList()) {

                Player partecipants = Bukkit.getPlayer(uuid);
                partecipants.sendTitle("VINCITORE : " + ChatColor.GOLD + winner.getDisplayName(),
                        "",20,100,20);
            }

            victoryFireworksEffect(2);
            AntiCamperService.getInstance().stopAnticamperTasks();
            RewardService.getInstance().stopRewardTask();
            MinigameService.getInstance().stopMiniGameTask();
            isFinished = true;

            return;
        }

        if (playersIN.isEmpty() && !isFinished) {


            for (UUID uuid : getEventPlayerList()) {
                Player p = Bukkit.getPlayer(uuid);
                p.sendMessage(ChatFormatter.formatSuccessMessage("WTF :( ?"));
            }

            AntiCamperService.getInstance().stopAnticamperTasks();
            RewardService.getInstance().stopRewardTask();
            MinigameService.getInstance().stopMiniGameTask();

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
        isDamageEnabled = false;

        playersIN.addAll(playersOUT);
        playersOUT.clear();

        for (UUID u : getPlayerIN()) {

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
        MinigameService.getInstance().stopMiniGameTask();


    }

    public void stopEvent() {

        isRunning = false;

        if (isStarted) {
            AntiCamperService.getInstance().stopAnticamperTasks();
            RewardService.getInstance().stopRewardTask();
            if (MinigameService.getInstance().isUniqueMiniGameRunning())
                MinigameService.getInstance().stopMiniGameTask();
            isStarted = false;
        }

        isFinished = false;
        isDamageEnabled = false;

        this.playersIN.clear();
        this.playersOUT.clear();

    }

    public static void victoryFireworksEffect(int amount) {

        new BukkitRunnable() {
            int cicle = 0;
            Location loc = EventService.getInstance().summonedArena.getTower().clone().add(0,3,0); //todo

            @Override
            public void run() {

                Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
                FireworkMeta fwm = fw.getFireworkMeta();

                fwm.setPower(1);
                fwm.addEffect(FireworkEffect.builder().flicker(true).trail(true)
                        .with(FireworkEffect.Type.BALL).with(FireworkEffect.Type.BALL_LARGE)
                        .with(FireworkEffect.Type.STAR).withColor(Color.ORANGE)
                        .withColor(Color.YELLOW).withFade(Color.PURPLE).withFade(Color.RED).build());

                fw.setFireworkMeta(fwm);

                for (int i = 0;i<amount; i++) {

                    Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
                    fw2.setFireworkMeta(fwm);
                }

                cicle++;

                if (cicle == 4)
                    this.cancel();

            }
        }.runTaskTimer(RIVevent.plugin,20,100);

    }

    public void sendMessages(String s,List<UUID> uuidPlayersList) {

        for (UUID uuid : uuidPlayersList) {

            Player p = Bukkit.getPlayer(uuid);

            if (p !=  null)
                p.sendMessage(ChatFormatter.formatSuccessMessage(s));
        }


    }

    public void sendTitle(String title,String subtitle,int inTime,int stayTime,int outTime,List<UUID> uuidPlayersList) {

        for (UUID uuid : uuidPlayersList) {

            Player p = Bukkit.getPlayer(uuid);

            if (p !=  null)
                p.sendTitle(title,subtitle,inTime,stayTime,outTime);
        }


    }



}
