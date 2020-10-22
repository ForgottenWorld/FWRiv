package me.architetto.rivevent.listener.event;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.event.EventService;
import org.bukkit.GameMode;
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


    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {

        if (!eventService.isRunning())
            return;

        if (eventService.getParticipantsPlayers().contains(event.getEntity().getUniqueId())) {

            event.setCancelled(true);
            event.getEntity().getInventory().clear();
            event.getEntity().setGameMode(GameMode.SPECTATOR);

            eventService.removePartecipant(event.getEntity().getUniqueId());

        }

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTotemEvent(EntityResurrectEvent event) {

        if (event.isCancelled())
            return;

        if (!eventService.isRunning())
            return;

        if (eventService.getParticipantsPlayers().contains(event.getEntity().getUniqueId())) {

            new BukkitRunnable() {

                @Override
                public void run(){


                    Player player = (Player) event.getEntity();
                    player.getWorld().createExplosion(eventService.getSummonedArena().getTower(),2,false,false);
                    player.getWorld().spawnParticle(Particle.EXPLOSION_LARGE,player.getLocation(),2);
                    player.teleport(eventService.getSummonedArena().getTower());

                }
            }.runTaskLater(RIVevent.plugin,5);

        }

    }
}
