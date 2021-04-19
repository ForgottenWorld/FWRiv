package me.architetto.fwriv.command.user;

import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.partecipant.Partecipant;
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
        return CommandDescription.LEAVE_COMMAND;
    }

    @Override
    public String getSyntax(){
        return "/fwriv leave";
    }

    @Override
    public String getPermission() {
        return "rivevent.user";
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

        Optional<Partecipant> optP = PartecipantsManager.getInstance().getPartecipant(sender);

        if (!optP.isPresent()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_NO_EVENT_JOINED));
            return;
        }

        if (optP.get().getPartecipantStatus().equals(PartecipantStatus.PLAYING))
            eventService.partecipantLeave(sender);
        else
            eventService.spectatorPlayerLeave(sender);

        sender.playSound(sender.getLocation(),Sound.ENTITY_ENDERMAN_TELEPORT,1,1);
        sender.sendMessage(ChatFormatter.formatSuccessMessage(Messages.LEAVE_OK));

        echoMessage(sender.getDisplayName());
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }

    public void echoMessage(String playername) {

        Bukkit.getServer().broadcast(ChatFormatter.formatEventMessage(ChatColor.YELLOW + playername
                + ChatColor.RESET + ChatColor.GRAY + "" + ChatColor.ITALIC + " ha " + ChatColor.RED + "abbandonato" + ChatColor.WHITE + " l'evento RIV. " + ChatColor.RESET
                + ChatColor.GREEN + "#" + PartecipantsManager.getInstance().getPartecipantsUUID(PartecipantStatus.ALL).size()),"riveven.echo");

    }
}

