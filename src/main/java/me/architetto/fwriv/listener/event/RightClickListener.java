package me.architetto.fwriv.listener.event;

import me.architetto.fwriv.FWRiv;
import me.architetto.fwriv.config.SettingsHandler;
import me.architetto.fwriv.partecipant.PartecipantStatus;
import me.architetto.fwriv.partecipant.PartecipantsManager;
import me.architetto.fwriv.event.EventService;
import me.architetto.fwriv.event.EventStatus;
import me.architetto.fwriv.reward.RewardService;
import me.architetto.fwriv.utils.ChatFormatter;
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

    EventService eventService = EventService.getInstance();
    SettingsHandler settingsHandler = SettingsHandler.getInstance();
    PartecipantsManager partecipantsManager = PartecipantsManager.getInstance();

    public final List<UUID> playerCooldown = new ArrayList<>();

    @EventHandler
    public void targetBlockInteract(PlayerInteractEvent event) {

        if (eventService.getEventStatus().equals(EventStatus.INACTIVE)) return;

        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();
        Material material = player.getInventory().getItemInMainHand().getType();

        if (!PartecipantsManager.getInstance().isPresent(player))
            return;


        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                && Objects.equals(event.getHand(), EquipmentSlot.HAND)
                && clickedBlock != null
                && clickedBlock.getType() == Material.TARGET) {

            player.playSound(player.getLocation(),Sound.BLOCK_STONE_BUTTON_CLICK_ON,2,1);

            if (playerCooldown.contains(player.getUniqueId())) {
                player.sendMessage(ChatFormatter.formatErrorMessage("target-block in ricarica..."));
                return;
            }

            RewardService.getInstance().giveTargetBlockReward(player);

            playerCooldown.add(player.getUniqueId());

            Bukkit.getScheduler().scheduleSyncDelayedTask(FWRiv.plugin, () -> {
                playerCooldown.remove(player.getUniqueId());
                partecipantsManager.getPartecipant(player).ifPresent(partecipant -> {
                    if (partecipant.getPartecipantStatus().equals(PartecipantStatus.PLAYING))
                        player.sendMessage(ChatFormatter.formatSuccessMessage("target-block reward disponibile!"));
                });

            }, settingsHandler.targetBlockCooldown);

            return;
        }

        switch (material) {
            case FIREWORK_ROCKET:
                event.setCancelled(true);
                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                player.setVelocity(new Vector(0,1.3,0));
                player.getWorld().playSound(player.getLocation(),Sound.ENTITY_FIREWORK_ROCKET_LAUNCH,3,1);
                return;
            case HONEYCOMB:
                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                player.setHealth(Math.min(player.getHealth() + 1,20));
                player.playSound(player.getLocation(),Sound.ENTITY_PLAYER_BURP,2,1);
        }


    }

}
