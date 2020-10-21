package me.architetto.rivevent.command.user;

import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.util.ChatFormatter;
import me.architetto.rivevent.util.CommandName;
import me.architetto.rivevent.util.Messages;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

public class JoinCommand extends SubCommand {

    @Override
    public String getName(){
        return CommandName.JOIN_COMMAND;
    }

    @Override
    public String getDescription(){
        return "Join event.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent join";
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

        } else {
            if (eventService.getAllPlayerEvent().contains(sender.getUniqueId())) {
                sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_ALREADY_JOINED));
                return;
            }

            if (eventService.isStarted()) {
                eventService.addEliminated(sender.getUniqueId());
                sender.setGameMode(GameMode.SPECTATOR);
                sender.teleport(eventService.getSummonedArena().getTower());
                sender.getWorld().playSound(sender.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,1,1);
                sender.sendMessage(ChatFormatter.formatSuccessMessage(Messages.JOIN_STARTED_EVENT));
            } else {
                eventService.addPartecipant(sender.getUniqueId());
                eventService.teleportToSpawnPoint(sender);
                sender.setGameMode(GameMode.SURVIVAL);
                sender.getWorld().playSound(sender.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,1,1);
                sender.sendMessage(ChatFormatter.formatSuccessMessage(Messages.JOIN_EVENT));
            }
        }

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }
}
