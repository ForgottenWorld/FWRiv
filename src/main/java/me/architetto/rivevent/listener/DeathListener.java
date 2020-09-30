package me.architetto.rivevent.listener;


import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.command.GameHandler;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.Messages;
import org.bukkit.*;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.*;

public class DeathListener implements Listener{

    GameHandler global = GameHandler.getInstance();

    @EventHandler
    public void deathEvent(PlayerDeathEvent event){

        Player deadPlayer = event.getEntity().getPlayer();

        if (deadPlayer == null)
            return;

        if (global.playerJoined.contains(deadPlayer.getUniqueId()) && global.setupStartFlag) {

            global.playerSpectate.add(deadPlayer.getUniqueId());
            global.playerJoined.remove(deadPlayer.getUniqueId());
            global.playerOut.add(deadPlayer.getUniqueId());

            event.getDrops().clear();
            deadPlayer.sendMessage(ChatMessages.GREEN(Messages.DEATH_MESSAGE));

            if (global.curseEventFlag && global.cursedPlayer == deadPlayer) {

                global.curseEventFlag = false;
                for (UUID u : global.allPlayerList()){
                    Objects.requireNonNull(Bukkit.getPlayer(u)).sendMessage(ChatMessages.RED(deadPlayer.getDisplayName()
                            + " e' morto portandosi la maledizione nella tomba!"));
                }

            }

            if (global.playerJoined.size() <= 1){

                Bukkit.broadcast(ChatMessages.RIVallert(Messages.ALLERT_END_EVENT),"rivevent.superuser");
                global.shutdownMinigames();

                if (global.playerJoined.size() == 1){

                    Player player = Bukkit.getPlayer(global.playerJoined.get(0)); //Il player vincitore !

                    if (player != null){
                        victoryFireworksEffect(player.getLocation().add(0, 1, 0), 2);
                        victoryMessage(player.getName());
                    }
                }
            }
        }
    }

    public void victoryMessage(String playerName) {

        List<UUID> mergedList = new ArrayList<>(global.playerJoined);
        mergedList.addAll(global.playerSpectate);

        for (UUID key : mergedList) {

            Player player = Bukkit.getPlayer(key);

            if (player != null)
                player.sendTitle(ChatColor.GOLD + playerName.toUpperCase(),
                    ChatColor.ITALIC + Messages.VICTORY_SUBTITLE,20,100,20);

        }
    }


    public static void victoryFireworksEffect(Location loc, int amount){

        new BukkitRunnable() {
            int cicle = 0;

            @Override
            public void run(){

                Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
                FireworkMeta fwm = fw.getFireworkMeta();

                fwm.setPower(1);
                fwm.addEffect(FireworkEffect.builder().flicker(true).trail(true)
                        .with(FireworkEffect.Type.BALL).with(FireworkEffect.Type.BALL_LARGE)
                        .with(FireworkEffect.Type.STAR).withColor(Color.ORANGE)
                        .withColor(Color.YELLOW).withFade(Color.PURPLE).withFade(Color.RED).build());

                fw.setFireworkMeta(fwm);

                for (int i = 0;i<amount; i++) {

                    Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
                    fw2.setFireworkMeta(fwm);
                }

                cicle++;

                if (cicle == 4)
                    this.cancel();

            }
        }.runTaskTimer(RIVevent.plugin,20,100);

    }

}
