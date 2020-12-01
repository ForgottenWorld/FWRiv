package me.architetto.fwriv.listener.event;

import me.architetto.fwriv.config.SettingsHandler;
import me.architetto.fwriv.event.PlayersManager;
import me.architetto.fwriv.event.service.EventService;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

public class DamageListener implements Listener{

    EventService eventService = EventService.getInstance();
    PlayersManager playersManager = PlayersManager.getInstance();
    SettingsHandler settingsHandler = SettingsHandler.getSettingsHandler();

    @EventHandler
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event){

        if (!eventService.isRunning())
            return;

        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player))
            return;

        Player damager = (Player) event.getDamager();
        Player damageTaker = (Player) event.getEntity();

        if (!playersManager.isPlayerActive(damager.getUniqueId())) {
            return;
        }

        if (!playersManager.isPlayerActive(damageTaker.getUniqueId())) {
            return;
        }

        if (!eventService.isDamageEnabled()) {
            event.setCancelled(true);
            return;
        }

        if (eventService.isEarlyDamagePrank()) {
            event.setCancelled(true);
            damager.damage(0.2);
            damager.sendActionBar(ChatColor.BOLD + "PROTEZIONE INIZIALE " + ChatColor.DARK_RED + "" + ChatColor.BOLD + "ATTIVATA");
        }

        if (damager.getInventory().getItemInMainHand().getType() == Material.TRIDENT) {

            Vector knockbackVector = damager.getLocation().getDirection();
            knockbackVector.multiply(settingsHandler.tridentKnockPower);

            event.setDamage(0.5);
            
            damageTaker.setVelocity(knockbackVector);

            damager.getInventory().setItemInMainHand(null);
        }

    }
}
