package me.architetto.rivevent.listener;


import me.architetto.rivevent.command.SettingsHandler;
import me.architetto.rivevent.command.GameHandler;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;


public class ProjectileHitListener implements Listener{

    GameHandler global = GameHandler.getInstance();
    SettingsHandler settings = SettingsHandler.getInstance();

    @EventHandler
    public void disableDamage(ProjectileHitEvent event){

        Projectile p = event.getEntity();

        if (!global.startDoneFlag)
            return;

        if (!(event.getHitEntity() instanceof Player))
            return;

        Player playerHitted = (Player) event.getHitEntity();

        if (p.getType() == EntityType.SNOWBALL && global.playerJoined.contains(playerHitted.getUniqueId())) {

            if (settings.snowballKnockbackToggle){
                Vector knockbackVector = playerHitted.getLocation().getDirection().multiply(settings.snowballKnockbackPower * -1).setY(0.2);
                playerHitted.setVelocity(knockbackVector);
            }

            if (settings.snowballDamageToggle){
                playerHitted.damage(settings.snowballHitDamage);
            }

            playerHitted.playSound(playerHitted.getLocation(), Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK,2,1);
        }

    }

}
