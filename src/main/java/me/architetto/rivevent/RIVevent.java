package me.architetto.rivevent;

import me.architetto.rivevent.command.CommandManager;
import me.architetto.rivevent.command.GlobalVar;
import me.architetto.rivevent.listener.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

public final class RIVevent extends JavaPlugin {

    private static final String pathPreset = "plugins/Rivevent/preset.txt";
    public static Plugin plugin;

    @Override
    public void onEnable() {

        plugin = this;

        getConfig().options().copyDefaults();
        saveDefaultConfig ();

        getCommand("rivevent").setExecutor(new CommandManager());

        getServer().getPluginManager().registerEvents(new LClickListener(),this);
        getServer().getPluginManager().registerEvents(new FoodLevelListener(),this);
        getServer().getPluginManager().registerEvents(new DeathListener(),this);
        getServer().getPluginManager().registerEvents(new PositionListener(),this);
        getServer().getPluginManager().registerEvents(new SpawnListener(),this);

        File presetFile = new File(pathPreset);
        GlobalVar global = GlobalVar.getInstance();


        try{
            if (!presetFile.createNewFile ())
                global.riveventPreset = load();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static <T extends Object> void save(T obj)
            throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
                pathPreset));
        oos.writeObject(obj);
        oos.flush();
        oos.close();
    }

    public static <T extends Object> T load() throws Exception {
        ObjectInputStream
                ois =
                new ObjectInputStream(
                        new FileInputStream(
                                pathPreset));
        @SuppressWarnings("unchecked")
        T result = (T) ois.readObject();
        ois.close();
        return result;
    }



}
