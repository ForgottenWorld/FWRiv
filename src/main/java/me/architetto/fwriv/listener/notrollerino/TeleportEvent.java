package me.architetto.fwriv.listener.notrollerino;

import me.architetto.fwriv.localization.Message;
import me.architetto.fwriv.partecipant.PartecipantsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportEvent implements Listener {

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if (PartecipantsManager.getInstance().isPresent(event.getPlayer())
                && event.getCause() == PlayerTeleportEvent.TeleportCause.SPECTATE) {
            Message.ERR_TPDISABLED.send(event.getPlayer());
            event.setCancelled(true);
        }
    }

}
