package me.architetto.fwriv.command.superuser;

import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.event.EventService;
import me.architetto.fwriv.event.EventStatus;
import me.architetto.fwriv.localization.Message;
import me.architetto.fwriv.partecipant.PartecipantStatus;
import me.architetto.fwriv.partecipant.PartecipantsManager;
import me.architetto.fwriv.command.CommandName;
import me.architetto.fwriv.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class RestartCommand extends SubCommand{
    @Override
    public String getName(){
        return CommandName.RESTART_COMMAND;
    }

    @Override
    public String getDescription(){
        return Message.RESTART_COMMAND.asString();
    }

    @Override
    public String getSyntax(){
        return "/fwriv " + CommandName.RESTART_COMMAND;
    }

    @Override
    public String getPermission() {
        return "rivevent.restart";
    }

    @Override
    public int getArgsRequired() {
        return 0;
    }

    @Override
    public void perform(Player sender, String[] args) {

        EventService eventService = EventService.getInstance();
        EventStatus eventStatus = eventService.getEventStatus();

        if (eventStatus.equals(EventStatus.INACTIVE)) {
            Message.ERR_NO_EVENT_IS_RUNNING.send(sender);
            return;
        }

        if (eventStatus.equals(EventStatus.READY)) {
            //todo: messaggio. Non serve il restart se non è ancora iniziato
            return;
        }

        eventService.restartEvent();

        broadcastRestartedEvent();

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }

    public void broadcastRestartedEvent() {

        Set<UUID> uuid = PartecipantsManager.getInstance().getPartecipantsUUID(PartecipantStatus.PLAYING);

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (uuid.contains(p.getUniqueId())) continue;
            Message.COMP_EVENT_JOIN.sendSpecialComponent(p, MessageUtil.joinComponent(),MessageUtil.infoComponent());
        }

    }
}
