package me.architetto.rivevent.command.user;

import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.config.SettingsHandler;
import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.util.ChatFormatter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

public class LeaveCommand extends SubCommand{
    @Override
    public String getName(){
        return "leave";
    }

    @Override
    public String getDescription(){
        return "Leave a event.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent leave";
    }


    @Override
    public void perform(Player sender, String[] args) {
        if (!sender.hasPermission("rivevent.leave")) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Permission Error: you do not have permission to use that command"));
            return;
        }

        EventService eventService = EventService.getInstance();
        SettingsHandler settingsHandler = SettingsHandler.getInstance();

        if (settingsHandler.respawnLocation == null) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Error: no respawn location configured!"));
            return;
        }

        if (!eventService.isRunning()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Error: no event in progress"));
            return;
        }

        if (eventService.getParticipantsPlayers().contains(sender.getUniqueId())){
            if (eventService.isSetupDone())
                sender.sendMessage(ChatFormatter.formatErrorMessage("You can't leave event now"));

            else {
                eventService.getParticipantsPlayers().remove(sender.getUniqueId());
                sender.teleport(settingsHandler.respawnLocation);
                sender.playSound(sender.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,1,1);
                sender.sendMessage(ChatFormatter.formatSuccessMessage("You left the event"));

            }

        } else if (eventService.getEliminatedPlayers().contains(sender.getUniqueId())) {
            eventService.getEliminatedPlayers().remove(sender.getUniqueId());
            sender.teleport(settingsHandler.respawnLocation);
            sender.playSound(sender.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,1,1);
            sender.sendMessage(ChatFormatter.formatSuccessMessage("You left the event"));

        } else
            sender.sendMessage(ChatFormatter.formatErrorMessage("Error: you are not attending any event"));

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }
}

