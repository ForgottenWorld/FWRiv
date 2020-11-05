package me.architetto.rivevent.command.user;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.event.PlayersManager;
import me.architetto.rivevent.util.ChatFormatter;
import me.architetto.rivevent.util.CommandName;
import me.architetto.rivevent.util.Messages;
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
        return "Join event.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent join";
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

        if (PlayersManager.getInstance().getAllEventPlayers().contains(sender.getUniqueId())) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_ALREADY_JOINED));
            return;
        }

        PlayersManager.getInstance().addPlayerLocation(sender.getUniqueId(),sender.getLocation());

        if (!eventService.isStarted()) {

            PlayersManager.getInstance().addActivePlayer(sender.getUniqueId());
            eventService.teleportToSpawnPoint(sender);
            sender.getInventory().clear();
            sender.setGameMode(GameMode.SURVIVAL);
            sender.getWorld().playSound(sender.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,1,1);
            sender.sendMessage(ChatFormatter.formatSuccessMessage(Messages.JOIN_EVENT));

        } else {

            PlayersManager.getInstance().addDeathPlayer(sender.getUniqueId());
            sender.setGameMode(GameMode.SPECTATOR);
            sender.teleport(eventService.getSummonedArena().getTower());
            sender.getWorld().playSound(sender.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
            sender.sendMessage(ChatFormatter.formatSuccessMessage(Messages.JOIN_STARTED_EVENT));

        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(RIVevent.plugin, () -> infoAboutEventMessage(sender), 20L);

        echoMessage(sender.getDisplayName());

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }


    public void echoMessage(String playername) {

        Bukkit.getServer().broadcast(ChatFormatter.formatEventMessage(ChatColor.YELLOW + playername
                + ChatColor.RESET + ChatColor.GRAY + "" + ChatColor.ITALIC + " ha joinato l'evento RIV. " + ChatColor.RESET
                + ChatColor.GREEN + "#" + PlayersManager.getInstance().getAllEventPlayers().size()),"riveven.echo");

    }

    public void infoAboutEventMessage(Player sender) {
        TextComponent infoClickText = new TextComponent(ChatColor.AQUA + "" + ChatColor.BOLD + "INFO");
        infoClickText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rivevent " + CommandName.INFO_COMMAND));

        sender.sendMessage(new TextComponent(ChatFormatter.formatInitializationMessage("Click ")), infoClickText,
                new TextComponent(" per conoscere le meccaniche di gioco dell'evento 'resta in vetta'"));

    }

}
