package me.architetto.rivevent.listener.event;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.config.SettingsHandler;
import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.util.ChatFormatter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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

        if (eventService.getParticipantsPlayers().contains(event.getPlayer().getUniqueId())
                && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                && Objects.requireNonNull(event.getClickedBlock()).getType() == Material.TARGET
                && Objects.equals(event.getHand(), EquipmentSlot.HAND)){

            if (playerCooldown.contains(player.getUniqueId())){
                player.sendMessage(ChatFormatter.formatErrorMessage("target-block in ricarica..."));
                return;
            }

            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, new SecureRandom().nextInt(300), 3));
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            playerCooldown.add(player.getUniqueId());

            Bukkit.getScheduler().scheduleSyncDelayedTask(RIVevent.plugin, () -> {
                playerCooldown.remove(player.getUniqueId());
                player.sendMessage(ChatFormatter.formatSuccessMessage("target-block reward disponibile!"));


            }, 600L);


        }

    }
}
