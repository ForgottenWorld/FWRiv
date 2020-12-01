package me.architetto.fwriv.command.user;

import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.utils.ChatFormatter;
import me.architetto.fwriv.utils.CommandDescription;
import me.architetto.fwriv.utils.CommandName;
import me.architetto.fwriv.utils.Messages;
import org.bukkit.entity.Player;

import java.util.List;

public class InfoCommand extends SubCommand {
    @Override
    public String getName(){
        return CommandName.INFO_COMMAND;
    }

    @Override
    public String getDescription(){
        return CommandDescription.INFO_COMMAND;
    }

    @Override
    public String getSyntax(){
        return "/fwriv info";
    }

    @Override
    public void perform(Player sender, String[] args) {
        if (!sender.hasPermission("rivevent.user")) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_PERMISSION));
            return;
        }

        sender.sendMessage(ChatFormatter.chatHeaderGameplayInfo());
        sender.sendMessage(Messages.RIV_GAMEPLAY_INFO);
        sender.sendMessage(ChatFormatter.chatFooter());

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }
}
