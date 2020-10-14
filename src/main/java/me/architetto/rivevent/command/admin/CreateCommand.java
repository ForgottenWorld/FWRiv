package me.architetto.rivevent.command.admin;

import me.architetto.rivevent.arena.Arena;
import me.architetto.rivevent.arena.ArenaManager;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.util.ChatFormatter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public class CreateCommand extends SubCommand{

    @Override
    public String getName(){
        return "create";
    }

    @Override
    public String getDescription(){
        return "Create a RIV preset.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent create <preset_name>";
    }

    @Override
    public void perform(Player sender, String[] args){

        if (!sender.hasPermission("rivevent.create")) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Permission Error: you do not have permission to use that command"));
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Syntax Error: insert preset name"));
            return;
        }

        String presetName = args[1];

        ArenaManager presetService = ArenaManager.getInstance();
        Optional<Arena> preset = presetService.getArena(presetName);

        if(preset.isPresent()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Error: a preset with that name already exists"));
        } else {
            if (presetService.isPlayerInCreationMode(sender)) {
                sender.sendMessage(ChatFormatter.formatErrorMessage("Error: you are already creating an arena"));
                return;
            }
            sender.sendMessage(ChatFormatter.formatSuccessMessage("Preset creation started."));
            sender.sendMessage(ChatFormatter.formatSuccessMessage("Select FIRST SPAWN by right clicking on the block"));
            ArenaManager.getInstance().addPlayerToArenaCreation((Player) sender, presetName);
        }

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }


}
