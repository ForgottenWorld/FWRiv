package me.architetto.fwriv.event;

import com.destroystokyo.paper.Title;
import me.architetto.fwriv.FWRiv;
import me.architetto.fwriv.arena.Arena;
import me.architetto.fwriv.config.ConfigManager;
import me.architetto.fwriv.config.SettingsHandler;
import me.architetto.fwriv.echelon.EchelonHolder;
import me.architetto.fwriv.event.service.AntiCamperService;
import me.architetto.fwriv.event.service.RewardSystemService;
import me.architetto.fwriv.localization.Message;
import me.architetto.fwriv.obj.ArenaDoors;
import me.architetto.fwriv.partecipant.Partecipant;
import me.architetto.fwriv.partecipant.PartecipantStatus;
import me.architetto.fwriv.partecipant.PartecipantsManager;
import me.architetto.fwriv.obj.timer.Countdown;
import me.architetto.fwriv.utils.MessageUtil;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class EventService {

    private static EventService eventService;

    private Arena summonedArena;

    private EventStatus eventStatus;

    private ArenaDoors arenaDoors;

    private Countdown startCountdown;

    private int spawnPointer = 0;

    private EventService() {
        if(eventService != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

        this.eventStatus = EventStatus.INACTIVE;

    }

    public static EventService getInstance() {
        if(eventService == null) {
            eventService = new EventService();
        }
        return eventService;
    }

    public Arena getArena() {
        return this.summonedArena;
    }

    public EventStatus getEventStatus() {
        return eventStatus;
    }

    public boolean initialization(Arena arena) {

        if (!this.eventStatus.equals(EventStatus.INACTIVE)) return false;

        this.summonedArena = arena;
        this.arenaDoors = new ArenaDoors(arena);
        this.eventStatus = EventStatus.READY;
        this.arenaDoors.close();
        return true;
    }

    private void arenaRoundTeleport(Player player) {
        switch (this.spawnPointer) {
            case 0:
                player.teleport(this.summonedArena.getSpawn1());
                this.spawnPointer++;
                break;
            case 1:
                player.teleport(this.summonedArena.getSpawn2());
                this.spawnPointer++;
                break;
            case 2:
                player.teleport(this.summonedArena.getSpawn3());

                this.spawnPointer++;
                break;
            case 3:
                player.teleport(this.summonedArena.getSpawn4());
                this.spawnPointer = 0;
        }
    }

    public void partecipantJoin(Player player) {
        PartecipantsManager partecipantsManager = PartecipantsManager.getInstance();

        switch (eventStatus) {
            case INACTIVE:
                return;
            case READY:
                partecipantsManager.addPartecipant(player, player.getLocation(), PartecipantStatus.PLAYING);
                arenaRoundTeleport(player);
                player.getInventory().clear();
                player.setGameMode(GameMode.SURVIVAL);
                Message.JOIN_READY_EVENT.send(player);
                break;
            case PRE_RUNNING:
            case RUNNING:
            case ENDED:
                partecipantsManager.addPartecipant(player, player.getLocation(), PartecipantStatus.DEAD);
                player.getInventory().clear();
                player.teleport(summonedArena.getTower().add(0,5,0));
                player.setGameMode(GameMode.SPECTATOR);
                Message.JOIN_STARTED_EVENT.send(player);
        }

        RewardSystemService.getInstance().addPlayerToRewardBar(player);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
        Message.BROADCAST_PLAYERJOINEVENT.broadcastWithPermission("fwriv.echo",player.getName());
    }

    public void partecipantDeath(Player player) {
        PartecipantsManager.getInstance().getPartecipant(player).ifPresent(partecipant -> {
            player.getInventory().clear();
            player.setGameMode(GameMode.SPECTATOR);
            partecipant.setPartecipantStatus(PartecipantStatus.DEAD);
        });

        if (eventStatus.equals(EventStatus.RUNNING))
            checkVictoryCondition();

    }

    public void partecipantLeave(Player player) {

        PartecipantsManager partecipantsManager = PartecipantsManager.getInstance();

        partecipantsManager.getPartecipant(player).ifPresent(partecipant -> {

            player.getInventory().clear();
            player.teleport(partecipant.getReturnLocation());
            player.setGameMode(GameMode.SURVIVAL);
            player.getInventory().setContents(partecipant.getInventory());
            mutexActivityLeaveSupport(player);

            if (partecipant.getPartecipantStatus().equals(PartecipantStatus.PLAYING)
                    && eventStatus.equals(EventStatus.RUNNING)) {
                partecipant.setPartecipantStatus(PartecipantStatus.DEAD);
                checkVictoryCondition();
            }

        });

        partecipantsManager.removePartecipant(player);

        RewardSystemService.getInstance().removePlayerToRewardBar(player);

    }

    private void checkVictoryCondition() {

        PartecipantsManager partecipantsManager = PartecipantsManager.getInstance();

        List<UUID> uuidSet = new ArrayList<>(partecipantsManager.getPartecipantsUUID(PartecipantStatus.PLAYING));

        if (uuidSet.size() == 1) {
            eventStatus = EventStatus.ENDED;

            partecipantsManager
                    .getPartecipant(uuidSet.get(0)).ifPresent(partecipant -> {
                victoryAnimation(partecipant.getName());
                Message.VICTORY_SERVER_BROADCAST.broadcast(partecipant.getName());
            });
            victoryFireworksEffect(2);
        }

        if (uuidSet.size() == 0)
            eventStatus = EventStatus.ENDED;


        if (eventStatus.equals(EventStatus.ENDED)) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(FWRiv.getPlugin(FWRiv.class),
                    () -> PartecipantsManager.getInstance().printStatistics(),40);
            stopEventServices();
            Message.COMP_EVENT_ENDED_BROADCAST.broadcastComponent("fwriv.echo", MessageUtil.restartComponent(),MessageUtil.stopComponent());
        }
    }

    private void victoryAnimation(String playerWinner) {
        String subtitle = Message.VICTORY_SUBTITLE.asString();
        PartecipantsManager.getInstance().getPartecipantsUUID(PartecipantStatus.ALL).stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull).forEach(p -> p.sendTitle(ChatColor.GOLD + playerWinner,
                subtitle,20,100,20));
    }

    public void startEventTimer() {

        AntiCamperService.getInstance().initializeAntiCamperSystem();
        RewardSystemService.getInstance().initializeRewardSystem();

        this.startCountdown = new Countdown(FWRiv.getPlugin(FWRiv.class),
                0,
                10,
                () -> {
            //
                    eventStatus = EventStatus.PRE_RUNNING;

                    PartecipantsManager.getInstance().getPartecipantsUUID(PartecipantStatus.PLAYING).stream()
                            .map(Bukkit::getPlayer)
                            .filter(Objects::nonNull)
                            .forEach(p -> {
                                Message.COUNTDOWN_START.send(p);
                                equipLoadout(p);
                            });

                },
                () -> {
            //
                    eventStatus = EventStatus.RUNNING;

                    PartecipantsManager.getInstance().getPartecipantsUUID(PartecipantStatus.PLAYING).stream()
                            .map(Bukkit::getPlayer)
                            .filter(Objects::nonNull)
                            .forEach(p -> {
                                p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,SettingsHandler.getInstance().getNoDamagePeriod(),3));
                                p.sendTitle(new Title(ChatColor.DARK_RED + "GO !", "", 1, 18, 1));
                                Message.START_MESSAGE.send(p, SettingsHandler.getInstance().getNoDamagePeriod() / 20);
                            });

                    this.arenaDoors.open();

                },
                (t) -> {
                    if(t.getSecondsLeft() <= 5) {

                        PartecipantsManager.getInstance().getPartecipantsUUID(PartecipantStatus.PLAYING).stream()
                                .map(Bukkit::getPlayer)
                                .filter(Objects::nonNull)
                                .forEach(p -> p.sendTitle(new Title(String.valueOf(t.getSecondsLeft()),
                                        "", 1, 18, 1)));

                    }
                });
        this.startCountdown.scheduleTimer();
    }

    public void equipLoadout(Player player) {

        player.getInventory().clear();
        player.setHealth(20);
        player.setFoodLevel(20);

        for (PotionEffect potionEffect : player.getActivePotionEffects())
            player.removePotionEffect(potionEffect.getType());

        SettingsHandler.getInstance().getStartEquipItems().forEach(is -> player.getInventory().addItem(is));

    }

    public void restartEvent() {

        this.eventStatus = EventStatus.READY;
        this.arenaDoors.close();

        PartecipantsManager.getInstance().replacePartecipantsStatus(PartecipantStatus.DEAD, PartecipantStatus.PLAYING);

        PartecipantsManager.getInstance().getPartecipantsUUID(PartecipantStatus.PLAYING).stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .forEach(p -> {
                    p.setGameMode(GameMode.SURVIVAL);
                    arenaRoundTeleport(p);
                    p.playSound(p.getLocation(),Sound.ENTITY_ENDERMAN_TELEPORT,1,1);
                    Message.PARTECIPANT_RESTART_MESSAGE.send(p);
                });

        stopEventServices();

    }

    public void stopEvent() {

        if (this.startCountdown != null
                && this.startCountdown.isStarted())
            this.startCountdown.cancelTimer();
        stopEventServices();

        this.eventStatus = EventStatus.INACTIVE;

        PartecipantsManager partecipantsManager = PartecipantsManager.getInstance();


        partecipantsManager.getPartecipantsUUID(PartecipantStatus.ALL).stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .forEach(p -> {

                    p.setGameMode(GameMode.SURVIVAL);
                    p.getActivePotionEffects().forEach(pot -> p.removePotionEffect(pot.getType()));
                    p.getInventory().clear();

                    partecipantsManager.getPartecipant(p).ifPresent(partecipant -> {
                        p.teleport(partecipant.getReturnLocation());
                        p.playSound(p.getLocation(),Sound.ENTITY_ENDERMAN_TELEPORT,1,1);
                        p.getInventory().setContents(partecipant.getInventory());
                    });

                    partecipantsManager.removePartecipant(p);
                    mutexActivityLeaveSupport(p);

                });

        this.arenaDoors.close();
        partecipantsManager.clearPartecipantsStats();
        Message.BROADCAST_EVENT_ENDED.broadcast();
        this.arenaDoors = null;

    }

    private void stopEventServices() {

        AntiCamperService.getInstance().stopAntiCamperSystem();
        RewardSystemService.getInstance().stopRewardService();

    }

    public static void victoryFireworksEffect(int amount) {

        new BukkitRunnable() {
            int cicle = 0;
            Location loc = EventService.getInstance().summonedArena.getTower().add(0,3,0);

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
        }.runTaskTimer(FWRiv.plugin,20,80);

    }

    public void mutexActivityLeaveSupport(Player player) {
        if (SettingsHandler.getInstance().isEchelonEnabled()) {
            if (EchelonHolder.getEchelonHolder().isPlayerInMutexActivity(player))
                EchelonHolder.getEchelonHolder().removePlayerMutexActivity(player);
        }
    }

}
