package me.architetto.fwriv.listener.event;

import me.architetto.fwriv.partecipant.PartecipantsManager;
import me.architetto.fwriv.event.EventService;
import me.architetto.fwriv.event.EventStatus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropListener implements Listener{

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {

        EventService eventService = EventService.getInstance();

        if (eventService.getEventStatus().equals(EventStatus.INACTIVE)) return;

        if (PartecipantsManager.getInstance().isPresent(event.getPlayer()))
            event.getItemDrop().remove();

    }

}
