package me.architetto.rivevent.command.admin;

import me.architetto.rivevent.arena.Arena;
import me.architetto.rivevent.arena.ArenaManager;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.util.ChatFormatter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public class DeleteCommand extends SubCommand{
    @Override
    public String getName(){
        return "delete";
    }

    @Override
    public String getDescription(){
        return "Deleta a preset.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent delete <nome_preset>";
    }


    @Override
    public void perform(Player sender, String[] args) {

        if (!sender.hasPermission("rivevent.delete")) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Permission Error: you do not have permission to use that command"));
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Syntax Error: insert arena name"));
            return;
        }

        EventService eventService = EventService.getInstance();

        if (eventService.isRunning()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Error: an event is running."));
            return;
        }

        String presetName = args[1];

        ArenaManager presetService = ArenaManager.getInstance();
        Optional<Arena> preset = presetService.getArena(presetName);

        if(preset.isPresent()) {
            presetService.removeArena(presetName);
            sender.sendMessage(ChatFormatter.formatSuccessMessage(presetName + "preset deleted successfully"));
        } else
            sender.sendMessage(ChatFormatter.formatErrorMessage("Error: there is no preset with this name"));

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }
}
