package me.architetto.rivevent.command;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.listener.RightClickListener;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.security.SecureRandom;
import java.util.*;

public class GameHandler{

    private static GameHandler instance;

    public HashMap<String, HashMap<RightClickListener.Step,String>> riveventPreset = new HashMap<>();
    public HashMap<UUID, String> listenerActivator = new HashMap<>();

    public String presetSummon = "";
    public List<UUID> playerJoined = new ArrayList<>();
    public List<UUID> playerSpectate = new ArrayList<>();

    public List<UUID> playerOut = new ArrayList<>();

    public HashMap<Material,Integer> startLoadOut = new HashMap<>();

    public boolean setupStartFlag = false;
    public boolean setupDoneFlag = false;
    public boolean startDoneFlag = false;

    public List<Block> doorsToOpen = new ArrayList<>();
    public Location endEventRespawnLocation;

    public HashMap<Material,Double> itemsListWeight = new HashMap<>();
    public HashMap<Material,Integer> itemsListMaxAmount = new HashMap<>();
    public Double totalWeight;

    //MINIGAME VARIABLES
    public boolean curseEventFlag = false;
    public Player cursedPlayer;

    public boolean backToLifeEventFlag = false;

    public boolean fallDownEventFlag = false;

    public boolean deathRaceEventFlag = false;
    public boolean deathRaceStartRunnable = false;

    private GameHandler(){
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

    public void resetEventVariables() {
        playerSpectate.clear();
        playerJoined.clear();
        playerOut.clear();
        presetSummon = "";
        setupStartFlag = false;
        setupDoneFlag = false;
        startDoneFlag = false;
        doorsToOpen.clear();

        //minigames flag
        shutdownMinigames();
    }

    public void shutdownMinigames() {

        curseEventFlag = false;
        backToLifeEventFlag = false;
        fallDownEventFlag = false;
        deathRaceEventFlag = false;

    }

    public void restartEvent() {

        setupStartFlag = false;
        setupDoneFlag = false;
        startDoneFlag = false;
        doorsToOpen.clear();

        //minigames flag
        shutdownMinigames();

    }


    public Material pickRandomItem() {

        SecureRandom secureRandom = new SecureRandom();
        double randomValue = totalWeight + secureRandom.nextInt((int) Math.round(totalWeight))
                + totalWeight * secureRandom.nextDouble();

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

    public boolean isMinigameInProgress() {

        return curseEventFlag || backToLifeEventFlag || fallDownEventFlag || deathRaceEventFlag;

    }

// LOAD & RELOAD

    public void loadStartLoadout() {

        List<String> MaterialName = RIVevent.plugin.getConfig().getStringList("START_LOADOUT");

        if (!MaterialName.isEmpty()){
            for(String name : MaterialName){

                String [] parts = name.split(",");

                if (Material.getMaterial(parts[0]) != null) {
                    startLoadOut.put(Material.getMaterial(parts[0]),Math.abs(Integer.parseInt(parts[1])));
                }

            }
        }

    }

    public void loadRewardItemList() {

        //FileConfiguration config = getDefaultConfig();

        List<String> MaterialName = RIVevent.plugin.getConfig().getStringList("LIST_ITEMS_REWARD");

        if (!MaterialName.isEmpty()) {
            for(String name : MaterialName){

                String [] parts = name.split(",");

                if (Material.getMaterial(parts[0]) != null) {
                    itemsListWeight.put(Material.getMaterial(parts[0]), Math.abs(Double.parseDouble(parts[1])));
                    itemsListMaxAmount.put(Material.getMaterial(parts[0]), Math.abs(Integer.parseInt(parts[2])));
                }

            }
        }

        totalWeight = itemsListWeight.values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .sum();
    }

}
