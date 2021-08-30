package me.architetto.fwriv.reward;

import me.architetto.fwriv.config.ConfigManager;
import me.architetto.fwriv.config.SettingsHandler;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class RewardService {

    private static RewardService instance;

    private HashMap<Reward, Double> towerRewardsWeightMap;
    private double towerRewardsWeightSum;

    private Reward nextTowerReward;

    private HashMap<Reward,Double> targetBlockRewardsMap;
    private double targetBlockWeightSum;


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
            double weight = Double.parseDouble(parts[1]);
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
            double weight = Double.parseDouble(parts[1]);
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
            double weight = Double.parseDouble(parts[1]);
            PotionReward pr = new PotionReward(PotionEffectType.getByName(parts[0]),
                    Integer.parseInt(parts[2]),
                    Integer.parseInt(parts[3]),
                    Integer.parseInt(parts[4]),
                    DyeColor.valueOf(parts[5]));
            this.towerRewardsWeightMap.put(pr, weight);
            this.towerRewardsWeightSum += weight;
        });

        this.towerRewardsWeightMap.forEach((key, value) -> Bukkit.getConsoleSender()
                .sendMessage(ChatColor.DARK_GREEN + "        -- [TOWER] "
                        + key.getName() + " (" + value + ")"));

        List<String> itemsTargetblockRewards = fc.getStringList("TARGETBLOCK_REWARDS.ITEMS");

        itemsTargetblockRewards.forEach(s -> {
            String[] parts = s.split(",");
            if (parts.length != 4) return;
            double weight = Double.parseDouble(parts[1]);
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
            double weight = Double.parseDouble(parts[1]);
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
            double weight = Double.parseDouble(parts[1]);
            PotionReward pr = new PotionReward(PotionEffectType.getByName(parts[0]),
                    Integer.parseInt(parts[2]),
                    Integer.parseInt(parts[3]),
                    Integer.parseInt(parts[4]),
                    DyeColor.valueOf(parts[5]));
            this.targetBlockRewardsMap.put(pr, weight);
            this.targetBlockWeightSum += weight;
        });

        this.targetBlockRewardsMap.forEach((key, value) -> Bukkit.getConsoleSender()
                .sendMessage(ChatColor.DARK_GREEN + "        -- [TARGET] "
                        + key.getName() + " (" + value + ")"));

    }

    public void reloadRewards() {
        towerRewardsWeightMap.clear();
        targetBlockRewardsMap.clear();
        towerRewardsWeightSum = 0;
        targetBlockWeightSum = 0;
        loadRewards();
    }

    public void pickNextTowerReward() {

        double randomvalue = ThreadLocalRandom.current().nextDouble(2,4);
        double weightSum = ThreadLocalRandom.current()
                .nextDouble(this.towerRewardsWeightSum, this.towerRewardsWeightSum * randomvalue);

        while (weightSum > 0) {
            for (Map.Entry<Reward, Double> entry : towerRewardsWeightMap.entrySet()) {
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

    public void giveRandomTowerReward(Player player) {
        //todo: sono pigro (see pickNextTowerReward)
        double randomvalue = ThreadLocalRandom.current().nextDouble(2,4);
        double weightSum = ThreadLocalRandom.current()
                .nextDouble(this.towerRewardsWeightSum, this.towerRewardsWeightSum * randomvalue);
        while (weightSum > 0) {
            for (Map.Entry<Reward, Double> entry : towerRewardsWeightMap.entrySet()) {
                weightSum -= entry.getValue();
                if (weightSum <= 0) {
                    entry.getKey().give(player);
                    return;
                }
            }
        }
    }

    public void giveTargetBlockReward(Player player) {

        double randomvalue = ThreadLocalRandom.current().nextDouble(2,4);
        double weightSum = ThreadLocalRandom.current()
                .nextDouble(this.targetBlockWeightSum, this.targetBlockWeightSum * randomvalue);

        if (isPrime((int) weightSum)) {
            double value = SettingsHandler.getInstance().getTargetBlockExplosionPower();
            player.setVelocity(player.getLocation().getDirection().multiply(-value));
            player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 3, 1);
            return;
        }

        while (weightSum > 0) {
            for (Map.Entry<Reward, Double> entry : targetBlockRewardsMap.entrySet()) {
                weightSum -= entry.getValue();
                if (weightSum < 0) {
                    entry.getKey().give(player);
                    return;
                }
            }
        }
    }

    private boolean isPrime(int n) {
        return !new String(new char[n]).matches(".?|(..+?)\\1+");
    }


}
