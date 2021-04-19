package me.architetto.fwriv.command.admin;

import me.architetto.fwriv.arena.Arena;
import me.architetto.fwriv.arena.ArenaManager;
import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.event.service.EventService;
import me.architetto.fwriv.event.service.EventStatus;
import me.architetto.fwriv.utils.ChatFormatter;
import me.architetto.fwriv.utils.CommandDescription;
import me.architetto.fwriv.command.CommandName;
import me.architetto.fwriv.utils.Messages;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public class DeleteCommand extends SubCommand{
    @Override
    public String getName(){
        return CommandName.DELETE_COMMAND;
    }

    @Override
    public String getDescription(){
        return CommandDescription.DELETE_COMMAND;
    }

    @Override
    public String getSyntax(){
        return "/fwriv delete <arena_name>";
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
    public void perform(Player sender, String[] args) {

        if (args.length < 2) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_ARENA_CMD_SYNTAX));
            return;
        }

        if (!EventService.getInstance().getEventStatus().equals(EventStatus.INACTIVE)) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_EVENT_RUNNING));
            return;
        }

        String presetName = args[1];

        ArenaManager presetService = ArenaManager.getInstance();
        Optional<Arena> arena = presetService.getArena(presetName);

        if(arena.isPresent()) {

            presetService.removeArena(presetName);
            sender.sendMessage(ChatFormatter.formatSuccessMessage(Messages.DELETE_CDM_SUCCESS));

        } else

            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_NO_ARENA_NAME));

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){

        if (args.length == 2)
            return ArenaManager.getInstance().getArenaNameList();

        return null;
    }
}
