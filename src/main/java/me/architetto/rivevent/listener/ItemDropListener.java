package me.architetto.rivevent.listener;

import me.architetto.rivevent.command.GameHandler;
import me.architetto.rivevent.util.ChatMessages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemDropListener implements Listener{

    GameHandler global = GameHandler.getInstance();

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event){

        if (global.presetSummon.isEmpty() || !global.setupStartFlag)
            return;

        if (global.playerJoined.contains(event.getPlayer().getUniqueId())) {
            event.getItemDrop().remove();
        }

        if (global.playerSpectate.contains(event.getPlayer().getUniqueId())) {
            event.getPlayer().sendMessage(ChatMessages.RED("Non puoi droppare items in questo momento!"));
            event.setCancelled(true);
        }

    }

}
