package me.architetto.rivevent.listener.event;


import me.architetto.rivevent.config.SettingsHandler;
import me.architetto.rivevent.event.EventService;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
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

        Projectile p = event.getEntity();

        Player playerHitted = (Player) event.getHitEntity();

        if (p.getType() == EntityType.SNOWBALL
                && eventService.getParticipantsPlayers().contains(playerHitted.getUniqueId())) {

            Vector knockbackVector = playerHitted.getLocation().getDirection()
                    .multiply(settings.snowballKnockbackPower * -1).setY(0.2);

            playerHitted.setVelocity(knockbackVector);

            playerHitted.damage(settings.snowballHitDamage);

            playerHitted.playSound(playerHitted.getLocation(), Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK,2,1);

        }

    }

}
