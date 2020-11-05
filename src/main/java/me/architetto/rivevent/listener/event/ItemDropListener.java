package me.architetto.rivevent.listener.event;

import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.event.PlayersManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemDropListener implements Listener{

    EventService eventService = EventService.getInstance();

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {

        if (!eventService.isRunning())
            return;

        if (PlayersManager.getInstance().getAllEventPlayers().contains(event.getPlayer().getUniqueId()))
            event.getItemDrop().remove();

    }

}
