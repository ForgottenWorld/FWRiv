package me.architetto.fwriv.command.user;

import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.config.SettingsHandler;
import me.architetto.fwriv.echelon.EchelonHolder;
import me.architetto.fwriv.event.PlayersManager;
import me.architetto.fwriv.event.service.EventService;
import me.architetto.fwriv.utils.ChatFormatter;
import me.architetto.fwriv.utils.CommandDescription;
import me.architetto.fwriv.utils.CommandName;
import me.architetto.fwriv.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

public class LeaveCommand extends SubCommand{
    @Override
    public String getName(){
        return CommandName.LEAVE_COMMAND;
    }

    @Override
    public String getDescription(){
        return CommandDescription.LEAVE_COMMAND;
    }

    @Override
    public String getSyntax(){
        return "/fwriv leave";
    }


    @Override
    public void perform(Player sender, String[] args) {
        if (!sender.hasPermission("rivevent.user")) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_PERMISSION));
            return;
        }

        EventService eventService = EventService.getInstance();

        if (!eventService.isRunning()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_NO_EVENT_RUNNING));
            return;
        }

        if (PlayersManager.getInstance().isPlayerActive(sender.getUniqueId())) {
            eventService.activePlayerLeave(sender.getUniqueId());
            sender.playSound(sender.getLocation(),Sound.ENTITY_ENDERMAN_TELEPORT,1,1);
            sender.sendMessage(ChatFormatter.formatSuccessMessage(Messages.LEAVE_OK));

            echoMessage(sender.getDisplayName());

            return;
        }

        if (PlayersManager.getInstance().isPlayerSpectator(sender.getUniqueId())) {
            eventService.spectatorPlayerLeave(sender.getUniqueId());
            sender.playSound(sender.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,1,1);
            sender.sendMessage(ChatFormatter.formatSuccessMessage(Messages.LEAVE_OK));

            echoMessage(sender.getDisplayName());

            return;

        }

        sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_NO_EVENT_JOINED));


    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }

    public void echoMessage(String playername) {

        Bukkit.getServer().broadcast(ChatFormatter.formatEventMessage(ChatColor.YELLOW + playername
                + ChatColor.RESET + ChatColor.GRAY + "" + ChatColor.ITALIC + " ha " + ChatColor.RED + "abbandonato" + ChatColor.WHITE + " l'evento RIV. " + ChatColor.RESET
                + ChatColor.GREEN + "#" + PlayersManager.getInstance().getPartecipants().size()),"riveven.echo");

    }
}

