package me.architetto.fwriv.command.superuser;

import me.architetto.fwriv.FWRiv;
import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.event.service.EventService;
import me.architetto.fwriv.event.service.EventStatus;
import me.architetto.fwriv.utils.ChatFormatter;
import me.architetto.fwriv.utils.CommandDescription;
import me.architetto.fwriv.command.CommandName;
import me.architetto.fwriv.utils.Messages;
import org.bukkit.Bukkit;

import org.bukkit.entity.Player;

import java.util.List;

public class StopCommand extends SubCommand{
    @Override
    public String getName(){
        return CommandName.STOP_COMMAND;
    }

    @Override
    public String getDescription(){
        return CommandDescription.STOP_COMMAND;
    }

    @Override
    public String getSyntax(){
        return "/fwriv stop [force]";
    }

    @Override
    public String getPermission() {
        return "rivevent.eventmanager";
    }

    @Override
    public int getArgsRequired() {
        return 0;
    }


    @Override
    public void perform(Player sender, String[] args) {

        EventService eventService = EventService.getInstance();

        if (eventService.getEventStatus().equals(EventStatus.INACTIVE)) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_NO_EVENT_RUNNING));
            return;
        }

        eventService.stopEvent();

        sender.sendMessage(ChatFormatter.formatSuccessMessage(Messages.STOP_CMD_SENDER_MESSAGE));

        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("force"))
                Bukkit.getScheduler().cancelTasks(FWRiv.plugin);
        }


    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }





}
