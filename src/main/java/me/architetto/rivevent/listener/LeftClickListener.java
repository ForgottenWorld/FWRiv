package me.architetto.rivevent.listener;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.command.GlobalVar;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.LocSerialization;
import me.architetto.rivevent.util.Messages;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class LeftClickListener implements Listener{

    GlobalVar global = GlobalVar.getInstance();

    private final  HashMap<UUID, HashMap<LOC, String>> tempHashMap = new HashMap<>();
    public enum LOC {
        SPAWN1,
        SPAWN2,
        SPAWN3,
        SPAWN4,
        TOWER,
        SPECTATE
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) throws Exception{
        Player player = event.getPlayer();


        if (global.listenerActivator.containsKey(player.getUniqueId()) && event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if (!tempHashMap.containsKey(player.getUniqueId())) {
                tempHashMap.put(player.getUniqueId(), new HashMap<>());
            }

            switch (tempHashMap.get(player.getUniqueId()).size()) {

                case 5:

                    player.playSound(player.getLocation (), Sound.ENTITY_PLAYER_LEVELUP, 4, 1);

                    tempHashMap.get(player.getUniqueId()).put(LOC.TOWER, LocSerialization.getSerializedLocation(event.getClickedBlock().getLocation().add(0,1,0)));

                    selectBlockEffect(event.getClickedBlock().getLocation().clone());

                    global.riveventPreset.put(global.listenerActivator.get(player.getUniqueId()),tempHashMap.get(player.getUniqueId()));
                    RIVevent.save(global.riveventPreset);
                    global.listenerActivator.remove(player.getUniqueId());

                    tempHashMap.remove(player.getUniqueId());
                    player.sendMessage(ChatMessages.GREEN(Messages.OKPRESET));

                    return;


                case 4:

                    player.playSound(player.getLocation (), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 4, 2);

                    tempHashMap.get(player.getUniqueId()).put(LOC.SPECTATE, LocSerialization.getSerializedLocation(event.getClickedBlock().getLocation().add(0,1,0)));

                    selectBlockEffect(event.getClickedBlock().getLocation().clone());

                    player.sendMessage(ChatMessages.PosMessage("6/6", LOC.TOWER));
                    return;

                case 3:

                    player.playSound(player.getLocation (), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 4, 2);

                    tempHashMap.get(player.getUniqueId()).put(LOC.SPAWN4, LocSerialization.getSerializedLocation(event.getClickedBlock().getLocation().add(0,1,0)));

                    selectBlockEffect(event.getClickedBlock().getLocation().clone());

                    player.sendMessage(ChatMessages.PosMessage("5/6", LOC.SPECTATE));

                    return;

                case 2:

                    player.playSound(player.getLocation (), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 4, 2);

                    tempHashMap.get(player.getUniqueId()).put(LOC.SPAWN3, LocSerialization.getSerializedLocation(event.getClickedBlock().getLocation().add(0,1,0)));

                    selectBlockEffect(event.getClickedBlock().getLocation().clone());

                    player.sendMessage(ChatMessages.PosMessage("4/6", LOC.SPAWN4));

                    return;

                case 1:

                    player.playSound(player.getLocation (), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 4, 2);

                    tempHashMap.get(player.getUniqueId()).put(LOC.SPAWN2, LocSerialization.getSerializedLocation(event.getClickedBlock().getLocation().add(0,1,0)));

                    selectBlockEffect(event.getClickedBlock().getLocation().clone());

                    player.sendMessage(ChatMessages.PosMessage("3/6", LOC.SPAWN3));

                    return;

                case 0:

                    player.playSound(player.getLocation (), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 4, 2);

                    tempHashMap.get(player.getUniqueId()).put(LOC.SPAWN1, LocSerialization.getSerializedLocation(event.getClickedBlock().getLocation().add(0,1,0)));

                    selectBlockEffect(event.getClickedBlock().getLocation().clone());

                    player.sendMessage(ChatMessages.PosMessage("2/6", LOC.SPAWN2));

            }
        }
    }

    public void selectBlockEffect (Location loc) {

        /*  BELLO, LO TENGO QUI PER RICORDO
        loc.add(0.5,0,0.5);

        Shulker entity = (Shulker) loc.getWorld().spawnEntity(loc, EntityType.SHULKER);
        entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,70,1));
        entity.setInvulnerable(true);
        entity.setGlowing(true);
        entity.setAI(false);

        new BukkitRunnable() {


            @Override
            public void run(){

                entity.remove();

            }
        }.runTaskLater(RIVevent.plugin,60);

        loc.add(0,1.5,0);

         */

        loc.add(0.5,1.5,0.5);
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(0, 127, 215), 2);

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
        }.runTaskTimer(RIVevent.plugin,0,18);
    }


}
