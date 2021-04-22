package me.architetto.fwriv.command.superuser;

import me.architetto.fwriv.FWRiv;
import me.architetto.fwriv.arena.Arena;
import me.architetto.fwriv.arena.ArenaManager;
import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.event.EventService;
import me.architetto.fwriv.event.EventStatus;
import me.architetto.fwriv.localization.Message;
import me.architetto.fwriv.command.CommandName;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
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
        return "rivevent.init";
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

        String presetName = args[1];

        Optional<Arena> arena = ArenaManager.getInstance().getArena(presetName);

        if (!arena.isPresent()) {
            Message.ERR_ARENA_NAME_NOT_EXIST.send(sender);
            return;
        }

        eventService.initRIV(arena.get());

        ComponentBuilder componentBuilder = new ComponentBuilder(" [JOIN]")
                .color(ChatColor.YELLOW)
                .bold(true)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fwriv join"))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new Text(Message.COMP_EVENT_JOIN_HOVER.asString())));

        Message.COMP_EVENT_JOIN.specialBroadcastComponent(new TextComponent(componentBuilder.create()));

        Bukkit.getScheduler().scheduleSyncDelayedTask(FWRiv.plugin,() -> suggestStartCommand(sender),20L);

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){

        if (args.length == 2)
            return ArenaManager.getInstance().getArenaNameList();

        return null;
    }

    public void suggestStartCommand(Player sender) {
        ComponentBuilder componentBuilder = new ComponentBuilder(" [START] ")
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fwriv start"))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new Text(Message.COMP_EVENT_START_HOVER.asString())))
                .color(net.md_5.bungee.api.ChatColor.GREEN)
                .bold(true)
                .append("[STOP]")
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fwriv stop"))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new Text(Message.COMP_EVENT_STOP_HOVER.asString())))
                .color(net.md_5.bungee.api.ChatColor.RED)
                .bold(true);

        Message.COMP_EVENT_STARTSTOP.sendComponent(sender, new TextComponent(componentBuilder.create()));
    }
}
