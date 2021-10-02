package me.architetto.fwriv.listener.parties;

import com.alessiodp.parties.api.events.bukkit.unique.BukkitPartiesFriendlyFireBlockedEvent;
import me.architetto.fwriv.partecipant.PartecipantsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class FriendlyFireListener implements Listener {

    @EventHandler
    public void onPartiesfrindlyFire(BukkitPartiesFriendlyFireBlockedEvent event) {
        if (PartecipantsManager.getInstance().isPresent(event.getPlayerAttacker().getPlayerUUID()))
            event.setCancelled(true);
    }
}
