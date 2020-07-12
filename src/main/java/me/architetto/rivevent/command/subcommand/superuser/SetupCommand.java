package me.architetto.rivevent.command.subcommand.superuser;

import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.command.subcommand.admin.CreateCommand;
import me.architetto.rivevent.listener.LeftClickOnBlock;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.LocSerialization;
import me.architetto.rivevent.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class SetupCommand extends SubCommand{
    @Override
    public String getName(){
        return "setup";
    }

    @Override
    public String getDescription(){
        return null;
    }

    @Override
    public String getSyntax(){
        return "/rivevent setup";
    }

    public static boolean setupStart = false; //Disabilita la possibilità di joinare
    public static boolean setupDone = false;  //Abilita il comando /rivevent start

    @Override
    public void perform(Player player, String[] args){
        if(!player.hasPermission("rivevent.setup")){
            player.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        if(setupStart){
            player.sendMessage(ChatMessages.RED(Messages.ERR_SETUP_DONE));
            return;
        }

        if(InitCommand.presetSum.isEmpty()){
            player.sendMessage(ChatMessages.RED(Messages.ERR_NO_EVENT));
        }else{
            setupStart = true; //ToDo : i comandi /rivevent [join,spectate] devono essere disabilitati dopo il lancio del comando /rivevent setup
            player.sendMessage("Qui ci sono ! CI SONO CI SONO");

            int randomNum = ThreadLocalRandom.current().nextInt(1, 4 + 1);
            player.sendMessage("" + randomNum);

            //Tippa tutti i player joinati nei vari punti di spawn.
            for (UUID key : InitCommand.playerJoined.keySet()) {
                Player target = Bukkit.getPlayer(key);
                target.sendMessage("HO SCELTO PROPRIO TE --> " + target.getName());
                if (target.isOnline()){
                    target.sendMessage(CreateCommand.riveventPreset.get(InitCommand.presetSum).get(LeftClickOnBlock.LOC.SPAWN1));
                    switch(randomNum){
                        case 1:
                            target.teleport(LocSerialization.getDeserializedLocation(CreateCommand.riveventPreset.get(InitCommand.presetSum).get(LeftClickOnBlock.LOC.SPAWN1)));
                            continue;
                        case 2:
                            target.teleport(LocSerialization.getDeserializedLocation(CreateCommand.riveventPreset.get(InitCommand.presetSum).get(LeftClickOnBlock.LOC.SPAWN2)));
                            continue;
                        case 3:
                            target.teleport(LocSerialization.getDeserializedLocation(CreateCommand.riveventPreset.get(InitCommand.presetSum).get(LeftClickOnBlock.LOC.SPAWN3)));
                            continue;
                        case 4:
                            target.teleport(LocSerialization.getDeserializedLocation(CreateCommand.riveventPreset.get(InitCommand.presetSum).get(LeftClickOnBlock.LOC.SPAWN4)));
                            continue;
                        default:
                            target.teleport(LocSerialization.getDeserializedLocation(CreateCommand.riveventPreset.get(InitCommand.presetSum).get(LeftClickOnBlock.LOC.SPAWN1)));

                    }
                }
            }

            setupDone = true;

        }
    }
}

