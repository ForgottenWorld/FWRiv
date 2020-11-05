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

    public final List<UUID> playerCooldown = new ArrayList<>();

    @EventHandler
    public void targetBlockInteract(PlayerInteractEvent event){

        if (!eventService.isRunning() || !settingsHandler.enableTargetBlock)
            return;

        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();

        if (clickedBlock == null || clickedBlock.getType() != Material.TARGET)
            return;

        if (PlayersManager.getInstance().getActivePlayers().contains(event.getPlayer().getUniqueId())
                && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                && Objects.equals(event.getHand(), EquipmentSlot.HAND)) {

            player.playSound(player.getLocation(),Sound.BLOCK_STONE_BUTTON_CLICK_ON,1,1);

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

        int randomInteger = new SecureRandom().nextInt(13);

        switch(randomInteger) {
            case 0:
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,200, 3));
                player.sendMessage(ChatFormatter.formatEventMessage("Reward : " + ChatColor.AQUA + "Regeneration"));
                break;
            case 1:
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 400 , 1));
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
                player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 400 , 1));
                player.sendMessage(ChatFormatter.formatEventMessage("Reward : " + ChatColor.AQUA + "Health boost"));
                break;
            case 7:
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 400 , 1));
                player.sendMessage(ChatFormatter.formatEventMessage("Reward : " + ChatColor.AQUA + "Invisibility"));
                break;
            case 8:
                player.getInventory().addItem(new ItemStack(Material.COOKED_PORKCHOP, 5));
                player.sendMessage(ChatFormatter.formatEventMessage("Reward : " + ChatColor.AQUA + "Cooked porkchop"));
                break;
            case 12:
                player.getInventory().addItem(new ItemStack(Material.SNOWBALL, 5));
                player.sendMessage(ChatFormatter.formatEventMessage("Reward : " + ChatColor.AQUA + "Snowball"));
                break;
            default:
                Vector knockbackVector = targetBlock.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(-2);

                player.getWorld().spawnParticle(Particle.EXPLOSION_LARGE,player.getLocation(),2);
                player.getWorld().playSound(player.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,2,1);

                player.setVelocity(knockbackVector);
        }
    }
}
