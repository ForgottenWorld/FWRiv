package me.architetto.rivevent.listener;

import me.architetto.rivevent.command.GlobalVar;
import me.architetto.rivevent.util.LocSerialization;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class DeathListener implements Listener{

    @EventHandler
    public void deathEvent(PlayerDeathEvent event){

        GlobalVar global = GlobalVar.getInstance();

        if (global.setupDone && global.playerJoined.contains(event.getEntity().getUniqueId())) {

            global.playerSpectate.add(event.getEntity().getUniqueId());
            global.playerJoined.remove(event.getEntity().getUniqueId());
            event.getEntity().teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LeftClickListener.LOC.SPECTATE)));

            if (global.playerJoined.size()==1){


                //E' rimasto un solo player in vita (il vincitore)
            }

        }
    }

    public void randomEffect() {

        //effetto random che si attiva ad un partecipante alla morte di qualche altro partecipante

        GlobalVar global = GlobalVar.getInstance();
        int randomNum = global.playerJoined.size();

        randomNum = ThreadLocalRandom.current().nextInt(1, randomNum+1);
        // pesca a caso tra i partecipanti
        Player target = Bukkit.getPlayer(global.playerJoined.get(randomNum-1));

        randomNum = ThreadLocalRandom.current().nextInt(1, global.positiveEffects.size()+1);

        if (target!=null)
            target.addPotionEffect(new PotionEffect(global.positiveEffects.get(randomNum), 200, 1));  //effetto ( va fatto in modo che sia casuale)

    }


}
