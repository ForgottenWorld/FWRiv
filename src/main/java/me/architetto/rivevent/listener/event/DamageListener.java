package me.architetto.rivevent.listener.event;

import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.event.MiniGameService;
import me.architetto.rivevent.util.ChatFormatter;
import me.architetto.rivevent.util.Messages;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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

            if (!eventService.getParticipantsPlayers().contains(damager.getUniqueId()))
                return;

            if (!eventService.isStarted()){
                event.setCancelled(true);
                return;
            }

            // --- TRIDENT CODE --- //

            if (damager.getInventory().getItemInMainHand().getType() == Material.TRIDENT) {

                event.setDamage(0.5);

                Vector knockbackVector = damageTaker.getLocation().getDirection()
                        .multiply(7 * -1).setY(0.1);

                damageTaker.setVelocity(knockbackVector);
                damageTaker.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING,60,1));

                damager.getInventory().setItemInMainHand(null);
            }

            // --- ----------- --- //

            if (MiniGameService.getInstance().isCurseEventRunning()
                    && eventService.getParticipantsPlayers().contains(damageTaker.getUniqueId())) {

                if (MiniGameService.getInstance().getCursedPlayer() == damager) {

                    MiniGameService.getInstance().setCursedPlayer(damageTaker);
                    damageTaker.playSound(damageTaker.getLocation(), Sound.ENTITY_GHAST_HURT,4,1);
                    damageTaker.spawnParticle(Particle.MOB_APPEARANCE,damageTaker.getLocation(),1,0,0,0);

                    damageTaker.sendMessage(ChatFormatter.formatEventMessage(Messages.CURSE_MSG1));
                    damager.sendMessage(ChatFormatter.formatEventMessage(Messages.CURSE_MSG2));

                }

            }



    }

}
