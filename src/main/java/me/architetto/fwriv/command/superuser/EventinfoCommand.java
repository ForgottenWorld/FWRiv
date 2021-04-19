package me.architetto.fwriv.command.superuser;

import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.partecipant.PartecipantStatus;
import me.architetto.fwriv.event.PartecipantsManager;
import me.architetto.fwriv.event.service.EventService;
import me.architetto.fwriv.event.service.EventStatus;
import me.architetto.fwriv.utils.ChatFormatter;
import me.architetto.fwriv.utils.CommandDescription;
import me.architetto.fwriv.command.CommandName;
import me.architetto.fwriv.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class EventinfoCommand extends SubCommand{
    @Override
    public String getName(){
        return CommandName.EVENTINFO_COMMAND;
    }

    @Override
    public String getDescription(){
        return CommandDescription.EVENTINFO_COMMAND;
    }

    @Override
    public String getSyntax(){
        return "/fwriv eventinfo";
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

        if (eventService.getEventStatus().equals(EventStatus.INACTIVE)){
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_NO_EVENT_RUNNING));
            return;
        }

        sender.sendMessage(ChatFormatter.formatSuccessMessage("GIOCATORI : " + ChatColor.YELLOW
                + PartecipantsManager.getInstance().getPartecipantsUUID(PartecipantStatus.ALL).size()));
        sender.sendMessage(ChatFormatter.formatSuccessMessage(getPlayersName(PartecipantsManager.getInstance().getPartecipantsUUID(PartecipantStatus.ALL)).toString()));

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }

    private List<String> getPlayersName(Set<UUID> playersList) {
        List<String> list = new ArrayList<>();
        for (UUID u : playersList) {
            Player p = Bukkit.getPlayer(u);
            if (p != null)
                list.add(p.getDisplayName());
        }
        return list;
    }


}
