package me.architetto.rivevent.command;

import me.architetto.rivevent.listener.LeftClickOnBlock;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.UUID;

public class GlobalVar{

    private static GlobalVar instance;

    public HashMap<String, HashMap<LeftClickOnBlock.LOC,String>> riveventPreset = new HashMap<>();
    public HashMap<UUID, String> listenerActivator = new HashMap<>();

    public String presetSummon = "";
    public HashMap<UUID, Location> playerJoined = new HashMap<>();
    public HashMap<UUID,Location> playersSpectate = new HashMap<>();

    public boolean setupStart = false;
    public boolean setupDone = false;


    private GlobalVar(){

    }

    public static GlobalVar getInstance(){
        if(instance==null)
            instance = new GlobalVar();

        return instance;
    }




}
