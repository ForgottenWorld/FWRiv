package me.architetto.rivevent.command.subcommand.superuser;

import me.architetto.rivevent.command.GameHandler;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.Messages;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class EventInfo extends SubCommand{
    @Override
    public String getName(){
        return "eventinfo";
    }

    @Override
    public String getDescription(){
        return "Info about current event.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent eventinfo";
    }

    GameHandler global = GameHandler.getInstance();

    @Override
    public void perform(Player sender, String[] args){

        if (!sender.hasPermission("rivevent.eventinfo")) {
            sender.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        if (global.presetSummon.isEmpty()){
            sender.sendMessage(ChatMessages.RED(Messages.ERR_NO_EVENT));
            return;
        }

        sender.sendTitle("", ChatColor.RED + "Partecipanti : " + ChatColor.RESET + global.playerJoined.size()
               + "  ||  " + ChatColor.AQUA + "Spettatori : " + ChatColor.RESET + global.playerSpectate.size(),0,100,0);

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }
}
