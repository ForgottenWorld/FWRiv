package me.architetto.rivevent;

import me.architetto.rivevent.arena.ArenaManager;
import me.architetto.rivevent.command.CommandManager;
import me.architetto.rivevent.config.ConfigManager;
import me.architetto.rivevent.config.SettingsHandler;
import me.architetto.rivevent.listener.arena.ArenaCreationListener;
import me.architetto.rivevent.listener.event.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class RIVevent extends JavaPlugin {

    public static Plugin plugin;

    @Override
    public void onEnable() {

        plugin = this;

        Bukkit.getConsoleSender().sendMessage("=====================[      RIVe      ]======================");

        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "RIVe >>" + ChatColor.RESET + " Loading settings files...");
        loadSettingsFile();

        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "RIVe >>" + ChatColor.RESET + " Loading presets ...");
        loadPresetFile();

        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "RIVe >>" + ChatColor.RESET + " Loading commands...");
        loadCommands();

        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "RIVe >>" + ChatColor.RESET + " Loading listeners...");
        loadListener();

        Bukkit.getConsoleSender().sendMessage("=============================================================");


    }

    @Override
    public void onDisable() {


    }

    private void loadSettingsFile(){

        ConfigManager.getInstance().setPlugin(plugin);
        ConfigManager.getInstance().getConfig("Settings.yml");
        SettingsHandler.getInstance().load();

        if (SettingsHandler.getInstance().safeRespawnLocation == null)
            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "RIVe >> " + ChatColor.UNDERLINE + "Respawn point missing ...");


    }

    public void loadPresetFile() {

        ConfigManager.getInstance().getConfig("Preset.yml");

        ConfigurationSection configurationSection = ConfigManager.getInstance()
                .getConfig("Preset.yml").getConfigurationSection("");

        if (configurationSection == null)
            return;

        ArenaManager presetService = ArenaManager.getInstance();
        ConfigManager configManager = ConfigManager.getInstance();

        for (String presetName : configurationSection.getKeys(false)) {

            presetService.newArena(
                    presetName,
                    configManager.getLocation(ConfigManager.getInstance().getConfig("Preset.yml"),presetName + ".SPAWN1" ),
                    configManager.getLocation(ConfigManager.getInstance().getConfig("Preset.yml"),presetName + ".SPAWN2"),
                    configManager.getLocation(ConfigManager.getInstance().getConfig("Preset.yml"),presetName + ".SPAWN3"),
                    configManager.getLocation(ConfigManager.getInstance().getConfig("Preset.yml"),presetName + ".SPAWN4"),
                    configManager.getLocation(ConfigManager.getInstance().getConfig("Preset.yml"),presetName + ".TOWER"));

        }
    }

    public void loadCommands() {

        Objects.requireNonNull(getCommand("rivevent")).setExecutor(new CommandManager());

    }

    public void loadListener() {

        getServer().getPluginManager().registerEvents(new ArenaCreationListener(),this);
        getServer().getPluginManager().registerEvents(new PlayerInteractionListener(),this);
        getServer().getPluginManager().registerEvents(new DamageListener(),this);
        getServer().getPluginManager().registerEvents(new FoodLevelListener(),this);
        getServer().getPluginManager().registerEvents(new DeathListener(),this);
        getServer().getPluginManager().registerEvents(new ItemDropListener(),this);
        getServer().getPluginManager().registerEvents(new ProjectileHitListener(),this);
        getServer().getPluginManager().registerEvents(new QuitListener(),this);
        getServer().getPluginManager().registerEvents(new RightClickListener(),this);

    }





}
