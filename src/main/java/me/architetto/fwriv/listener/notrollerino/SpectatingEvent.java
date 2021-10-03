package me.architetto.fwriv.listener.notrollerino;

import com.destroystokyo.paper.event.player.PlayerStartSpectatingEntityEvent;
import me.architetto.fwriv.partecipant.PartecipantsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SpectatingEvent implements Listener {

    @EventHandler
    public void onStartSpectate(PlayerStartSpectatingEntityEvent event) {
        if (PartecipantsManager.getInstance().isPresent(event.getPlayer()))
            event.setCancelled(true);
    }

}
