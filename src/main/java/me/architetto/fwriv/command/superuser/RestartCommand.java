package me.architetto.fwriv.command.superuser;

import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.event.service.EventService;
import me.architetto.fwriv.event.service.EventStatus;
import me.architetto.fwriv.localization.Message;
import me.architetto.fwriv.utils.ChatFormatter;
import me.architetto.fwriv.command.CommandName;
import me.architetto.fwriv.utils.Messages;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

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

        //todo: non mi convince
        broadcastRestartedEvent();

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }

    public void broadcastRestartedEvent() {

        TextComponent joinClickMessage = new TextComponent(ChatColor.YELLOW + "JOIN");
        joinClickMessage.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/fwriv join") );
        joinClickMessage.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new Text("Click to join event")));

        for (Player p : Bukkit.getOnlinePlayers()) {

            p.sendMessage(new TextComponent(ChatFormatter.formatInitializationMessage("Click ")),joinClickMessage,
                    new TextComponent(" per partecipare all'evento 'RIV'"));
            p.sendMessage(ChatFormatter.formatInitializationMessage(ChatColor.RED + "ATTENZIONE : " + ChatColor.RESET + "Partecipando il tuo inventario verra' cancellato !!"));
            p.sendTitle("", ChatColor.AQUA + "EVENTO 'RIV'",15,200,15);

        }

    }
}
