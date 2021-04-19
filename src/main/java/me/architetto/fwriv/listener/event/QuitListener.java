package me.architetto.fwriv.listener.event;

import me.architetto.fwriv.partecipant.PartecipantStatus;
import me.architetto.fwriv.event.PartecipantsManager;
import me.architetto.fwriv.event.service.EventService;
import me.architetto.fwriv.event.service.EventStatus;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        EventService eventService = EventService.getInstance();
        if (eventService.getEventStatus().equals(EventStatus.INACTIVE))
            return;

        PartecipantsManager.getInstance().getPartecipant(event.getPlayer()).ifPresent(partecipant -> {
            Player player = event.getPlayer();
            if (partecipant.getPartecipantStatus().equals(PartecipantStatus.PLAYING))
                eventService.partecipantLeave(player);
            else
                eventService.spectatorPlayerLeave(player);
        });

    }
}
