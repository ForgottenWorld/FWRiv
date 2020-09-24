package me.architetto.rivevent.command.subcommand.user;

import me.architetto.rivevent.command.GameHandler;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.listener.RightClickListener;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.LocSerialization;
import me.architetto.rivevent.util.Messages;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

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

    GameHandler global = GameHandler.getInstance();

    @Override
    public void perform(Player sender, String[] args){

        if (!sender.hasPermission("rivevent.join")) {
            sender.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        if (global.presetSummon.isEmpty()) {

            sender.sendMessage(ChatMessages.RED(Messages.ERR_NO_EVENT));
            return;
            
        }

        if (global.playerJoined.contains(sender.getUniqueId())) {

            sender.sendMessage(ChatMessages.RED(Messages.ERR_JOIN));
            return;

        }

        if (global.playerSpectate.contains(sender.getUniqueId())) {

            sender.sendMessage(ChatMessages.RED(Messages.ERR_JOIN2));
            return;

        }

        if (global.setupStartFlag) {

            sender.sendMessage(ChatMessages.RED(Messages.USE_SPECTATE));
            return;

        }

        global.playerJoined.add(sender.getUniqueId());
        sender.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(RightClickListener.Step.SPECTATE)));
        sender.playSound(sender.getLocation(),Sound.ENTITY_ENDERMAN_TELEPORT,2,1);
        sender.sendMessage(ChatMessages.GREEN(Messages.OK_JOIN));

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }
}
