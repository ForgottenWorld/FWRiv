package me.architetto.fwriv.listener.event;

import me.architetto.fwriv.partecipant.PartecipantsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodListener implements Listener {

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {

        PartecipantsManager.getInstance().getPartecipant(event.getEntity().getUniqueId()).ifPresent(partecipant -> {
            if (event.getFoodLevel() < 7)
                event.setFoodLevel(7);
        });

    }
}
