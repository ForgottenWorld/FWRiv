package me.architetto.fwriv.config;

import me.architetto.fwriv.localization.LocalizationManager;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SettingsHandler {

    private static SettingsHandler settingsHandler;

    public boolean echelonSupport;

    public int antiCamperStartDelay;
    public int antiCamperDamage;
    public int antiCamperFinalDamage;
    public int antiCamperGrowPeriod;
    public int antiCamperGrowValue;
    public int antiCamperRedLineTopTowerDif; //Questo non è indispensabile

    public int redLineAnimationRadius;

    public int rewardPeriod;

    public List<ItemStack> startEquipItems = new ArrayList<>();

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

        loadStartEquip();

        this.rewardPeriod = fileConfiguration.getInt("REWARD_PERIOD",20) * 20;

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


}
