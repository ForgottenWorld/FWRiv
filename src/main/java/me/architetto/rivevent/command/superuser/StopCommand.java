package me.architetto.rivevent.command.superuser;

import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.config.SettingsHandler;
import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.util.ChatFormatter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class StopCommand extends SubCommand{
    @Override
    public String getName(){
        return "stop";
    }

    @Override
    public String getDescription(){
        return "Cancel the current event.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent stop";
    }


    @Override
    public void perform(Player sender, String[] args) {

        if (!sender.hasPermission("rivevent.stop")) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Permission Error: you do not have permission to use that command"));
            return;
        }

        EventService eventService = EventService.getInstance();

        if (!eventService.isRunning()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Error: no event in progress"));
            return;
        }

        SettingsHandler settingsHandler = SettingsHandler.getInstance();

        for (UUID u : eventService.getParticipantsPlayers()) {
            Player p = Bukkit.getPlayer(u);
            p.getInventory().clear();
            p.sendMessage(ChatFormatter.formatInitializationMessage("event ended !"));
            p.teleport(settingsHandler.respawnLocation);
            p.playSound(p.getLocation(),Sound.ENTITY_ENDERMAN_TELEPORT,1,1);

        }

        for (UUID u : eventService.getEliminatedPlayers()) {
            Player p = Bukkit.getPlayer(u);
            p.sendMessage(ChatFormatter.formatInitializationMessage("event ended !"));
            p.teleport(settingsHandler.respawnLocation);
            p.playSound(p.getLocation(),Sound.ENTITY_ENDERMAN_TELEPORT,1,1);
           // p.getInventory().clear(); todo: l'invetario è wipato alla morte ?

        }

        eventService.stopEvent();
        sender.sendMessage(ChatFormatter.formatSuccessMessage("event ended !"));


    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }





}
