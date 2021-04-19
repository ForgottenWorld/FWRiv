package me.architetto.fwriv.command.admin;

import me.architetto.fwriv.arena.Arena;
import me.architetto.fwriv.arena.ArenaManager;
import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.utils.ChatFormatter;
import me.architetto.fwriv.utils.CommandDescription;
import me.architetto.fwriv.command.CommandName;
import me.architetto.fwriv.utils.Messages;
import org.bukkit.ChatColor;
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
        return CommandDescription.CREATE_COMMAND;
    }

    @Override
    public String getSyntax(){
        return "/fwriv create <arena_name>";
    }

    @Override
    public String getPermission() {
        return "rivevent.admin";
    }

    @Override
    public int getArgsRequired() {
        return 0;
    }

    @Override
    public void perform(Player sender, String[] args){

        if (args.length < 2) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_ARENA_CMD_SYNTAX));
            return;
        }

        String arenaName = args[1];

        ArenaManager arenaManager = ArenaManager.getInstance();
        Optional<Arena> arena = arenaManager.getArena(arenaName);

        if (arena.isPresent()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_ARENA_NAME));
            return;
        }

        if (arenaManager.isPlayerInCreationMode(sender)) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_CREATION1));
            return;
        }

        sender.sendMessage(ChatFormatter.formatArenaCreation("Indica posizione SPAWN 1 ... "
                + ChatColor.AQUA + "" + ChatColor.ITALIC + "(CLICK DX con STICK equipaggiato)"));

        ArenaManager.getInstance().addPlayerToArenaCreation(sender, arenaName);

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }


}
