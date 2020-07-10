package me.architetto.rivevent.command.subcommand.superuser;

import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.Messages;
import org.bukkit.entity.Player;

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

        if (!player.hasPermission("rivevent.stop")){
            player.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        if (InitCommand.presetSum.isEmpty()){
            player.sendMessage(ChatMessages.RED(Messages.ERR_NO_EVENT));
        }else{
            InitCommand.presetSum = "";
            player.sendMessage(ChatMessages.GREEN(Messages.STOP_EVENT));
            //TODO: non è completo, ci saranno anche altre variabili da azzerare come la lista dei player che hanno joinato.
            //TODO: Broadcast message ?

        }

    }
}
