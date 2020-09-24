package me.architetto.rivevent.command.subcommand.admin;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.command.GameHandler;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.Messages;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DeleteCommand extends SubCommand{
    @Override
    public String getName(){
        return "delete";
    }

    @Override
    public String getDescription(){
        return "Deleta a preset.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent delete <nome_preset>";
    }

    GameHandler global = GameHandler.getInstance();

    @Override
    public void perform(Player sender, String[] args) {

        if (!sender.hasPermission("rivevent.delete")) {
            sender.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        if (global.riveventPreset.isEmpty()) {
            sender.sendMessage(ChatMessages.RED(Messages.VOID_PRESET_LIST));
            return;
        }

        if (!global.presetSummon.isEmpty()) {
            sender.sendMessage(ChatMessages.RED(Messages.ERR_DELETE));
            return;
        }

        if (!global.riveventPreset.containsKey(args[1].toLowerCase())) {
            sender.sendMessage(ChatMessages.RED(Messages.NO_PRESET));
            return;
        }

        global.riveventPreset.remove(args[1].toLowerCase());

        try{
            RIVevent.save(global.riveventPreset);
        }catch(Exception e){
            e.printStackTrace();
        }

        sender.sendMessage(ChatMessages.GREEN(Messages.SUCCESS_DELETE));

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){

        if (args.length == 2){

            return new ArrayList<>(global.riveventPreset.keySet());

        }

        return null;
    }
}
