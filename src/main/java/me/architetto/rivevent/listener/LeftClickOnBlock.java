package me.architetto.rivevent.listener;

import me.architetto.rivevent.command.subcommand.CreateCommand;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.Messages;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.UUID;

public class LeftClickOnBlock implements Listener{

    private final  HashMap<UUID, HashMap<LOC, Location>> tempHashMap = new HashMap<UUID, HashMap<LOC, Location>>();
    public enum LOC {
        SPAWN1,
        SPAWN2,
        SPAWN3,
        SPAWN4,
        TOWER,
        SPECTATE
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();


        if(CreateCommand.listenerActivator.containsKey(player.getUniqueId()) && event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if(!tempHashMap.containsKey(player.getUniqueId())){
                tempHashMap.put(player.getUniqueId(), new HashMap<LOC, Location>());
            }

            //Manca tutta la parte relativa al caricare e salvare l'hashmap

            switch(tempHashMap.get(player.getUniqueId()).size()){

                case 5:

                    tempHashMap.get(player.getUniqueId()).put(LOC.TOWER, event.getClickedBlock().getLocation());
                    CreateCommand.riveventPreset.put(CreateCommand.listenerActivator.get(player.getUniqueId()),tempHashMap.get(player.getUniqueId()));
                    CreateCommand.listenerActivator.remove(player.getUniqueId());
                    tempHashMap.remove(player.getUniqueId());
                    player.sendMessage(ChatMessages.GREEN(Messages.OK_PRESET));

                    return;


                case 4:

                    tempHashMap.get(player.getUniqueId()).put(LOC.SPECTATE, event.getClickedBlock().getLocation());
                    player.sendMessage(ChatMessages.PosMessage("6/6", LOC.TOWER));
                    return;

                case 3:

                    tempHashMap.get(player.getUniqueId()).put(LOC.SPAWN4, event.getClickedBlock().getLocation());
                    player.sendMessage(ChatMessages.PosMessage("5/6", LOC.SPECTATE));
                    return;

                case 2:

                    tempHashMap.get(player.getUniqueId()).put(LOC.SPAWN3, event.getClickedBlock().getLocation());
                    player.sendMessage(ChatMessages.PosMessage("4/6", LOC.SPAWN4));
                    return;

                case 1:

                    tempHashMap.get(player.getUniqueId()).put(LOC.SPAWN2, event.getClickedBlock().getLocation());
                    player.sendMessage(ChatMessages.PosMessage("3/6", LOC.SPAWN3));
                    return;

                case 0:

                    tempHashMap.get(player.getUniqueId()).put(LOC.SPAWN1, event.getClickedBlock().getLocation());
                    player.sendMessage(ChatMessages.PosMessage("2/6", LOC.SPAWN2));

            }

        }
    }



}
