package me.architetto.fwriv.command.user;

import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.command.CommandName;
import me.architetto.fwriv.event.EventService;
import me.architetto.fwriv.event.EventStatus;
import me.architetto.fwriv.localization.LocalizationManager;
import me.architetto.fwriv.localization.Message;
import me.architetto.fwriv.partecipant.PartecipantStatus;
import me.architetto.fwriv.partecipant.PartecipantsManager;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class GameCommand extends SubCommand {
    @Override
    public String getName(){
        return CommandName.GAME_COMMAND;
    }

    @Override
    public String getDescription(){
        return "placeholder";
    }

    @Override
    public String getSyntax(){
        return "/fwriv " + CommandName.GAME_COMMAND;
    }

    @Override
    public String getPermission() {
        return "fwriv.game";
    }

    @Override
    public int getArgsRequired() {
        return 2;
    }

    private static final List<String> params = Arrays.asList(
            "rules",
            "partecipants"
    );

    @Override
    public void perform(Player sender, String[] args) {
        EventStatus eventStatus = EventService.getInstance().getEventStatus();

        switch (args[1].toLowerCase()) {
            case "rules":
                sender.openBook(LocalizationManager.getInstance().getInfoBook());
                break;
            case "partecipants":
                if (eventStatus.equals(EventStatus.INACTIVE)) {
                    Message.ERR_NO_EVENT_IS_RUNNING.send(sender);
                    return;
                }
                PartecipantsManager partecipantsManager = PartecipantsManager.getInstance();
                int total = partecipantsManager.getPartecipantsUUID(PartecipantStatus.ALL).size();
                int playing = partecipantsManager.getPartecipantsUUID(PartecipantStatus.PLAYING).size();
                Message.PARTECIPANT_INFO.send(sender,total,playing,total - playing);
        }


    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){

        if (args.length == 2)
            return params;

        return null;
    }
}
