package me.architetto.rivevent.command.superuser;

import me.architetto.rivevent.arena.Arena;
import me.architetto.rivevent.arena.ArenaManager;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.config.SettingsHandler;
import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.util.ChatFormatter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;


public class InitCommand extends SubCommand{
    @Override
    public String getName(){
        return "init";
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

        if (!sender.hasPermission("rivevent.init")) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Permission Error: you do not have permission to use that command"));
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Syntax Error: insert preset name"));
            return;
        }

        String presetName = args[1];

        ArenaManager arenaManager = ArenaManager.getInstance();
        SettingsHandler settingsHandler = SettingsHandler.getInstance();
        Optional<Arena> preset = arenaManager.getArena(presetName);

        if (preset.isPresent()) {
            EventService eventService = EventService.getInstance();

            if (settingsHandler.respawnLocation == null) {
                sender.sendMessage(ChatFormatter.formatErrorMessage("Error: set respawn location with " + ChatColor.GOLD + "/rivevent config setrespawn"));
                return;
            }

            if (eventService.isRunning()) {
                sender.sendMessage(ChatFormatter.formatErrorMessage("Error: event is already running"));
                return;
            }

            eventService.initializeEvent(arenaManager.getArenaContainer().get(presetName));
            sender.sendMessage(ChatFormatter.formatSuccessMessage("Event successfully initialized"));
            broadcastNewEvent();
        } else
            sender.sendMessage(ChatFormatter.formatErrorMessage("Error: this preset doesn't exist"));


    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }

    public void broadcastNewEvent() {

        TextComponent joinClickMessage = new TextComponent(ChatColor.YELLOW + "[ JOIN ]");
        joinClickMessage.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/rivevent join") );
        joinClickMessage.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new Text("Click here to join event or use" + ChatColor.YELLOW + " /rivevent join")));

        for (Player p : Bukkit.getOnlinePlayers()) {

            p.sendMessage(new TextComponent(ChatFormatter.formatInitializationMessage("click ")),joinClickMessage,
                    new TextComponent(" and play with us !" + ChatColor.RED
                            + ChatColor.ITALIC + " !! your inventory will be deleted !!"));

            p.sendTitle("", ChatColor.AQUA + "RIV event started!"
                    + ChatColor.YELLOW + " /rivevent join",15,200,15);

        }

    }


}
