package me.architetto.fwriv.command.superuser;

import me.architetto.fwriv.FWRiv;
import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.event.PlayersManager;
import me.architetto.fwriv.event.service.EventService;
import me.architetto.fwriv.utils.ChatFormatter;
import me.architetto.fwriv.utils.CommandDescription;
import me.architetto.fwriv.utils.CommandName;
import me.architetto.fwriv.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class StopCommand extends SubCommand{
    @Override
    public String getName(){
        return CommandName.STOP_COMMAND;
    }

    @Override
    public String getDescription(){
        return CommandDescription.STOP_COMMAND;
    }

    @Override
    public String getSyntax(){
        return "/fwriv stop [force]";
    }


    @Override
    public void perform(Player sender, String[] args) {

        if (!sender.hasPermission("rivevent.eventmanager")) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_PERMISSION));
            return;
        }

        EventService eventService = EventService.getInstance();

        if (!eventService.isRunning()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_NO_EVENT_RUNNING));
            return;
        }

        for (UUID u : PlayersManager.getInstance().getPartecipants()) {

            Player p = Bukkit.getPlayer(u);

            if (p == null) continue;

            p.teleport(PlayersManager.getInstance().getReturnLocation(u));
            p.playSound(p.getLocation(),Sound.ENTITY_ENDERMAN_TELEPORT,1,1);

            p.setGameMode(GameMode.SURVIVAL);
            p.getInventory().clear();

            p.sendMessage(ChatFormatter.formatInitializationMessage(Messages.STOP_CMD_PLAYER_MESSAGE));

        }

        eventService.setDoorsStatus(false);
        eventService.stopEvent();

        sender.sendMessage(ChatFormatter.formatSuccessMessage(Messages.STOP_CMD_SENDER_MESSAGE));

        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("force"))
                Bukkit.getScheduler().cancelTasks(FWRiv.plugin);
        }


    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }





}
