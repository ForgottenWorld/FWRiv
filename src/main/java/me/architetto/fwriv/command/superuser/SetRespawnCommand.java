package me.architetto.fwriv.command.superuser;

import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.config.ConfigManager;
import me.architetto.fwriv.config.SettingsHandler;
import me.architetto.fwriv.event.service.EventService;
import me.architetto.fwriv.utils.ChatFormatter;
import me.architetto.fwriv.utils.CommandDescription;
import me.architetto.fwriv.utils.CommandName;
import me.architetto.fwriv.utils.Messages;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class SetRespawnCommand extends SubCommand {
    @Override
    public String getName(){
        return CommandName.SETRESPAWN_COMMAND;
    }

    @Override
    public String getDescription(){
        return CommandDescription.SETRESPAWN_COMMAND;
    }

    @Override
    public String getSyntax(){
        return "/fwriv setrespawn";
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

        SettingsHandler.getSettingsHandler().safeRespawnLocation = sender.getLocation();

        FileConfiguration fileConfiguration = ConfigManager.getInstance().getConfig("RespawnPoint.yml");

        ConfigManager.getInstance().addLocation(fileConfiguration,sender.getLocation(),"RESPAWN_POINT");

        sender.sendMessage(ChatFormatter.formatSuccessMessage("respawn location saved"));

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }

}
