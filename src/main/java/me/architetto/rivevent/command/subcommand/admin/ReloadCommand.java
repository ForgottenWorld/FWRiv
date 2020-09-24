package me.architetto.rivevent.command.subcommand.admin;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.command.SettingsHandler;
import me.architetto.rivevent.command.GameHandler;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.Messages;
import org.bukkit.entity.Player;

import java.util.List;

public class ReloadCommand extends SubCommand{
    @Override
    public String getName(){
        return "reload";
    }

    @Override
    public String getDescription(){
        return "Reload Rivevent settings.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent reload";
    }

    SettingsHandler settings = SettingsHandler.getInstance();
    GameHandler global = GameHandler.getInstance();

    @Override
    public void perform(Player sender, String[] args){

        if (!sender.hasPermission("rivevent.reload")) {
            sender.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        if (global.setupStartFlag){
            sender.sendMessage(ChatMessages.RED(Messages.ERR_SETUP_DONE));
            return;
        }

        RIVevent.plugin.reloadConfig();

        settings.load();

        global.loadStartLoadout();

        global.loadRewardItemList();

        sender.sendMessage(ChatMessages.GREEN(Messages.OK_RELOAD));

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }
}
