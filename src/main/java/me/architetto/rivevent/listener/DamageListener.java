package me.architetto.rivevent.listener;

import me.architetto.rivevent.command.GameHandler;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageListener implements Listener{

    GameHandler global = GameHandler.getInstance();

    @EventHandler
    public void disableDamage(EntityDamageByEntityEvent event){

        if (event.getDamager() instanceof Player){

            Player damager = (Player) event.getDamager();

            if (!global.startDoneFlag && global.playerJoined.contains(damager.getUniqueId())){
                event.setCancelled(true);
            }

            if (global.playerSpectate.contains(damager.getUniqueId())){ //TODO: probabile che debba essere preso in considerazione il caso di snowball showdown (quando lo implementero')
                event.setCancelled(true);
            }

            if (global.curseEventFlag && global.playerJoined.contains(damager.getUniqueId())
                    && event.getEntity() instanceof Player) {

                Player damageTaker = ((Player) event.getEntity()).getPlayer();

                if (global.cursedPlayer == damager){ //Todo: inserire un cooldown per trasferire la maledizione ?
                    assert damageTaker != null;
                    if (global.playerJoined.contains(damageTaker.getUniqueId())){
                        global.cursedPlayer = damageTaker;
                        damageTaker.playSound(damageTaker.getLocation(), Sound.ENTITY_GHAST_HURT,4,1);
                        damageTaker.sendMessage("Ti hanno passato la maledizione! Colpisci qualcuno per sbarazzartene ...");
                        damager.sendMessage("Hai passato la maledizione a " + damageTaker.getDisplayName() + ". Stagli lontano!");
                    }
                }

            }

        }
    }

}
