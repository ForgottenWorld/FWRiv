package me.architetto.fwriv.listener.event;

import me.architetto.fwriv.config.SettingsHandler;
import me.architetto.fwriv.event.PartecipantsManager;
import me.architetto.fwriv.event.service.EventService;
import me.architetto.fwriv.event.service.EventStatus;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageListener implements Listener{

    PartecipantsManager partecipantsManager = PartecipantsManager.getInstance();
    SettingsHandler settingsHandler = SettingsHandler.getSettingsHandler();

    @EventHandler
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event) {

        EventStatus eventStatus =  EventService.getInstance().getEventStatus();

        if (eventStatus.equals(EventStatus.INACTIVE))
            return;

        if (!(event.getDamager() instanceof Player)
                || !(event.getEntity() instanceof Player))
            return;

        PartecipantsManager.getInstance().getPartecipant(event.getDamager().getUniqueId()).ifPresent(partecipant -> {
            if (!eventStatus.equals(EventStatus.RUNNING)) {
                event.setCancelled(true);
                return;
            }

            Player damager = (Player) event.getDamager();
            Player damageTaker = (Player) event.getEntity();
            Material mainHandItem = damager.getInventory().getItemInMainHand().getType();

            if (mainHandItem == Material.TRIDENT) {
                event.setDamage(0.5);
                damageTaker.setVelocity(damager.getLocation()
                        .getDirection().normalize().multiply(settingsHandler.tridentKnockPower));
                damager.getInventory().setItemInMainHand(null);
            }

            partecipantsManager.getPartecipantStats(damager).ifPresent(stats -> stats.addDamageDealt(event.getFinalDamage()));
            partecipantsManager.getPartecipantStats(damageTaker).ifPresent(stats -> stats.addDamageTaken(event.getFinalDamage()));


        });

    }
}
