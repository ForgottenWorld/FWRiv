package me.architetto.rivevent.command.superuser;

import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.event.PlayersManager;
import me.architetto.rivevent.util.ChatFormatter;
import me.architetto.rivevent.util.CommandName;
import me.architetto.rivevent.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EventinfoCommand extends SubCommand{
    @Override
    public String getName(){
        return CommandName.EVENTINFO_COMMAND;
    }

    @Override
    public String getDescription(){
        return "Info about current event.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent eventinfo";
    }

    @Override
    public void perform(Player sender, String[] args){

        if (!sender.hasPermission("rivevent.eventmanager")) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_PERMISSION));
            return;
        }

        EventService eventService = EventService.getInstance();

        if (!eventService.isRunning()){
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_NO_EVENT_RUNNING));
            return;
        }

        sender.sendMessage(ChatFormatter.formatSuccessMessage("GIOCATORI : " + ChatColor.YELLOW
                + PlayersManager.getInstance().getPartecipants().size()));
        sender.sendMessage(ChatFormatter.formatSuccessMessage(getPlayersName(PlayersManager.getInstance().getPartecipants()).toString()));

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }

    private List<String> getPlayersName(List<UUID> playersList) {
        List<String> list = new ArrayList<>();
        for (UUID u : playersList) {
            Player p = Bukkit.getPlayer(u);
            if (p != null)
                list.add(p.getDisplayName());
        }
        return list;
    }


}
