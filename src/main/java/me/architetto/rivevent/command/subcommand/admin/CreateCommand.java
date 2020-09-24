package me.architetto.rivevent.command.subcommand.admin;

import me.architetto.rivevent.command.GameHandler;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.listener.RightClickListener;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.Messages;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

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
    public void perform(Player sender, String[] args){

        if (!sender.hasPermission("rivevent.create")) {
            sender.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        if (args.length != 2) {
            if (args.length < 2)
                sender.sendMessage(ChatMessages.RED(Messages.NO_PRESET_NAME));
            else
                sender.sendMessage(ChatMessages.RED(Messages.NO_PARAM));
            return;
        }

        if (global.riveventPreset.containsKey(args[1].toLowerCase())) {
            sender.sendMessage(ChatMessages.RED(Messages.ERR_PRESET));
            return;
        }

        if (global.listenerActivator.containsKey(sender.getUniqueId())) {
            sender.sendMessage(ChatMessages.RED(Messages.ERR_PRESET2));
            return;
        }


        global.riveventPreset.put(args[1].toLowerCase(), new HashMap<>());

        global.listenerActivator.put(sender.getUniqueId(), args[1].toLowerCase());

        sender.sendMessage(ChatMessages.GREEN(Messages.USE_RIGHT_CLICK));
        sender.sendMessage(ChatMessages.PosMessage("1/6", RightClickListener.Step.SPAWN1));

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }


}
