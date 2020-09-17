package me.architetto.rivevent.command.subcommand.user;

import me.architetto.rivevent.command.GameHandler;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.Messages;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class LeaveCommand extends SubCommand{
    @Override
    public String getName(){
        return "leave";
    }

    @Override
    public String getDescription(){
        return "Leave a event.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent leave";
    }

    GameHandler global = GameHandler.getInstance();

    @Override
    public void perform(Player sender, String[] args){

        if (!sender.hasPermission("rivevent.leave")) {
            sender.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        if (global.presetSummon.isEmpty() && global.playerJoined.isEmpty() && global.playerSpectate.isEmpty()) {
            sender.sendMessage(ChatMessages.RED(Messages.ERR_NO_EVENT));
            return;
        }

        if (global.playerSpectate.contains(sender.getUniqueId())) {

            sender.teleport(global.endEventRespawnLocation);
            sender.playSound(sender.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,2,1);
            global.playerSpectate.remove(sender.getUniqueId());
            global.playerOut.remove(sender.getUniqueId());

        } else if (global.playerJoined.contains(sender.getUniqueId())) {

            sender.getInventory().clear();
            global.playerJoined.remove(sender.getUniqueId());

            if (global.setupStartFlag) {
                sender.setHealth(0);
                sender.sendMessage(ChatMessages.GREEN(Messages.OK_LEAVE));
                return;
            }


            sender.teleport(global.endEventRespawnLocation);
            sender.playSound(sender.getLocation(),Sound.ENTITY_ENDERMAN_TELEPORT,2,1);


        } else{
            sender.sendMessage(ChatMessages.RED(Messages.NO_EVENT_JOINED));
            return;
        }

        sender.sendMessage(ChatMessages.GREEN(Messages.OK_LEAVE));

    }
}
