package me.architetto.rivevent.command.subcommand.superuser;

import me.architetto.rivevent.command.GlobalVar;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
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

            //TODO: Vanno tippati a SPES eventuali player joinati. (E ripuliti tutti gli inventari)


            if (!global.playerJoined.isEmpty()){
                clearInventories(global.playerJoined);
            }
            global.clearVar();
            player.sendMessage(ChatMessages.GREEN(Messages.STOP_EVENT));

        }
    }

    public void clearInventories (List<UUID> playerJoined) {

        for (UUID key : playerJoined) {

            Player player = Bukkit.getPlayer(key);
            player.getInventory().clear();


        }

    }

    public void tpBackToSpes (List<UUID> playerJoined) {

        //TODO. va preso il punto di spes in cui tippare.

    }



}
