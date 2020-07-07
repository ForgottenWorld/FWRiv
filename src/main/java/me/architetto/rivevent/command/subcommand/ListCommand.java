package me.architetto.rivevent.command.subcommand;

import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.Messages;
import me.architetto.rivevent.util.RIVLocation;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;

public class ListCommand extends SubCommand{
    @Override
    public String getName(){
        return "list";
    }

    @Override
    public String getDescription(){
        return "Preset list.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent list <preset_name>";
    }

    @Override
    public void perform(Player player, String[] args){

        if (!player.hasPermission("rivevent.list")){
            player.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        if (args.length > 2){
            player.sendMessage(ChatMessages.RED(Messages.NO_PARAM));
            return;
        }

        if(args.length == 2 ){
            if(!CreateCommand.riveventPreset.containsKey(args[1])){
                player.sendMessage(ChatMessages.RED(Messages.NO_PRESET));
                return;
            }

            for(Map.Entry entry : CreateCommand.riveventPreset.get(args[1]).entrySet()){
                player.sendMessage(ChatColor.GOLD + "PUNTO : " + ChatColor.RESET + entry.getKey() + "\t" + RIVLocation.getStringFromLocation((Location) entry.getValue()));
            }

            return;
        }

        player.sendMessage(CreateCommand.riveventPreset.keySet().toString());





    }
}
