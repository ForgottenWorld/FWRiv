package me.architetto.fwriv.config;

import me.architetto.fwriv.reward.ItemReward;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SettingsHandler {

    private static SettingsHandler settingsHandler;

    public boolean echelonSupport;

    public Location safeRespawnLocation;

    public int foodLevel;

    public int antiCamperStartDelay;
    public int antiCamperDamage;
    public int antiCamperFinalDamage;
    public int antiCamperGrowPeriod;
    public int antiCamperGrowValue;
    public int antiCamperRedLineTopTowerDif;

    public int redLineAnimationRadius;

    public int rewardPeriod;

    public List<ItemReward> itemRewardList = new ArrayList<>();

    public HashMap<Material,Integer> startEquipItems = new HashMap<>();

    public double snowballKnockbackPower;
    public double snowballHitDamage;

    public double fishingRodPower;

    public double tridentKnockPower;

    public boolean enableTargetBlock;
    public double targetBlockExplosionPower;
    public int targetBlockCooldown;

    private SettingsHandler() {
        if(settingsHandler != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

    }

    public static SettingsHandler getSettingsHandler() {
        if(settingsHandler ==null)
            settingsHandler = new SettingsHandler();

        return settingsHandler;
    }

    public void load(){

        FileConfiguration fileConfiguration = ConfigManager.getInstance().getConfig("Settings.yml");

        loadRewards();

        loadStartEquip();

        this.foodLevel = fileConfiguration.getInt("FOOD_LEVEL",18);

        this.rewardPeriod = fileConfiguration.getInt("REWARD_PERIOD",20) * 20;

        this.safeRespawnLocation = ConfigManager.getInstance()
                .getLocation(ConfigManager.getInstance().getConfig("RespawnPoint.yml"), "RESPAWN_POINT");

        this.antiCamperStartDelay = fileConfiguration.getInt("ANTI_CAMPER_START_DELAY",60) * 20;
        this.antiCamperDamage = fileConfiguration.getInt("ANTI_CAMPER_START_DAMAGE",2);
        this.antiCamperFinalDamage = fileConfiguration.getInt("ANTI_CAMPER_FINAL_DAMAGE",6);
        this.antiCamperGrowPeriod = fileConfiguration.getInt("ANTI_CAMPER_GROW_PERIOD",20) * 20;
        this.antiCamperGrowValue = fileConfiguration.getInt("ANTI_CAMPER_GROW_VALUE",2);
        this.antiCamperRedLineTopTowerDif = fileConfiguration.getInt("ANTI_CAMPER_RL_DIF",5);

        this.redLineAnimationRadius = fileConfiguration.getInt("RED_LINE_ANIMATION_RADIUS",10);

        this.snowballKnockbackPower = fileConfiguration.getDouble("SNOWBALL_KNOCKBACK_POWER",3);
        this.snowballHitDamage = fileConfiguration.getDouble("SNOWBALL_DAMAGE",0.1);

        this.fishingRodPower = fileConfiguration.getDouble("FISHINGROD_POWER",7);

        this.tridentKnockPower = fileConfiguration.getDouble("TRIDENT_KNOCKBACK_POWER",1);

        this.enableTargetBlock = fileConfiguration.getBoolean("ENABLE_TARGET_BLOCK",true);
        this.targetBlockExplosionPower = fileConfiguration.getDouble("TRAGET_BLOCK_KNOCKBACK_POWER",1.4);
        this.targetBlockCooldown = fileConfiguration.getInt("TARGET_BLOCK_COOLDOWN",15) * 20;

    }

    private void loadRewards() {

        List<String> materialsStringList = ConfigManager.getInstance().getConfig("Settings.yml")
                .getStringList("REWARD_LIST");

        if (!materialsStringList.isEmpty()) {
            for(String materialName : materialsStringList) {

                String [] parts = materialName.split(",");

                if (Material.getMaterial(parts[0]) != null) {

                    ItemReward itemReward = new ItemReward(Material.getMaterial(parts[0]),Double.parseDouble(parts[1]),
                            Integer.parseInt(parts[2]),Boolean.parseBoolean(parts[3]));

                    this.itemRewardList.add(itemReward);
                }
            }
        }
    }

    private void loadStartEquip() {

        FileConfiguration fileConfiguration = ConfigManager.getInstance().getConfig("Settings.yml");
        List<String> MaterialName = fileConfiguration.getStringList("START_LOADOUT");

        if (!MaterialName.isEmpty()){

            for(String name : MaterialName){

                String [] parts = name.split(",");

                if (Material.getMaterial(parts[0]) != null) {
                    this.startEquipItems.put(Material.getMaterial(parts[0]),Math.abs(Integer.parseInt(parts[1])));
                }

            }
        }

    }

    public void reload() {

        this.startEquipItems.clear();

        this.itemRewardList.clear();

        load();
    }


}
