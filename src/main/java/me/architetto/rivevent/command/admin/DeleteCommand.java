package me.architetto.rivevent.command.admin;

import me.architetto.rivevent.arena.Arena;
import me.architetto.rivevent.arena.ArenaManager;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.util.ChatFormatter;
import me.architetto.rivevent.util.CommandName;
import me.architetto.rivevent.util.Messages;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DeleteCommand extends SubCommand{
    @Override
    public String getName(){
        return CommandName.DELETE_COMMAND;
    }

    @Override
    public String getDescription(){
        return "Deleta arena.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent delete <arena_name>";
    }


    @Override
    public void perform(Player sender, String[] args) {

        if (!sender.hasPermission("rivevent.admin")) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_PERMISSION));
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_ARENA_CMD_SYNTAX));
            return;
        }

        EventService eventService = EventService.getInstance();

        if (eventService.isRunning()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_EVENT_RUNNING));
            return;
        }

        String presetName = args[1];

        ArenaManager presetService = ArenaManager.getInstance();
        Optional<Arena> preset = presetService.getArena(presetName);

        if(preset.isPresent()) {
            presetService.removeArena(presetName);
            sender.sendMessage(ChatFormatter.formatSuccessMessage(Messages.DELETE_CDM_SUCCESS));
        } else
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_NO_ARENA_NAME));

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){

        if (args.length == 2){

            return new ArrayList<>(ArenaManager.getInstance().getArenaContainer().keySet());

        }

        return null;
    }
}
