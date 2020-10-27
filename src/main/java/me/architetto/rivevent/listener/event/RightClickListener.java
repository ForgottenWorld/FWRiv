package me.architetto.rivevent.listener.event;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.config.SettingsHandler;
import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.util.ChatFormatter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class RightClickListener implements Listener{

    EventService eventService = EventService.getInstance();
    SettingsHandler settingsHandler = SettingsHandler.getInstance();

    public final List<UUID> playerCooldown = new ArrayList<>();

    @EventHandler
    public void targetBlockInteract(PlayerInteractEvent event){

        if (!eventService.isRunning() || !settingsHandler.enableTargetBlock)
            return;

        Player player = event.getPlayer();

        if (eventService.getPlayerIN().contains(event.getPlayer().getUniqueId())
                && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                && Objects.requireNonNull(event.getClickedBlock()).getType() == Material.TARGET
                && Objects.equals(event.getHand(), EquipmentSlot.HAND)) {

            player.playSound(player.getLocation(),Sound.BLOCK_STONE_BUTTON_CLICK_ON,1,1);

            if (playerCooldown.contains(player.getUniqueId())){
                player.sendMessage(ChatFormatter.formatErrorMessage("target-block in ricarica..."));
                return;
            }

            int targetBlockEffect = new SecureRandom().nextInt(100);

            if (targetBlockEffect < 45) {

                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,200, 3));
                playerCooldown.add(player.getUniqueId());

            } else if (targetBlockEffect > 55) {

                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 400 , 1));
                playerCooldown.add(player.getUniqueId());


            } else if (targetBlockEffect == 50) {

                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 400 , 1));
                playerCooldown.add(player.getUniqueId());


            } else {

                Vector knockbackVector = eventService.getSummonedArena().getTower()
                        .toVector().subtract(player.getLocation().toVector()).multiply(-2);

                player.getWorld().spawnParticle(Particle.EXPLOSION_LARGE,player.getLocation(),2);
                player.getWorld().playSound(player.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,2,1);

                player.setVelocity(knockbackVector);

                playerCooldown.add(player.getUniqueId());

            }



            Bukkit.getScheduler().scheduleSyncDelayedTask(RIVevent.plugin, () -> {
                playerCooldown.remove(player.getUniqueId());
                player.sendMessage(ChatFormatter.formatSuccessMessage("target-block reward disponibile!"));


            }, settingsHandler.targetBlockCooldown);


        }

    }
}
