package me.architetto.rivevent;

import me.architetto.rivevent.command.CommandManager;
import me.architetto.rivevent.command.GameHandler;
import me.architetto.rivevent.listener.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.List;
import java.util.Objects;

public final class RIVevent extends JavaPlugin {

    private static final String pathPreset = "plugins/Rivevent/preset.txt";
    public static Plugin plugin;
    static FileConfiguration defaultConfig;

    @Override
    public void onEnable() {

        plugin = this;

        loadConfiguration();

        loadCommands();

        loadListener();

        loadPreset();

        loadRewardItemList();



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static <T> void save(T obj) throws Exception {
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

        global.endEventRespawnLocation = new Location(w, getConfig().getInt("RESPAWN.x"), getConfig().getInt("RESPAWN.y"),
                getConfig().getInt("RESPAWN.z"));


        try{
            if (!presetFile.createNewFile ()) {
                if (presetFile.length() != 0)
                    global.riveventPreset = load();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void loadCommands() {

        Objects.requireNonNull(getCommand("rivevent")).setExecutor(new CommandManager());

    }

    public void loadListener() {

        getServer().getPluginManager().registerEvents(new LeftclickListener(),this);
        getServer().getPluginManager().registerEvents(new FoodLevelListener(),this);
        getServer().getPluginManager().registerEvents(new DeathListener(),this);
        getServer().getPluginManager().registerEvents(new QuitListener(),this);
        getServer().getPluginManager().registerEvents(new DamageListener(),this);
        getServer().getPluginManager().registerEvents(new SpawnListener(),this);
        getServer().getPluginManager().registerEvents(new ProjectileHitListener(),this);


    }

    public void loadRewardItemList() {

        GameHandler global = GameHandler.getInstance();
        FileConfiguration config = getDefaultConfig();

        List<String> MaterialName = config.getStringList("LIST_ITEMS_REWARD");

        if (!MaterialName.isEmpty()){
            for(String name : MaterialName){

                String [] parts = name.split(",");

                if (Material.getMaterial(parts[0]) != null) {
                    global.itemsListWeight.put(Material.getMaterial(parts[0]), Integer.parseInt(parts[1]));
                    global.itemsListMaxAmount.put(Material.getMaterial(parts[0]),Integer.parseInt(parts[2]));
                }

            }
        }

        global.totalWeight = global.itemsListWeight.values()
                .stream()
                .mapToInt(Integer::valueOf)
                .sum();
    }




}
