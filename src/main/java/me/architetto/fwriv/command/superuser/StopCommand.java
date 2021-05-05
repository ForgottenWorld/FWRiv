package me.architetto.fwriv.command.superuser;

import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.event.EventService;
import me.architetto.fwriv.event.EventStatus;
import me.architetto.fwriv.localization.Message;
import me.architetto.fwriv.command.CommandName;

import org.bukkit.entity.Player;

import java.util.List;

public class StopCommand extends SubCommand{
    @Override
    public String getName(){
        return CommandName.STOP_COMMAND;
    }

    @Override
    public String getDescription(){
        return Message.STOP_COMMAND.asString();
    }

    @Override
    public String getSyntax(){
        return "/fwriv " + CommandName.STOP_COMMAND;
    }

    @Override
    public String getPermission() {
        return "fwriv.stop";
    }

    @Override
    public int getArgsRequired() {
        return 0;
    }


    @Override
    public void perform(Player sender, String[] args) {

        EventService eventService = EventService.getInstance();

        if (eventService.getEventStatus().equals(EventStatus.INACTIVE)) {
            Message.ERR_NO_EVENT_IS_RUNNING.send(sender);
            return;
        }

        eventService.stopEvent();

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }





}
