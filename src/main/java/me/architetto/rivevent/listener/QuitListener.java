package me.architetto.rivevent.listener;


import me.architetto.rivevent.command.GameHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;


public class QuitListener implements Listener{

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){

        GameHandler global = GameHandler.getInstance();
        Player player = event.getPlayer();

        if (global.playerJoined.contains(player.getUniqueId())) {

            global.playerJoined.remove(player.getUniqueId()); //per evitare l'eventuale attivazione di eventi legati al deathListener

            if (global.setupStart) {

                player.getInventory().clear();
                player.setHealth(0);

            } else {

                player.teleport(global.endEventRespawnLocation);

            }

            return;
        }

        if (global.playerSpectate.contains(player.getUniqueId())) {

            player.teleport(global.endEventRespawnLocation);
            global.playerSpectate.remove(player.getUniqueId());

        }
    }
}
