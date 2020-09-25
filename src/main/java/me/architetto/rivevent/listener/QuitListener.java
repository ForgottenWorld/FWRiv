package me.architetto.rivevent.listener;


import me.architetto.rivevent.command.GameHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;


public class QuitListener implements Listener{

    GameHandler global = GameHandler.getInstance();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){

        Player player = event.getPlayer();

        if (global.playerJoined.contains(player.getUniqueId())) {

            global.playerJoined.remove(player.getUniqueId());

            if (global.setupStartFlag) {

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
            global.playerOut.remove(player.getUniqueId());

        }
    }
}
