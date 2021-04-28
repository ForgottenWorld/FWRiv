package me.architetto.fwriv.command.user;

import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.config.SettingsHandler;
import me.architetto.fwriv.echelon.EchelonHolder;
import me.architetto.fwriv.event.service.RewardSystemService;
import me.architetto.fwriv.localization.Message;
import me.architetto.fwriv.partecipant.PartecipantsManager;
import me.architetto.fwriv.event.EventService;
import me.architetto.fwriv.event.EventStatus;
import me.architetto.fwriv.command.CommandName;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

public class JoinCommand extends SubCommand {

    @Override
    public String getName(){
        return CommandName.JOIN_COMMAND;
    }

    @Override
    public String getDescription(){
        return Message.JOIN_COMMAND.asString();
    }

    @Override
    public String getSyntax(){
        return "/fwriv " + CommandName.JOIN_COMMAND;
    }

    @Override
    public String getPermission() {
        return "fwriv.join";
    }

    @Override
    public int getArgsRequired() {
        return 0;
    }

    @Override
    public void perform(Player sender, String[] args) {

        EventService eventService = EventService.getInstance();
        EventStatus eventStatus = eventService.getEventStatus();

        if (eventStatus.equals(EventStatus.INACTIVE)) {
            Message.ERR_NO_EVENT_IS_RUNNING.send(sender);
            return;
        }

        PartecipantsManager partecipantsManager = PartecipantsManager.getInstance();

        if (partecipantsManager.isPresent(sender)) {
            Message.ERR_ALREADY_JOINED.send(sender);
            return;
        }

        SettingsHandler settingsHandler = SettingsHandler.getInstance();

        if (settingsHandler.isEchelonEnabled()) {
            if (EchelonHolder.getEchelonHolder().isPlayerInMutexActivity(sender)) {
                Message.ERR_ECHELON_MUTEXACTIVITY
                        .send(sender,EchelonHolder.getEchelonHolder().getPlayerMutexActivityName(sender));
                return;
            } else
                EchelonHolder.getEchelonHolder().addPlayerMutexActivity(sender);
        }

        eventService.partecipantJoin(sender);

        RewardSystemService.getInstance().addPlayerToRewardBar(sender);

        sender.playSound(sender.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);

        Message.BROADCAST_PLAYERJOINEVENT.broadcast("fwriv.echo",sender.getDisplayName());

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }

}
