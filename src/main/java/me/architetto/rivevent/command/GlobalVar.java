package me.architetto.rivevent.command;

import me.architetto.rivevent.listener.LeftClickListener;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class GlobalVar{

    private static GlobalVar instance;

    public HashMap<String, HashMap<LeftClickListener.LOC,String>> riveventPreset = new HashMap<>();
    public HashMap<UUID, String> listenerActivator = new HashMap<>();

    public String presetSummon = "";
    public List<UUID> playerJoined = new ArrayList<>();
    public List<UUID> playerSpectate = new ArrayList<>();

    public boolean setupStart = false;
    public boolean setupDone = false;

    public List<Block> doorsToOpen = new ArrayList<>();
    public List<PotionEffectType> positivePotionEffects = new ArrayList<>();
    public List<PotionEffectType> negativePotionEffects = new ArrayList<>();
    public List<Material> itemList = new ArrayList<>();


    private GlobalVar(){

        positivePotionEffects.add(PotionEffectType.REGENERATION);
        positivePotionEffects.add(PotionEffectType.DAMAGE_RESISTANCE);
        positivePotionEffects.add(PotionEffectType.INCREASE_DAMAGE);
        positivePotionEffects.add(PotionEffectType.INVISIBILITY);
        positivePotionEffects.add(PotionEffectType.HEAL);
        positivePotionEffects.add(PotionEffectType.HEALTH_BOOST);

        negativePotionEffects.add(PotionEffectType.BLINDNESS);
        negativePotionEffects.add(PotionEffectType.CONFUSION);
        negativePotionEffects.add(PotionEffectType.POISON);
        negativePotionEffects.add(PotionEffectType.WEAKNESS);
        negativePotionEffects.add(PotionEffectType.WITHER);
        negativePotionEffects.add(PotionEffectType.SLOW);

        itemList.add(Material.MILK_BUCKET);
        itemList.add(Material.GOLDEN_APPLE);
        itemList.add(Material.WOODEN_HOE);
        itemList.add(Material.LEATHER_CHESTPLATE);
        itemList.add(Material.LEATHER_HELMET);
        itemList.add(Material.LEATHER_BOOTS);
        itemList.add(Material.LEATHER_LEGGINGS);
        itemList.add(Material.EMERALD);
        itemList.add(Material.COOKED_PORKCHOP);
        itemList.add(Material.SUGAR);
        itemList.add(Material.GLOWSTONE_DUST);
        itemList.add(Material.BAKED_POTATO);
        itemList.add(Material.POISONOUS_POTATO);


    }

    public static GlobalVar getInstance(){
        if(instance==null)
            instance = new GlobalVar();

        return instance;
    }

    public  void clearVar () {
        playerSpectate.clear();
        playerJoined.clear();
        presetSummon = "";
        setupStart = false;
        setupDone = false;
    }
}
