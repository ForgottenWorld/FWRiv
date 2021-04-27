package me.architetto.fwriv.echelon;

import it.forgottenworld.echelonapi.mutexactivity.MutexActivity;
import me.architetto.fwriv.event.EventService;
import org.bukkit.entity.Player;

public class MyMutexActivity implements MutexActivity {

    @Override
    public void onAllPlayersForceRemoved(String s) {
        EventService.getInstance().stopEvent();
    }

    @Override
    public void onPlayerForceRemoved(Player player, String s) {
        EventService.getInstance().partecipantLeave(player);
    }

    @Override
    public String getId() {
        return "FWRiv";
    }
}
