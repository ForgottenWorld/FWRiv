package me.architetto.rivevent.command;

import me.architetto.rivevent.listener.LeftClickListener;
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
    public List<PotionEffectType> positiveEffects = new ArrayList<>();
    public List<PotionEffectType> negativeEffects = new ArrayList<>();


    private GlobalVar(){

        positiveEffects.add(PotionEffectType.REGENERATION);
        positiveEffects.add(PotionEffectType.DAMAGE_RESISTANCE);
        positiveEffects.add(PotionEffectType.INCREASE_DAMAGE);
        positiveEffects.add(PotionEffectType.INVISIBILITY);
        positiveEffects.add(PotionEffectType.HEAL);
        positiveEffects.add(PotionEffectType.HEALTH_BOOST);

        negativeEffects.add(PotionEffectType.BLINDNESS);
        negativeEffects.add(PotionEffectType.CONFUSION);
        negativeEffects.add(PotionEffectType.POISON);
        negativeEffects.add(PotionEffectType.WEAKNESS);
        negativeEffects.add(PotionEffectType.WITHER);
        negativeEffects.add(PotionEffectType.SLOW);

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
