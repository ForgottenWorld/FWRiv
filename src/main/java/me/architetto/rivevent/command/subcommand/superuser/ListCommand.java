package me.architetto.rivevent.command.subcommand.superuser;

import me.architetto.rivevent.command.GameHandler;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.listener.RightClickListener;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;


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

    GameHandler global = GameHandler.getInstance();

    @Override
    public void perform(Player player, String[] args){

        if (!player.hasPermission("rivevent.list")) {
            player.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

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
                    "\n" + "//--------------------------------------//" +
                    "\n" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "SPECTATE : " + ChatColor.RESET + formatCoord(global.riveventPreset.get(args[1]).get(RightClickListener.Step.SPECTATE)) +
                    "\n" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "SPAWN POINT #1 : " + ChatColor.RESET + formatCoord(global.riveventPreset.get(args[1]).get(RightClickListener.Step.SPAWN1)) +
                    "\n" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "SPAWN POINT #2 : " + ChatColor.RESET + formatCoord(global.riveventPreset.get(args[1]).get(RightClickListener.Step.SPAWN2)) +
                    "\n" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "SPAWN POINT #3 : " + ChatColor.RESET + formatCoord(global.riveventPreset.get(args[1]).get(RightClickListener.Step.SPAWN3)) +
                    "\n" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "SPAWN POINT #4 : " + ChatColor.RESET + formatCoord(global.riveventPreset.get(args[1]).get(RightClickListener.Step.SPAWN4)) +
                    "\n" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "TOWER POINT : " + ChatColor.RESET + formatCoord(global.riveventPreset.get(args[1]).get(RightClickListener.Step.TOWER)) +
                    "\n" + "//--------------------------------------//");

            return;
        }

        player.sendMessage(ChatMessages.GREEN(" PRESET LIST :  " + global.riveventPreset.keySet().toString()));

    }

    public String formatCoord (String s) {

        String [] parts = s.split(":");
        UUID u = UUID.fromString(parts[3]);

        return glowXYZW("X: ") + parts[0] + glowXYZW(" Y: ") + parts[1] + glowXYZW(" Z: ") + parts[2]
                + glowXYZW(" WORLD: ") + Objects.requireNonNull(Bukkit.getWorld(u)).getName();

    }

    public String glowXYZW (String s) {

        return ChatColor.DARK_GREEN + s + ChatColor.RESET;
    }
}
