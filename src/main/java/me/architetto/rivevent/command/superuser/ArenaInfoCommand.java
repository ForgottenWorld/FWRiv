package me.architetto.rivevent.command.superuser;

import me.architetto.rivevent.arena.Arena;
import me.architetto.rivevent.arena.ArenaManager;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.util.ChatFormatter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;


public class ArenaInfoCommand extends SubCommand{
    @Override
    public String getName(){
        return "arenainfo";
    }

    @Override
    public String getDescription(){
        return "arenas list and arena saved points";
    }

    @Override
    public String getSyntax(){
        return "/rivevent list <preset_name>";
    }

    @Override
    public void perform(Player sender, String[] args){

        if (!sender.hasPermission("rivevent.arenainfo")) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Permission Error: you do not have permission to use that command"));
            return;
        }

        ArenaManager arenaManager = ArenaManager.getInstance();
        if (args.length == 1) {
            sender.sendMessage(ChatFormatter.formatSuccessMessage(arenaManager.getArenaContainer().keySet().toString()));
            return;
        }

        String presetName = args[1];
        Optional<Arena> arena = arenaManager.getArena(presetName);

        if (arenaManager.getArenaContainer().isEmpty()){
            sender.sendMessage(ChatFormatter.formatErrorMessage("Error: no arena has been created yet"));
            return;
        }

        if (arena.isPresent()) {
            Arena selectedArena = arena.get();
            sender.sendMessage(ChatFormatter.chatHeaderPresetInfo());
            sender.sendMessage(ChatFormatter.formatPresetName(selectedArena.getName()));
            sender.sendMessage(ChatFormatter.formatLocation("SPAWN #1", selectedArena.getSpawn1()));
            sender.sendMessage(ChatFormatter.formatLocation("SPAWN #2", selectedArena.getSpawn2()));
            sender.sendMessage(ChatFormatter.formatLocation("SPAWN #3", selectedArena.getSpawn3()));
            sender.sendMessage(ChatFormatter.formatLocation("SPAWN #4", selectedArena.getSpawn4()));
            sender.sendMessage(ChatFormatter.formatLocation("TOWER POINT", selectedArena.getTower()));
            sender.sendMessage(ChatFormatter.formatLocation("SPECTATOR", selectedArena.getSpectator()));
            sender.sendMessage(ChatFormatter.chatFooter());

        } else {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Error: this arena doesn't exist"));
        }

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){

        return null;
    }
}
