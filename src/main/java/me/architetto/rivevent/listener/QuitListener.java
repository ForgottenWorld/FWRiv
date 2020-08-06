package me.architetto.rivevent.listener;


import me.architetto.rivevent.command.GlobalVar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;


public class QuitListener implements Listener{

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){

        GlobalVar global = GlobalVar.getInstance();
        Player player = event.getPlayer();

        if (global.playerJoined.contains(player.getUniqueId())) {

            if (global.setupStart) {

                player.getInventory().clear();
                player.setHealth(0);

            } else {

                player.teleport(global.respawnLoc);

            }

            global.playerJoined.remove(player.getUniqueId());
            return;
        }

        if (global.playerSpectate.contains(player.getUniqueId())) {

            player.teleport(global.respawnLoc);
            global.playerSpectate.remove(player.getUniqueId());

        }
    }
}
