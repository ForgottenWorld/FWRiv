package me.architetto.fwriv.command.admin;

import me.architetto.fwriv.arena.ArenaManager;
import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.event.EventService;
import me.architetto.fwriv.event.EventStatus;
import me.architetto.fwriv.localization.Message;
import me.architetto.fwriv.command.CommandName;
import org.bukkit.entity.Player;

import java.util.List;

public class DeleteCommand extends SubCommand{
    @Override
    public String getName(){
        return CommandName.DELETE_COMMAND;
    }

    @Override
    public String getDescription(){
        return Message.DELETE_COMMAND.asString();
    }

    @Override
    public String getSyntax(){
        return "/fwriv " + CommandName.DELETE_COMMAND + " <arena_name>";
    }

    @Override
    public String getPermission() {
        return "rivevent.delete";
    }

    @Override
    public int getArgsRequired() {
        return 2;
    }


    @Override
    public void perform(Player sender, String[] args) {

        if (!EventService.getInstance().getEventStatus().equals(EventStatus.INACTIVE)) {
            Message.ERR_EVENT_IS_RUNNING.send(sender);
            return;
        }

        ArenaManager arenaManager = ArenaManager.getInstance();

        if(arenaManager.getArena(args[1]).isPresent()) {

            arenaManager.removeArena(args[1]);
            Message.SUCCESS_ARENA_DELETED.send(sender);

        } else
            Message.ERR_ARENA_NAME_NOT_EXIST.send(sender);

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){

        if (args.length == 2)
            return ArenaManager.getInstance().getArenaNameList();

        return null;
    }
}
