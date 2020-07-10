package me.architetto.rivevent.command.subcommand.superuser;

import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.command.subcommand.admin.CreateCommand;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.Messages;
import org.bukkit.entity.Player;

public class InitCommand extends SubCommand{
    @Override
    public String getName(){
        return "init";
    }

    @Override
    public String getDescription(){
        return "Initializes an event.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent init <preset_name>";
    }

    public static String presetSum = "";

    @Override
    public void perform(Player player, String[] args){

        if(!player.hasPermission("rivevent.init")){
            player.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        if(args.length!=2){
            player.sendMessage(ChatMessages.RED(Messages.NO_PARAM));
        }else{
            if(!presetSum.isEmpty()){
                player.sendMessage(ChatMessages.RED(Messages.ERR_EVENT) + " [ " + presetSum + " ] ");
                return;
            }

            if(!CreateCommand.riveventPreset.containsKey(args[1])){
                player.sendMessage(ChatMessages.RED(Messages.NO_PRESET));
            }else{
                presetSum = args[1];
                player.sendMessage(ChatMessages.GREEN(Messages.OK_INIT));
                //TODO : Broadcast message ?
                //TODO : Per sicurezza clear pure delle eventuali liste di player (lista ancora non aggiunta)
            }
        }
    }
}
