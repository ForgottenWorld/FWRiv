package me.architetto.rivevent.listener.event;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.config.SettingsHandler;
import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.event.PlayersManager;
import me.architetto.rivevent.util.ChatFormatter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
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
    PlayersManager playersManager = PlayersManager.getInstance();

    public final List<UUID> playerCooldown = new ArrayList<>();

    @EventHandler
    public void targetBlockInteract(PlayerInteractEvent event){

        if (!eventService.isRunning() || !settingsHandler.enableTargetBlock)
            return;

        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();
        Material material = player.getInventory().getItemInMainHand().getType();


        if (playersManager.isPlayerActive(player.getUniqueId())
                && material.equals(Material.FIREWORK_ROCKET)) {

            player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
            player.setVelocity(new Vector(0,1.5,0));
            player.getWorld().playSound(player.getLocation(),Sound.ENTITY_FIREWORK_ROCKET_LAUNCH,3,1);
            return;

        }


        if (playersManager.isPlayerActive(player.getUniqueId())
                && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                && Objects.equals(event.getHand(), EquipmentSlot.HAND)
                && clickedBlock != null
                && clickedBlock.getType() == Material.TARGET) {

            player.playSound(player.getLocation(),Sound.BLOCK_STONE_BUTTON_CLICK_ON,2,1);

            if (playerCooldown.contains(player.getUniqueId())){
                player.sendMessage(ChatFormatter.formatErrorMessage("target-block in ricarica..."));
                return;
            }

            targetBlockRewardSystem(player, clickedBlock);

            playerCooldown.add(player.getUniqueId());

            Bukkit.getScheduler().scheduleSyncDelayedTask(RIVevent.plugin, () -> {
                playerCooldown.remove(player.getUniqueId());
                player.sendMessage(ChatFormatter.formatSuccessMessage("target-block reward disponibile!"));

            }, settingsHandler.targetBlockCooldown);

        }

    }

    public void targetBlockRewardSystem(Player player, Block targetBlock) {

        int randomInteger = new SecureRandom().nextInt(12);

        switch(randomInteger) {
            case 0:
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,600, 3));
                player.sendMessage(ChatFormatter.formatEventMessage("Reward : " + ChatColor.AQUA + "Regeneration"));
                break;
            case 1:
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600 , 1));
                player.sendMessage(ChatFormatter.formatEventMessage("Reward : " + ChatColor.AQUA + "Damage resistance "));
                break;
            case 3:
                player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 1));
                player.sendMessage(ChatFormatter.formatEventMessage("Reward : " + ChatColor.AQUA + "Ender pearl"));
                break;
            case 4:
                player.getInventory().addItem(new ItemStack(Material.FISHING_ROD, 1));
                player.sendMessage(ChatFormatter.formatEventMessage("Reward : " + ChatColor.AQUA + "Fishing rod"));
                break;
            case 5:
                player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 1200 , 1));
                player.sendMessage(ChatFormatter.formatEventMessage("Reward : " + ChatColor.AQUA + "Health boost"));
                break;
            case 7:
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 600 , 1));
                player.sendMessage(ChatFormatter.formatEventMessage("Reward : " + ChatColor.AQUA + "Invisibility"));
                break;
            case 8:
                player.getInventory().addItem(new ItemStack(Material.COOKED_PORKCHOP, 5));
                player.sendMessage(ChatFormatter.formatEventMessage("Reward : " + ChatColor.AQUA + "Cooked porkchop"));
                break;
            case 9:
                player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 180 , 1));
                player.sendMessage(ChatFormatter.formatEventMessage("Reward : " + ChatColor.AQUA + "Levitation"));
                break;
            case 10:
                player.getInventory().addItem(new ItemStack(Material.SNOWBALL, 5));
                player.sendMessage(ChatFormatter.formatEventMessage("Reward : " + ChatColor.AQUA + "Snowball"));
                break;
            case 11:

                Bukkit.getScheduler().scheduleSyncDelayedTask(RIVevent.plugin, () ->
                        player.getInventory().addItem(new ItemStack(Material.FIREWORK_ROCKET, 2)), 10);

                player.sendMessage(ChatFormatter.formatEventMessage("Reward : " + ChatColor.AQUA + "Firewok rocket"));
                break;
            default:
                Vector knockbackVector = targetBlock.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(-1.6);

                player.getWorld().spawnParticle(Particle.EXPLOSION_LARGE,player.getLocation(),2);
                player.getWorld().playSound(player.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,2,1);

                player.setVelocity(knockbackVector);
        }
    }
}
