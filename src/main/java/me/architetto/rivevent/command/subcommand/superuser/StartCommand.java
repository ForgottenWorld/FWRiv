package me.architetto.rivevent.command.subcommand.superuser;

import me.architetto.rivevent.command.GlobalVar;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.Messages;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.Player;

import java.util.Iterator;

public class StartCommand extends SubCommand{
    @Override
    public String getName(){
        return "start";
    }

    @Override
    public String getDescription(){
        return "Start event.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent start";
    }

    @Override
    public void perform(Player player, String[] args){

        if (!player.hasPermission("rivevent.start")) {
            player.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        GlobalVar global = GlobalVar.getInstance();

        if(global.presetSummon.isEmpty()){
            player.sendMessage(ChatMessages.RED(Messages.ERR_NO_EVENT));
            return;
        }

        if (!global.setupDone) {
            player.sendMessage(ChatMessages.RED(Messages.ERR_SETUP_NOTREADY));
            return;
        }

        for(org.bukkit.block.Block block : global.doorsToOpen){

            BlockData data = block.getBlockData();
            Openable door = (Openable) data;
            door.setOpen(true);
            block.setBlockData(door, true);


        }





    }
}
