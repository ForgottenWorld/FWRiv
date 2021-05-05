package me.architetto.fwriv.listener.arena;

import me.architetto.fwriv.arena.ArenaManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;


public class ArenaCreationListener implements Listener {

    @EventHandler
    public void onRightClickSelection(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        ArenaManager am = ArenaManager.getInstance();

        if(!am.isPlayerInCreationMode(player)) return;

        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        if(event.getItem() == null || event.getItem().getType() != Material.STICK) return;

        Block clickedBlock = event.getClickedBlock();

        if (clickedBlock == null) return;

        am.arenaCreationHandler(player, clickedBlock.getLocation().toCenterLocation());

    }

}
