package me.architetto.fwriv.command.admin;

import me.architetto.fwriv.arena.ArenaManager;
import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.localization.Message;
import me.architetto.fwriv.command.CommandName;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CreateCommand extends SubCommand{

    @Override
    public String getName(){
        return CommandName.CREATE_COMMAND;
    }

    @Override
    public String getDescription(){
        return Message.CREATE_COMMAND.asString();
    }

    @Override
    public String getSyntax(){
        return "/fwriv " + CommandName.CREATE_COMMAND + " <arena_name>";
    }

    @Override
    public String getPermission() {
        return "fwriv.create";
    }

    @Override
    public int getArgsRequired() {
        return 2;
    }

    @Override
    public void perform(Player sender, String[] args) {

        String arenaName = String.join("_", Arrays.copyOfRange(args, 1, args.length));

        ArenaManager arenaManager = ArenaManager.getInstance();

        if (arenaManager.isArenaName(arenaName)) {
            Message.ERR_ARENA_NAME_UNAVAIBLE.send(sender);
            return;
        }

        if (arenaManager.isPlayerInCreationMode(sender)) {
            Message.ERR_CREATION_MODE.send(sender);
            return;
        }

        Message.CREATION_MODE_INFO.send(sender);
        Message.CREATION_MODE_STEP.send(sender,"SPAWN 1");

        arenaManager.addPlayerToArenaCreation(sender, arenaName);

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }


}
