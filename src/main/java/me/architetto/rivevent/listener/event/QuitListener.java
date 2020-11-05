package me.architetto.rivevent.listener.event;

import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.event.PlayersManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;


public class QuitListener implements Listener {

    EventService eventService = EventService.getInstance();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        if (!eventService.isRunning())
            return;

        Player player = event.getPlayer();
        if (PlayersManager.getInstance().getActivePlayers().contains(player.getUniqueId())) {

            eventService.activePlayerLeave(player.getUniqueId());

        } else if (PlayersManager.getInstance().getDeathPlayers().contains(event.getPlayer().getUniqueId())) {

            eventService.spectatorPlayerLeave(player.getUniqueId());

        }
    }
}
