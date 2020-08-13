package me.architetto.rivevent.listener;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.command.GameHandler;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class DeathListener implements Listener{

    @EventHandler
    public void deathEvent(PlayerDeathEvent event){

        GameHandler global = GameHandler.getInstance();


        if (global.playerJoined.contains(event.getEntity().getUniqueId())) {

            if (!global.setupStart) {
                return;
            }

            global.playerSpectate.add(event.getEntity().getUniqueId());
            global.playerJoined.remove(event.getEntity().getUniqueId());


            event.getEntity().sendMessage(ChatMessages.GREEN(Messages.DEATH_MESSAGE));

            if (global.playerJoined.size() <= 1){

                Bukkit.broadcast(ChatMessages.RIVallert(Messages.ALLERT_END_EVENT),"rivevent.superuser");

                Player player = Bukkit.getPlayer(global.playerJoined.get(0)); //Il player vincitore !

                victoryEffect(player); //Non so se funziona.... DA TESTARE

                assert player != null;
                victoryMessage(player.getName()); //Viene mandato a tutti i player attualmente presenti all'evento

            }

        }
    }

    public void victoryEffect(Player playerName) {

        new BukkitRunnable() {

            private int count = 0;

            @Override
            public void run(){

                count++;

                playerName.spawnParticle(Particle.FIREWORKS_SPARK,playerName.getLocation(),0,0,5,0);
                if (count >= 5) {
                    this.cancel();
                }

            }
        }.runTaskTimer(RIVevent.plugin,0,20);


    }

    public void victoryMessage(String playerName) {
        GameHandler global = GameHandler.getInstance();
        List<UUID> mergedList = new ArrayList<>(global.playerJoined);
        mergedList.addAll(global.playerSpectate);

        for (UUID key : mergedList) {

            Player player = Bukkit.getPlayer(key);
            assert player != null;
            player.sendTitle(ChatColor.GOLD + playerName.toUpperCase(),
                    ChatColor.ITALIC + Messages.VICTORY_SUBTITLE,20,100,20);

        }


    }



    //NOT IMPLEMENTED - WIP - EXPERIMENTAL MODE {

    //non mi convince ... meglio metterlo come evento che coinvolge gli spettatori .....

    /*
    public void randomPotionEffect() {

        GameHandler global = GameHandler.getInstance();

        SecureRandom random = new SecureRandom();

        List<PotionEffectType> mergedList = new ArrayList<>(global.positivePotionEffects);
        mergedList.addAll(global.negativePotionEffects);

        int randomNum = random.nextInt(mergedList.size());

        for (UUID target : global.playerJoined) {

            Player player = Bukkit.getPlayer(target);
            assert player != null;
            player.addPotionEffect(new PotionEffect(mergedList.get(randomNum),200,1));

        }
    }

    public void cooldown() {

        new BukkitRunnable() {

            @Override
            public void run(){

                cooldown = false;

            }
        }.runTaskLater(RIVevent.plugin,randomPotionEffectCooldown);


    }

     */


    //NOT IMPLEMENTED - WIP - EXPERIMENTAL MODE


}
