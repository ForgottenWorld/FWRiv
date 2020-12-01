package me.architetto.fwriv.command.superuser;

import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.event.PlayersManager;
import me.architetto.fwriv.event.service.EventService;
import me.architetto.fwriv.utils.ChatFormatter;
import me.architetto.fwriv.utils.CommandDescription;
import me.architetto.fwriv.utils.CommandName;
import me.architetto.fwriv.utils.Messages;
import org.bukkit.entity.Player;

import java.util.List;

public class StartCommand extends SubCommand{
    @Override
    public String getName(){
        return CommandName.START_COMMAND;
    }

    @Override
    public String getDescription(){
        return CommandDescription.START_COMMAND;
    }

    @Override
    public String getSyntax(){
        return "/fwriv start";
    }

    @Override
    public void perform(Player sender, String[] args){

        if (!sender.hasPermission("rivevent.eventmanager")) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_PERMISSION));
            return;
        }

        EventService eventService = EventService.getInstance();

        if (!eventService.isRunning()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_NO_EVENT_RUNNING));
            return;
        }

       if (PlayersManager.getInstance().getActivePlayers().isEmpty()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.NOT_ENOUGH_PLAYERS));
            return;
        }

        if (eventService.isStarted()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_EVENT_RUNNING));
            return;
        }

        eventService.startEventTimer();

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }


}
