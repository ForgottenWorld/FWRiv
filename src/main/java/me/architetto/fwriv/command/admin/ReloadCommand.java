package me.architetto.fwriv.command.admin;

import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.config.ConfigManager;
import me.architetto.fwriv.config.SettingsHandler;
import me.architetto.fwriv.event.service.EventService;
import me.architetto.fwriv.event.service.EventStatus;
import me.architetto.fwriv.utils.ChatFormatter;
import me.architetto.fwriv.utils.CommandDescription;
import me.architetto.fwriv.command.CommandName;
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
    public String getPermission() {
        return "rivevent.admin";
    }

    @Override
    public int getArgsRequired() {
        return 0;
    }

    @Override
    public void perform(Player sender, String[] args){

        EventService eventService = EventService.getInstance();

        if (!eventService.getEventStatus().equals(EventStatus.INACTIVE)) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_EVENT_RUNNING));
            return;
        }

        ConfigManager configManager = ConfigManager.getInstance();
        configManager.reloadConfigs();

        SettingsHandler.getSettingsHandler().reload();

        sender.sendMessage(ChatFormatter.formatSuccessMessage(Messages.CONFIG_RELOADED));

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }
}
