package me.architetto.rivevent;

import me.architetto.rivevent.command.CommandManager;
import me.architetto.rivevent.command.GameHandler;
import me.architetto.rivevent.command.SettingsHandler;
import me.architetto.rivevent.listener.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.Objects;
import java.util.UUID;

public final class RIVevent extends JavaPlugin {

    private static final String pathPreset = "plugins/Rivevent/preset.txt";
    public static Plugin plugin;

    GameHandler global = GameHandler.getInstance();
    SettingsHandler settings = SettingsHandler.getInstance();


    @Override
    public void onEnable() {

        plugin = this;

        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[RIVevent]" + ChatColor.RESET + " Loading configuration...");
        loadConfiguration();

        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[RIVevent]" + ChatColor.RESET + " Loading settings...");
        loadSettings();

        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[RIVevent]" + ChatColor.RESET + " Loading commands...");
        loadCommands();

        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[RIVevent]" + ChatColor.RESET + " Loading listeners...");
        loadListener();

        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[RIVevent]" + ChatColor.RESET + " Loading preset...");
        loadRIVPreset();

    }

    @Override
    public void onDisable() {

        if (global.setupStartFlag && !global.playerJoined.isEmpty())
            clearPlayerJoinedInventory();


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

    public void loadConfiguration() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }

    public void loadRIVPreset() {

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

        getServer().getPluginManager().registerEvents(new RightClickListener(),this);
        getServer().getPluginManager().registerEvents(new FoodLevelListener(),this);
        getServer().getPluginManager().registerEvents(new DeathListener(),this);
        getServer().getPluginManager().registerEvents(new QuitListener(),this);
        getServer().getPluginManager().registerEvents(new DamageListener(),this);
        getServer().getPluginManager().registerEvents(new SpawnListener(),this);
        getServer().getPluginManager().registerEvents(new ProjectileHitListener(),this);
        getServer().getPluginManager().registerEvents(new BlockListener(),this);
        getServer().getPluginManager().registerEvents(new ItemDropListener(),this);


    }

    public void loadSettings() {

        settings.load();

        global.loadRewardItemList();
        global.loadStartLoadout();

    }

    public void clearPlayerJoinedInventory() {

        Player player;

        for (UUID target : global.playerJoined) {
            player = Bukkit.getPlayer(target);

            if (player != null)
                player.getInventory().clear();
        }

    }




}
