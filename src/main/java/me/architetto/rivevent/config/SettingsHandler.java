package me.architetto.rivevent.config;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SettingsHandler {

    private static SettingsHandler instance;

    public Location respawnLocation;

    public int foodLevel;

    public int antiCamperStartDelay;
    public int antiCamperDamagePeriod;
    public int antiCamperDamage;
    public int antiCamperDamageBoost;
    public int antiCamperGrowPeriod;
    public int antiCamperGrowValue;
    public int antiCamperRedLineTopTowerDif;

    public int redLineAnimationRadius;

    public int rewardPeriod;

    public HashMap<Material,Double> itemsListWeight = new HashMap<>();
    public HashMap<Material,Integer> itemsListMaxAmount = new HashMap<>();

    public List<Material> uniquerewardItemList = new ArrayList<>();

    public HashMap<Material,Integer> startEquipItems = new HashMap<>();

    public double snowballKnockbackPower;
    public double snowballHitDamage;

    public boolean enableTargetBlock;
    public int targetBlockCooldown;

    public int deathRacePeriod;

    public int cursePeriod;

    private SettingsHandler() {

    }

    public static SettingsHandler getInstance(){
        if(instance==null)
            instance = new SettingsHandler();

        return instance;
    }

    public void load(){

        FileConfiguration fileConfiguration = ConfigManager.getInstance().getConfig("Settings.yml");

        loadRewards();

        loadUniqueRewardList();

        loadStartEquip();

        this.foodLevel = fileConfiguration.getInt("FOOD_LEVEL",18);

        this.rewardPeriod = fileConfiguration.getInt("REWARD_PERIOD",20) * 20;

        this.respawnLocation = ConfigManager.getInstance()
                .getLocation(ConfigManager.getInstance().getConfig("Settings.yml"), "RESPAWN_POINT");

        this.antiCamperStartDelay = fileConfiguration.getInt("ANTI_CAMPER_START_DELAY",60) * 20;
        this.antiCamperDamagePeriod = fileConfiguration.getInt("ANTI_CAMPER_DAMAGE_PERIOD",1) * 20;
        this.antiCamperDamage = fileConfiguration.getInt("ANTI_CAMPER_DAMAGE",2);
        this.antiCamperDamageBoost = fileConfiguration.getInt("ANTI_CAMPER_DAMAGE_FINAL_BOOST",4);
        this.antiCamperGrowPeriod = fileConfiguration.getInt("ANTI_CAMPER_GROW_PERIOD",20) * 20;
        this.antiCamperGrowValue = fileConfiguration.getInt("ANTI_CAMPER_GROW_VALUE",2);
        this.antiCamperRedLineTopTowerDif = fileConfiguration.getInt("ANTI_CAMPER_RL_DIF",5);

        this.redLineAnimationRadius = fileConfiguration.getInt("RED_LINE_ANIMATION_RADIUS",10);

        this.snowballKnockbackPower = fileConfiguration.getDouble("SNOWBALL_KNOCKBACK_POWER",1);
        this.snowballHitDamage = fileConfiguration.getDouble("SNOWBALL_DAMAGE",0.1);

        this.enableTargetBlock = fileConfiguration.getBoolean("ENABLE_TARGET_BLOCK",true);
        this.targetBlockCooldown = fileConfiguration.getInt("TARGET_BLOCK_COOLDOWN",30);

        //non serve convertire in tick
        this.deathRacePeriod = fileConfiguration.getInt("DEATH_RACE_PERIOD",60);

        this.cursePeriod = fileConfiguration.getInt("CURSE_PERIOD",60) * 20;

    }

    private void loadRewards() {

        FileConfiguration fileConfiguration = ConfigManager.getInstance().getConfig("Settings.yml");
        List<String> materialsStringList = fileConfiguration.getStringList("LIST_ITEMS_REWARD");

        if (!materialsStringList.isEmpty()) {
            for(String materialName : materialsStringList) {

                String [] parts = materialName.split(",");

                if (Material.getMaterial(parts[0]) != null) {
                    this.itemsListWeight.put(Material.getMaterial(parts[0]), Math.abs(Double.parseDouble(parts[1])));
                    this.itemsListMaxAmount.put(Material.getMaterial(parts[0]), Math.abs(Integer.parseInt(parts[2])));
                }

            }
        }
    }

    private void loadUniqueRewardList() {

        FileConfiguration fileConfiguration = ConfigManager.getInstance().getConfig("Settings.yml");
        List<String> materialsStringList = fileConfiguration.getStringList("LIST_UNIQUE_ITEMS");

        if (!materialsStringList.isEmpty()) {
            for(String materialName : materialsStringList) {

                if (Material.getMaterial(materialName) != null)
                    uniquerewardItemList.add(Material.getMaterial(materialName));

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


}
