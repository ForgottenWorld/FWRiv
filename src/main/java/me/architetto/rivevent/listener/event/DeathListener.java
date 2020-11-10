package me.architetto.rivevent.listener.event;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.event.PlayersManager;
import me.architetto.rivevent.util.ChatFormatter;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class DeathListener implements Listener{

    EventService eventService = EventService.getInstance();
    PlayersManager playersManager = PlayersManager.getInstance();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {

        if (!eventService.isRunning())
            return;

        if (playersManager.getActivePlayers().contains(event.getEntity().getUniqueId())) {

            event.setCancelled(true);

            eventService.activePlayerDeath(event.getEntity().getUniqueId());

        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerResurection(EntityResurrectEvent event) {

        if (!eventService.isRunning())
            return;

        if (event.isCancelled())
            return;

        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        if (!playersManager.isPartecipants(player.getUniqueId()))
            return;

        new BukkitRunnable() {

            @Override
            public void run(){

                player.getWorld().createExplosion(eventService.getSummonedArena().getTower(),1,false,false);
                player.getWorld().spawnParticle(Particle.EXPLOSION_LARGE,player.getLocation(),1);
                player.teleport(eventService.getSummonedArena().getTower());
                event.getEntity().sendMessage(ChatFormatter.formatEventMessage("Il totem ti ha protetto da una morte certa!"));


            }
        }.runTaskLater(RIVevent.plugin,15);

    }

}
