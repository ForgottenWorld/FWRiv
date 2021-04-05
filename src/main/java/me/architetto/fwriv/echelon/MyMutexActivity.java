package me.architetto.fwriv.echelon;

import it.forgottenworld.echelonapi.mutexactivity.MutexActivity;
import me.architetto.fwriv.event.service.EventService;
import org.bukkit.entity.Player;

public class MyMutexActivity implements MutexActivity {
    @Override
    public void onAllPlayersForceRemoved(String s) {
    }

    @Override
    public void onPlayerForceRemoved(Player player, String s) {
        EventService.getInstance().activePlayerLeave(player.getUniqueId());
    }

    @Override
    public String getId() {
        return "FWRiv";
    }
}
