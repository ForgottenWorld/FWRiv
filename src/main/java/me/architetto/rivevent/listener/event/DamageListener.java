package me.architetto.rivevent.listener.event;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.event.MinigameService;
import me.architetto.rivevent.event.PlayersManager;
import me.architetto.rivevent.util.ChatFormatter;
import me.architetto.rivevent.util.Messages;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class DamageListener implements Listener{

    EventService eventService = EventService.getInstance();
    PlayersManager playersManager = PlayersManager.getInstance();
    MinigameService minigameService = MinigameService.getInstance();

    private boolean tranferCooldown = false;

    @EventHandler
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event){

        if (!eventService.isRunning())
            return;

        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player))
            return;

        Player damager = (Player) event.getDamager();
        Player damageTaker = (Player) event.getEntity();

        if (!playersManager.isPlayerActive(damager.getUniqueId())) {
            return;
        }

        if (!playersManager.isPlayerActive(damageTaker.getUniqueId())) {
            return;
        }

        if (!eventService.isDamageEnabled()) {
            event.setCancelled(true);
            return;
        }

        if (eventService.isEarlyDamagePrank()) {
            damager.sendTitle(ChatColor.YELLOW + "KEEP CALM","DON'T BE SO RUDE",10,100,10);
            damager.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,100,1));
            damageTaker.setNoDamageTicks(60);
        }

        if (minigameService.isCurseEventRunning()
                && playersManager.isPlayerActive(damageTaker.getUniqueId())) {

            if (minigameService.getCursedPlayer() == damager && !tranferCooldown) {

                tranferCooldown = true;
                Bukkit.getScheduler().scheduleSyncDelayedTask(RIVevent.plugin, () -> tranferCooldown = false, 60);

                minigameService.setCursedPlayer(damageTaker);

                damageTaker.playSound(damageTaker.getLocation(), Sound.ENTITY_GHAST_HURT,4,1);
                damageTaker.spawnParticle(Particle.MOB_APPEARANCE,damageTaker.getLocation(),1,0,0,0);

                damageTaker.sendMessage(ChatFormatter.formatEventMessage(Messages.CURSE_MSG1));

                damager.sendMessage(ChatFormatter.formatEventMessage(Messages.CURSE_MSG2));
                damager.playSound(damager.getLocation(),Sound.BLOCK_ANVIL_BREAK,1,1);

            }
        }

        if (damager.getInventory().getItemInMainHand().getType() == Material.TRIDENT) {

            Vector knockbackVector = damager.getLocation().toVector().subtract(damageTaker.getLocation().toVector()).multiply(-1.5); //new code, test it

            event.setDamage(0.5);
            
            damageTaker.setVelocity(knockbackVector);

            damager.getInventory().setItemInMainHand(null);
        }

    }
}
