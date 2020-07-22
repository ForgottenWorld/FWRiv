package me.architetto.rivevent.listener;

import me.architetto.rivevent.command.GlobalVar;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.LocSerialization;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;

import java.util.concurrent.ThreadLocalRandom;

public class DeathListener implements Listener{

    @EventHandler
    public void deathEvent(PlayerDeathEvent event){

        GlobalVar global = GlobalVar.getInstance();


        if (global.setupDone && global.playerJoined.contains(event.getEntity().getUniqueId())) {


            global.playerSpectate.add(event.getEntity().getUniqueId());
            global.playerJoined.remove(event.getEntity().getUniqueId());
            event.getEntity().sendMessage(ChatMessages.GREEN("Hai perso, ma hai giocato bene ! Sarai più fortunato la prossima volta ."
                    + "\n" + "Se vuoi abbandonare l'evento fai /rivevent leave ." ));

            if (global.playerJoined.size()==1){

                Player player = Bukkit.getPlayer(global.playerJoined.get(0)); //Il player vincitore !

            } else if (global.playerJoined.size()>=4)   //Applica effeto random
                randomEffect();
        }
    }

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
