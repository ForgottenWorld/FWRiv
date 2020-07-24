package me.architetto.rivevent.command.subcommand.admin;

import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.Messages;
import org.bukkit.entity.Player;

public class SetSpawnCommand extends SubCommand{
    @Override
    public String getName(){
        return "setspawn";
    }

    @Override
    public String getDescription(){
        return "Set default spawn location.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent setspawn";
    }

    @Override
    public void perform(Player player, String[] args){

        if (!player.hasPermission("rivevent.setspawn")) {
            player.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        //TODO.

    }
}
