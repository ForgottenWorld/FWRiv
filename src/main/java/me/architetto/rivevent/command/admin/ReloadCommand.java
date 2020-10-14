package me.architetto.rivevent.command.admin;

import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.config.ConfigManager;
import me.architetto.rivevent.config.SettingsHandler;
import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.util.ChatFormatter;
import org.bukkit.entity.Player;

import java.util.List;

public class ReloadCommand extends SubCommand{
    @Override
    public String getName(){
        return "reload";
    }

    @Override
    public String getDescription(){
        return "Reload Rivevent settings.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent reload";
    }

    @Override
    public void perform(Player sender, String[] args){

        if (!sender.hasPermission("rivevent.reload")) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Permission Error: you do not have permission to use that command"));
            return;
        }

        EventService eventService = EventService.getInstance();

        if (eventService.isRunning()){
            sender.sendMessage(ChatFormatter.formatErrorMessage("Error: an event is running"));
            return;
        }

        ConfigManager configManager = ConfigManager.getInstance();
        configManager.reloadConfigs();

        //todo sono necessari questi 2 getConfig ?
        configManager.getConfig("Settings.yml");
        configManager.getConfig("Preset.yml");

        SettingsHandler.getInstance().load();

        sender.sendMessage(ChatFormatter.formatSuccessMessage("Config files reloaded."));

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }
}
