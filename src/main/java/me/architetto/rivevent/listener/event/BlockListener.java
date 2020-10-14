package me.architetto.rivevent.listener.event;

import me.architetto.rivevent.event.EventService;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockListener implements Listener{

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        EventService eventService = EventService.getInstance();
        if (eventService.getParticipantsPlayers().contains(event.getPlayer().getUniqueId()))
            event.setCancelled(true);

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        EventService eventService = EventService.getInstance();
        if (eventService.getParticipantsPlayers().contains(event.getPlayer().getUniqueId()))
            event.setCancelled(true);

    }

    @EventHandler
    public void onPlayerInteraction(PlayerInteractEvent event) {

        EventService eventService = EventService.getInstance();
        if (eventService.getParticipantsPlayers().contains(event.getPlayer().getUniqueId())) {

            Block block = event.getClickedBlock();

            if (block != null) {

                if (Tag.DOORS.getValues().contains(block.getType()) || Tag.TRAPDOORS.getValues().contains(block.getType()))
                    event.setCancelled(true);
            }


        }
    }


}
