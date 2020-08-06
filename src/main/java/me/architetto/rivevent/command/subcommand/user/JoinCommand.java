package me.architetto.rivevent.command.subcommand.user;

import me.architetto.rivevent.command.GlobalVar;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.listener.LeftclickListener;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.LocSerialization;
import me.architetto.rivevent.util.Messages;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class JoinCommand extends SubCommand{

    @Override
    public String getName(){
        return "join";
    }

    @Override
    public String getDescription(){
        return "Join an event.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent join";
    }

    @Override
    public void perform(Player player, String[] args){

        if (!player.hasPermission("rivevent.join")) {
            player.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        GlobalVar global = GlobalVar.getInstance();

        if (global.presetSummon.isEmpty()) {

            player.sendMessage(ChatMessages.RED(Messages.ERR_NO_EVENT));
            return;
            
        }

        if (global.playerJoined.contains(player.getUniqueId())) {

            player.sendMessage(ChatMessages.RED(Messages.ERR_JOIN));
            return;

        }

        if (global.playerSpectate.contains(player.getUniqueId())) {

            player.sendMessage(ChatMessages.RED(Messages.ERR_JOIN2));
            return;

        }

        if (global.setupStart) {

            player.sendMessage(ChatMessages.RED(Messages.USE_SPECTATE));
            return;

        }

        global.playerJoined.add(player.getUniqueId());
        player.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LeftclickListener.LOC.SPECTATE)));
        player.playSound(player.getLocation(),Sound.ENTITY_ENDERMAN_TELEPORT,2,1);
        player.sendMessage(ChatMessages.GREEN(Messages.OK_JOIN));

    }
}
