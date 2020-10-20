package me.architetto.rivevent.command.superuser;

import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.util.ChatFormatter;
import me.architetto.rivevent.util.Messages;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class EventCommand extends SubCommand{
    @Override
    public String getName(){
        return "event";
    }

    @Override
    public String getDescription(){
        return "Info about current event.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent eventinfo";
    }

    @Override
    public void perform(Player sender, String[] args){

        if (!sender.hasPermission("rivevent.eventmanager")) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_PERMISSION));
            return;
        }

        EventService eventService = EventService.getInstance();

        if (!eventService.isRunning()){
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_NO_EVENT_RUNNING));
            return;
        }


        sender.sendMessage(ChatFormatter.formatSuccessMessage("PLAYERS JOINED : " + ChatColor.YELLOW
                + eventService.getAllPlayerEvent().size()));
        sender.sendMessage(ChatFormatter.formatSuccessMessage("PLAYERS LIST : " + ChatColor.YELLOW
                + eventService.getAllPlayerEvent()));
        sender.sendMessage(ChatFormatter.formatSuccessMessage("SPECTATORS : WIP"));

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }


}
