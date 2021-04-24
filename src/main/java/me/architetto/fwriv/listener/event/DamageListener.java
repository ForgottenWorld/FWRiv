package me.architetto.fwriv.listener.event;

import me.architetto.fwriv.event.EventService;
import me.architetto.fwriv.event.EventStatus;
import me.architetto.fwriv.partecipant.PartecipantsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageListener implements Listener{

    @EventHandler
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event) {

        EventService eventService = EventService.getInstance();
        EventStatus eventStatus =  eventService.getEventStatus();

        if (eventStatus.equals(EventStatus.INACTIVE)) return;

        if (!(event.getDamager() instanceof Player)
                || !(event.getEntity() instanceof Player)) return;

        PartecipantsManager partecipantsManager = PartecipantsManager.getInstance();

        partecipantsManager.getPartecipant(event.getDamager().getUniqueId()).ifPresent(partecipant -> {
            if (!eventStatus.equals(EventStatus.RUNNING)) {
                event.setCancelled(true);
                return;
            }

            Player damager = (Player) event.getDamager();
            Player damageTaker = (Player) event.getEntity();

            partecipantsManager.getPartecipantStats(damager)
                    .ifPresent(stats -> stats.addDamageDealt(event.getFinalDamage()));
            partecipantsManager.getPartecipantStats(damageTaker)
                    .ifPresent(stats -> stats.addDamageTaken(event.getFinalDamage()));

        });

    }
}
