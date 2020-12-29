package me.architetto.fwriv.listener;

import it.forgottenworld.echelonapi.mutexactivity.MutexActivityListener;
import me.architetto.fwriv.event.service.EventService;
import org.bukkit.entity.Player;

public class MyMutexActivity implements MutexActivityListener {
    @Override
    public void onAllPlayersForceRemoved(String s) {
        EventService.getInstance().stopEvent();
    }

    @Override
    public void onPlayerForceRemoved(Player player, String s) {
        EventService.getInstance().activePlayerLeave(player.getUniqueId());
    }
}
