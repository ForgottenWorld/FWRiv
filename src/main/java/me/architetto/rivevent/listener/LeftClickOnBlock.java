package me.architetto.rivevent.listener;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.command.GlobalVar;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.LocSerialization;
import me.architetto.rivevent.util.Messages;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.UUID;

public class LeftClickOnBlock implements Listener{

    GlobalVar global = GlobalVar.getInstance();

    private final  HashMap<UUID, HashMap<LOC, String>> tempHashMap = new HashMap<UUID, HashMap<LOC, String>>();
    public enum LOC {
        SPAWN1,
        SPAWN2,
        SPAWN3,
        SPAWN4,
        TOWER,
        SPECTATE
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) throws Exception{
        Player player = event.getPlayer();


        if (global.listenerActivator.containsKey(player.getUniqueId()) && event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if (!tempHashMap.containsKey(player.getUniqueId())) {
                tempHashMap.put(player.getUniqueId(), new HashMap<LOC, String>());
            }

            switch (tempHashMap.get(player.getUniqueId()).size()) {

                case 5:
                    event.getClickedBlock().setType(Material.GOLD_BLOCK);
                    player.playSound(player.getLocation (), Sound.ENTITY_PLAYER_LEVELUP, 5, 1);

                    tempHashMap.get(player.getUniqueId()).put(LOC.TOWER, LocSerialization.getSerializedLocation(event.getClickedBlock().getLocation().add(0,1,0)));


                    global.riveventPreset.put(global.listenerActivator.get(player.getUniqueId()),tempHashMap.get(player.getUniqueId()));
                    RIVevent.save(global.riveventPreset);
                    global.listenerActivator.remove(player.getUniqueId());


                    tempHashMap.remove(player.getUniqueId());
                    player.sendMessage(ChatMessages.GREEN(Messages.OKPRESET));

                    return;


                case 4:

                    event.getClickedBlock().setType(Material.GOLD_BLOCK);
                    player.playSound(player.getLocation (), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, 1);

                    tempHashMap.get(player.getUniqueId()).put(LOC.SPECTATE, LocSerialization.getSerializedLocation(event.getClickedBlock().getLocation().add(0,1,0)));
                    player.sendMessage(ChatMessages.PosMessage("6/6", LOC.TOWER));
                    return;

                case 3:

                    event.getClickedBlock().setType(Material.GOLD_BLOCK);
                    player.playSound(player.getLocation (), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, 1);

                    tempHashMap.get(player.getUniqueId()).put(LOC.SPAWN4, LocSerialization.getSerializedLocation(event.getClickedBlock().getLocation().add(0,1,0)));
                    player.sendMessage(ChatMessages.PosMessage("5/6", LOC.SPECTATE));

                    return;

                case 2:

                    event.getClickedBlock().setType(Material.GOLD_BLOCK);
                    player.playSound(player.getLocation (), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, 1);

                    tempHashMap.get(player.getUniqueId()).put(LOC.SPAWN3, LocSerialization.getSerializedLocation(event.getClickedBlock().getLocation().add(0,1,0)));
                    player.sendMessage(ChatMessages.PosMessage("4/6", LOC.SPAWN4));

                    return;

                case 1:

                    event.getClickedBlock().setType(Material.GOLD_BLOCK);
                    player.playSound(player.getLocation (), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, 1);

                    tempHashMap.get(player.getUniqueId()).put(LOC.SPAWN2, LocSerialization.getSerializedLocation(event.getClickedBlock().getLocation().add(0,1,0)));
                    player.sendMessage(ChatMessages.PosMessage("3/6", LOC.SPAWN3));

                    return;

                case 0:

                    event.getClickedBlock().setType(Material.GOLD_BLOCK);
                    player.playSound(player.getLocation (), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, 1);

                    tempHashMap.get(player.getUniqueId()).put(LOC.SPAWN1, LocSerialization.getSerializedLocation(event.getClickedBlock().getLocation().add(0,1,0)));

                    player.sendMessage(ChatMessages.PosMessage("2/6", LOC.SPAWN2));

            }
        }
    }
}
