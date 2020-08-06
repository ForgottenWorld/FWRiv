package me.architetto.rivevent.command.subcommand.superuser;

import me.architetto.rivevent.command.GlobalVar;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.Messages;
import org.bukkit.Bukkit;
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

    @Override
    public void perform(Player player, String[] args){

        if (!player.hasPermission("rivevent.init")) {
            player.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        GlobalVar global = GlobalVar.getInstance();

        if (args.length!=2) {
            player.sendMessage(ChatMessages.RED(Messages.NO_PARAM));
        }else{
            if (!global.presetSummon.isEmpty()) {
                player.sendMessage(ChatMessages.RED(Messages.ERR_EVENT) + " [ " + global.presetSummon + " ] ");
                return;
            }

            if (!global.riveventPreset.containsKey(args[1])) {
                player.sendMessage(ChatMessages.RED(Messages.NO_PRESET));
            }else{
                global.presetSummon = args[1];
                player.sendMessage(ChatMessages.GREEN(Messages.OK_INIT + " \n "));
                Bukkit.getServer().broadcastMessage(ChatMessages.AQUA(Messages.BROADCAST_EVENT));

            }
        }
    }
}
