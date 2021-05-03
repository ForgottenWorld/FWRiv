package me.architetto.fwriv.listener.event;

import me.architetto.fwriv.config.SettingsHandler;
import me.architetto.fwriv.localization.Message;
import me.architetto.fwriv.partecipant.PartecipantsManager;
import me.architetto.fwriv.event.EventService;
import me.architetto.fwriv.event.EventStatus;
import org.bukkit.Material;
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

    SettingsHandler settings = SettingsHandler.getInstance();
    double ipsilon = 0.5;

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {

        if (EventService.getInstance().getEventStatus().equals(EventStatus.INACTIVE)
                || !(event.getHitEntity() instanceof Player)) return;

        Projectile pj = event.getEntity();

        if(!(pj.getShooter() instanceof Player)) return;

        Player playerShooter = (Player) pj.getShooter();
        Player playerHitted = (Player) event.getHitEntity();

        PartecipantsManager partecipantsManager = PartecipantsManager.getInstance();

        if (!partecipantsManager.isPresent(playerShooter)
                || !partecipantsManager.isPresent(playerHitted)) return;

        switch (pj.getType()) {
            case SNOWBALL:
                playerHitted.damage(settings.getSnowballHitDamage());
                playerHitted.setVelocity(playerShooter.getLocation()
                        .getDirection().normalize().multiply(settings.getSnowballKnockbackPower()));
                break;
            case FISHING_HOOK:
                if (playerShooter.getInventory().getItemInMainHand().getType() == Material.FISHING_ROD)
                    playerShooter.getInventory().setItemInMainHand(null);
                else if (playerShooter.getInventory().getItemInOffHand().getType() == Material.FISHING_ROD)
                    playerShooter.getInventory().setItemInOffHand(null);
                else
                    return;

                //todo: recode this pls

                Vector vector = playerShooter.getLocation().getDirection().multiply(-1);

                if (playerHitted.getLocation().getY() >= playerShooter.getLocation().getY())
                    vector.setY(0);

                vector.multiply(settings.fishingRodPower);
                vector.add(new Vector(0, ipsilon,0));


                playerHitted.setVelocity(vector);

                playerHitted.getWorld().playSound(playerHitted.getLocation(),Sound.ENTITY_DOLPHIN_SPLASH,2,1);

                Message.FISHINGROD_1.send(playerShooter);
                Message.FISHINGROD_2.send(playerHitted);

        }
    }


    @EventHandler
    public void onEnderpearlUse(ProjectileLaunchEvent event) {

        EventService eventService = EventService.getInstance();

        if (eventService.getEventStatus().equals(EventStatus.INACTIVE)) return;

        Projectile pj = event.getEntity();

        if(!(pj.getShooter() instanceof Player)) return;

        Player shooter = (Player) pj.getShooter();

        PartecipantsManager partecipantsManager = PartecipantsManager.getInstance();

        if (!partecipantsManager.isPresent(shooter)) return;

        if (pj.getType() == EntityType.ENDER_PEARL) {

            event.setCancelled(true);

            shooter.getInventory().getItemInMainHand().setAmount(shooter.getInventory().getItemInMainHand().getAmount() - 1);

            shooter.teleport(eventService.getArena().getTower().add(0,0.5,0));

            shooter.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,10,1));

            shooter.getWorld().strikeLightningEffect(shooter.getLocation());

        }

    }

}
