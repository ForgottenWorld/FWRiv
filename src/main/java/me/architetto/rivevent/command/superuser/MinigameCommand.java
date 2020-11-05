package me.architetto.rivevent.command.superuser;

import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.event.MinigameService;
import me.architetto.rivevent.util.ChatFormatter;
import me.architetto.rivevent.util.CommandName;
import me.architetto.rivevent.util.Messages;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MinigameCommand extends SubCommand{
    @Override
    public String getName(){
        return CommandName.MINIGAME_COMMAND;
    }

    @Override
    public String getDescription(){
        return "Start a minigame.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent minigame <minigame_name>";
    }

    @Override
    public void perform(Player sender, String[] args){


        if (!sender.hasPermission("rivevent.eventmanager")){
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_PERMISSION));
            return;
        }

        EventService eventService = EventService.getInstance();

        if (!eventService.isStarted()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_NO_EVENT_RUNNING));
            return;
        }

        if (eventService.isFinished()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("L'evento e' gia' terminato"));
            return;
        }

        MinigameService miniGameService = MinigameService.getInstance();

        if (miniGameService.isUniqueMiniGameRunning()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Error: un minigame è attualmente in corso"));
            return;
        }

        if (args.length == 2){
            switch(args[1].toUpperCase()){
                case "CURSE":
                    miniGameService.startCurseEvent(sender);
                    return;
                case "DEATHRACE":
                    miniGameService.startDeathRaceEvent(sender);
                    return;
                default:
                    sender.sendMessage(ChatFormatter.formatErrorMessage("Error: nessun minigame con questo nome"));

            }
        }

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){

        if (args.length == 2){

            return new ArrayList<>(
                    Arrays.asList("Curse",
                            "DeathRace"));

        }
        return null;
    }
}

//EVENTO BACK TO LIFE