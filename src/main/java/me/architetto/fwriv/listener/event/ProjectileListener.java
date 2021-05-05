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

    final double ipsilon = 0.7;

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {

        Projectile pj = event.getEntity();

        if(!(pj.getShooter() instanceof Player) ||
                !(event.getHitEntity() instanceof Player)) return;

        Player playerShooter = (Player) pj.getShooter();
        Player playerHitted = (Player) event.getHitEntity();

        PartecipantsManager partecipantsManager = PartecipantsManager.getInstance();

        partecipantsManager.getPartecipant(playerShooter).ifPresent(partecipant -> {
            if (!partecipantsManager.isPresent(playerHitted)
                    || !EventService.getInstance().getEventStatus().equals(EventStatus.RUNNING))
                return;

            SettingsHandler settings = SettingsHandler.getInstance();

            switch (pj.getType()) {
                case SNOWBALL:
                    playerHitted.damage(settings.getSnowballHitDamage());
                    playerHitted.setVelocity(playerShooter.getLocation()
                            .getDirection().multiply(settings.getSnowballKnockbackPower()));
                    break;
                case FISHING_HOOK:
                    if (playerShooter.getInventory().getItemInMainHand().getType() == Material.FISHING_ROD)
                        playerShooter.getInventory().setItemInMainHand(null);
                    else if (playerShooter.getInventory().getItemInOffHand().getType() == Material.FISHING_ROD)
                        playerShooter.getInventory().setItemInOffHand(null);
                    else
                        return;

                    Vector direction = playerHitted.getLocation().toVector()
                            .subtract(playerShooter.getLocation().toVector()).normalize();
                    if (direction.getY() < 0)
                        direction.setY(ipsilon);
                    direction.multiply(settings.getFishingRodPullPower());
                    playerHitted.setVelocity(direction);

                    playerHitted.getWorld().playSound(playerHitted.getLocation(),Sound.ENTITY_DOLPHIN_SPLASH,2,1);

                    Message.FISHINGROD_1.send(playerShooter);
                    Message.FISHINGROD_2.send(playerHitted);

            }

        });

    }


    @EventHandler
    public void onEnderpearlUse(ProjectileLaunchEvent event) {
        Projectile pj = event.getEntity();

        if(!(pj.getShooter() instanceof Player)) return;

        Player shooter = (Player) pj.getShooter();

        PartecipantsManager pm = PartecipantsManager.getInstance();

        pm.getPartecipant(shooter).ifPresent(partecipant -> {
            if (pj.getType() == EntityType.ENDER_PEARL) {
                event.setCancelled(true);

                EventService es = EventService.getInstance();
                if (es.getEventStatus().equals(EventStatus.RUNNING)) {
                    shooter.getInventory().getItemInMainHand().setAmount(shooter.getInventory().getItemInMainHand().getAmount() - 1);
                    shooter.teleport(es.getArena().getTower().add(0, 0.5, 0));
                    shooter.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 10, 1));
                    shooter.getWorld().strikeLightningEffect(shooter.getLocation());
                }
            }
        });

    }

}
