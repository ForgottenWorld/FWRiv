package me.architetto.rivevent.listener;

import me.architetto.rivevent.command.admin.Create;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Collections;
import java.util.HashMap;

public class LeftClickBlockEvent implements Listener{

    private HashMap<String,Location> tempHashMap = new HashMap<String, Location>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) throws Exception{
        Player player = event.getPlayer();


        if(Create.presetName.containsKey(player.getUniqueId()) && event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {


            if(tempHashMap.size()==5){
                tempHashMap.put("HighestTowerPoint", event.getClickedBlock().getLocation());
                Create.riveventPreset.put(Create.presetName.get(player.getUniqueId()),tempHashMap);
                player.sendMessage("Hai inserito tutti i punti necessari. Il preset e' stato creato !");
                player.sendMessage(""+Collections.singletonList(tempHashMap));
                Create.presetName.remove(player.getUniqueId());


                //SLAPI.save(Create.riveventPreset,"plugins/RIVevent/presetRIVevents.txt");

                //Va salvata l'hashmap

            }

            if(tempHashMap.size()==4){
                tempHashMap.put("SpectatePoint", event.getClickedBlock().getLocation());
                player.sendMessage("[6/6] Inserisci il punto piu' alto della torre ...");
            }
            if(tempHashMap.size()==3){
                tempHashMap.put("QuartoPuntoSpawn", event.getClickedBlock().getLocation());
                player.sendMessage("[5/6] Inserisci il punto di spectate ...");
            }
            if(tempHashMap.size()==2){
                tempHashMap.put("TerzoPuntoSpawn", event.getClickedBlock().getLocation());
                player.sendMessage("[4/6] Inserisci il quarto punto di spawn ...");
            }
            if(tempHashMap.size()==1){
                tempHashMap.put("SecondoPuntoSpawn", event.getClickedBlock().getLocation());
                player.sendMessage("[3/6] Inserisci il terzo punto di spawn ...");
            }
            if(tempHashMap.isEmpty()){
                tempHashMap.put("PrimoPuntoSpawn", event.getClickedBlock().getLocation());
                player.sendMessage("[2/6] Inserisci il secondo punto di spawn ...");
            }







        }
    }
}
