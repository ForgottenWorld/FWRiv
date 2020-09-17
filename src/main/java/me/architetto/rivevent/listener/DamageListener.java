package me.architetto.rivevent.listener;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.command.GameHandler;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.Messages;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class DamageListener implements Listener{

    GameHandler global = GameHandler.getInstance();
    public boolean curseCooldown = false;

    @EventHandler
    public void disableDamage(EntityDamageByEntityEvent event){

        if (event.getDamager() instanceof Player){

            Player damager = (Player) event.getDamager();

            if (!global.startDoneFlag && global.playerJoined.contains(damager.getUniqueId())){
                event.setCancelled(true);
            }

            if (global.playerSpectate.contains(damager.getUniqueId())){
                event.setCancelled(true);
            }

            if (global.curseEventFlag && global.playerJoined.contains(damager.getUniqueId())
                    && event.getEntity() instanceof Player) {

                Player damageTaker = ((Player) event.getEntity()).getPlayer();

                if (global.cursedPlayer == damager){
                    assert damageTaker != null;
                    if (global.playerJoined.contains(damageTaker.getUniqueId()) && !curseCooldown){

                        transferCurseCooldown();

                        global.cursedPlayer = damageTaker;
                        damageTaker.playSound(damageTaker.getLocation(), Sound.ENTITY_GHAST_HURT,4,1);
                        damageTaker.sendMessage(ChatMessages.GOLD(Messages.CURSE_TRANSFER_EVENT));
                        damager.sendMessage("Hai passato la maledizione a " + damageTaker.getDisplayName() + ". Stagli lontano!");
                    }
                }

            }
        }
    }

    public void transferCurseCooldown() {

        curseCooldown = true;

        new BukkitRunnable() {

            @Override
            public void run(){
                curseCooldown = false;
            }
        }.runTaskLater(RIVevent.plugin,60);

    }
}
