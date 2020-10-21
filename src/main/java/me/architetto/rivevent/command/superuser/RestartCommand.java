package me.architetto.rivevent.command.superuser;

import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.util.ChatFormatter;
import me.architetto.rivevent.util.CommandName;
import me.architetto.rivevent.util.Messages;
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
        return "Restart RIV event.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent restart";
    }

    @Override
    public void perform(Player sender, String[] args){

        if (!sender.hasPermission("rivevent.eventmanager")) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_PERMISSION));
            return;
        }

        EventService eventService = EventService.getInstance();

        if (!eventService.isRunning()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_NO_EVENT_RUNNING));
            return;
        }

        eventService.setDoorsStatus(false);
        eventService.restartEvent();
        broadcastRestartedEvent(sender);

        TextComponent startCMD = new TextComponent(ChatColor.YELLOW + "START");
        startCMD.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/rivevent start") );
        startCMD.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new Text("Click to start event")));
        sender.sendMessage(new TextComponent(ChatFormatter.formatSuccessMessage("Evento 'RIV' re-inizializzato. Click ")),startCMD,
                new TextComponent(" per startare."));


    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }

    public void broadcastRestartedEvent(Player sender) {

        TextComponent joinClickMessage = new TextComponent(ChatColor.YELLOW + "JOIN");
        joinClickMessage.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/rivevent join") );
        joinClickMessage.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new Text("Click to join event")));
        EventService eventService = EventService.getInstance();

        for (Player p : Bukkit.getOnlinePlayers()) {

            if (eventService.getAllPlayerEvent().contains(p.getUniqueId()) || p == sender)
                continue;

            p.sendMessage(new TextComponent(ChatFormatter.formatInitializationMessage("Click ")),joinClickMessage,
                    new TextComponent(" per partecipare all'evento 'RIV'"));
            p.sendMessage(ChatFormatter.formatInitializationMessage(ChatColor.RED + "ATTENZIONE : " + ChatColor.RESET + "Partecipando il tuo inventario verra' cancellato !!"));
            p.sendTitle("", ChatColor.AQUA + "EVENTO 'RIV'",15,200,15);

        }

    }
}
