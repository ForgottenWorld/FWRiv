package me.architetto.fwriv.command.superuser;

import me.architetto.fwriv.FWRiv;
import me.architetto.fwriv.arena.Arena;
import me.architetto.fwriv.arena.ArenaManager;
import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.event.EventService;
import me.architetto.fwriv.event.EventStatus;
import me.architetto.fwriv.localization.Message;
import me.architetto.fwriv.command.CommandName;
import me.architetto.fwriv.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;


public class InitCommand extends SubCommand{
    @Override
    public String getName(){
        return CommandName.INIT_COMMAND;
    }

    @Override
    public String getDescription(){
        return Message.INIT_COMMAND.asString();
    }

    @Override
    public String getSyntax(){
        return "/fwriv " + CommandName.INIT_COMMAND + " <arena_name>";
    }

    @Override
    public String getPermission() {
        return "fwriv.init";
    }

    @Override
    public int getArgsRequired() {
        return 2;
    }

    @Override
    public void perform(Player sender, String[] args){

        EventService eventService = EventService.getInstance();

        if (!eventService.getEventStatus().equals(EventStatus.INACTIVE)) {
            Message.ERR_EVENT_IS_RUNNING.send(sender);
            return;
        }

        Optional<Arena> arena = ArenaManager.getInstance().getArena(args[1]);

        if (!arena.isPresent()) {
            Message.ERR_ARENA_NAME_NOT_EXIST.send(sender);
            return;
        }

        eventService.initialization(arena.get());

        Message.COMP_EVENT_JOIN.specialBroadcastComponent(MessageUtil.joinComponent(), MessageUtil.infoComponent());

        Bukkit.getScheduler().scheduleSyncDelayedTask(FWRiv.plugin,() -> suggestStartCommand(sender),20L);

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        if (args.length == 2)
            return ArenaManager.getInstance().getArenaNameList();
        return null;
    }

    public void suggestStartCommand(Player sender) {
        Message.COMP_EVENT_START.sendComponent(sender, MessageUtil.startComponent());
    }
}
