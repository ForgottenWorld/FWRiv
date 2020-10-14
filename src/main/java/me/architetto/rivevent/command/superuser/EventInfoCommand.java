package me.architetto.rivevent.command.superuser;

import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.util.ChatFormatter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class EventInfoCommand extends SubCommand{
    @Override
    public String getName(){
        return "eventinfo";
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

        if (!sender.hasPermission("rivevent.eventinfo")) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Permission Error: you do not have permission to use that command"));
            return;
        }

        EventService eventService = EventService.getInstance();

        if (!eventService.isRunning()){
            sender.sendMessage(ChatFormatter.formatErrorMessage("Error: no event in progress"));
            return;
        }


        sender.sendTitle("", ChatColor.RED + "Partecipanti : " + ChatColor.RESET
                + eventService.getParticipantsPlayers().size() + eventService.getEliminatedPlayers().size(),0,100,0);

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }


}
