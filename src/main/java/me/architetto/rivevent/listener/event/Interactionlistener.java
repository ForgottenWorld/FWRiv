package me.architetto.rivevent.listener.event;

import me.architetto.rivevent.event.PlayersManager;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Interactionlistener implements Listener {
    PlayersManager playersManager = PlayersManager.getInstance();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        if (playersManager.isPlayerActive(event.getPlayer().getUniqueId()))
            event.setCancelled(true);

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        if (playersManager.isPlayerActive(event.getPlayer().getUniqueId()))
            event.setCancelled(true);

    }

    @EventHandler
    public void onPlayerInteraction(PlayerInteractEvent event) {

        if (!playersManager.isPlayerActive(event.getPlayer().getUniqueId()))
            return;

        Block block = event.getClickedBlock();

        if (block != null && (Tag.DOORS.getValues().contains(block.getType()) || Tag.TRAPDOORS.getValues().contains(block.getType())))
            event.setCancelled(true);

    }
}
