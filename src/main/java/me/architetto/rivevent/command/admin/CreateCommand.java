package me.architetto.rivevent.command.admin;

import me.architetto.rivevent.arena.Arena;
import me.architetto.rivevent.arena.ArenaManager;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.util.ChatFormatter;
import me.architetto.rivevent.util.CommandName;
import me.architetto.rivevent.util.Messages;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public class CreateCommand extends SubCommand{

    @Override
    public String getName(){
        return CommandName.CREATE_COMMAND;
    }

    @Override
    public String getDescription(){
        return "Create a new RIV arena.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent create <arena_name>";
    }

    @Override
    public void perform(Player sender, String[] args){

        if (!sender.hasPermission("rivevent.admin")) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_PERMISSION));
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_ARENA_CMD_SYNTAX));
            return;
        }

        String presetName = args[1];

        ArenaManager presetService = ArenaManager.getInstance();
        Optional<Arena> preset = presetService.getArena(presetName);

        if(preset.isPresent())

            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_ARENA_NAME));

        else {

            if (presetService.isPlayerInCreationMode(sender)) {
                sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_CREATION1));
                return;
            }

            sender.sendMessage(ChatFormatter.formatArenaCreation("Indica posizione SPAWN 1 (Click destro)"));
            ArenaManager.getInstance().addPlayerToArenaCreation(sender, presetName);
        }

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }


}
