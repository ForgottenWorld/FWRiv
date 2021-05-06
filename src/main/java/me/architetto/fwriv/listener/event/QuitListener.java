package me.architetto.fwriv.listener.event;

import me.architetto.fwriv.partecipant.PartecipantsManager;
import me.architetto.fwriv.event.EventService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        PartecipantsManager.getInstance().getPartecipant(event.getPlayer())
                .ifPresent(partecipant -> EventService.getInstance().partecipantLeave(event.getPlayer()));

    }
}
