package me.architetto.fwriv.command.user;

import me.architetto.fwriv.FWRiv;
import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.config.SettingsHandler;
import me.architetto.fwriv.echelon.EchelonHolder;
import me.architetto.fwriv.partecipant.PartecipantStatus;
import me.architetto.fwriv.event.PartecipantsManager;
import me.architetto.fwriv.event.service.EventService;
import me.architetto.fwriv.event.service.EventStatus;
import me.architetto.fwriv.utils.ChatFormatter;
import me.architetto.fwriv.utils.CommandDescription;
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
        return CommandDescription.JOIN_COMMAND;
    }

    @Override
    public String getSyntax(){
        return "/fwriv join";
    }

    @Override
    public String getPermission() {
        return "rivevent.user";
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
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_NO_EVENT_RUNNING));
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
            sender.getWorld().playSound(sender.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
            sender.sendMessage(ChatFormatter.formatSuccessMessage(Messages.JOIN_STARTED_EVENT));
            return;

        }

        partecipantsManager.addPartecipant(sender, sender.getLocation(), PartecipantStatus.PLAYING);
        eventService.getRoundSpawn().teleport(sender);
        sender.getInventory().clear();
        sender.setGameMode(GameMode.SURVIVAL);
        sender.getWorld().playSound(sender.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,1,1);
        sender.sendMessage(ChatFormatter.formatSuccessMessage(Messages.JOIN_EVENT));

        Bukkit.getScheduler().scheduleSyncDelayedTask(FWRiv.plugin, () -> infoAboutEventMessage(sender), 20L);

        echoMessage(sender.getDisplayName());

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }


    public void echoMessage(String playername) {

        Bukkit.getServer().broadcast(ChatFormatter.formatEventMessage(ChatColor.YELLOW + playername
                + ChatColor.RESET + ChatColor.GRAY + "" + ChatColor.ITALIC + " ha " + ChatColor.GREEN + "joinato" + ChatColor.WHITE + " l'evento RIV. " + ChatColor.RESET
                + ChatColor.GREEN + "#" + PartecipantsManager.getInstance().getPartecipantsUUID(PartecipantStatus.ALL).size()),"riveven.echo");

    }

    public void infoAboutEventMessage(Player sender) {
        TextComponent infoClickText = new TextComponent(ChatColor.AQUA + "" + ChatColor.BOLD + "INFO");
        infoClickText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fwriv " + CommandName.INFO_COMMAND));

        sender.sendMessage(new TextComponent(ChatFormatter.formatInitializationMessage("Click ")), infoClickText,
                new TextComponent(" per conoscere le meccaniche di gioco dell'evento 'resta in vetta'"));

    }

}
