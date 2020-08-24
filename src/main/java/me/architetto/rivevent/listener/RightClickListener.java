package me.architetto.rivevent.listener;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.command.GameHandler;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.LocSerialization;
import me.architetto.rivevent.util.Messages;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class RightClickListener implements Listener{

    GameHandler global = GameHandler.getInstance();

    public final HashMap<UUID, Step> saveCreatorStep = new HashMap<>();

    public enum Step {SPAWN1, SPAWN2, SPAWN3, SPAWN4, TOWER, SPECTATE}

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) throws Exception{
        Player player = event.getPlayer();

        if (!global.listenerActivator.containsKey(player.getUniqueId()) || !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                || Objects.equals(event.getHand(), EquipmentSlot.HAND))
            return;

        if (!saveCreatorStep.containsKey(player.getUniqueId())){
            saveCreatorStep.put(player.getUniqueId(), Step.SPAWN1);
            global.riveventPreset.put(global.listenerActivator.get(player.getUniqueId()),new HashMap<>());
        }

        Location clickedBlock = Objects.requireNonNull(event.getClickedBlock()).getLocation().add(0,1,0);

        switch(saveCreatorStep.get(player.getUniqueId())) {

            case SPAWN1:

                player.playSound(player.getLocation (), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3, 2);

                spawnEffectAtBlock(event.getClickedBlock().getLocation().clone(),0,0,102);

                global.riveventPreset.get(global.listenerActivator.get(player.getUniqueId())).put(Step.SPAWN1, LocSerialization.getSerializedLocation(clickedBlock));

                saveCreatorStep.put(player.getUniqueId(), Step.SPAWN2);

                player.sendMessage(ChatMessages.PosMessage("2/6", Step.SPAWN2));

                return;

            case SPAWN2:

                player.playSound(player.getLocation (), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3, 2);

                spawnEffectAtBlock(event.getClickedBlock().getLocation().clone(),51,102,255);

                global.riveventPreset.get(global.listenerActivator.get(player.getUniqueId())).put(Step.SPAWN2, LocSerialization.getSerializedLocation(clickedBlock));

                saveCreatorStep.put(player.getUniqueId(), Step.SPAWN3);

                player.sendMessage(ChatMessages.PosMessage("3/6", Step.SPAWN3));

                return;

            case SPAWN3:

                player.playSound(player.getLocation (), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3, 2);

                spawnEffectAtBlock(event.getClickedBlock().getLocation().clone(),153,204,255);

                global.riveventPreset.get(global.listenerActivator.get(player.getUniqueId())).put(Step.SPAWN3, LocSerialization.getSerializedLocation(clickedBlock));

                saveCreatorStep.put(player.getUniqueId(), Step.SPAWN4);

                player.sendMessage(ChatMessages.PosMessage("4/6", Step.SPAWN4));

                return;

            case SPAWN4:

                player.playSound(player.getLocation (), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3, 2);

                spawnEffectAtBlock(event.getClickedBlock().getLocation().clone(),255,255,102);

                global.riveventPreset.get(global.listenerActivator.get(player.getUniqueId())).put(Step.SPAWN4, LocSerialization.getSerializedLocation(clickedBlock));

                saveCreatorStep.put(player.getUniqueId(), Step.TOWER);

                player.sendMessage(ChatMessages.PosMessage("5/6", Step.TOWER));

                return;

            case TOWER:

                player.playSound(player.getLocation (), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3, 2);

                spawnEffectAtBlock(event.getClickedBlock().getLocation().clone(),0,153,0);

                global.riveventPreset.get(global.listenerActivator.get(player.getUniqueId())).put(Step.TOWER, LocSerialization.getSerializedLocation(clickedBlock));

                saveCreatorStep.put(player.getUniqueId(), Step.SPECTATE);

                player.sendMessage(ChatMessages.PosMessage("6/6", Step.SPECTATE));

                return;

            case SPECTATE:

                player.playSound(player.getLocation (), Sound.ENTITY_PLAYER_LEVELUP, 3, 2);

                spawnEffectAtBlock(event.getClickedBlock().getLocation().clone(),255,0,0);

                global.riveventPreset.get(global.listenerActivator.get(player.getUniqueId())).put(Step.SPECTATE, LocSerialization.getSerializedLocation(clickedBlock));

                if (checkPreset(global.listenerActivator.get(player.getUniqueId()))){
                    RIVevent.save(global.riveventPreset);
                    player.sendMessage(ChatMessages.GREEN(Messages.OK_PRESET));
                }
                else {
                    player.sendMessage(ChatMessages.RED(Messages.ERR_INSERT_PRESET));
                    global.riveventPreset.remove(global.listenerActivator.get(player.getUniqueId()));
                }

                saveCreatorStep.remove(player.getUniqueId());
                global.listenerActivator.remove(player.getUniqueId());


        }
    }

    public void spawnEffectAtBlock(Location loc, int redValue, int greenValue, int blueValue) {

        loc.add(0.5,1.5,0.5);
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(redValue, greenValue, blueValue), 3);

        new BukkitRunnable() {

            private int count = 0;

            @Override
            public void run(){

                count++;
                loc.getWorld().spawnParticle(Particle.REDSTONE,loc,10,dustOptions);
                if (count == 5) {
                    this.cancel();
                }

            }
        }.runTaskTimer(RIVevent.plugin,0,20);
    }

    public boolean checkPreset(String presetName) {

        return new ArrayList<>(global.riveventPreset.get(presetName).keySet()).containsAll(new ArrayList<Step>())
                && !global.riveventPreset.get(presetName).containsValue(null);
    }



}
