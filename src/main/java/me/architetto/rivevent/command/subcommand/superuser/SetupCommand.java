package me.architetto.rivevent.command.subcommand.superuser;

import me.architetto.rivevent.command.GlobalVar;
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

    //public static boolean setupStart = false; //Disabilita la possibilità di joinare
    //public static boolean setupDone = false;  //Abilita il comando /rivevent start

    @Override
    public void perform(Player player, String[] args){
        if(!player.hasPermission("rivevent.setup")){
            player.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        GlobalVar global = GlobalVar.getInstance();

        if(global.setupStart){
            player.sendMessage(ChatMessages.RED(Messages.ERR_SETUP_DONE));
            return;
        }

        if(global.presetSum.isEmpty()){
            player.sendMessage(ChatMessages.RED(Messages.ERR_NO_EVENT));
        }else{
            global.setupDone = true; //ToDo : i comandi /rivevent [join,spectate] devono essere disabilitati dopo il lancio del comando /rivevent setup



            int randomNum ;

            //Tippa tutti i player joinati nei vari punti di spawn.
            for (UUID key : global.playerJoined.keySet()) {
                randomNum = ThreadLocalRandom.current().nextInt(1, 4 + 1);
                Player target = Bukkit.getPlayer(key);
                if (target.isOnline()){
                    target.sendMessage(global.riveventPreset.get(global.presetSum).get(LeftClickOnBlock.LOC.SPAWN1));
                    switch(randomNum){
                        case 1:
                            target.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSum).get(LeftClickOnBlock.LOC.SPAWN1)));
                            continue;
                        case 2:
                            target.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSum).get(LeftClickOnBlock.LOC.SPAWN2)));
                            continue;
                        case 3:
                            target.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSum).get(LeftClickOnBlock.LOC.SPAWN3)));
                            continue;
                        case 4:
                            target.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSum).get(LeftClickOnBlock.LOC.SPAWN4)));
                            continue;
                        default:
                            target.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSum).get(LeftClickOnBlock.LOC.SPAWN1)));

                    }
                }
            }

            global.setupDone = true;

        }
    }
}

