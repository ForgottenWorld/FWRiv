package me.architetto.fwriv.command.superuser;

import me.architetto.fwriv.FWRiv;
import me.architetto.fwriv.arena.Arena;
import me.architetto.fwriv.arena.ArenaManager;
import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.config.SettingsHandler;
import me.architetto.fwriv.event.service.EventService;
import me.architetto.fwriv.utils.ChatFormatter;
import me.architetto.fwriv.utils.CommandDescription;
import me.architetto.fwriv.utils.CommandName;
import me.architetto.fwriv.utils.Messages;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
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
        return CommandDescription.INIT_COMMAND;
    }

    @Override
    public String getSyntax(){
        return "/fwriv init <arena_name>";
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

        if (SettingsHandler.getSettingsHandler().safeRespawnLocation == null) {
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

        TextComponent startCMD = new TextComponent(ChatColor.YELLOW + "" + ChatColor.BOLD + "START");
        startCMD.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fwriv start"));
        sender.sendMessage(new TextComponent(ChatFormatter.formatSuccessMessage("Evento 'RIV' inizializzato. Click ")),startCMD,
                new TextComponent(" per startare l'evento."));

        Bukkit.getScheduler().scheduleSyncDelayedTask(FWRiv.plugin, this::broadcastNewEvent,10L);

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){

        if (args.length == 2)
            return new ArrayList<>(ArenaManager.getInstance().getArenaContainer().keySet());

        return null;
    }

    public void broadcastNewEvent() {

        TextComponent joinClickMessage = new TextComponent(ChatColor.YELLOW + "" + ChatColor.BOLD + "JOIN");
        joinClickMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fwriv join"));

        for (Player p : Bukkit.getOnlinePlayers()) {

            p.sendMessage(new TextComponent(ChatFormatter.formatInitializationMessage("Click ")),joinClickMessage,
                    new TextComponent(" per partecipare all'evento 'RIV'"));

            p.sendMessage(ChatFormatter.formatInitializationMessage(ChatColor.RED + ""
                    + ChatColor.BOLD + "ATTENZIONE : " + ChatColor.RESET + ChatColor.ITALIC
                    + "Partecipando all'evento il tuo inventario verra' cancellato !!"));

            p.sendTitle("",  "Evento "+ ChatColor.YELLOW + " RESTA IN VETTA " + ChatColor.RESET + " | "
                    +ChatColor.ITALIC + "Vieni a giocare con noi!",15,200,15);

        }
    }
}
