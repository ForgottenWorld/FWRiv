package me.architetto.fwriv.command.superuser;

import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.localization.Message;
import me.architetto.fwriv.partecipant.PartecipantsManager;
import me.architetto.fwriv.event.EventService;
import me.architetto.fwriv.event.EventStatus;
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
        return Message.START_COMMAND.asString();
    }

    @Override
    public String getSyntax(){
        return "/fwriv " + CommandName.START_COMMAND;
    }

    @Override
    public String getPermission() {
        return "rivevent.start";
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
            Message.ERR_NO_EVENT_IS_RUNNING.send(sender);
            return;
        }

       if (PartecipantsManager.getInstance().getPartecipantsUUID(PartecipantStatus.PLAYING).isEmpty()) {
           Message.ERR_NOT_ENOUGH_PLAYERS.send(sender);
            return;
        }

        if (!eventStatus.equals(EventStatus.READY)) {
            Message.ERR_EVENT_NOT_READY.send(sender);
            return;
        }

        eventService.startEventTimer();

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }


}
