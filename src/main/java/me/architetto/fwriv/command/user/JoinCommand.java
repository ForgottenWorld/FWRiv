package me.architetto.fwriv.command.user;

import me.architetto.fwriv.FWRiv;
import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.config.SettingsHandler;
import me.architetto.fwriv.echelon.EchelonHolder;
import me.architetto.fwriv.localization.Message;
import me.architetto.fwriv.partecipant.PartecipantStatus;
import me.architetto.fwriv.partecipant.PartecipantsManager;
import me.architetto.fwriv.event.EventService;
import me.architetto.fwriv.event.EventStatus;
import me.architetto.fwriv.utils.ChatFormatter;
import me.architetto.fwriv.command.CommandName;
import me.architetto.fwriv.utils.Messages;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

public class JoinCommand extends SubCommand {

    @Override
    public String getName(){
        return CommandName.JOIN_COMMAND;
    }

    @Override
    public String getDescription(){
        return Message.JOIN_COMMAND.asString();
    }

    @Override
    public String getSyntax(){
        return "/fwriv " + CommandName.JOIN_COMMAND;
    }

    @Override
    public String getPermission() {
        return "rivevent.join";
    }

    @Override
    public int getArgsRequired() {
        return 0;
    }

    @Override
    public void perform(Player sender, String[] args) {

        EventService eventService = EventService.getInstance();
        EventStatus eventStatus = eventService.getEventStatus();

        if (eventStatus.equals(EventStatus.INACTIVE)) {
            Message.ERR_NO_EVENT_IS_RUNNING.send(sender);
            return;
        }

        PartecipantsManager partecipantsManager = PartecipantsManager.getInstance();

        if (partecipantsManager.isPresent(sender)) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_ALREADY_JOINED));
            return;
        }

        if (SettingsHandler.getSettingsHandler().echelonSupport) {
            if (EchelonHolder.getEchelonHolder().isPlayerInMutexActivity(sender)) {
                sender.sendMessage(ChatFormatter.formatErrorMessage("Stai già partecipando ad una attività : " +
                        EchelonHolder.getEchelonHolder().getPlayerMutexActivityName(sender)));
                return;
            } else
                EchelonHolder.getEchelonHolder().addPlayerMutexActivity(sender);
        }

        if (!eventStatus.equals(EventStatus.READY)) {
            partecipantsManager.addPartecipant(sender, sender.getLocation(), PartecipantStatus.DEAD);
            sender.setGameMode(GameMode.SPECTATOR);
            sender.teleport(eventService.getArena().getTower());
            Message.JOIN_STARTED_EVENT.send(sender);

        } else {
            partecipantsManager.addPartecipant(sender, sender.getLocation(), PartecipantStatus.PLAYING);
            sender.getInventory().clear();
            sender.setGameMode(GameMode.SURVIVAL);
            eventService.getRoundSpawn().teleport(sender);
            Message.JOIN_READY_EVENT.send(sender);
        }

        sender.playSound(sender.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);

        Bukkit.getScheduler().scheduleSyncDelayedTask(FWRiv.plugin, () -> infoAboutEventMessage(sender), 20L);

        Message.BROADCAST_PLAYERJOINEVENT.broadcast("fwriv.echo",sender.getDisplayName());

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }

    public void infoAboutEventMessage(Player sender) {
        //todo: vorrei par apparire un libro con dentro tutte le meccaniche dell'evento
        TextComponent infoClickText = new TextComponent(ChatColor.AQUA + "" + ChatColor.BOLD + "INFO");
        infoClickText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fwriv " + CommandName.INFO_COMMAND));

        sender.sendMessage(new TextComponent(ChatFormatter.formatInitializationMessage("Click ")), infoClickText,
                new TextComponent(" per conoscere le meccaniche di gioco dell'evento 'resta in vetta'"));

    }

}
