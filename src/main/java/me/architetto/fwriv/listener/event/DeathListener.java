package me.architetto.fwriv.listener.event;

import me.architetto.fwriv.FWRiv;
import me.architetto.fwriv.partecipant.PartecipantStats;
import me.architetto.fwriv.partecipant.PartecipantsManager;
import me.architetto.fwriv.event.EventService;
import me.architetto.fwriv.event.EventStatus;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener{


    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {

        EventService eventService = EventService.getInstance();

        if (eventService.getEventStatus().equals(EventStatus.INACTIVE)) return;

        PartecipantsManager partecipantsManager = PartecipantsManager.getInstance();

        partecipantsManager.getPartecipant(event.getEntity()).ifPresent(partecipant -> {
            event.setCancelled(true);
            eventService.partecipantDeath(event.getEntity());
            Player killer = event.getEntity().getKiller();
            if (killer != null)
                partecipantsManager.getPartecipantStats(killer).ifPresent(PartecipantStats::addKill);
        });

    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerResurection(EntityResurrectEvent event) {

        EventService eventService = EventService.getInstance();

        if (!(event.getEntity() instanceof Player)) return;

        if (eventService.getEventStatus().equals(EventStatus.INACTIVE)) return;

        PartecipantsManager partecipantsManager = PartecipantsManager.getInstance();

        //Eventuali plugin che bloccano i teleport come DeluxeCombat devono essere disabilitati nel
        // mondo in cui si svolge l'evento.

        partecipantsManager.getPartecipant(event.getEntity().getUniqueId()).ifPresent(partecipant -> {

            Player player = (Player) event.getEntity();

            Bukkit.getScheduler().scheduleSyncDelayedTask(FWRiv.getPlugin(FWRiv.class),() -> {
                player.teleport(eventService.getArena().getTower());
                player.getWorld().playSound(eventService.getArena().getTower(), Sound.ENTITY_GENERIC_EXPLODE,5,1);
                player.getWorld().spawnParticle(Particle.EXPLOSION_LARGE,player.getLocation(),1);
            },5);

        });

    }

}
