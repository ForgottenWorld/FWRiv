package me.architetto.rivevent.command.subcommand.superuser;

import me.architetto.rivevent.command.GameHandler;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.Messages;
import org.bukkit.*;

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

    GameHandler global = GameHandler.getInstance();

    @Override
    public void perform(Player sender, String[] args){

        if (!sender.hasPermission("rivevent.init")) {
            sender.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        if (!global.presetSummon.isEmpty()) {
            sender.sendMessage(ChatMessages.RED(Messages.ERR_EVENT) + " [ " + global.presetSummon + " ] ");
            return;
        }

        if (!global.riveventPreset.containsKey(args[1].toLowerCase())) {
            sender.sendMessage(ChatMessages.RED(Messages.NO_PRESET));

        } else {

            global.presetSummon = args[1].toLowerCase();
            sender.sendMessage(ChatMessages.GREEN(Messages.OK_INIT));

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendTitle("", Messages.NEW_EVENT_TITLE,20,200,20);
                p.sendMessage(ChatMessages.AQUA(Messages.BROADCAST_EVENT));
            }

        }

    }


}
