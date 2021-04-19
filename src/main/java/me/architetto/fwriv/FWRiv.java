package me.architetto.fwriv;

import me.architetto.fwriv.arena.ArenaManager;
import me.architetto.fwriv.command.CommandManager;
import me.architetto.fwriv.config.ConfigManager;
import me.architetto.fwriv.config.SettingsHandler;
import me.architetto.fwriv.echelon.EchelonHolder;
import me.architetto.fwriv.event.PartecipantsManager;
import me.architetto.fwriv.event.service.EventService;
import me.architetto.fwriv.event.service.EventStatus;
import me.architetto.fwriv.listener.arena.ArenaCreationListener;
import me.architetto.fwriv.listener.event.*;
import me.architetto.fwriv.localization.LocalizationManager;
import me.architetto.fwriv.partecipant.PartecipantStatus;
import me.architetto.fwriv.reward.RewardService;
import me.architetto.fwriv.utils.ChatFormatter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class FWRiv extends JavaPlugin {

    public static Plugin plugin;

    @Override
    public void onEnable() {

        plugin = this;
        ConfigManager.getInstance().setPlugin(plugin);

        Bukkit.getConsoleSender().sendMessage("=====================[      RIVe      ]======================");

        Bukkit.getConsoleSender().sendMessage(ChatFormatter.pluginPrefix() + " Loading settings files...");
        SettingsHandler.getSettingsHandler().load();

        Bukkit.getConsoleSender().sendMessage(ChatFormatter.pluginPrefix() + " Loading localization...");
        LocalizationManager.getInstance().loadLanguageFile();

        Bukkit.getConsoleSender().sendMessage(ChatFormatter.pluginPrefix() + " Loading commands...");
        loadCommands();

        Bukkit.getConsoleSender().sendMessage(ChatFormatter.pluginPrefix() + " Loading listeners...");
        loadListener();

        Bukkit.getConsoleSender().sendMessage(ChatFormatter.pluginPrefix() + " Loading rewards...");
        RewardService.getInstance().loadRewards();

        Bukkit.getConsoleSender().sendMessage(ChatFormatter.pluginPrefix() + " Loading arenas ...");
        ArenaManager.getInstance().loadArenas();

        Bukkit.getConsoleSender().sendMessage(ChatFormatter.pluginPrefix() + " Loading FWEchelon support ...");
        loadEchelon();

        Bukkit.getConsoleSender().sendMessage("=============================================================");

    }

    @Override
    public void onDisable() {

        if (!EventService.getInstance().getEventStatus().equals(EventStatus.INACTIVE)) {
            Bukkit.getConsoleSender().sendMessage(ChatFormatter.pluginPrefix() + "Secure inventories clear...");
            secureInventoryClear();
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
        PartecipantsManager.getInstance().getPartecipantsUUID(PartecipantStatus.ALL).stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .forEach(p -> {
                    p.getInventory().clear();
                    p.setGameMode(GameMode.SURVIVAL);
                });
    }





}
