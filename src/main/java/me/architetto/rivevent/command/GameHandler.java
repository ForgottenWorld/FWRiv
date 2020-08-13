package me.architetto.rivevent.command;

import me.architetto.rivevent.listener.LeftclickListener;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.security.SecureRandom;
import java.util.*;

public class GameHandler{

    private static GameHandler instance;

    public HashMap<String, HashMap<LeftclickListener.LOC,String>> riveventPreset = new HashMap<>();
    public HashMap<UUID, String> listenerActivator = new HashMap<>();

    public String presetSummon = "";
    public List<UUID> playerJoined = new ArrayList<>();
    public List<UUID> playerSpectate = new ArrayList<>();
    public List<UUID> playerOut = new ArrayList<>(); //Player che vengono eliminati durante l'evento

    public boolean setupStart = false;
    public boolean setupDone = false;
    public boolean startDone = false;

    public List<Block> doorsToOpen = new ArrayList<>();
    public Location endEventRespawnLocation;

    public HashMap<Material,Integer> itemsListWeight = new HashMap<>();
    public HashMap<Material,Integer> itemsListMaxAmount = new HashMap<>();
    public int totalWeight;
    


    private GameHandler(){

        //NOT IMPLEMENTED - WIP - EXPERIMENTAL MODE {

        /*
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

         */


    }

    public static GameHandler getInstance(){
        if(instance==null)
            instance = new GameHandler();

        return instance;
    }

    public void clearEventVariables() {
        playerSpectate.clear();
        playerJoined.clear();
        playerOut.clear();
        presetSummon = "";
        setupStart = false;
        setupDone = false;
        startDone = false;
        doorsToOpen.clear();
    }

    public Material pickRandomItem() {

        SecureRandom secureRandom = new SecureRandom();
        int randomValue = secureRandom.nextInt(totalWeight) + secureRandom.nextInt(totalWeight) + 10;

        while (randomValue > 0){

            for(Material material : itemsListWeight.keySet()){

                randomValue -= (secureRandom.nextInt(itemsListWeight.get(material)) + 1);

                if (randomValue <= 0) {

                    return material;

                }
            }
        }


        return null;
    }

    public int pickRandomAmount(Material material) {

        if (itemsListMaxAmount.get(material) == 1)
            return 1;

        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.nextInt(itemsListMaxAmount.get(material)) + 1;


    }

}
