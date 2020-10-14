package me.architetto.rivevent.listener.arena;

import me.architetto.rivevent.arena.ArenaManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.io.IOException;
import java.util.Objects;

public class ArenaCreationListener implements Listener{

    @EventHandler
    public void onRightClickSelection(PlayerInteractEvent event) throws IOException{
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        if(!event.getHand().equals(EquipmentSlot.HAND)) {
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
