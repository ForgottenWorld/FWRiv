package me.architetto.rivevent.listener;

import me.architetto.rivevent.command.GlobalVar;
import me.architetto.rivevent.util.LocSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class TopOfTowerListener implements Listener{

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){

        GlobalVar global = GlobalVar.getInstance();

        if (global.playerJoined.contains(event.getPlayer().getUniqueId())){

            if (event.getPlayer().getLocation().distance(LocSerialization  //Probabilmente sostituirò 'distance' con qualcosa di più leggero.
                    .getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LeftClickListener.LOC.TOWER))) <= 1) {

                //DEVE DARGLI UN ITEM RANDOM (CIBO , LATTE , ECT...)

                //TEST TEST TEST

                Player player = event.getPlayer();
                player.sendMessage("FUNZIONA!!!!!!");

            }

        }


    }

}
