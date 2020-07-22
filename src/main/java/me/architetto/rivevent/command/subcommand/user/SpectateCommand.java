package me.architetto.rivevent.command.subcommand.user;

import me.architetto.rivevent.command.GlobalVar;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.listener.LClickListener;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.LocSerialization;
import me.architetto.rivevent.util.Messages;
import org.bukkit.Sound;
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

        if (global.playerJoined.contains(player.getUniqueId())) {

            //TODO: Va inserito un messaggio apposito
            //Leavare l'evento ed entrare come spettatore.
            player.sendMessage(ChatMessages.RED(Messages.ERR_JOIN));
            return;

        }

        if (global.playerSpectate.contains(player.getUniqueId())) {

            player.sendMessage(ChatMessages.RED(Messages.ERR_SPECTATE));
            return;

        }

        global.playerSpectate.add(player.getUniqueId());
        player.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LClickListener.LOC.SPECTATE)));
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,5,1);
        player.sendMessage(ChatMessages.GREEN(Messages.OK_SPECTATE));

    }
}
