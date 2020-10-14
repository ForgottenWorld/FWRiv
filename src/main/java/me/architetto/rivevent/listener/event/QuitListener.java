package me.architetto.rivevent.listener.event;

import me.architetto.rivevent.config.SettingsHandler;
import me.architetto.rivevent.event.EventService;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;


public class QuitListener implements Listener {

    EventService eventService = EventService.getInstance();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        if (!eventService.isRunning())
            return;

        if (eventService.getParticipantsPlayers().contains(event.getPlayer().getUniqueId())) {

            event.getPlayer().setHealth(0);
            //todo: se non si vuole far morire bisogna fare in modo che ci sia un unico metodo che controlla se c'è un vincitore dell'evento

        } else if (eventService.getEliminatedPlayers().contains(event.getPlayer().getUniqueId())) {

            eventService.getEliminatedPlayers().remove(event.getPlayer().getUniqueId());
            event.getPlayer().teleport(SettingsHandler.getInstance().respawnLocation);
            event.getPlayer().setGameMode(GameMode.SURVIVAL);

        }
    }
}
