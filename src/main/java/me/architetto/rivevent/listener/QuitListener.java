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

        if (global.playerJoined.contains(player.getUniqueId())){

            player.setHealth(0);


        }
    }
}
