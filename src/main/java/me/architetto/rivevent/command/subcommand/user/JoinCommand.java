package me.architetto.rivevent.command.subcommand.user;

import me.architetto.rivevent.command.GlobalVar;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.listener.LeftClickOnBlock;
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


        if (global.playerJoined.containsKey(player.getUniqueId())) {

            player.sendMessage(ChatMessages.RED(Messages.ERR_JOIN));

        }else{

            if (global.setupStart) {

                player.sendMessage(ChatMessages.RED(Messages.USE_SPECTATE));
                return;

            }

            if (global.playerSpectate.containsKey(player.getUniqueId())) {

                global.playerJoined.put(player.getUniqueId(),global.playerSpectate.get(player.getUniqueId()));
                global.playerSpectate.remove(player.getUniqueId());

            }else{

                global.playerJoined.put(player.getUniqueId(), player.getLocation());
                player.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LeftClickOnBlock.LOC.SPECTATE)));
                player.playSound(player.getLocation(),Sound.ENTITY_ENDERMAN_TELEPORT,5,1);

            }

            player.sendMessage(ChatMessages.GREEN(Messages.OK_JOIN));

        }
    }
}
