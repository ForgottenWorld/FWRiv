package me.architetto.fwriv.listener.event;

import me.architetto.fwriv.partecipant.PartecipantsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropListener implements Listener{

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {

        PartecipantsManager.getInstance()
                .getPartecipant(event.getPlayer()).ifPresent(partecipant -> event.getItemDrop().remove());

    }

}
