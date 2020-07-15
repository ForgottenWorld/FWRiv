package me.architetto.rivevent.listener;

import me.architetto.rivevent.command.GlobalVar;
import me.architetto.rivevent.command.subcommand.superuser.InitCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;


public class FoodLevelListener implements Listener{
    GlobalVar global = GlobalVar.getInstance();

    @EventHandler
    public void foodStasi(FoodLevelChangeEvent event){
        Player entity = (Player) event.getEntity();

        if (global.playerJoined.containsKey(entity.getUniqueId())) {
            if (entity.isOnline()  && entity.getFoodLevel()<=10) {
                event.setFoodLevel(18); //se scende sotto i 10 lo setta a 18 (per non dare l'effeto regen)
            }
        }
    }
}
