package me.architetto.rivevent.listener;

import me.architetto.rivevent.command.GlobalVar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageListener implements Listener{

    @EventHandler
    public void disableDamage(EntityDamageByEntityEvent event){

        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getDamager();

        GlobalVar global = GlobalVar.getInstance();

        if (!global.startDone && global.playerJoined.contains(player.getUniqueId())) {
            event.setCancelled(true);
        }

        if (global.playerSpectate.contains(player.getUniqueId())) {
            event.setCancelled(true);
        }


    }

}
