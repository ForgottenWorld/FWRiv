package me.architetto.rivevent.command;

import me.architetto.rivevent.listener.LeftclickListener;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.security.SecureRandom;
import java.util.*;

public class GameHandler{

    private static GameHandler instance;

    public HashMap<String, HashMap<LeftclickListener.LOC,String>> riveventPreset = new HashMap<>();
    public HashMap<UUID, String> listenerActivator = new HashMap<>();

    public String presetSummon = "";
    public List<UUID> playerJoined = new ArrayList<>();
    public List<UUID> playerSpectate = new ArrayList<>();

    public List<UUID> playerOut = new ArrayList<>();

    public HashMap<Material,Integer> startLoadOut = new HashMap<>();

    public boolean setupStartFlag = false;
    public boolean setupDoneFlag = false;
    public boolean startDoneFlag = false;
    public boolean miniEventFlag = false;

    public List<Block> doorsToOpen = new ArrayList<>();
    public Location endEventRespawnLocation;

    public HashMap<Material,Double> itemsListWeight = new HashMap<>();
    public HashMap<Material,Integer> itemsListMaxAmount = new HashMap<>();
    public Double totalWeight;

    public boolean curseEventFlag = false;
    public Player cursedPlayer;
    


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

    public List<UUID> allPlayerList() {

        List<UUID> mergedList = new ArrayList<>(playerJoined);
        mergedList.addAll(playerSpectate);

        return mergedList;
    }

    public void clearEventVariables() {
        playerSpectate.clear();
        playerJoined.clear();
        playerOut.clear();
        presetSummon = "";
        setupStartFlag = false;
        setupDoneFlag = false;
        startDoneFlag = false;
        curseEventFlag = false;
        doorsToOpen.clear();
    }


    public Material pickRandomItem() {

        SecureRandom secureRandom = new SecureRandom();
        double randomValue = totalWeight + secureRandom.nextInt((int) Math.round(totalWeight)) + totalWeight * secureRandom.nextDouble();

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

    public int pickRandomAmount(Material material) {

        if (itemsListMaxAmount.get(material) == 1)
            return 1;

        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.nextInt(itemsListMaxAmount.get(material)) + 1;


    }

}
