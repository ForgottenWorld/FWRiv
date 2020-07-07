package me.architetto.rivevent.command.subcommand;

import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.listener.LeftClickOnBlock;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.Messages;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class CreateCommand extends SubCommand{

    public static HashMap<String, HashMap<LeftClickOnBlock.LOC, Location>> riveventPreset = new HashMap<>();
    public static HashMap<UUID, String> listenerActivator = new HashMap<>();

    @Override
    public String getName(){
        return "create";
    }

    @Override
    public String getDescription(){
        return "Create a RIV preset.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent create <preset_name>";
    }

    @Override
    public void perform(Player player, String[] args){

        if (!player.hasPermission("rivevent.create")){
            player.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        if (args.length != 2){
            player.sendMessage(ChatMessages.RED(Messages.NO_PARAM));
            return;
        }


        if (riveventPreset.containsKey(args[1])){
            player.sendMessage(ChatMessages.RED(Messages.ERR_PRESET));
            return;
        }

        riveventPreset.put(args[1], new HashMap<LeftClickOnBlock.LOC, Location>());
        player.sendMessage(ChatMessages.PosMessage("1/6", LeftClickOnBlock.LOC.SPAWN1));
        listenerActivator.put(player.getUniqueId(), args[1]);


    }
}
