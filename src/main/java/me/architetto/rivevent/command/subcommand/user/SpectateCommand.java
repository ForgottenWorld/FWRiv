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
    public void perform(Player sender, String[] args){

        if (!sender.hasPermission("rivevent.spectate")) {
            sender.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        GameHandler global = GameHandler.getInstance();

        if (global.presetSummon.isEmpty()) {
            sender.sendMessage(ChatMessages.RED(Messages.ERR_NO_EVENT));
            return;
        }

        if (global.playerJoined.contains(sender.getUniqueId())) {

            sender.sendMessage(ChatMessages.RED(Messages.ERR_SPECTATE2));
            return;

        }

        if (global.playerSpectate.contains(sender.getUniqueId())) {

            sender.sendMessage(ChatMessages.RED(Messages.ERR_SPECTATE));
            return;

        }

        global.playerSpectate.add(sender.getUniqueId());
        sender.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(RightClickListener.Step.SPECTATE)));
        sender.playSound(sender.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,2,1);
        sender.sendMessage(ChatMessages.GREEN(Messages.OK_SPECTATE));

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }
}
