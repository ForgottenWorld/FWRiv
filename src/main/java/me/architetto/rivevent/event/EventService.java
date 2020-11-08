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
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EventService {

    private static EventService eventService;

    private Arena summonedArena;
    private int spawnID;

    public List<Block> spawnDoorsList;

    private boolean isRunning = false;
    private boolean isStarted = false;
    private boolean isFinished = false;

    private boolean isDamageEnabled = false;
    private boolean isEarlyDamagePrank = false;

    private EventService() {
        if(eventService != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

        this.spawnDoorsList = new ArrayList<>();

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

    public void activePlayerDeath(UUID uuid) {
        PlayersManager playersManager = PlayersManager.getInstance();

        playersManager.removeActivePlayer(uuid);
        playersManager.addSpectatorPlayer(uuid);

        Player player = Bukkit.getPlayer(uuid);

        if (player != null) {

            player.getInventory().clear();
            player.setGameMode(GameMode.SPECTATOR);

        }

        if (isStarted && !isFinished)
            checkVictoryCondition();
    }

    public void activePlayerLeave(UUID uuid) {
        PlayersManager playersManager = PlayersManager.getInstance();

        playersManager.removeActivePlayer(uuid);

        Player player = Bukkit.getPlayer(uuid);

        if (player != null) {

            player.getInventory().clear();
            player.setGameMode(GameMode.SURVIVAL);

            player.teleport(playersManager.getReturnLocation(uuid));

        }

        playersManager.removeReturnLocation(uuid);

        if (isStarted && !isFinished)
            checkVictoryCondition();

    }

    public void spectatorPlayerLeave(UUID uuid) {
        PlayersManager playersManager = PlayersManager.getInstance();

        playersManager.removeSpectatorPlayer(uuid);

        Player player = Bukkit.getPlayer(uuid);

        if (player != null) {

            player.getInventory().clear();
            player.setGameMode(GameMode.SURVIVAL);

            player.teleport(playersManager.getReturnLocation(uuid));

        }

        playersManager.removeReturnLocation(uuid);

    }

    private void checkVictoryCondition() {
        PlayersManager playersManager = PlayersManager.getInstance();

        if (playersManager.getActivePlayers().size() == 1) {

            isFinished = true;

            Player playerWinner = Bukkit.getPlayer(playersManager.getActivePlayers().get(0));

            if (playerWinner != null) {
                victoryAnimation(playerWinner.getDisplayName());

            }

            victoryFireworksEffect(2);
            stopTasks();

        }

        if (playersManager.getActivePlayers().size() == 0 && !isFinished) {

            isFinished = true;

            stopTasks();

        }

    }

    private void victoryAnimation(String playerWinner) {
        PlayersManager playersManager = PlayersManager.getInstance();
        for (UUID uuid : playersManager.getPartecipants()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null)
                player.sendTitle(ChatColor.GOLD + playerWinner,
                        "E' IL VINCITORE DELL'EVENTO",20,100,20);

        }
    }

    public void stopTasks() {

        AntiCamperService.getInstance().stopAnticamperTasks();
        RewardService.getInstance().stopRewardTask();
        MinigameService.getInstance().stopMiniGameTask();

    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public boolean isDamageEnabled() { return this.isDamageEnabled; }

    public boolean isEarlyDamagePrank() { return this.isEarlyDamagePrank; }

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

                    this.isStarted = true;

                    for (UUID u : PlayersManager.getInstance().getActivePlayers()) {
                        Player player = Bukkit.getPlayer(u);
                        if (player == null)
                            continue;

                        player.sendMessage(ChatFormatter.formatInitializationMessage(Messages.START_CD_MSG));
                        equipLoadout(player); //clear inventory,max health, max food, remove potion effects, set loadout

                    }
                },
                () -> {

                    for (UUID u : PlayersManager.getInstance().getActivePlayers()) {
                        Player p = Bukkit.getPlayer(u);

                        if (p == null)
                            continue;

                        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,1200,2));


                        p.sendTitle(new Title(ChatColor.RED + "GO !!!", "", 1, 18, 1));

                    }

                    setDoorsStatus(true);

                    isDamageEnabled = true;
                    isEarlyDamagePrank = true;

                    Bukkit.getScheduler().scheduleSyncDelayedTask(RIVevent.plugin, () -> isEarlyDamagePrank = false, 600);

                    AntiCamperService.getInstance().startAntiCamperSystem();

                    RewardService.getInstance().startRewardSystem();

                },
                (t) -> {
                    if(t.getSecondsLeft() <= 5) {

                        for (UUID u : PlayersManager.getInstance().getActivePlayers()) {
                            Player p = Bukkit.getPlayer(u);

                            if (p == null)
                                continue;

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
        isEarlyDamagePrank = false;

        PlayersManager.getInstance().addActivePlayer(PlayersManager.getInstance().getSpectatorPlayers());
        PlayersManager.getInstance().removeSpectatorPlayer();

        for (UUID u : PlayersManager.getInstance().getActivePlayers()) {

            Player player = Bukkit.getPlayer(u);

            if (player == null) continue;

            player.setGameMode(GameMode.SURVIVAL);

            teleportToSpawnPoint(player);
            player.playSound(player.getLocation(),Sound.ENTITY_ENDERMAN_TELEPORT,1,1);

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
        isEarlyDamagePrank = false;

        PlayersManager.getInstance().removeActivePlayer();
        PlayersManager.getInstance().removeSpectatorPlayer();

    }

    public static void victoryFireworksEffect(int amount) {

        new BukkitRunnable() {
            int cicle = 0;
            Location loc = EventService.getInstance().summonedArena.getTower().clone().add(0,3,0);

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

}
