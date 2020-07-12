package me.architetto.rivevent.command.user;

import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.command.subcommand.superuser.InitCommand;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.Messages;
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
        if(!player.hasPermission("rivevent.join")){
            player.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        if(InitCommand.presetSum.isEmpty()){
            player.sendMessage(ChatMessages.RED(Messages.ERR_NO_EVENT));
            return;
        }

        if(InitCommand.playerJoined.containsKey(player.getUniqueId()) || InitCommand.playersSpectate.containsKey(player.getUniqueId())){
            player.sendMessage(ChatMessages.RED(Messages.ERR_JOIN));

        }else{

            //Todo: Vorrei inserire un messaggio che avverte che i player perdono tutto il loro inventario al momento del tp
            //Todo: ... e che al termine dell'evento verranno teletrasportati nel punto in cui fanno /rivevent join

            InitCommand.playerJoined.put(player.getUniqueId(), player.getLocation());
            InitCommand.playersSpectate.remove(player.getUniqueId());
            player.sendMessage(ChatMessages.GREEN(Messages.OK_JOIN));
        }


    }
}
