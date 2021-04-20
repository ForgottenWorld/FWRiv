package me.architetto.fwriv.command.superuser;

import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.partecipant.PartecipantsManager;
import me.architetto.fwriv.event.service.EventService;
import me.architetto.fwriv.event.service.EventStatus;
import me.architetto.fwriv.partecipant.PartecipantStatus;
import me.architetto.fwriv.utils.ChatFormatter;
import me.architetto.fwriv.command.CommandName;
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
        return "placeholder";
    }

    @Override
    public String getSyntax(){
        return "/fwriv start";
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
    public void perform(Player sender, String[] args){

        EventService eventService = EventService.getInstance();
        EventStatus eventStatus = eventService.getEventStatus();

        if (eventStatus.equals(EventStatus.INACTIVE)) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_NO_EVENT_RUNNING));
            return;
        }

       if (PartecipantsManager.getInstance().getPartecipantsUUID(PartecipantStatus.PLAYING).isEmpty()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.NOT_ENOUGH_PLAYERS));
            return;
        }

        if (!eventStatus.equals(EventStatus.READY)) {
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
