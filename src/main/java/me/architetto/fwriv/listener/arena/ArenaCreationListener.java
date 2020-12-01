package me.architetto.fwriv.listener.arena;

import me.architetto.fwriv.arena.ArenaManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class ArenaCreationListener implements Listener{

    @EventHandler
    public void onRightClickSelection(PlayerInteractEvent event) {
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        if(event.getItem() == null || event.getItem().getType() != Material.STICK) {
            return;
        }

        Player player = event.getPlayer();
        if(!ArenaManager.getInstance().isPlayerInCreationMode(player)) {
            return;
        }

        ArenaManager presetService = ArenaManager.getInstance();
        presetService.arenaCreationHandler(player, Objects.requireNonNull(event.getClickedBlock()).getLocation());
    }

}
