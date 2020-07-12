package me.architetto.rivevent.command.subcommand.superuser;

import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.command.subcommand.admin.CreateCommand;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.Messages;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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
    public static HashMap<UUID,Location> playerJoined = new HashMap<>();
    public static HashMap<UUID,Location> playersSpectate = new HashMap<>();


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
                playerJoined.clear();
                playersSpectate.clear();
                player.sendMessage(ChatMessages.GREEN(Messages.OK_INIT));
                //TODO : Broadcast message ?
                //TODO : Potrebbe contenere un chat event che se cliccato fa joinare il palyer
            }
        }
    }
}
