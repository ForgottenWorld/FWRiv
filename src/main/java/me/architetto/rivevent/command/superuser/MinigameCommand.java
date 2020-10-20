package me.architetto.rivevent.command.superuser;

import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.event.MiniGameService;
import me.architetto.rivevent.util.ChatFormatter;
import me.architetto.rivevent.util.Messages;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MinigameCommand extends SubCommand{
    @Override
    public String getName(){
        return "minigame";
    }

    @Override
    public String getDescription(){
        return "Starts a minigame.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent minigame [event name]";
    }

    @Override
    public void perform(Player sender, String[] args){


        if (!sender.hasPermission("rivevent.eventmanager")){
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_PERMISSION));
            return;
        }

        EventService eventService = EventService.getInstance();

        if (!eventService.isStarted()){
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_NO_EVENT_RUNNING));
            return;
        }

        MiniGameService miniGameService = MiniGameService.getInstance();

        if (miniGameService.isUniqueMiniGameRunning()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Error: a unique minigame is already running"));
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
                    sender.sendMessage(ChatFormatter.formatErrorMessage("Error: no minigame with this name"));

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