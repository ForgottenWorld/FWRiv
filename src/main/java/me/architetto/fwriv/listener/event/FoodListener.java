package me.architetto.fwriv.listener.event;

import me.architetto.fwriv.partecipant.PartecipantsManager;
import me.architetto.fwriv.event.service.EventService;
import me.architetto.fwriv.event.service.EventStatus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodListener implements Listener {

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {

        if (EventService.getInstance().getEventStatus().equals(EventStatus.INACTIVE))
            return;

        PartecipantsManager.getInstance().getPartecipant(event.getEntity().getUniqueId()).ifPresent(p -> {
            /*
            Optional.ofNullable(event.getItem()).ifPresent(itemStack -> {
                if (itemStack.getType().equals(Material.MUSHROOM_STEW)) {
                    new PotionEffect(PotionEffectType.HEALTH_BOOST, 6000, 3).apply(event.getEntity());
                }
            });

             */
            if (event.getFoodLevel() < 7)
                event.setFoodLevel(7);
        });

    }
}
