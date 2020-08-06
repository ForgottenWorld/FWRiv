package me.architetto.rivevent;

import me.architetto.rivevent.command.CommandManager;
import me.architetto.rivevent.command.GameHandler;
import me.architetto.rivevent.listener.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.Objects;

public final class RIVevent extends JavaPlugin {

    private static final String pathPreset = "plugins/Rivevent/preset.txt";
    public static Plugin plugin;
    static FileConfiguration defaultConfig;

    @Override
    public void onEnable() {

        plugin = this;

        loadConfiguration();

        loadPreset();

        Objects.requireNonNull(getCommand("rivevent")).setExecutor(new CommandManager());

        getServer().getPluginManager().registerEvents(new LeftclickListener(),this);
        getServer().getPluginManager().registerEvents(new FoodLevelListener(),this);
        getServer().getPluginManager().registerEvents(new DeathListener(),this);
        getServer().getPluginManager().registerEvents(new QuitListener(),this);
        getServer().getPluginManager().registerEvents(new DamageListener(),this);
        getServer().getPluginManager().registerEvents(new SpawnListener(),this);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static <T> void save(T obj)
            throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
                pathPreset));
        oos.writeObject(obj);
        oos.flush();
        oos.close();
    }

    public static <T> T load() throws Exception {
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

    private void loadConfiguration() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        defaultConfig = getConfig();
    }

    public static FileConfiguration getDefaultConfig() {
        return defaultConfig;
    }


    public void loadPreset () {

        GameHandler global = GameHandler.getInstance();
        File presetFile = new File(pathPreset);

        World w = Bukkit.getServer().getWorld(Objects.requireNonNull(getConfig().getString("RESPAWN.w")));

        global.respawnLoc = new Location(w, getConfig().getInt("RESPAWN.x"), getConfig().getInt("RESPAWN.y"),
                getConfig().getInt("RESPAWN.z"));

        global.experimentalMode = getConfig().getBoolean("EXPERIMENTAL_MODE");

        try{
            if (!presetFile.createNewFile ()) {
                if (presetFile.length() != 0)
                    global.riveventPreset = load();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    

}
