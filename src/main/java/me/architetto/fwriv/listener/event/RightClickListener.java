package me.architetto.fwriv.listener.event;

import me.architetto.fwriv.FWRiv;
import me.architetto.fwriv.config.SettingsHandler;
import me.architetto.fwriv.localization.Message;
import me.architetto.fwriv.partecipant.PartecipantStatus;
import me.architetto.fwriv.partecipant.PartecipantsManager;
import me.architetto.fwriv.reward.RewardService;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;

import java.util.*;

public class RightClickListener implements Listener{

    private final Set<UUID> cooldown = new HashSet<>();

    @EventHandler
    public void interactEvent(PlayerInteractEvent event) {

        if (!Objects.equals(event.getHand(), EquipmentSlot.HAND)) return;

        PartecipantsManager pm = PartecipantsManager.getInstance();

        Player player = event.getPlayer();
        pm.getPartecipant(player).ifPresent(partecipant -> {
            if (!partecipant.getPartecipantStatus().equals(PartecipantStatus.PLAYING)) return;

            Block clickedBlock = event.getClickedBlock();
            Action action = event.getAction();
            if (clickedBlock != null
                    && clickedBlock.getType().equals(Material.TARGET)
                    && action.equals(Action.RIGHT_CLICK_BLOCK)) {

                event.setCancelled(true);
                player.playSound(player.getLocation(),Sound.BLOCK_STONE_BUTTON_CLICK_ON,2,1);

                if (cooldown.contains(player.getUniqueId())) {
                    Message.TARGETBLOCK_COOLDOWN.send(player);
                    return;
                }

                RewardService.getInstance().giveTargetBlockReward(player);
                this.cooldown.add(player.getUniqueId());
                scheduleCooldown(player);
                return;

            }

            if (action.equals(Action.RIGHT_CLICK_BLOCK)
                    || action.equals(Action.RIGHT_CLICK_AIR)) {

                switch (player.getInventory().getItemInMainHand().getType()) {
                    case FIREWORK_ROCKET:
                        event.setCancelled(true);
                        rocketeer(player);
                        return;
                    case HONEYCOMB:
                        if (player.getHealth() >= 20) return;
                        honeyheal(player);
                        break;
                    case LEAD:
                        event.setCancelled(true); //
                }
            }

        });
    }

    private void scheduleCooldown(Player player) {

        Bukkit.getScheduler().scheduleSyncDelayedTask(FWRiv.plugin, () -> {
            cooldown.remove(player.getUniqueId());
            PartecipantsManager.getInstance().getPartecipant(player).ifPresent(partecipant -> {
                if (partecipant.getPartecipantStatus().equals(PartecipantStatus.PLAYING))
                    Message.TARGETBLOCK_READY.send(player);
            });

        }, SettingsHandler.getInstance().getTargetBlockCooldown());

    }

    private void rocketeer(Player player) {
        player.getInventory().getItemInMainHand()
                .setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
        player.setVelocity(new Vector(0,1.3,0));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 3, 1);
    }

    private void honeyheal(Player player) {
        player.getInventory().getItemInMainHand()
                .setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
        player.setHealth(Math.min(player.getHealth() + SettingsHandler.getInstance().getHoneyHealth(), 20));
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 2, 1);
    }

}
