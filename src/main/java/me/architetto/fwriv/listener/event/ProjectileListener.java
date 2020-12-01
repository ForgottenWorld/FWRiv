package me.architetto.fwriv.listener.event;

import me.architetto.fwriv.config.SettingsHandler;
import me.architetto.fwriv.event.PlayersManager;
import me.architetto.fwriv.event.service.EventService;
import me.architetto.fwriv.utils.ChatFormatter;
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
    SettingsHandler settings = SettingsHandler.getSettingsHandler();
    PlayersManager playersManager = PlayersManager.getInstance();
    double ipsilon = 0.5;

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

            Vector knockbackVector = playerShooter.getEyeLocation().getDirection();
            knockbackVector.multiply(settings.snowballKnockbackPower);

            playerHitted.setVelocity(knockbackVector);

            playerHitted.damage(settings.snowballHitDamage);

            return;
        }

        if (pj.getType() == EntityType.FISHING_HOOK) {

            if (playerShooter.getInventory().getItemInMainHand().getType() == Material.FISHING_ROD)
                playerShooter.getInventory().setItemInMainHand(null);
            else if (playerShooter.getInventory().getItemInOffHand().getType() == Material.FISHING_ROD)
                playerShooter.getInventory().setItemInOffHand(null);
            else
                return;

            Vector vector = playerShooter.getEyeLocation().getDirection().multiply(-1);

            if (playerHitted.getLocation().getY() >= playerShooter.getLocation().getY())
                vector.setY(0);

            vector.multiply(settings.fishingRodPower);
            vector.add(new Vector(0, ipsilon,0));


            playerHitted.setVelocity(vector);

            playerHitted.getWorld().playSound(playerHitted.getLocation(),Sound.ENTITY_DOLPHIN_SPLASH,2,1);

            playerHitted.sendMessage(ChatFormatter.formatSuccessMessage("Hai abboccato all'amo!"));
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

            shooter.getInventory().getItemInMainHand().setAmount(shooter.getInventory().getItemInMainHand().getAmount() - 1);

            shooter.setNoDamageTicks(25);

            shooter.teleport(eventService.getSummonedArena().getTower().clone().add(0,0.5,0));
            shooter.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,10,1));
            shooter.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,40,1));

            shooter.getWorld().strikeLightningEffect(shooter.getLocation());

            shooter.getWorld().createExplosion(eventService.getSummonedArena().getTower(),1,false,false);
            shooter.getWorld().spawnParticle(Particle.EXPLOSION_LARGE,shooter.getLocation(),1);

            shooter.getWorld().playSound(shooter.getLocation(),Sound.ENTITY_LIGHTNING_BOLT_IMPACT,3,1);
            shooter.getWorld().playSound(shooter.getLocation(),Sound.ENTITY_LIGHTNING_BOLT_THUNDER,3,1);


        }

    }

}
