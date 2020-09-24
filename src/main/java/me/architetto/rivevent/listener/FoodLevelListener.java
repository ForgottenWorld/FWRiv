package me.architetto.rivevent.listener;

import me.architetto.rivevent.command.SettingsHandler;
import me.architetto.rivevent.command.GameHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;


public class FoodLevelListener implements Listener{

    SettingsHandler settings = SettingsHandler.getInstance();
    GameHandler global = GameHandler.getInstance();

    @EventHandler
    public void foodStasi(FoodLevelChangeEvent event){
        Player entity = (Player) event.getEntity();

        if (global.playerJoined.contains(entity.getUniqueId())
                || global.playerSpectate.contains(entity.getUniqueId()) ) {

            if (entity.getFoodLevel() <= settings.minFoodLevel) {
                event.setFoodLevel(settings.resetFoodLevel);

            }
        }
    }
}
