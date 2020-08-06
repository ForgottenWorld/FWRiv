package me.architetto.rivevent.listener;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.command.GlobalVar;
import me.architetto.rivevent.util.ChatMessages;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class DeathListener implements Listener{

    @EventHandler
    public void deathEvent(PlayerDeathEvent event){

        GlobalVar global = GlobalVar.getInstance();


        if (global.playerJoined.contains(event.getEntity().getUniqueId())) {

            if (!global.setupStart) {
                return;
            }

            global.playerSpectate.add(event.getEntity().getUniqueId());
            global.playerJoined.remove(event.getEntity().getUniqueId());

            //TODO: listener Spawnevent che tippa in LOC.spectate

            event.getEntity().sendMessage(ChatMessages.GREEN("Hai perso, ma hai giocato bene ! Sarai più fortunato la prossima volta ."
                    + "\n" + "Se vuoi abbandonare l'evento fai /rivevent leave ." ));

            if (global.playerJoined.size()==1){

                Player player = Bukkit.getPlayer(global.playerJoined.get(0)); //Il player vincitore !
                victoryEffect(player);

                List<UUID> mergedList = new ArrayList<>(global.playerJoined);
                mergedList.addAll(global.playerSpectate);
                for (UUID key : mergedList) {

                    Player target = Bukkit.getPlayer(key);
                    target.sendTitle(player.getDisplayName(),"Complimenti! Sei il vincitore.",20,100,20);

                }

            }


            //TODO: Experimental Mode ? [metod : randomEffect]

        }
    }

    public void victoryEffect(Player winner) {

        new BukkitRunnable() {

            private int count = 0;

            @Override
            public void run(){

                count++;
                winner.spawnParticle(Particle.FIREWORKS_SPARK,winner.getLocation(),0,0,1,0);
                if (count >= 5) {
                    this.cancel();
                }

            }
        }.runTaskTimer(RIVevent.plugin,0,20);


    }

    //WIP WIP WIP WIP
    public void randomEffect() {

        //effetto random che si attiva ad un partecipante alla morte di qualche altro partecipante
        //SI POTREBBE APPLICARE A TUTTI I PARTECIPANTI
        //E OPPORTUNO INSERIRE ANCHE UN COOLDOWN

        GlobalVar global = GlobalVar.getInstance();
        int randomNum = global.playerJoined.size();

        randomNum = ThreadLocalRandom.current().nextInt(0, randomNum-1);
        // pesca a caso tra i partecipanti
        Player target = Bukkit.getPlayer(global.playerJoined.get(randomNum));

        randomNum = ThreadLocalRandom.current().nextInt(1, global.positivePotionEffects.size()+1);

        if (target!=null)
            target.addPotionEffect(new PotionEffect(global.positivePotionEffects.get(randomNum), 200, 1));  //effetto ( va fatto in modo che sia casuale)

    }


}
