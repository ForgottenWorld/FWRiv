package me.architetto.rivevent.listener;

import me.architetto.rivevent.command.GameHandler;
import me.architetto.rivevent.command.SettingsHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener{

    GameHandler global = GameHandler.getInstance();
    SettingsHandler setting = SettingsHandler.getInstance();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){

        if (!setting.allowBreackblock && global.allPlayerList().contains(event.getPlayer().getUniqueId()))
            event.setCancelled(true);

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){

        if (!setting.allowPlaceBlock && global.allPlayerList().contains(event.getPlayer().getUniqueId()))
            event.setCancelled(true);

    }


}
