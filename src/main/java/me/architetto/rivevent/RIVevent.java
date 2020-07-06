package me.architetto.rivevent;

import me.architetto.rivevent.command.admin.Create;
import me.architetto.rivevent.listener.LeftClickBlockEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class RIVevent extends JavaPlugin {

    private static Plugin plugin;


    @Override
    public void onEnable() {
        // Plugin startup logic

        plugin = this;

        //Comandi
        getCommand("rivevent").setExecutor(new Create());

        //Listener
        this.getServer ().getPluginManager ().registerEvents ( new LeftClickBlockEvent(),this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
