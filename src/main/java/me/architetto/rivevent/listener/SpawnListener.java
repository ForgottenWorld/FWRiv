package me.architetto.rivevent.listener;

import me.architetto.rivevent.command.GameHandler;
import me.architetto.rivevent.util.LocSerialization;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class SpawnListener implements Listener{

    @EventHandler
    public void onPlayerSpawn(PlayerRespawnEvent event){

        GameHandler global = GameHandler.getInstance();

        if (global.playerSpectate.contains(event.getPlayer().getUniqueId())) {

            event.setRespawnLocation(LocSerialization.getDeserializedLocation(global.riveventPreset
                    .get(global.presetSummon).get(LeftclickListener.LOC.SPECTATE)));

        }
    }
}
