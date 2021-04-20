package me.architetto.fwriv.listener.event;

import me.architetto.fwriv.partecipant.PartecipantsManager;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Interactionlistener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        PartecipantsManager.getInstance().getPartecipant(event.getPlayer())
                .ifPresent(partecipant -> event.setCancelled(true));

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        PartecipantsManager.getInstance().getPartecipant(event.getPlayer())
                .ifPresent(partecipant -> event.setCancelled(true));

    }

    @EventHandler
    public void onPlayerInteraction(PlayerInteractEvent event) {

        PartecipantsManager.getInstance().getPartecipant(event.getPlayer())
                .ifPresent(partecipant -> {

                    Block block = event.getClickedBlock();

                    if (block != null &&
                            (Tag.DOORS.getValues().contains(block.getType())
                                    || Tag.TRAPDOORS.getValues().contains(block.getType())
                                    || Tag.FENCE_GATES.getValues().contains(block.getType())))
                        event.setCancelled(true);
                });


    }
}
