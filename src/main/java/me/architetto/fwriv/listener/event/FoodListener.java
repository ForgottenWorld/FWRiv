package me.architetto.fwriv.listener.event;

import me.architetto.fwriv.config.SettingsHandler;
import me.architetto.fwriv.event.PlayersManager;
import me.architetto.fwriv.event.service.EventService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;


public class FoodListener implements Listener {

    EventService eventService = EventService.getInstance();
    SettingsHandler settingsHandler = SettingsHandler.getSettingsHandler();
    PlayersManager playersManager = PlayersManager.getInstance();

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {

        if (!eventService.isRunning())
            return;

        Player entity = (Player) event.getEntity();

        if (playersManager.isPartecipants(entity.getUniqueId())) {

            if (event.getFoodLevel() < settingsHandler.foodLevel)
                event.setFoodLevel(settingsHandler.foodLevel);

        }

    }
}
