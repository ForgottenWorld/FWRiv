package me.architetto.rivevent.listener.event;

import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.event.MinigameService;
import me.architetto.rivevent.util.ChatFormatter;
import me.architetto.rivevent.util.Messages;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

public class DamageListener implements Listener{

    EventService eventService = EventService.getInstance();

    @EventHandler
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event){

        if (!eventService.isRunning())
            return;

        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player))
            return;

        Player damager = (Player) event.getDamager();
        Player damageTaker = (Player) event.getEntity();

        if (!eventService.getPlayerIN().contains(damager.getUniqueId()))
            return;

        if (!eventService.isDamageEnabled()) {
            event.setCancelled(true);
            return;
        }

        if (MinigameService.getInstance().isCurseEventRunning()
                && eventService.getPlayerIN().contains(damageTaker.getUniqueId())) {

            if (MinigameService.getInstance().getCursedPlayer() == damager) {

                MinigameService.getInstance().setCursedPlayer(damageTaker);
                damageTaker.playSound(damageTaker.getLocation(), Sound.ENTITY_GHAST_HURT,4,1);
                damageTaker.spawnParticle(Particle.MOB_APPEARANCE,damageTaker.getLocation(),1,0,0,0);

                damageTaker.sendMessage(ChatFormatter.formatEventMessage(Messages.CURSE_MSG1));
                damager.sendMessage(ChatFormatter.formatEventMessage(Messages.CURSE_MSG2));

            }

        }

        // --- TRIDENT CODE --- //

        if (damager.getInventory().getItemInMainHand().getType() == Material.TRIDENT) {

            Vector knockbackVector = damager.getLocation().toVector().subtract(damageTaker.getLocation().toVector()).multiply(-1); //new code, test it

            event.setDamage(0.5);

           // Vector knockbackVector = damageTaker.getLocation().getDirection().multiply(5 * -1).setY(0.5); //old code

            damageTaker.setVelocity(knockbackVector);
           // damageTaker.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING,60,1));

            damager.getInventory().setItemInMainHand(null);
        }

        // --- ----------- --- //

    }

}
