package me.architetto.rivevent.command.subcommand.user;

import me.architetto.rivevent.command.GlobalVar;
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

    @Override
    public void perform(Player player, String[] args){

        if (!player.hasPermission("rivevent.leave")) {
            player.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        GlobalVar global = GlobalVar.getInstance();

        if(global.presetSummon.isEmpty()){
            player.sendMessage(ChatMessages.RED(Messages.ERR_NO_EVENT));
            return;
        }

        if (global.playerSpectate.containsKey(player.getUniqueId())) {

            player.teleport(global.playerSpectate.get(player.getUniqueId()));
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,5,1);

            global.playerSpectate.remove(player.getUniqueId());

        } else if (global.playerJoined.containsKey(player.getUniqueId())) {

            player.teleport(global.playerJoined.get(player.getUniqueId()));
            player.playSound(player.getLocation(),Sound.ENTITY_ENDERMAN_TELEPORT,5,1);

            global.playerJoined.remove(player.getUniqueId());

        } else{
            player.sendMessage(ChatMessages.RED(Messages.NO_EVENT_JOINED));
            return;
        }

        player.sendMessage(ChatMessages.GREEN(Messages.OK_LEAVE));

    }
}
