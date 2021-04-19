package me.architetto.fwriv.reward;

import me.architetto.fwriv.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class RewardService {

    private static RewardService instance;

    private HashMap<Reward, Integer> towerRewardsWeightMap;
    private long towerRewardsWeightSum;

    private Reward nextTowerReward;

    private HashMap<Reward, Integer> targetBlockRewardsMap;
    private long targetBlockWeightSum;


    private RewardService(){
        if(instance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

        towerRewardsWeightMap = new HashMap<>();
        targetBlockRewardsMap = new HashMap<>();


    }

    public static RewardService getInstance() {
        if(instance == null) {
            instance = new RewardService();
        }
        return instance;
    }

    public void loadRewards() {
        FileConfiguration fc = ConfigManager.getInstance().getConfig("Settings.yml");
        List<String> itemsTowerRewards = fc.getStringList("TOWER_REWARDS.ITEMS");

        itemsTowerRewards.forEach(s -> {
            String[] parts = s.split(",");
            if (parts.length != 4) return;
            int weight = Integer.parseInt(parts[1]);
            ItemReward ir = new ItemReward(Material.valueOf(parts[0]),
                    Integer.parseInt(parts[2]),
                    Boolean.parseBoolean(parts[3]));
            this.towerRewardsWeightMap.put(ir, weight);
            this.towerRewardsWeightSum += weight;
        });

        List<String> effectsTowerRewards = fc.getStringList("TOWER_REWARDS.EFFECTS");

        effectsTowerRewards.forEach(s -> {
            String[] parts = s.split(",");
            if (parts.length != 5) return;
            int weight = Integer.parseInt(parts[1]);
            EffectReward er = new EffectReward(PotionEffectType.getByName(parts[0]),
                    Integer.parseInt(parts[2]),
                    Integer.parseInt(parts[3]),
                    Integer.parseInt(parts[4]));
            this.towerRewardsWeightMap.put(er, weight);
            this.towerRewardsWeightSum += weight;
        });

        List<String> potionsTowerRewards = fc.getStringList("TOWER_REWARDS.POTIONS");

        potionsTowerRewards.forEach(s -> {
            String[] parts = s.split(",");
            if (parts.length != 6) return;
            int weight = Integer.parseInt(parts[1]);
            PotionReward pr = new PotionReward(PotionEffectType.getByName(parts[0]),
                    Integer.parseInt(parts[2]),
                    Integer.parseInt(parts[3]),
                    Integer.parseInt(parts[4]),
                    DyeColor.valueOf(parts[5]));
            this.towerRewardsWeightMap.put(pr, weight);
            this.towerRewardsWeightSum += weight;
        });

        //////

        List<String> itemsTargetblockRewards = fc.getStringList("TARGETBLOCK_REWARDS.ITEMS");

        itemsTargetblockRewards.forEach(s -> {
            String[] parts = s.split(",");
            if (parts.length != 4) return;
            int weight = Integer.parseInt(parts[1]);
            ItemReward ir = new ItemReward(Material.valueOf(parts[0]),
                    Integer.parseInt(parts[2]),
                    Boolean.parseBoolean(parts[3]));
            this.targetBlockRewardsMap.put(ir, weight);
            this.targetBlockWeightSum += weight;
        });

        List<String> effectsTargetblockRewards = fc.getStringList("TARGETBLOCK_REWARDS.EFFECTS");

        effectsTargetblockRewards.forEach(s -> {
            String[] parts = s.split(",");
            if (parts.length != 5) return;
            int weight = Integer.parseInt(parts[1]);
            EffectReward er = new EffectReward(PotionEffectType.getByName(parts[0]),
                    Integer.parseInt(parts[2]),
                    Integer.parseInt(parts[3]),
                    Integer.parseInt(parts[4]));
            this.targetBlockRewardsMap.put(er, weight);
            this.targetBlockWeightSum += weight;
        });

        List<String> potionsTargetblockRewards = fc.getStringList("TARGETBLOCK_REWARDS.POTIONS");

        potionsTargetblockRewards.forEach(s -> {
            String[] parts = s.split(",");
            if (parts.length != 6) return;
            int weight = Integer.parseInt(parts[1]);
            PotionReward pr = new PotionReward(PotionEffectType.getByName(parts[0]),
                    Integer.parseInt(parts[2]),
                    Integer.parseInt(parts[3]),
                    Integer.parseInt(parts[4]),
                    DyeColor.valueOf(parts[5]));
            this.targetBlockRewardsMap.put(pr, weight);
            this.targetBlockWeightSum += weight;
        });

        Bukkit.getConsoleSender().sendMessage("TR : " + towerRewardsWeightSum + " || TBR : " + targetBlockWeightSum);

    }

    public void reloadRewards() {
        towerRewardsWeightMap.clear();
        targetBlockRewardsMap.clear();
        towerRewardsWeightSum = 0;
        targetBlockWeightSum = 0;
        loadRewards();
    }

    public void pickNextTowerReward() {

        long weightSum = ThreadLocalRandom.current()
                .nextLong(this.towerRewardsWeightSum, this.towerRewardsWeightSum * 2);

        while (weightSum > 0) {
            for (Map.Entry<Reward, Integer> entry : towerRewardsWeightMap.entrySet()) {
                weightSum -= entry.getValue();
                if (weightSum <= 0) {
                    this.nextTowerReward = entry.getKey();
                    return;
                }
            }
        }
    }

    public String getNextTowerReward() {
        return this.nextTowerReward.getName();
    }

    public void giveTowerReward(Player players) {
        nextTowerReward.give(players);
    }

    public void giveTargetBlockReward(Player player) {
        long weightSum = ThreadLocalRandom.current()
                .nextLong(this.targetBlockWeightSum, this.targetBlockWeightSum * 2);

        while (weightSum > 0) {
            for (Map.Entry<Reward, Integer> entry : targetBlockRewardsMap.entrySet()) {
                weightSum -= entry.getValue();
                if (weightSum <= 0) {
                    entry.getKey().give(player);
                }
            }
        }
    }

}