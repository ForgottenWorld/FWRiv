package me.architetto.fwriv.event.service;

import me.architetto.fwriv.FWRiv;
import me.architetto.fwriv.config.SettingsHandler;
import me.architetto.fwriv.event.EventService;
import me.architetto.fwriv.obj.timer.Repeater;
import me.architetto.fwriv.partecipant.PartecipantStats;
import me.architetto.fwriv.partecipant.PartecipantStatus;
import me.architetto.fwriv.partecipant.PartecipantsManager;
import me.architetto.fwriv.reward.RewardService;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.Objects;

public class RewardSystemService {

    private static RewardSystemService instance;

    private Repeater rewardRunnable;
    private BossBar rewardBar;

    private int rewardLine;
    private float rewardPeriod;

    private RewardSystemService(){
        if(instance != null)
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");

        this.rewardBar = Bukkit.createBossBar("placeholder",
                BarColor.YELLOW, BarStyle.SEGMENTED_20);
        this.rewardBar.setVisible(false);

    }

    public static RewardSystemService getInstance() {
        if(instance == null)
            instance = new RewardSystemService();

        return instance;
    }

    public void initializeRewardSystem() {
        EventService eventService = EventService.getInstance();
        SettingsHandler settingsHandler = SettingsHandler.getInstance();
        RewardService.getInstance().pickNextTowerReward();

        this.rewardLine = eventService.getArena().getTower().getBlockY();
        this.rewardPeriod = 1f/settingsHandler.getRewardPeriod();
        this.rewardBar.setTitle("REWARD :" + RewardService.getInstance().getNextTowerReward());

        startRewardSystem(settingsHandler.getRewarDelay());

    }

    private void startRewardSystem(long delay) {

        this.rewardRunnable = new Repeater(FWRiv.getPlugin(FWRiv.class),
                delay,
                () -> {
            //
                    this.rewardBar.setProgress(1);
                    this.rewardBar.setVisible(true);

                    PartecipantsManager.getInstance().getPartecipantsUUID(PartecipantStatus.ALL).stream()
                            .map(Bukkit::getPlayer)
                            .filter(Objects::nonNull)
                            .forEach(this.rewardBar::addPlayer);

                },
                (s) -> {
            //
                    this.rewardBar.setProgress(Math.max(0,this.rewardBar.getProgress() - rewardPeriod));


                    if (this.rewardBar.getProgress() == 0) {
                        PartecipantsManager.getInstance().getPartecipantsUUID(PartecipantStatus.PLAYING).stream()
                                .map(Bukkit::getPlayer)
                                .filter(Objects::nonNull)
                                .filter(p -> p.getLocation().getBlockY() >= rewardLine)
                                .forEach(p -> {
                                    RewardService.getInstance().giveTowerReward(p);
                                    PartecipantsManager.getInstance().getPartecipantStats(p).ifPresent(PartecipantStats::addReward);
                                });
                        RewardService.getInstance().pickNextTowerReward();
                        this.rewardBar.setProgress(1);
                        this.rewardBar.setTitle("REWARD : " + RewardService.getInstance().getNextTowerReward());
                    }
                });
        this.rewardRunnable.scheduleTimer();
    }

    public void stopRewardService() {
        this.rewardRunnable.cancelTimer();
        this.rewardBar.removeAll();
        this.rewardBar.setVisible(false);
    }

    public void addPlayerToRewardBar(Player player) {
        this.rewardBar.addPlayer(player);
    }

    public void removePlayerToRewardBar(Player player) {
        this.rewardBar.removePlayer(player);
    }



}
