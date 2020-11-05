package me.architetto.rivevent.command.user;

import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.event.PlayersManager;
import me.architetto.rivevent.util.ChatFormatter;
import me.architetto.rivevent.util.CommandName;
import me.architetto.rivevent.util.Messages;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

public class LeaveCommand extends SubCommand{
    @Override
    public String getName(){
        return CommandName.LEAVE_COMMAND;
    }

    @Override
    public String getDescription(){
        return "Leave event.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent leave";
    }


    @Override
    public void perform(Player sender, String[] args) {
        if (!sender.hasPermission("rivevent.user")) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_PERMISSION));
            return;
        }

        EventService eventService = EventService.getInstance();

        if (!eventService.isRunning()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_NO_EVENT_RUNNING));
            return;
        }

        if (PlayersManager.getInstance().isPlayerActive(sender.getUniqueId())) {
            eventService.activePlayerLeave(sender.getUniqueId());
            sender.playSound(sender.getLocation(),Sound.ENTITY_ENDERMAN_TELEPORT,1,1);
            sender.sendMessage(ChatFormatter.formatSuccessMessage(Messages.LEAVE_OK));
            return;
        }

        if (PlayersManager.getInstance().isPlayerDeath(sender.getUniqueId())) {
            eventService.spectatorPlayerLeave(sender.getUniqueId());
            sender.playSound(sender.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,1,1);
            sender.sendMessage(ChatFormatter.formatSuccessMessage(Messages.LEAVE_OK));
            return;

        }

        sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_NO_EVENT_JOINED));

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }



}

