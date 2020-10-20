package me.architetto.rivevent.listener.event;

import me.architetto.rivevent.config.SettingsHandler;
import me.architetto.rivevent.event.EventService;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;


public class QuitListener implements Listener {

    EventService eventService = EventService.getInstance();
    SettingsHandler settingsHandler = SettingsHandler.getInstance();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        if (!eventService.isRunning())
            return;

        Player player = event.getPlayer();
        if (eventService.getParticipantsPlayers().contains(player.getUniqueId())) {

            eventService.removePartecipant(player.getUniqueId());
            player.teleport(settingsHandler.respawnLocation);

        } else if (eventService.getEliminatedPlayers().contains(event.getPlayer().getUniqueId())) {

            eventService.removeEliminated(event.getPlayer().getUniqueId());
            event.getPlayer().teleport(SettingsHandler.getInstance().respawnLocation);
            event.getPlayer().setGameMode(GameMode.SURVIVAL);

        }
    }
}
