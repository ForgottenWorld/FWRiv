package me.architetto.rivevent.listener.event;

import me.architetto.rivevent.config.SettingsHandler;
import me.architetto.rivevent.event.EventService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;


public class FoodLevelListener implements Listener{

    EventService eventService = EventService.getInstance();
    int foodLevel = SettingsHandler.getInstance().foodLevel;

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {

        if (!eventService.isRunning())
            return;

        Player entity = (Player) event.getEntity();

        if (eventService.getEventPlayerList().contains(entity.getUniqueId())) {

            if (event.getFoodLevel() <= foodLevel)
                event.setFoodLevel(foodLevel); //event.setCancelled(true)

        }

    }
}
