package me.architetto.fwriv.command.superuser;

import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.event.PlayersManager;
import me.architetto.fwriv.event.service.EventService;
import me.architetto.fwriv.utils.ChatFormatter;
import me.architetto.fwriv.utils.CommandDescription;
import me.architetto.fwriv.utils.CommandName;
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
        return CommandDescription.RESTART_COMMAND;
    }

    @Override
    public String getSyntax(){
        return "/fwriv restart";
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

        TextComponent startCMD = new TextComponent(ChatColor.YELLOW + "" + ChatColor.BOLD + "START");
        startCMD.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/fwriv start") );
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
        joinClickMessage.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/fwriv join") );
        joinClickMessage.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new Text("Click to join event")));

        for (Player p : Bukkit.getOnlinePlayers()) {

            if (PlayersManager.getInstance().getActivePlayers().contains(p.getUniqueId()) || p == sender)
                continue;

            p.sendMessage(new TextComponent(ChatFormatter.formatInitializationMessage("Click ")),joinClickMessage,
                    new TextComponent(" per partecipare all'evento 'RIV'"));
            p.sendMessage(ChatFormatter.formatInitializationMessage(ChatColor.RED + "ATTENZIONE : " + ChatColor.RESET + "Partecipando il tuo inventario verra' cancellato !!"));
            p.sendTitle("", ChatColor.AQUA + "EVENTO 'RIV'",15,200,15);

        }

    }
}
