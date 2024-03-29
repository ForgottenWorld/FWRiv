package me.architetto.fwriv;

import me.architetto.fwriv.arena.ArenaManager;
import me.architetto.fwriv.command.CommandManager;
import me.architetto.fwriv.config.ConfigManager;
import me.architetto.fwriv.config.SettingsHandler;
import me.architetto.fwriv.echelon.EchelonHolder;
import me.architetto.fwriv.event.EventService;
import me.architetto.fwriv.event.EventStatus;
import me.architetto.fwriv.listener.arena.ArenaCreationListener;
import me.architetto.fwriv.listener.event.*;
import me.architetto.fwriv.listener.notrollerino.SpectatingEvent;
import me.architetto.fwriv.listener.notrollerino.TeleportEvent;
import me.architetto.fwriv.listener.parties.FriendlyFireListener;
import me.architetto.fwriv.localization.LocalizationManager;
import me.architetto.fwriv.localization.Message;
import me.architetto.fwriv.reward.RewardService;
import me.architetto.fwriv.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class FWRiv extends JavaPlugin {

    public static Plugin plugin;

    @Override
    public void onEnable() {

        plugin = this;
        ConfigManager.getInstance().setPlugin(plugin);

        Bukkit.getConsoleSender().sendMessage(MessageUtil.fwrivHeader());

        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + " >>" + ChatColor.RESET + " Loading Strings.yml...");
        LocalizationManager.getInstance().loadLanguageFile();

        Bukkit.getConsoleSender().sendMessage( ChatColor.YELLOW + " >>" + ChatColor.RESET + " Loading Settings.yml...");
        SettingsHandler.getInstance().load();

        Bukkit.getConsoleSender().sendMessage( ChatColor.YELLOW + " >>" + ChatColor.RESET + " Loading rewards...");
        RewardService.getInstance().loadRewards();

        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + " >>" + ChatColor.RESET + " Loading commands & listeners...");
        loadCommands();
        loadListener();

        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + " >>" + ChatColor.RESET + " Loading Arena.yml ...");
        ArenaManager.getInstance().loadArenas();

        hookIntoEchelon();

        hookIntoParties();

        Bukkit.getConsoleSender().sendMessage("=============================================================");

    }

    @Override
    public void onDisable() {

        if (!EventService.getInstance().getEventStatus().equals(EventStatus.INACTIVE)) {
            Bukkit.getConsoleSender().sendMessage(Message.PREFIX.asString() + "Secure inventories clear...");
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
        getServer().getPluginManager().registerEvents(new SpectatingEvent(),this);
        getServer().getPluginManager().registerEvents(new TeleportEvent(),this);


    }

    public void hookIntoEchelon() {
        SettingsHandler settingsHandler = SettingsHandler.getInstance();

        if (Bukkit.getPluginManager().getPlugin("FWEchelon") != null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + " >>"
                    + ChatColor.RESET + " FWEchelon found ...");
            if (EchelonHolder.getEchelonHolder().loadEchelonService()) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN
                        + "        -- FWEchelon enabled !");
                settingsHandler.enableEchelon(true);
                return;
            } else
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED
                        + "        -- Error on register mutex activity, FWEchelon not enabled!");
        }
        settingsHandler.enableEchelon(false);
    }

    public void hookIntoParties() {
        if (getServer().getPluginManager().getPlugin("Parties") != null) {
            if (getServer().getPluginManager().getPlugin("Parties").isEnabled()) {
                // Parties is enabled
                ConfigManager configManager = ConfigManager.getInstance();
                if (configManager.getBoolean(configManager.getConfig("Settings.yml"),
                        "ENABLE_PARTIES_FRINDLY_FIRE")) {
                    getServer().getPluginManager().registerEvents(new FriendlyFireListener(),this);
                    Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + " >>"
                            + ChatColor.RESET + " Hooked into Paries!");
                }
            }
        }

    }

    public  void secureInventoryClear() {
        EventService eventService = EventService.getInstance();
        if (!eventService.getEventStatus().equals(EventStatus.INACTIVE))
            eventService.stopEvent();
    }





}
