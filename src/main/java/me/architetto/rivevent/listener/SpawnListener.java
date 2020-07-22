package me.architetto.rivevent.listener;

import me.architetto.rivevent.command.GlobalVar;
import me.architetto.rivevent.util.LocSerialization;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class SpawnListener implements Listener{

    @EventHandler
    public void onPlayerSpawn(PlayerSpawnLocationEvent event){

        GlobalVar global = GlobalVar.getInstance();

        if (global.playerSpectate.contains(event.getPlayer().getUniqueId())) {

            Player player = event.getPlayer();

            player.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LClickListener.LOC.SPECTATE)));
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,5,1);

        }

        //TODO: PER TIPPARE I MORTI IN SPECTATE (ED EVENTUALMENTE RIPULIRE L?INVENTARIO DEI PLAYER CHE QUITTANO)

    }

}
