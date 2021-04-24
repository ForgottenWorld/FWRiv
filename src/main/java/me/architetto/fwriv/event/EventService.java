package me.architetto.fwriv.event;

import com.destroystokyo.paper.Title;
import me.architetto.fwriv.FWRiv;
import me.architetto.fwriv.arena.Arena;
import me.architetto.fwriv.config.SettingsHandler;
import me.architetto.fwriv.echelon.EchelonHolder;
import me.architetto.fwriv.event.service.OldAntiCamperService;
import me.architetto.fwriv.event.service.RewardSystemService;
import me.architetto.fwriv.localization.Message;
import me.architetto.fwriv.obj.ArenaDoors;
import me.architetto.fwriv.obj.RoundSpawn;
import me.architetto.fwriv.partecipant.PartecipantStatus;
import me.architetto.fwriv.partecipant.PartecipantsManager;
import me.architetto.fwriv.obj.timer.Countdown;
import me.architetto.fwriv.utils.ChatFormatter;
import me.architetto.fwriv.utils.MessageUtil;
import me.architetto.fwriv.utils.Messages;
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

    private EventStatus eventStatus = EventStatus.INACTIVE;

    private RoundSpawn roundSpawn;

    private ArenaDoors arenaDoors;

    private Countdown startCountdown;

    private EventService() {
        if(eventService != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

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

    public RoundSpawn getRoundSpawn() {
        return this.roundSpawn;
    }

    public boolean initialization(Arena arena) {
        if (!this.eventStatus.equals(EventStatus.INACTIVE)) return false;

        this.summonedArena = arena;
        this.roundSpawn = new RoundSpawn(arena);
        this.arenaDoors = new ArenaDoors(arena);
        this.eventStatus = EventStatus.READY;
        this.arenaDoors.close();

        return true;
    }

    public void partecipantJoin(Player player) {
        PartecipantsManager partecipantsManager = PartecipantsManager.getInstance();

        if (!eventStatus.equals(EventStatus.READY)) {
            partecipantsManager.addPartecipant(player, player.getLocation(), PartecipantStatus.DEAD);
            player.setGameMode(GameMode.SPECTATOR);
            player.teleport(eventService.getArena().getTower());
            Message.JOIN_STARTED_EVENT.send(player);

        } else {
            partecipantsManager.addPartecipant(player, player.getLocation(), PartecipantStatus.PLAYING);
            player.getInventory().clear();
            player.setGameMode(GameMode.SURVIVAL);
            eventService.getRoundSpawn().teleport(player);
            Message.JOIN_READY_EVENT.send(player);
        }

        RewardSystemService.getInstance().addPlayerToRewardBar(player);
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
            player.setGameMode(GameMode.SURVIVAL);
            player.teleport(partecipant.getReturnLocation());
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
                    .getPartecipant(uuidSet.get(0)).ifPresent(partecipant -> victoryAnimation(partecipant.getName()));

            victoryFireworksEffect(2);
        }

        if (uuidSet.size() == 0)
            eventStatus = EventStatus.ENDED;

        if (eventStatus.equals(EventStatus.ENDED)) {
            stopTasks();
            Message.COMP_EVENT_ENDED_BROADCAST.broadcastComponent("fwriv.echo", MessageUtil.restartCmponent(),MessageUtil.stopCmponent());
            Bukkit.getScheduler().scheduleSyncDelayedTask(FWRiv.getPlugin(FWRiv.class), this::stopEvent,1800L);
        }
    }

    private void victoryAnimation(String playerWinner) {
        PartecipantsManager.getInstance().getPartecipantsUUID(PartecipantStatus.ALL).stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull).forEach(p -> p.sendTitle(ChatColor.GOLD + playerWinner,
                "E' IL VINCITORE DELL'EVENTO",20,100,20));
    }

    public void stopTasks() {

        OldAntiCamperService.getInstance().stopAnticamperTasks();
        RewardSystemService.getInstance().stopRewardService();

    }

    public void startEventTimer() {

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
                                p.sendMessage(ChatFormatter.formatInitializationMessage(Messages.START_CD_MSG));
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
                                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,1200,6));
                                p.sendTitle(new Title(ChatColor.DARK_RED + "GO !", "", 1, 18, 1));
                                Message.START_MESSAGE.send(p);
                            });

                    this.arenaDoors.open();

                    OldAntiCamperService.getInstance().startAntiCamperSystem();
                    RewardSystemService.getInstance().initializeRewardSystem();

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

        SettingsHandler.getInstance().startEquipItems.forEach(is -> player.getInventory().addItem(is));

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
                    this.roundSpawn.teleport(p);
                    p.playSound(p.getLocation(),Sound.ENTITY_ENDERMAN_TELEPORT,1,1);
                    p.sendMessage(ChatFormatter.formatSuccessMessage(Messages.RESTART_CMD_PLAYER_MESSAGE));

                });


        OldAntiCamperService.getInstance().stopAnticamperTasks();
        RewardSystemService.getInstance().stopRewardService();

    }

    public void stopEvent() {

        if (!eventStatus.equals(EventStatus.READY)) {
            this.startCountdown.cancelTimer();
            OldAntiCamperService.getInstance().stopAnticamperTasks();
            RewardSystemService.getInstance().stopRewardService();
        }

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
                        p.sendMessage(ChatFormatter.formatInitializationMessage(Messages.STOP_CMD_PLAYER_MESSAGE));
                    });

                    partecipantsManager.removePartecipant(p);

                });

        this.arenaDoors.close();

        removeAllPlayerMutexActivitySupport();


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
        if (SettingsHandler.getInstance().isEchelonEnabled())
            EchelonHolder.getEchelonHolder().removePlayerMutexActivity(player);
    }

    public void removeAllPlayerMutexActivitySupport() {
        if (SettingsHandler.getInstance().isEchelonEnabled())
            EchelonHolder.getEchelonHolder().removeAllMutexActivityRIVPlayer();
    }

}
