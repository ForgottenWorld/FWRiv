package me.architetto.rivevent.listener.event;

import me.architetto.rivevent.config.SettingsHandler;
import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.event.PlayersManager;
import me.architetto.rivevent.util.ChatFormatter;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class ProjectileListener implements Listener {

    EventService eventService = EventService.getInstance();
    SettingsHandler settings = SettingsHandler.getInstance();
    PlayersManager playersManager = PlayersManager.getInstance();

    @EventHandler
    public void disableDamage(ProjectileHitEvent event) {

        if (!eventService.isRunning())
            return;

        if (!(event.getHitEntity() instanceof Player))
            return;


        Player playerHitted = (Player) event.getHitEntity();

        Projectile pj = event.getEntity();

        if(!(pj.getShooter() instanceof Player))
            return;

        Player playerShooter = (Player) pj.getShooter();

        if (!playersManager.isPlayerActive(playerHitted.getUniqueId()) || !playersManager.isPlayerActive(playerShooter.getUniqueId()))
            return;

        if (pj.getType() == EntityType.SNOWBALL) {

            //todo: testare sti vettori
            Vector knockbackVector = playerHitted.getLocation().toVector().subtract(playerShooter.getLocation().toVector()).normalize().multiply(settings.snowballKnockbackPower);

          //  Vector knockbackVector = playerHitted.getLocation().getDirection().multiply(settings.snowballKnockbackPower * -1).setY(0.3);
         // Vector knockbackVector = pj.getVelocity(); //precedente versione

          //  Vector knockbackVector = pj.getVelocity().normalize().multiply(settings.snowballKnockbackPower); //settings.snowballKnockbackPower lo teniamo o no ?

            playerHitted.setVelocity(knockbackVector);

            playerHitted.damage(settings.snowballHitDamage);

            playerHitted.playSound(playerHitted.getLocation(), Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK,2,1);

            return;
        }

        //TODO: DA TESTARE


        if (pj.getType() == EntityType.FISHING_HOOK) {

                if (playerShooter.getInventory().getItemInMainHand().getType() == Material.FISHING_ROD)
                    playerShooter.getInventory().setItemInMainHand(null);
                else if (playerShooter.getInventory().getItemInOffHand().getType() == Material.FISHING_ROD)
                    playerShooter.getInventory().setItemInOffHand(null);
                else
                    return;

                //todo: testare bontà della canna da pesca
                Vector vector = playerHitted.getLocation().add(0,3,0).toVector().subtract(playerShooter.getLocation().toVector()).normalize().multiply(-4);
                //Vector vector = playerHitted.getLocation().toVector().subtract(playerShooter.getLocation().toVector()).normalize().multiply(-4);
                playerHitted.setVelocity(vector);

                playerHitted.sendMessage(ChatFormatter.formatSuccessMessage("Hai abboccato all'amo!"));
                playerHitted.playSound(playerHitted.getLocation(),Sound.ENTITY_DOLPHIN_PLAY,2,1);
                playerShooter.sendMessage(ChatFormatter.formatSuccessMessage("WoW! Hai preso un pesce bello grosso ...")); // :)

            return;
        }

        if (pj.getType() == EntityType.TRIDENT) {

            playerHitted.sendMessage(ChatFormatter.formatEventMessage("OUCH!"));
            playerHitted.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 400, 4));
            pj.remove();

        }
    }


    @EventHandler
    public void enderPearlUse(ProjectileLaunchEvent event) {

        if (!eventService.isRunning())
            return;

        Projectile pj = event.getEntity();

        if(!(pj.getShooter() instanceof Player))
            return;

        Player shooter = (Player) pj.getShooter();

        if (!playersManager.isPlayerActive(shooter.getUniqueId()))
            return;

        if (pj.getType() == EntityType.ENDER_PEARL) {

            event.setCancelled(true);
            shooter.setNoDamageTicks(30);

            shooter.teleport(eventService.getSummonedArena().getTower());

            shooter.getWorld().strikeLightningEffect(shooter.getLocation());

            shooter.getWorld().createExplosion(eventService.getSummonedArena().getTower(),1,false,false);
            shooter.getWorld().spawnParticle(Particle.EXPLOSION_LARGE,shooter.getLocation(),1);

            shooter.getWorld().playSound(shooter.getLocation(),Sound.ENTITY_LIGHTNING_BOLT_IMPACT,3,1);
            shooter.getWorld().playSound(shooter.getLocation(),Sound.ENTITY_LIGHTNING_BOLT_THUNDER,3,1);
            shooter.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,80,1));

            shooter.getInventory().getItemInMainHand().setAmount(shooter.getInventory().getItemInMainHand().getAmount() - 1);

        }

    }

}
