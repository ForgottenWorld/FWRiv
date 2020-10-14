package me.architetto.rivevent.command.user;

import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.util.ChatFormatter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

public class JoinCommand extends SubCommand {

    @Override
    public String getName(){
        return "join";
    }

    @Override
    public String getDescription(){
        return "Join an event.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent join";
    }

    @Override
    public void perform(Player sender, String[] args) {

        if (!sender.hasPermission("rivevent.join")) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Permission Error: you do not have permission to use that command"));
            return;
        }

        EventService eventService = EventService.getInstance();

        if (eventService.isRunning()) {
            if (eventService.getParticipantsPlayers().contains(sender.getUniqueId())
                    || eventService.getEliminatedPlayers().contains(sender.getUniqueId())) {
                sender.sendMessage(ChatFormatter.formatErrorMessage("Error: you already joined an event"));
                return;
            }
            if (eventService.isStarted()) {
                eventService.addEliminated(sender.getUniqueId());
                sender.teleport(eventService.getSummonedArena().getSpectator());
                sender.getWorld().playSound(sender.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,1,1);
                sender.sendMessage(ChatFormatter.formatSuccessMessage("Event is already in progress. You're been added to spectator"));
                sender.sendMessage(ChatFormatter.formatSuccessMessage("You are elegible for minievents, so stay ready !"));
            } else {
                eventService.addPartecipant(sender.getUniqueId());
                sender.teleport(eventService.getSummonedArena().getSpectator());
                sender.getWorld().playSound(sender.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,1,1);
                sender.sendMessage(ChatFormatter.formatSuccessMessage("You're been added to partecipant. Stay ready!"));
            }
        } else
            sender.sendMessage(ChatFormatter.formatErrorMessage("Error: no event in progress"));

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }
}
