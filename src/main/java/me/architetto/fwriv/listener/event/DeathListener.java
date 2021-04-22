package me.architetto.fwriv.listener.event;

import me.architetto.fwriv.partecipant.PartecipantsManager;
import me.architetto.fwriv.event.EventService;
import me.architetto.fwriv.event.EventStatus;
import me.architetto.fwriv.utils.ChatFormatter;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener{

    EventService eventService = EventService.getInstance();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {

        if (EventService.getInstance().getEventStatus().equals(EventStatus.INACTIVE))
            return;

        PartecipantsManager.getInstance().getPartecipant(event.getEntity()).ifPresent(partecipant -> {
            event.setCancelled(true);
            eventService.partecipantDeath(event.getEntity());
        });

    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerResurection(EntityResurrectEvent event) {

        if (!(event.getEntity() instanceof Player))
            return;

        if (EventService.getInstance().getEventStatus().equals(EventStatus.INACTIVE))
            return;

        //todo: combatdelux non blocca il totem vero ? ma di sicuro blocca il tp quindi deve essere disattivato nel mondo eventi

        PartecipantsManager.getInstance().getPartecipant(event.getEntity().getUniqueId()).ifPresent(partecipant -> {
            Player player = (Player) event.getEntity();
            player.teleport(eventService.getArena().getTower());
            player.getWorld().createExplosion(eventService.getArena().getTower(),1,false,false);
            player.getWorld().spawnParticle(Particle.EXPLOSION_LARGE,player.getLocation(),1);
            player.sendMessage(ChatFormatter.formatEventMessage("Il totem ti ha protetto da una morte certa!"));


        });

    }

}
