package me.architetto.rivevent.command.user;

import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.util.ChatFormatter;
import me.architetto.rivevent.util.CommandName;
import me.architetto.rivevent.util.Messages;
import org.bukkit.entity.Player;

import java.util.List;

public class InfoCommand extends SubCommand {
    @Override
    public String getName(){
        return CommandName.INFO_COMMAND;
    }

    @Override
    public String getDescription(){
        return "info about RIVe gameplay";
    }

    @Override
    public String getSyntax(){
        return "/rivevent info";
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
