package me.architetto.rivevent.command.subcommand.superuser;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.command.GameHandler;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.listener.RightClickListener;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.LocSerialization;
import me.architetto.rivevent.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RestartCommand extends SubCommand{
    @Override
    public String getName(){
        return "restart";
    }

    @Override
    public String getDescription(){
        return "Restart RIV event.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent restart";
    }

    GameHandler global = GameHandler.getInstance();

    @Override
    public void perform(Player sender, String[] args){

        if (!sender.hasPermission("rivevent.restart")) {
            sender.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        if (global.presetSummon.isEmpty()) {
            sender.sendMessage(ChatMessages.RED(Messages.ERR_NO_EVENT));
            return;
        }

        Bukkit.getScheduler().cancelTasks(RIVevent.plugin);
        global.restartEvent();

        if (!global.playerJoined.isEmpty()) {

            for (UUID u : global.playerJoined) {
                Player p = Bukkit.getPlayer(u);
                if (p != null){
                    p.getInventory().clear();
                    p.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(RightClickListener.Step.SPECTATE)));
                    p.playSound(sender.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 2, 1);
                    p.sendMessage(ChatMessages.AQUA(Messages.OK_JOIN));
                }

            }
        }

        if (!global.playerOut.isEmpty()) {

            global.playerJoined.addAll(global.playerOut);

            for (UUID u : global.playerOut) {
                Player p = Bukkit.getPlayer(u);
                if (p != null)
                    p.sendMessage(ChatMessages.GREEN(Messages.OK_JOIN));
            }

            global.playerOut.clear();

        }

        for (Player p : Bukkit.getOnlinePlayers()) {

            if (!global.playerJoined.contains(p.getUniqueId())){
                p.sendTitle("", Messages.NEW_EVENT_TITLE, 20, 200, 20);
                p.sendMessage(ChatMessages.AQUA(Messages.BROADCAST_EVENT));
            }
        }



    }
}
