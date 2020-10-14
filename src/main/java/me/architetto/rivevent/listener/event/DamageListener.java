package me.architetto.rivevent.listener.event;

import me.architetto.rivevent.event.EventService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageListener implements Listener{

    EventService eventService = EventService.getInstance();

    @EventHandler
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event){

        if (!eventService.isRunning())
            return;

        if (event.getDamager() instanceof Player){

            Player damager = (Player) event.getDamager();

            if (!eventService.getParticipantsPlayers().contains(damager.getUniqueId()))
                return;

            if (!eventService.isStarted()){
                event.setCancelled(true);

            }
        }
    }


    /*
            if (global.curseEventFlag && global.playerJoined.contains(damager.getUniqueId())
                    && event.getEntity() instanceof Player) {

                Player damageTaker = ((Player) event.getEntity()).getPlayer();

                if (damageTaker == null)
                    return;

                if (global.cursedPlayer == damager) {

                    if (global.playerJoined.contains(damageTaker.getUniqueId()) && !curseCooldown){

                        transferCurseCooldown();

                        global.cursedPlayer = damageTaker;
                        damageTaker.playSound(damageTaker.getLocation(), Sound.ENTITY_GHAST_HURT,4,1);
                        damageTaker.sendMessage(ChatMessages.GOLD(Messages.CURSE_TRANSFER_EVENT));

                        damager.sendMessage(ChatMessages.CurseMessage(Messages.CURSE_TRANSFER_MSG1,damageTaker.getDisplayName(),Messages.CURSE_TRANSFER_MSG2));
                    }
                }
            }
        }
    }

     */

    /*

    public void transferCurseCooldown() {

        curseCooldown = true;

        new BukkitRunnable() {

            @Override
            public void run(){
                curseCooldown = false;
            }
        }.runTaskLater(RIVevent.plugin,60);

    }

     */
}
