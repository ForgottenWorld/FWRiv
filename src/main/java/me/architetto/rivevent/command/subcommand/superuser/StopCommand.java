package me.architetto.rivevent.command.subcommand.superuser;

import me.architetto.rivevent.command.GlobalVar;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class StopCommand extends SubCommand{
    @Override
    public String getName(){
        return "stop";
    }

    @Override
    public String getDescription(){
        return "Cancel the current event.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent stop";
    }

    @Override
    public void perform(Player player, String[] args){

        if (!player.hasPermission("rivevent.stop")) {
            player.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        GlobalVar global = GlobalVar.getInstance();

        if (global.presetSummon.isEmpty()) {
            player.sendMessage(ChatMessages.RED(Messages.ERR_NO_EVENT));
        }else{
            global.presetSummon = "";

            if (!global.playerJoined.isEmpty()) {
                for (UUID key : global.playerJoined.keySet()) {

                    Player target = Bukkit.getPlayer(key);
                    target.teleport(global.playerJoined.get(key));

                }

                global.playerJoined.clear();
            }

            if (!global.playerSpectate.isEmpty()) {
                for (UUID key : global.playerSpectate.keySet()) {

                    Player target = Bukkit.getPlayer(key);
                    target.teleport(global.playerSpectate.get(key));

                }

                global.playerSpectate.clear();
            }

            global.setupDone=false;
            global.setupStart=false;

            player.sendMessage(ChatMessages.GREEN(Messages.STOP_EVENT));

        }
    }
}
