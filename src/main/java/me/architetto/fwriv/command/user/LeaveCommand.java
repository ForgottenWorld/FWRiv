package me.architetto.fwriv.command.user;

import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.localization.Message;
import me.architetto.fwriv.partecipant.Partecipant;
import me.architetto.fwriv.partecipant.PartecipantsManager;
import me.architetto.fwriv.event.EventService;
import me.architetto.fwriv.event.EventStatus;
import me.architetto.fwriv.command.CommandName;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public class LeaveCommand extends SubCommand{
    @Override
    public String getName(){
        return CommandName.LEAVE_COMMAND;
    }

    @Override
    public String getDescription(){
        return "placeholder";
    }

    @Override
    public String getSyntax(){
        return "/fwriv " + CommandName.LEAVE_COMMAND;
    }

    @Override
    public String getPermission() {
        return "fwriv.leave";
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

        Optional<Partecipant> optP = PartecipantsManager.getInstance().getPartecipant(sender);

        if (!optP.isPresent()) {
            Message.ERR_NO_EVENT_JOINED.send(sender);
            return;
        }

        eventService.partecipantLeave(sender);

        sender.playSound(sender.getLocation(),Sound.ENTITY_ENDERMAN_TELEPORT,1,1);
        Message.PARTECIPANT_LEAVE.send(sender);
        Message.BROADCAST_PLAYERLEAVEEVENT.broadcast("riveven.echo",sender.getDisplayName());

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }

}

