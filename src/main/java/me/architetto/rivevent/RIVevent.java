package me.architetto.rivevent;

import me.architetto.rivevent.command.CommandManager;
import me.architetto.rivevent.listener.LeftClickOnBlock;
import org.bukkit.plugin.java.JavaPlugin;

public final class RIVevent extends JavaPlugin {

    /*
    ---> SOTTOLINEO CHE E' TUTTO WIP ! <---
     */

    @Override
    public void onEnable() {
        // Plugin startup logic

        getCommand("rivevent").setExecutor(new CommandManager());

        getServer().getPluginManager().registerEvents(new LeftClickOnBlock(),this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }



}
