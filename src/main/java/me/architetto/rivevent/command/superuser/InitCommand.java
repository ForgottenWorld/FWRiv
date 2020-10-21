package me.architetto.rivevent.command.superuser;

import me.architetto.rivevent.arena.Arena;
import me.architetto.rivevent.arena.ArenaManager;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.config.SettingsHandler;
import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.util.ChatFormatter;
import me.architetto.rivevent.util.CommandName;
import me.architetto.rivevent.util.Messages;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class InitCommand extends SubCommand{
    @Override
    public String getName(){
        return CommandName.INIT_COMMAND;
    }

    @Override
    public String getDescription(){
        return "Initializes an event.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent init <arena_name>";
    }

    @Override
    public void perform(Player sender, String[] args){

        if (!sender.hasPermission("rivevent.eventmanager")) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_PERMISSION));
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_ARENA_CMD_SYNTAX));
            return;
        }

        String presetName = args[1];

        ArenaManager arenaManager = ArenaManager.getInstance();

        Optional<Arena> arena = arenaManager.getArena(presetName);

        if (!arena.isPresent()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_NO_ARENA_NAME));
            return;
        }

        if (SettingsHandler.getInstance().respawnLocation == null) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_NO_RESPAWN_LOCATION));
            return;
        }

        EventService eventService = EventService.getInstance();

        if (eventService.isRunning()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_EVENT_RUNNING));
            return;
        }

        eventService.initializeEvent(arenaManager.getArenaContainer().get(presetName));
        eventService.setDoorsStatus(false);
        broadcastNewEvent();

        TextComponent startCMD = new TextComponent(ChatColor.YELLOW + "START");
        startCMD.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/rivevent start") );
        startCMD.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new Text("Click to start event")));
        sender.sendMessage(new TextComponent(ChatFormatter.formatSuccessMessage("Evento 'RIV' inizializzato. Click ")),startCMD,
                new TextComponent(" per startare."));

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){

        if (args.length == 2){

            return new ArrayList<>(ArenaManager.getInstance().getArenaContainer().keySet());

        }


        return null;
    }

    public void broadcastNewEvent() {

        TextComponent joinClickMessage = new TextComponent(ChatColor.YELLOW + "JOIN");
        joinClickMessage.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/rivevent join") );
        joinClickMessage.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new Text("Click to join event")));

        for (Player p : Bukkit.getOnlinePlayers()) {

            p.sendMessage(new TextComponent(ChatFormatter.formatInitializationMessage("Click ")),joinClickMessage,
                    new TextComponent(" per partecipare all'evento 'RIV'"));
            p.sendMessage(ChatFormatter.formatInitializationMessage(ChatColor.RED + "ATTENZIONE : " + ChatColor.RESET + "Partecipando il tuo inventario verra' cancellato !!"));
            p.sendTitle("", ChatColor.AQUA + "EVENTO 'RIV'",15,200,15);

        }

    }


}
