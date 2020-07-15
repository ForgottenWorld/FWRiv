package me.architetto.rivevent.command.subcommand.admin;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.command.GlobalVar;
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
        return "Deleta a preset.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent delete <nome_preset>";
    }

    @Override
    public void perform(Player player, String[] args) {

        if (!player.hasPermission("rivevent.delete")) {
            player.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        GlobalVar global = GlobalVar.getInstance();

        if (args.length != 2 ) {
            player.sendMessage(ChatMessages.RED(Messages.NO_PARAM));
        }else{

            if (global.riveventPreset.isEmpty()) {
                player.sendMessage(ChatMessages.RED(Messages.VOID_PRESET_LIST));
                return;
            }

            if (!global.riveventPreset.containsKey(args[1])) {
                player.sendMessage(ChatMessages.RED(Messages.NO_PRESET));
                return;
            }

            global.riveventPreset.remove(args[1]);

            try{
                RIVevent.save(global.riveventPreset);
            }catch(Exception e){
                e.printStackTrace();
            }

            player.sendMessage(ChatMessages.GREEN(Messages.SUCCESS_DELETE));
        }
    }
}
