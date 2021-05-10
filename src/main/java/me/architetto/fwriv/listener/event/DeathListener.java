package me.architetto.fwriv.listener.event;

import me.architetto.fwriv.FWRiv;
import me.architetto.fwriv.localization.Message;
import me.architetto.fwriv.partecipant.PartecipantStats;
import me.architetto.fwriv.partecipant.PartecipantStatus;
import me.architetto.fwriv.partecipant.PartecipantsManager;
import me.architetto.fwriv.event.EventService;
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

        PartecipantsManager pm = PartecipantsManager.getInstance();
        pm.getPartecipant(event.getEntity()).ifPresent(partecipant -> {
            if (!partecipant.getPartecipantStatus().equals(PartecipantStatus.PLAYING))
                return;
            event.setCancelled(true);
            EventService.getInstance().partecipantDeath(event.getEntity());
            Player killer = event.getEntity().getKiller();
            if (killer != null) {
                pm.getPartecipantStats(killer).ifPresent(PartecipantStats::addKill);
                Message.PLAYER_DEATH1.sendToPartecipants(partecipant.getName(),
                        killer.getDisplayName(),
                        pm.getPartecipantsUUID(PartecipantStatus.PLAYING).size());
            } else
                Message.PLAYER_DEATH2.sendToPartecipants(partecipant.getName(),
                        pm.getPartecipantsUUID(PartecipantStatus.PLAYING).size());
        });

    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerResurection(EntityResurrectEvent event) {

        if (!(event.getEntity() instanceof Player)) return;

        PartecipantsManager pm = PartecipantsManager.getInstance();
        pm.getPartecipant(event.getEntity().getUniqueId()).ifPresent(partecipant -> {
            if (!partecipant.getPartecipantStatus().equals(PartecipantStatus.PLAYING))
                return;
            EventService es = EventService.getInstance();
            Player player = (Player) event.getEntity();
            Bukkit.getScheduler().scheduleSyncDelayedTask(FWRiv.getPlugin(FWRiv.class),() -> {
                player.teleport(es.getArena().getTower());
                player.getWorld().playSound(es.getArena().getTower(), Sound.ENTITY_GENERIC_EXPLODE,5,1);
                player.getWorld().spawnParticle(Particle.EXPLOSION_LARGE,player.getLocation(),1);
            },5);
        });
    }

}
