package me.architetto.fwriv.command.user;

import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.utils.ChatFormatter;
import me.architetto.fwriv.command.CommandName;
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
        return "placeholder";
    }

    @Override
    public String getSyntax(){
        return "/fwriv info";
    }

    @Override
    public String getPermission() {
        return "rivevent.user";
    }

    @Override
    public int getArgsRequired() {
        return 0;
    }

    @Override
    public void perform(Player sender, String[] args) {

        sender.sendMessage(ChatFormatter.chatHeaderGameplayInfo());
        sender.sendMessage(Messages.RIV_GAMEPLAY_INFO);
        sender.sendMessage(ChatFormatter.chatFooter());

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }
}
