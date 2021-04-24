package me.architetto.fwriv.config;

import me.architetto.fwriv.localization.LocalizationManager;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SettingsHandler {

    private static SettingsHandler settingsHandler;

    private boolean echelonSupport;

    private long rewarDelay;
    private long rewardPeriod;

    public int acDelay;
    public int acDamage;
    public int acFinalDamage;
    public int acGrowPeriod;
    public int acGrowValue;
    public int antiCamperRedLineTopTowerDif; //Questo non è indispensabile
    public int redLineAnimationRadius;


    public List<ItemStack> startEquipItems = new ArrayList<>();

    public double snowballKnockbackPower;
    public double snowballHitDamage;

    public double fishingRodPower;

    public boolean enableTargetBlock;
    public double targetBlockExplosionPower;
    public int targetBlockCooldown;

    private SettingsHandler() {
        if(settingsHandler != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

    }

    public static SettingsHandler getInstance() {
        if(settingsHandler ==null)
            settingsHandler = new SettingsHandler();

        return settingsHandler;
    }

    public void load(){

        FileConfiguration fc = ConfigManager.getInstance().getConfig("Settings.yml");

        loadStartEquip();

        this.rewarDelay = fc.getLong("REWARD_SYSTEM.DELAY",30) * 20;
        this.rewardPeriod = fc.getLong("REWARD_SYSTEM.PERIOD",20);

        this.acDelay = fc.getInt("ANTI_CAMPER_START_DELAY",60) * 20;
        this.acDamage = fc.getInt("ANTI_CAMPER_START_DAMAGE",2);
        this.acFinalDamage = fc.getInt("ANTI_CAMPER_FINAL_DAMAGE",6);
        this.acGrowPeriod = fc.getInt("ANTI_CAMPER_GROW_PERIOD",20) * 20;
        this.acGrowValue = fc.getInt("ANTI_CAMPER_GROW_VALUE",2);
        this.antiCamperRedLineTopTowerDif = fc.getInt("ANTI_CAMPER_RL_DIF",5);

        this.redLineAnimationRadius = fc.getInt("RED_LINE_ANIMATION_RADIUS",10);

        this.snowballKnockbackPower = fc.getDouble("SNOWBALL_KNOCKBACK_POWER",3);
        this.snowballHitDamage = fc.getDouble("SNOWBALL_DAMAGE",0.1);

        this.fishingRodPower = fc.getDouble("FISHINGROD_POWER",7);

        this.enableTargetBlock = fc.getBoolean("ENABLE_TARGET_BLOCK",true); //non ha senso, da rimuovere
        this.targetBlockExplosionPower = fc.getDouble("TRAGET_BLOCK_KNOCKBACK_POWER",1.4);
        this.targetBlockCooldown = fc.getInt("TARGET_BLOCK_COOLDOWN",15) * 20;

    }

    private void loadStartEquip() {

        FileConfiguration fileConfiguration = ConfigManager.getInstance().getConfig("Settings.yml");
        LocalizationManager lm = LocalizationManager.getInstance();
        List<String> materialName = fileConfiguration.getStringList("START_LOADOUT");

        materialName.forEach(s -> {
            String[] parts = s.split(",");
            if (parts.length != 2) return;
            Material material = Material.valueOf(parts[0]);
            ItemStack is = new ItemStack(material,Integer.parseInt(parts[1]));
            is.setLore(lm.localizeItemLore(material));
            this.startEquipItems.add(is);
        });

    }

    public void reload() {

        this.startEquipItems.clear();

        load();
    }

    public void enableEchelon(boolean value) {
        this.echelonSupport = value;
    }

    public boolean isEchelonEnabled() {
        return echelonSupport;
    }

    public long getRewarDelay() {
        return rewarDelay;
    }

    public long getRewardPeriod() {
        return rewardPeriod;
    }
}
