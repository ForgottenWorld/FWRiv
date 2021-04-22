package me.architetto.fwriv.event.service;

import me.architetto.fwriv.FWRiv;
import me.architetto.fwriv.config.SettingsHandler;
import me.architetto.fwriv.event.EventService;
import me.architetto.fwriv.partecipant.PartecipantStatus;
import me.architetto.fwriv.partecipant.PartecipantsManager;
import me.architetto.fwriv.reward.RewardService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class OldRewardService {

    private static OldRewardService oldRewardService;

    private int rewardLine;
    private int rewardPeriod;

    private int taskID;

    private OldRewardService(){
        if(oldRewardService != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

    }

    public static OldRewardService getInstance() {
        if(oldRewardService == null) {
            oldRewardService = new OldRewardService();
        }
        return oldRewardService;
    }

    public void startRewardSystem() {
        EventService eventService = EventService.getInstance();
        SettingsHandler settingsHandler = SettingsHandler.getSettingsHandler();

        this.rewardLine = eventService.getArena().getTower().getBlockY();
        this.rewardPeriod = settingsHandler.rewardPeriod;

        towerRewardRunnable();

    }

    private void towerRewardRunnable() {

        BukkitTask bukkitTask = new BukkitRunnable() {

            @Override
            public void run() {

                RewardService.getInstance().pickNextTowerReward();

                for (UUID uuid : PartecipantsManager.getInstance().getPartecipantsUUID(PartecipantStatus.PLAYING)) {

                    Player player = Bukkit.getPlayer(uuid);

                    if (player == null)
                        continue;

                    if (player.getLocation().getBlockY() < rewardLine)
                        continue;

                    RewardService.getInstance().giveTowerReward(player);


                    /*


                    ItemReward itemReward = pickRandomReward(itemRewardList);
                    
                    assert itemReward != null;
                    ItemStack itemStack = new ItemStack(itemReward.item(), itemReward.amount());

                    if (itemReward.isUnique() && Arrays.stream(player.getInventory().getContents()).anyMatch(itemStack::isSimilar)) {
                        player.sendMessage(ChatFormatter.formatEventMessage(Messages.REWARD_NO_UNIQUE));
                        continue;
                    }

                    player.getInventory().addItem(itemStack);

                    player.sendMessage(ChatFormatter.formatEventMessage("Reward : " + ChatColor.AQUA + itemStack.getI18NDisplayName()));

                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,1,1);

                     */

                }
            }
        }.runTaskTimer(FWRiv.plugin,0,rewardPeriod);
        taskID = bukkitTask.getTaskId();

    }

    public void stopRewardTask() {
        Bukkit.getScheduler().cancelTask(taskID);
    }



}
