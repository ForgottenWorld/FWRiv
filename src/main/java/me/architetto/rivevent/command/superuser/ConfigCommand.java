package me.architetto.rivevent.command.superuser;

import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.config.ConfigManager;
import me.architetto.rivevent.config.SettingsHandler;
import me.architetto.rivevent.util.ChatFormatter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class ConfigCommand extends SubCommand {
    @Override
    public String getName(){
        return "config";
    }

    @Override
    public String getDescription(){
        return null;
    }

    @Override
    public String getSyntax(){
        return null;
    }

    @Override
    public void perform(Player sender, String[] args) {

        if (!sender.hasPermission("rivevent.config")) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Permission Error: you do not have permission to use that command"));
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Syntax Error: insert preset name"));
            return;
        }

        switch(args[1].toLowerCase()) {
            case "setrespawn":
                SettingsHandler.getInstance().respawnLocation = sender.getLocation();
                FileConfiguration fileConfiguration = ConfigManager.getInstance().getConfig("Settings.yml");

                ConfigManager.getInstance().addLocation(fileConfiguration,sender.getLocation(),"RESPAWN_POINT");
                sender.sendMessage(ChatFormatter.formatSuccessMessage("respawn location saved"));
                return;
            default:
                return;

        }

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }
}
