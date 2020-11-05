package me.architetto.rivevent.listener.event;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.event.MinigameService;
import me.architetto.rivevent.event.PlayersManager;
import me.architetto.rivevent.util.ChatFormatter;
import me.architetto.rivevent.util.Messages;
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

        if (PlayersManager.getInstance().getActivePlayers().contains(event.getEntity().getUniqueId())) {

            event.setCancelled(true);

            eventService.activePlayerDeath(event.getEntity().getUniqueId());

        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerResurection(EntityResurrectEvent event) {

        if (!eventService.isRunning())
            return;

        if (!(event.getEntity() instanceof Player))
            return;

        if (event.isCancelled())
            return;

        if (MinigameService.getInstance().isCurseEventRunning()) {
            if (MinigameService.getInstance().getCursedPlayer().equals(event.getEntity())) {
                event.setCancelled(true);
                event.getEntity().sendMessage(ChatFormatter.formatEventMessage(Messages.CURSED_PLAYER_DIE_WITH_TOTEM));
                return;
            }
        }

        if (PlayersManager.getInstance().getActivePlayers().contains(event.getEntity().getUniqueId())) {

            new BukkitRunnable() {

                @Override
                public void run(){


                    Player player = (Player) event.getEntity();
                    player.getWorld().createExplosion(eventService.getSummonedArena().getTower(),2,false,false);
                    player.getWorld().spawnParticle(Particle.EXPLOSION_LARGE,player.getLocation(),2);
                    player.teleport(eventService.getSummonedArena().getTower());

                }
            }.runTaskLater(RIVevent.plugin,10);

        }

    }

}
