package me.architetto.rivevent.listener.event;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.event.EventService;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;


public class DeathListener implements Listener{
    EventService eventService = EventService.getInstance();


    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {

        if (!eventService.isRunning())
            return;

        if (eventService.getParticipantsPlayers().contains(event.getEntity().getUniqueId())) {

            event.setCancelled(true);
            event.getEntity().getInventory().clear();
            event.getEntity().setGameMode(GameMode.SPECTATOR);

            eventService.removePartecipant(event.getEntity().getUniqueId());

        }

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTotemEvent(EntityResurrectEvent event) {

        if (event.isCancelled())
            return;

        if (!eventService.isRunning())
            return;

        if (eventService.getParticipantsPlayers().contains(event.getEntity().getUniqueId())) {

            new BukkitRunnable() {

                @Override
                public void run(){


                    Player player = (Player) event.getEntity();
                    player.getWorld().createExplosion(eventService.getSummonedArena().getTower(),2,false,false);
                    player.getWorld().spawnParticle(Particle.EXPLOSION_LARGE,player.getLocation(),2);
                    player.teleport(eventService.getSummonedArena().getTower());

                }
            }.runTaskLater(RIVevent.plugin,5);

        }

    }

    /*

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

     */

}
