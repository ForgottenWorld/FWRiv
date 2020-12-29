package me.architetto.fwriv;

import me.architetto.fwriv.arena.ArenaManager;
import me.architetto.fwriv.command.CommandManager;
import me.architetto.fwriv.config.ConfigManager;
import me.architetto.fwriv.config.SettingsHandler;
import me.architetto.fwriv.echelon.EchelonHolder;
import me.architetto.fwriv.event.PlayersManager;
import me.architetto.fwriv.event.service.EventService;
import me.architetto.fwriv.listener.arena.ArenaCreationListener;
import me.architetto.fwriv.listener.event.*;
import me.architetto.fwriv.utils.ChatFormatter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.UUID;

public final class FWRiv extends JavaPlugin {

    public static Plugin plugin;

    @Override
    public void onEnable() {

        plugin = this;

        Bukkit.getConsoleSender().sendMessage("=====================[      RIVe      ]======================");

        Bukkit.getConsoleSender().sendMessage(ChatFormatter.pluginPrefix() + " Loading settings files...");
        loadSettingsFile();

        Bukkit.getConsoleSender().sendMessage(ChatFormatter.pluginPrefix() + " Loading commands...");
        loadCommands();

        Bukkit.getConsoleSender().sendMessage(ChatFormatter.pluginPrefix() + " Loading listeners...");
        loadListener();

        Bukkit.getConsoleSender().sendMessage(ChatFormatter.pluginPrefix() + " Loading presets ...");
        loadPresetFile();

        Bukkit.getConsoleSender().sendMessage(ChatFormatter.pluginPrefix() + " Loading FWEchelon support ...");
        loadEchelon();

        Bukkit.getConsoleSender().sendMessage("=============================================================");

    }

    @Override
    public void onDisable() {

        if (EventService.getInstance().isRunning()) {
            Bukkit.getConsoleSender().sendMessage(ChatFormatter.pluginPrefix() + "Secure inventories clear...");
            secureInventoryClear();
        }

    }

    private void loadSettingsFile(){

        ConfigManager.getInstance().setPlugin(plugin);
        ConfigManager.getInstance().getConfig("Settings.yml");
        SettingsHandler.getSettingsHandler().load();

        if (SettingsHandler.getSettingsHandler().safeRespawnLocation == null)
            Bukkit.getConsoleSender().sendMessage(ChatFormatter.pluginPrefix() + ChatColor.YELLOW + ChatColor.UNDERLINE + " Respawn point missing ...");

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

        Objects.requireNonNull(getCommand("fwriv")).setExecutor(new CommandManager());

    }

    public void loadListener() {

        getServer().getPluginManager().registerEvents(new ArenaCreationListener(),this);
        getServer().getPluginManager().registerEvents(new Interactionlistener(),this);
        getServer().getPluginManager().registerEvents(new DamageListener(),this);
        getServer().getPluginManager().registerEvents(new FoodListener(),this);
        getServer().getPluginManager().registerEvents(new DeathListener(),this);
        getServer().getPluginManager().registerEvents(new DropListener(),this);
        getServer().getPluginManager().registerEvents(new ProjectileListener(),this);
        getServer().getPluginManager().registerEvents(new QuitListener(),this);
        getServer().getPluginManager().registerEvents(new RightClickListener(),this);

    }

    public void loadEchelon() {

        if (Bukkit.getPluginManager().getPlugin("FWEchelon") != null) {

            if (EchelonHolder.getEchelonHolder().loadEchelonService()) {
                Bukkit.getConsoleSender().sendMessage(ChatFormatter.pluginPrefix() + "Support to FWEchelon enabled ! ");
                SettingsHandler.getSettingsHandler().echelonSupport = true;
                return;
            } else
                Bukkit.getConsoleSender().sendMessage(ChatFormatter.pluginPrefix() + "Error on register mutex activity !");
        } else
            Bukkit.getConsoleSender().sendMessage(ChatFormatter.pluginPrefix() + "FWEchelon not found...");

        SettingsHandler.getSettingsHandler().echelonSupport = false;
    }

    public  void secureInventoryClear() {
        for (UUID uuid : PlayersManager.getInstance().getPartecipants()) {
            Player player = Bukkit.getPlayer(uuid);

            if (player == null)
                continue;

            player.getInventory().clear();
            player.setGameMode(GameMode.SURVIVAL);

        }
    }





}
