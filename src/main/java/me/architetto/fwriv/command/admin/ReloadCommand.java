package me.architetto.fwriv.command.admin;

import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.config.ConfigManager;
import me.architetto.fwriv.config.SettingsHandler;
import me.architetto.fwriv.event.service.EventService;
import me.architetto.fwriv.utils.ChatFormatter;
import me.architetto.fwriv.utils.CommandDescription;
import me.architetto.fwriv.utils.CommandName;
import me.architetto.fwriv.utils.Messages;
import org.bukkit.entity.Player;

import java.util.List;

public class ReloadCommand extends SubCommand{
    @Override
    public String getName(){
        return CommandName.RELOAD_COMMAND;
    }

    @Override
    public String getDescription(){
        return CommandDescription.RELOAD_COMMAND;
    }

    @Override
    public String getSyntax(){
        return "/fwriv reload";
    }

    @Override
    public void perform(Player sender, String[] args){

        if (!sender.hasPermission("rivevent.admin")) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_PERMISSION));
            return;
        }

        EventService eventService = EventService.getInstance();

        if (eventService.isRunning()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_EVENT_RUNNING));
            return;
        }

        ConfigManager configManager = ConfigManager.getInstance();
        configManager.reloadConfigs();

        configManager.getConfig("Settings.yml");
        configManager.getConfig("Preset.yml");
        configManager.getConfig("RespawnPoint.yml");

        SettingsHandler.getSettingsHandler().reload();

        sender.sendMessage(ChatFormatter.formatSuccessMessage(Messages.CONFIG_RELOADED));

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }
}
