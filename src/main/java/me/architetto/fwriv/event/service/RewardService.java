package me.architetto.fwriv.event.service;

import me.architetto.fwriv.FWRiv;
import me.architetto.fwriv.config.SettingsHandler;
import me.architetto.fwriv.event.PlayersManager;
import me.architetto.fwriv.reward.ItemReward;
import me.architetto.fwriv.utils.ChatFormatter;
import me.architetto.fwriv.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class RewardService {

    private static RewardService rewardService;

    private int rewardLine;
    private int rewardPeriod;

    private List<ItemReward> itemRewardList;
    private Double rewardsWeightSum;

    private int taskID;

    private RewardService(){
        if(rewardService != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

        this.itemRewardList = new ArrayList<>();

    }

    public static RewardService getInstance() {
        if(rewardService == null) {
            rewardService = new RewardService();
        }
        return rewardService;
    }

    public void startRewardSystem() {
        EventService eventService = EventService.getInstance();
        SettingsHandler settingsHandler = SettingsHandler.getSettingsHandler();

        this.rewardLine = eventService.getSummonedArena().getTower().getBlockY();
        this.rewardPeriod = settingsHandler.rewardPeriod;

        this.itemRewardList = settingsHandler.itemRewardList;
        this.rewardsWeightSum = this.itemRewardList.stream().mapToDouble(ItemReward::weight).sum();

        towerRewardRunnable();

    }

    private void towerRewardRunnable() {

        BukkitTask bukkitTask = new BukkitRunnable() {

            @Override
            public void run() {

                for (UUID uuid : PlayersManager.getInstance().getActivePlayers()) {

                    Player player = Bukkit.getPlayer(uuid);

                    if (player == null)
                        continue;

                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(ChatFormatter.formatErrorMessage(Messages.REWARD_INVENTORY_FULL));
                        continue;
                    }

                    if (player.getLocation().getBlockY() < rewardLine)
                        continue;

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

                }
            }
        }.runTaskTimer(FWRiv.plugin,0,rewardPeriod);
        taskID = bukkitTask.getTaskId();

    }

    private ItemReward pickRandomReward(List<ItemReward> providedItemRewardList) {

        double randomValue = rewardsWeightSum + new SecureRandom().nextInt((int) Math.round(rewardsWeightSum));

        while (randomValue > 0) {

            for(ItemReward itemReward : providedItemRewardList) {

                randomValue -= itemReward.weight();

                if (randomValue <= 0)
                    return itemReward;

            }
        }

        return null;
    }

    public void stopRewardTask() {
        Bukkit.getScheduler().cancelTask(taskID);
    }



}
