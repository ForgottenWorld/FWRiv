package me.architetto.rivevent.command.superuser;

import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.config.SettingsHandler;
import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.util.ChatFormatter;
import me.architetto.rivevent.util.CommandName;
import me.architetto.rivevent.util.Messages;
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
        return "Stop current event.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent stop";
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

        SettingsHandler settingsHandler = SettingsHandler.getInstance();

        for (UUID u : eventService.getAllPlayerEvent()) {

            Player p = Bukkit.getPlayer(u);
            if (p == null) continue;

            p.setGameMode(GameMode.SURVIVAL);
            p.getInventory().clear();
            p.teleport(settingsHandler.respawnLocation);
            p.playSound(p.getLocation(),Sound.ENTITY_ENDERMAN_TELEPORT,1,1);

            p.sendMessage(ChatFormatter.formatInitializationMessage(Messages.STOP_CMD_PLAYER_MESSAGE));

        }

        eventService.stopEvent();
        sender.sendMessage(ChatFormatter.formatSuccessMessage(Messages.STOP_CMD_SENDER_MESSAGE));


    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }





}
