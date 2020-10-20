package me.architetto.rivevent.command.admin;

import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.config.ConfigManager;
import me.architetto.rivevent.config.SettingsHandler;
import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.util.ChatFormatter;
import me.architetto.rivevent.util.Messages;
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

        if (!sender.hasPermission("rivevent.admin")) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_PERMISSION));
            return;
        }

        EventService eventService = EventService.getInstance();

        if (eventService.isRunning()){
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_EVENT_RUNNING));
            return;
        }

        ConfigManager configManager = ConfigManager.getInstance();
        configManager.reloadConfigs();

        configManager.getConfig("Settings.yml");
        configManager.getConfig("Preset.yml");

        SettingsHandler.getInstance().load();

        sender.sendMessage(ChatFormatter.formatSuccessMessage(Messages.CONFIG_RELOADED));

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }
}
