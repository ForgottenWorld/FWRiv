package me.architetto.rivevent.command.subcommand;

import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.Messages;
import org.bukkit.entity.Player;

public class DeleteCommand extends SubCommand{
    @Override
    public String getName(){
        return "delete";
    }

    @Override
    public String getDescription(){
        return "Delete a preset.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent delete <preset_name>";
    }

    @Override
    public void perform(Player player, String[] args){

        if (!player.hasPermission("rivevent.delete")){
            player.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        if (args.length != 2){
            player.sendMessage(ChatMessages.RED(Messages.NO_PARAM));
            return;
        }

        if (!CreateCommand.riveventPreset.containsKey(args[1])){
            player.sendMessage(ChatMessages.RED(Messages.NO_PRESET));
            return;
        }

        CreateCommand.riveventPreset.remove(args[1]);
        player.sendMessage(ChatMessages.GREEN(Messages.OK_DELETE));
        //ToDo. salva l'HashMap modificata

    }
}
