package me.architetto.rivevent.command.superuser;

import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.config.ConfigManager;
import me.architetto.rivevent.config.SettingsHandler;
import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.util.ChatFormatter;
import me.architetto.rivevent.util.Messages;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetCommand extends SubCommand {
    @Override
    public String getName(){
        return "set";
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

        if (!sender.hasPermission("rivevent.eventmanager")) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_PERMISSION));
            return;
        }

        if (EventService.getInstance().isStarted()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_EVENT_RUNNING));
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_ARENA_CMD_SYNTAX));
            return;
        }

        switch(args[1].toLowerCase()) {
            case "respawn":
                SettingsHandler.getInstance().respawnLocation = sender.getLocation();

                FileConfiguration fileConfiguration = ConfigManager.getInstance().getConfig("Settings.yml");
                ConfigManager.getInstance().addLocation(fileConfiguration,sender.getLocation(),"RESPAWN_POINT");

                sender.sendMessage(ChatFormatter.formatSuccessMessage("respawn location saved"));
                return;
            case "acgrow":
                if (args.length < 3)
                    return;
                SettingsHandler.getInstance().antiCamperGrowValue = Integer.parseInt(args[2]);
                ConfigManager.getInstance().setData(ConfigManager.getInstance().getConfig("Settings.yml"),
                        "ANTI_CAMPER_GROW_VALUE",Integer.parseInt(args[2]));

                sender.sendMessage(ChatFormatter.formatSuccessMessage("anti camper grow value changed (new value: "
                        + ChatColor.YELLOW + args[2] + ChatColor.RESET + " )"));
                return;
            case "acgrowperiod":
                if (args.length < 3)
                    return;
                SettingsHandler.getInstance().antiCamperGrowPeriod = Integer.parseInt(args[2]) * 20 ;
                ConfigManager.getInstance().setData(ConfigManager.getInstance().getConfig("Settings.yml"),
                        "ANTI_CAMPER_GROW_PERIOD",Integer.parseInt(args[2]));

                sender.sendMessage(ChatFormatter.formatSuccessMessage("anti camper grow period changed (new value: "
                        + ChatColor.YELLOW + args[2] + ChatColor.RESET + " )"));
                return;
            case "rewardperiod":
                if (args.length < 3)
                    return;
                SettingsHandler.getInstance().rewardPeriod = Integer.parseInt(args[2]) * 20 ;
                ConfigManager.getInstance().setData(ConfigManager.getInstance().getConfig("Settings.yml"),
                        "REWARD_PERIOD",Integer.parseInt(args[2]));

                sender.sendMessage(ChatFormatter.formatSuccessMessage("reward period changed (new value: "
                        + ChatColor.YELLOW + args[2] + ChatColor.RESET + " )"));
                return;
            case "redlineradius":
                if (args.length < 3)
                    return;
                SettingsHandler.getInstance().redLineAnimationRadius = Integer.parseInt(args[2]);
                ConfigManager.getInstance().setData(ConfigManager.getInstance().getConfig("Settings.yml"),
                        "RED_LINE_ANIMATION_RADIUS",Integer.parseInt(args[2]));

                sender.sendMessage(ChatFormatter.formatSuccessMessage("red line radius changed (new value: "
                        + ChatColor.YELLOW + args[2] + ChatColor.RESET + " )"));
                return;


            default:
                return;

        }

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        if (args.length == 2){

            return new ArrayList<>(
                    Arrays.asList("Respawn",
                            "ACGrow",
                            "ACGrowPeriod",
                            "RewardPeriod"
                            ));

        }
        return null;
    }
}
