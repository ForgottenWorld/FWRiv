package me.architetto.rivevent.command.subcommand.superuser;

import me.architetto.rivevent.command.GlobalVar;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.listener.LClickListener;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.Messages;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class ListCommand extends SubCommand{
    @Override
    public String getName(){
        return "list";
    }

    @Override
    public String getDescription(){
        return "Preset list.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent list <preset_name>";
    }

    @Override
    public void perform(Player player, String[] args){

        if (!player.hasPermission("rivevent.list")) {
            player.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        if (args.length > 2) {
            player.sendMessage(ChatMessages.RED(Messages.NO_PARAM));
            return;
        }

        GlobalVar global = GlobalVar.getInstance();

        if (global.riveventPreset.isEmpty()) {
            player.sendMessage(ChatMessages.RED(Messages.VOID_PRESET_LIST));
            return;
        }

        if (args.length == 2 ) {

            if (!global.riveventPreset.containsKey(args[1])) {
                player.sendMessage(ChatMessages.RED(Messages.NO_PRESET));
                return;
            }

            player.sendMessage(ChatColor.DARK_AQUA + "PRESET NAME : " + ChatColor.RESET + ChatColor.ITALIC + args[1] +
                    "\n" + "------------------------------" +
                    "\n" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "SPECTATE : " + ChatColor.RESET + global.riveventPreset.get(args[1]).get(LClickListener.LOC.SPECTATE) +
                    "\n" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "SPAWN 1 : " + ChatColor.RESET + global.riveventPreset.get(args[1]).get(LClickListener.LOC.SPAWN1) +
                    "\n" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "SPAWN 2 : " + ChatColor.RESET + global.riveventPreset.get(args[1]).get(LClickListener.LOC.SPAWN2) +
                    "\n" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "SPAWN 3 : " + ChatColor.RESET + global.riveventPreset.get(args[1]).get(LClickListener.LOC.SPAWN3) +
                    "\n" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "SPAWN 4 : " + ChatColor.RESET + global.riveventPreset.get(args[1]).get(LClickListener.LOC.SPAWN4) +
                    "\n" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "TOWER : " + ChatColor.RESET + global.riveventPreset.get(args[1]).get(LClickListener.LOC.TOWER));

            return;
        }

        player.sendMessage(ChatMessages.GREEN(global.riveventPreset.keySet().toString()));

    }
}
