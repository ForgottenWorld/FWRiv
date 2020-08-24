package me.architetto.rivevent.command.subcommand.admin;

import me.architetto.rivevent.command.GameHandler;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.listener.RightClickListener;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.Messages;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CreateCommand extends SubCommand{

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

    GameHandler global = GameHandler.getInstance();

    @Override
    public void perform(Player player, String[] args){

        if (!player.hasPermission("rivevent.create")) {
            player.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        if (args.length != 2) {
            if (args.length < 2)
                player.sendMessage(ChatMessages.RED(Messages.NO_PRESET_NAME));
            else
                player.sendMessage(ChatMessages.RED(Messages.NO_PARAM));
            return;
        }

        if (global.riveventPreset.containsKey(args[1])) {
            player.sendMessage(ChatMessages.RED(Messages.ERR_PRESET));
            return;
        }

        if (global.listenerActivator.containsKey(player.getUniqueId())) {
            player.sendMessage(ChatMessages.RED(Messages.ERR_PRESET2));
            return;
        }

        global.riveventPreset.put(args[1], new HashMap<>());
        global.listenerActivator.put(player.getUniqueId(), args[1]);

        player.sendMessage(ChatMessages.GREEN("Right-click block to insert position  ..."));
        player.sendMessage(ChatMessages.PosMessage("1/6", RightClickListener.Step.SPAWN1));

    }
}
