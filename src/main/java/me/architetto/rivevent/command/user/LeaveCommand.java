package me.architetto.rivevent.command.user;

import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.config.SettingsHandler;
import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.util.ChatFormatter;
import me.architetto.rivevent.util.CommandName;
import me.architetto.rivevent.util.Messages;
import org.bukkit.GameMode;
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
        SettingsHandler settingsHandler = SettingsHandler.getInstance();

        if (!eventService.isRunning()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_NO_EVENT_RUNNING));
            return;
        }

        if (eventService.getPlayerIN().contains(sender.getUniqueId())) {
            eventService.removePartecipant(sender.getUniqueId());
            sender.teleport(SettingsHandler.getInstance().respawnLocation);
            sender.playSound(sender.getLocation(),Sound.ENTITY_ENDERMAN_TELEPORT,1,1);
            sender.sendMessage(ChatFormatter.formatSuccessMessage(Messages.LEAVE_OK));

        } else if (eventService.getPlayerOUT().contains(sender.getUniqueId())) {
            eventService.removePlayerOUT(sender.getUniqueId());
            sender.setGameMode(GameMode.SURVIVAL);
            sender.teleport(settingsHandler.respawnLocation);
            sender.playSound(sender.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,1,1);
            sender.sendMessage(ChatFormatter.formatSuccessMessage(Messages.LEAVE_OK));

        } else
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_NO_EVENT_JOINED));

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }
}

