package me.architetto.rivevent.listener;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.command.GameHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;


public class FoodLevelListener implements Listener{
    int minFoodLevel = RIVevent.getDefaultConfig().getInt("MIN_FOOD_LEVEL");
    int resetFoodLevel = RIVevent.getDefaultConfig().getInt("RESET_FOOD_LEVEL");
    GameHandler global = GameHandler.getInstance();

    @EventHandler
    public void foodStasi(FoodLevelChangeEvent event){
        Player entity = (Player) event.getEntity();

        if (global.playerJoined.contains(entity.getUniqueId())
                || global.playerSpectate.contains(entity.getUniqueId()) ) {

            if (entity.isOnline()  && entity.getFoodLevel() <= minFoodLevel) {
                event.setFoodLevel(resetFoodLevel);

            }
        }
    }
}
