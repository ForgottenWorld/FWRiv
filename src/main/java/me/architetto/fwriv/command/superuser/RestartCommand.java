package me.architetto.fwriv.command.superuser;

import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.event.EventService;
import me.architetto.fwriv.event.EventStatus;
import me.architetto.fwriv.localization.Message;
import me.architetto.fwriv.partecipant.PartecipantStatus;
import me.architetto.fwriv.partecipant.PartecipantsManager;
import me.architetto.fwriv.command.CommandName;
import me.architetto.fwriv.utils.MessageUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class RestartCommand extends SubCommand{
    @Override
    public String getName(){
        return CommandName.RESTART_COMMAND;
    }

    @Override
    public String getDescription(){
        return Message.RESTART_COMMAND.asString();
    }

    @Override
    public String getSyntax(){
        return "/fwriv " + CommandName.RESTART_COMMAND;
    }

    @Override
    public String getPermission() {
        return "rivevent.restart";
    }

    @Override
    public int getArgsRequired() {
        return 0;
    }

    @Override
    public void perform(Player sender, String[] args){

        EventService eventService = EventService.getInstance();

        if (eventService.getEventStatus().equals(EventStatus.INACTIVE)) {
            Message.ERR_NO_EVENT_IS_RUNNING.send(sender);
            return;
        }

        eventService.restartEvent();

        broadcastRestartedEvent();

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }

    public void broadcastRestartedEvent() {

        Set<UUID> uuid = PartecipantsManager.getInstance().getPartecipantsUUID(PartecipantStatus.PLAYING);

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (uuid.contains(p.getUniqueId())) continue;
            Message.COMP_EVENT_JOIN.sendSpecialComponent(p, MessageUtil.joinCmponent(),MessageUtil.infoCmponent());
        }

    }
}
