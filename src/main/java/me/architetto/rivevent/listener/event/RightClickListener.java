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
import org.bukkit.inventory.meta.PotionMeta;
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

        if (!playersManager.isPlayerActive(player.getUniqueId()))
            return;


        if (material.equals(Material.FIREWORK_ROCKET)) {

            event.setCancelled(true);
            player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
            player.setVelocity(new Vector(0,1.3,0));
            player.getWorld().playSound(player.getLocation(),Sound.ENTITY_FIREWORK_ROCKET_LAUNCH,3,1);
            return;

        }

        if (material.equals(Material.HONEYCOMB)) {

            player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
            player.setHealth(Math.min(player.getHealth() + 1,20));
            player.playSound(player.getLocation(),Sound.ENTITY_PLAYER_BURP,2,1);
            return;

        }


        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
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
                if (!playersManager.isPlayerActive(player.getUniqueId()))
                    return;
                player.sendMessage(ChatFormatter.formatSuccessMessage("target-block reward disponibile!"));

            }, settingsHandler.targetBlockCooldown);

        }
    }

    public void targetBlockRewardSystem(Player player, Block targetBlock) {

        int randomInteger = new SecureRandom().nextInt(16);

        switch(randomInteger) {
            case 0:
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,600, 3));
                player.sendMessage(ChatFormatter.formatEventMessage("Reward : " + ChatColor.AQUA + "Regeneration"));
                break;
            case 1:
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600 , 1));
                player.sendMessage(ChatFormatter.formatEventMessage("Reward : " + ChatColor.AQUA + "Damage resistance "));
                break;
            case 2:
                Bukkit.getScheduler().scheduleSyncDelayedTask(RIVevent.plugin, () ->
                        player.getInventory().addItem(new ItemStack(Material.FIREWORK_ROCKET, 2)), 10);

                player.sendMessage(ChatFormatter.formatEventMessage("Reward : " + ChatColor.AQUA + "Firework rocket"));
                break;
            case 4:
                player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 1));
                player.sendMessage(ChatFormatter.formatEventMessage("Reward : " + ChatColor.AQUA + "Ender pearl"));
                break;
            case 5:
                player.getInventory().addItem(new ItemStack(Material.FISHING_ROD, 1));
                player.sendMessage(ChatFormatter.formatEventMessage("Reward : " + ChatColor.AQUA + "Fishing rod"));
                break;
            case 6:
                player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 1200 , 1));
                player.sendMessage(ChatFormatter.formatEventMessage("Reward : " + ChatColor.AQUA + "Health boost"));
                break;
            case 8:
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 600 , 1));
                player.sendMessage(ChatFormatter.formatEventMessage("Reward : " + ChatColor.AQUA + "Invisibility"));
                break;
            case 9:
                player.getInventory().addItem(new ItemStack(Material.COOKED_PORKCHOP, 5));
                player.sendMessage(ChatFormatter.formatEventMessage("Reward : " + ChatColor.AQUA + "Cooked porkchop"));
                break;
            case 10:
                player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 180 , 1));
                player.sendMessage(ChatFormatter.formatEventMessage("Reward : " + ChatColor.AQUA + "Levitation"));
                break;
            case 11:
                player.getInventory().addItem(new ItemStack(Material.SNOWBALL, 5));
                player.sendMessage(ChatFormatter.formatEventMessage("Reward : " + ChatColor.AQUA + "Snowball"));
                break;
            case 13:
                Bukkit.getScheduler().scheduleSyncDelayedTask(RIVevent.plugin, () ->
                        player.getInventory().addItem(new ItemStack(Material.FIREWORK_ROCKET, 2)), 10);

                player.sendMessage(ChatFormatter.formatEventMessage("Reward : " + ChatColor.AQUA + "Firework rocket"));
                break;
            case 14:
                player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 1200 , 1));
                player.sendMessage(ChatFormatter.formatEventMessage("Reward : " + ChatColor.AQUA + "Saturation"));
                break;
            case 15:
                player.getInventory().addItem(randomLingeringPotion());
                player.sendMessage(ChatFormatter.formatEventMessage("Reward : " + ChatColor.AQUA + "Lingering potion (random)"));
                break;
            default:
                Vector knockbackVector = targetBlock.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(-1.6);

                player.getWorld().spawnParticle(Particle.EXPLOSION_LARGE,player.getLocation(),2);
                player.getWorld().playSound(player.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,2,1);

                player.setVelocity(knockbackVector);
        }
    }


    public ItemStack randomLingeringPotion() {
        ItemStack p = new ItemStack(Material.LINGERING_POTION, 1);
        PotionMeta m = (PotionMeta) p.getItemMeta();
        int randomValue = new SecureRandom().nextInt(6);

        switch(randomValue) {
            case 0:
                m.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 200, 0), true);
                m.setColor(Color.GREEN);
                break;
            case 1:
                m.addCustomEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 2), true);
                m.setColor(Color.SILVER);
                break;
            case 2:
                m.addCustomEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 2), true);
                m.setColor(Color.BLACK);
                break;
            case 3:
                m.addCustomEffect(new PotionEffect(PotionEffectType.HUNGER, 300, 1), true);
                m.setColor(Color.MAROON);
                break;
            case 4:
                m.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2), true);
                m.setColor(Color.RED);
                break;
            case 5:
                m.addCustomEffect(new PotionEffect(PotionEffectType.JUMP, 400, 2), true);
                m.setColor(Color.AQUA);
                break;
        }

        p.setItemMeta(m);

        return p;
    }


}
