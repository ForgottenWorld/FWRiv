package me.architetto.rivevent.event;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.config.SettingsHandler;
import me.architetto.rivevent.util.ChatFormatter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

public class RewardService {

    private static RewardService rewardService;

    private int taskID;

    private int rewardLine;

    private int rewardPeriod;

    private HashMap<Material,Double> itemsListWeight = new HashMap<>();
    private HashMap<Material,Integer> itemsListMaxAmount = new HashMap<>();
    private Double totalWeight;


    private RewardService(){
        if(rewardService != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

        this.itemsListWeight = new HashMap<>();
        this.itemsListMaxAmount = new HashMap<>();

    }

    public static RewardService getInstance() {
        if(rewardService == null) {
            rewardService = new RewardService();
        }
        return rewardService;
    }

    public void startRewardSystem() {
        EventService eventService = EventService.getInstance();
        SettingsHandler settingsHandler = SettingsHandler.getInstance();

        this.rewardLine = eventService.getSummonedArena().getTower().getBlockY();
        this.rewardPeriod = settingsHandler.rewardPeriod;
        this.itemsListWeight = settingsHandler.itemsListWeight;
        this.itemsListMaxAmount = settingsHandler.itemsListMaxAmount;
        this.totalWeight = this.itemsListWeight.values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        rewardPlayerOnTop();

    }



    private void rewardPlayerOnTop() {

        EventService eventService = EventService.getInstance();
        BukkitTask bukkitTask = new BukkitRunnable() {

            @Override
            public void run(){

                for (UUID uuid : Collections.unmodifiableList(eventService.getParticipantsPlayers())) {

                    Player player = Bukkit.getPlayer(uuid);

                    if (player.getInventory().firstEmpty() == -1){
                        player.sendMessage(ChatFormatter.formatErrorMessage("Inventory full! No reward"));
                        continue;
                    }

                    if (player.getLocation().getBlockY() >= rewardLine) {

                        Material material = pickRandomItem();

                        ItemStack itemStack = new ItemStack(material, pickRandomAmount(material));

                        player.getInventory().addItem(itemStack);

                        player.sendMessage(ChatFormatter.formatEventMessage("reward obtained: " + ChatColor.AQUA + itemStack.getI18NDisplayName()));

                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,1,1);

                    }
                }
            }
        }.runTaskTimer(RIVevent.plugin,0,rewardPeriod);
        taskID = bukkitTask.getTaskId();

    }

    private Material pickRandomItem() {

        SecureRandom secureRandom = new SecureRandom();
        double randomValue = totalWeight + secureRandom.nextInt((int) Math.round(totalWeight))
                + totalWeight * secureRandom.nextDouble();

        while (randomValue > 0){

            for(Material material : itemsListWeight.keySet()){

                randomValue -= (itemsListWeight.get(material) + secureRandom.nextDouble());

                if (randomValue <= 0) {

                    return material;

                }
            }
        }

        return null;
    }

    private int pickRandomAmount(Material material) {

        if (itemsListMaxAmount.get(material) == 1)
            return 1;

        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.nextInt(itemsListMaxAmount.get(material)) + 1;


    }

    public void stopRewardTask() {
        Bukkit.getScheduler().cancelTask(taskID);
    }



}
