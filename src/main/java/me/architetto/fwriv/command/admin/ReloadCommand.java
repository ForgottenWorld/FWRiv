package me.architetto.fwriv.command.admin;

import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.config.ConfigManager;
import me.architetto.fwriv.config.SettingsHandler;
import me.architetto.fwriv.event.EventService;
import me.architetto.fwriv.event.EventStatus;
import me.architetto.fwriv.localization.LocalizationManager;
import me.architetto.fwriv.localization.Message;
import me.architetto.fwriv.reward.RewardService;
import me.architetto.fwriv.command.CommandName;
import org.bukkit.entity.Player;

import java.util.List;

public class ReloadCommand extends SubCommand {
    @Override
    public String getName(){
        return CommandName.RELOAD_COMMAND;
    }

    @Override
    public String getDescription(){
        return Message.RELOAD_COMMAND.asString();
    }

    @Override
    public String getSyntax(){
        return "/fwriv " + CommandName.RELOAD_COMMAND;
    }

    @Override
    public String getPermission() {
        return "fwriv.reload";
    }

    @Override
    public int getArgsRequired() {
        return 0;
    }

    @Override
    public void perform(Player sender, String[] args){

        EventService eventService = EventService.getInstance();

        if (!eventService.getEventStatus().equals(EventStatus.INACTIVE)) {
            Message.ERR_EVENT_IS_RUNNING.send(sender);
            return;
        }

        ConfigManager.getInstance().reloadConfigs();
        SettingsHandler.getInstance().reload();
        LocalizationManager.getInstance().reload();
        RewardService.getInstance().reloadRewards();

        Message.SUCCESS_CONFIG_RELOAD.send(sender);

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }
}
