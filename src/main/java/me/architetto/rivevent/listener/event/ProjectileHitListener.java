package me.architetto.rivevent.listener.event;


import me.architetto.rivevent.config.SettingsHandler;
import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.util.ChatFormatter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;


public class ProjectileHitListener implements Listener {

    EventService eventService = EventService.getInstance();
    SettingsHandler settings = SettingsHandler.getInstance();

    @EventHandler
    public void disableDamage(ProjectileHitEvent event) {

        if (!eventService.isRunning())
            return;

        if (!(event.getHitEntity() instanceof Player))
            return;


        Player playerHitted = (Player) event.getHitEntity();

        if (!eventService.getParticipantsPlayers().contains(playerHitted.getUniqueId()))
            return;

        Projectile pj = event.getEntity();

        if (pj.getType() == EntityType.SNOWBALL) {



         // Vector knockbackVector = playerHitted.getLocation().getDirection().multiply(settings.snowballKnockbackPower * -1).setY(0.3);

            Vector knockbackVector = pj.getVelocity();

            playerHitted.setVelocity(knockbackVector);

            playerHitted.damage(settings.snowballHitDamage);

            playerHitted.playSound(playerHitted.getLocation(), Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK,2,1);

            return;
        }

        //TODO: DA TESTARE


        if (pj.getType() == EntityType.FISHING_HOOK) {

            if (pj.getShooter() instanceof Player) {

                Player shooter = (Player) pj.getShooter();

                if (shooter.getInventory().getItemInMainHand().getType() == Material.FISHING_ROD)
                    shooter.getInventory().setItemInMainHand(null);
                else if (shooter.getInventory().getItemInOffHand().getType() == Material.FISHING_ROD)
                    shooter.getInventory().setItemInOffHand(null);
                else
                    return; //se non trova una fishing rod non esegue nulla

                Location shooterLoc = shooter.getLocation();

                shooter.teleport(playerHitted.getLocation());
                shooter.playSound(shooter.getLocation(),Sound.ENTITY_ENDERMAN_TELEPORT,1,1);

                playerHitted.teleport(shooterLoc);
                playerHitted.playSound(playerHitted.getLocation(),Sound.ENTITY_ENDERMAN_TELEPORT,1,1);
            }
            return;

        }

        if (pj.getType() == EntityType.TRIDENT) {

            if (pj.getShooter() instanceof Player) {

                playerHitted.sendMessage(ChatFormatter.formatEventMessage("OUCH!"));
                playerHitted.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 4));
                pj.remove();


            }

            return;

        }

    }

}
