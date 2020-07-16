package me.architetto.rivevent.command.subcommand.user;

import me.architetto.rivevent.command.GlobalVar;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.Messages;
import org.bukkit.entity.Player;

public class SpectateCommand extends SubCommand{
    @Override
    public String getName(){
        return "spectate";
    }

    @Override
    public String getDescription(){
        return "Join an event as spectator.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent spectate";
    }

    @Override
    public void perform(Player player, String[] args){

        if (!player.hasPermission("rivevent.spectate")) {
            player.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        GlobalVar global = GlobalVar.getInstance();

        if (global.presetSummon.isEmpty()) {
            player.sendMessage(ChatMessages.RED(Messages.ERR_NO_EVENT));
            return;
        }

        if (global.playerSpectate.containsKey(player.getUniqueId())) {
            player.sendMessage(ChatMessages.RED(Messages.ERR_JOIN));

        }else{




            global.playerSpectate.put(player.getUniqueId(), player.getLocation());
            global.playerJoined.remove(player.getUniqueId());
            player.sendMessage(ChatMessages.GREEN(Messages.OK_JOIN));

        }
    }
}
