package me.architetto.rivevent.command.superuser;

import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.util.ChatFormatter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class RestartEventCommand extends SubCommand{
    @Override
    public String getName(){
        return "restart";
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

        if (!sender.hasPermission("rivevent.restartevent")) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Permission Error: you do not have permission to use that command"));
            return;
        }

        EventService eventService = EventService.getInstance();

        if (!eventService.isRunning()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Error: no event is running"));
            return;
        }

        eventService.restartEvent();

        TextComponent joinClickMessage = new TextComponent(ChatColor.YELLOW + "[ JOIN ]");
        joinClickMessage.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/rivevent join") );
        joinClickMessage.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new Text("Click here to join event or use" + ChatColor.YELLOW + " /rivevent join")));


        for (Player p : Bukkit.getOnlinePlayers()) {

            if (eventService.getParticipantsPlayers().contains(p.getUniqueId()))
                return;

            p.sendTitle("", ChatColor.AQUA + "RIV event started!"
                    + ChatColor.YELLOW + " /rivevent join",15,200,15);

            p.sendMessage(new TextComponent(ChatFormatter.formatInitializationMessage("click ")),joinClickMessage,
                    new TextComponent(" and play with us !" + ChatColor.RED
                            + ChatColor.ITALIC + " !! your inventory will be deleted !!"));

        }

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }
}
