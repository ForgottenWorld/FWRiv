package me.architetto.fwriv.listener.event;

import me.architetto.fwriv.config.SettingsHandler;
import me.architetto.fwriv.event.EventService;
import me.architetto.fwriv.event.EventStatus;
import me.architetto.fwriv.localization.Message;
import me.architetto.fwriv.partecipant.PartecipantStats;
import me.architetto.fwriv.partecipant.PartecipantsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DamageListener implements Listener{

    @EventHandler
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event) {

        if (!(event.getDamager() instanceof Player)
                || !(event.getEntity() instanceof Player)) return;

        PartecipantsManager pm = PartecipantsManager.getInstance();
        pm.getPartecipant(event.getDamager().getUniqueId()).ifPresent(partecipant -> {
            if (!EventService.getInstance().getEventStatus().equals(EventStatus.RUNNING)) {
                event.setCancelled(true);
                return;
            }

            Player damager = (Player) event.getDamager();
            Player damageTaker = (Player) event.getEntity();

            pm.getPartecipantStats(damager)
                    .ifPresent(stats -> stats.addDamageDealt(event.getFinalDamage()));
            pm.getPartecipantStats(damageTaker)
                    .ifPresent(stats -> stats.addDamageTaken(event.getFinalDamage()));


            ItemStack itemStack = damager.getInventory().getItemInMainHand();

            switch (itemStack.getType()) {
                case LEAD:
                    pickpoket(damager,damageTaker);
                    break;
                case SHEARS:
                    armorshred(damager,damageTaker);
            }

        });
    }

    private void pickpoket(Player damager, Player damageTaker) {
        ItemStack[] inv = damageTaker.getInventory().getContents();
        List<Integer> avaible = new ArrayList<>();
        for (int i = 0; i < inv.length; i++) {
            if (inv[i] != null)
                avaible.add(i);
        }

        if (avaible.isEmpty()) {
            Message.PICKPOKET3.send(damager,damageTaker.getName());
            return;
        }

        int pick = avaible.get(new Random().nextInt(avaible.size()));
        int qty = inv[pick].getAmount();

        if (qty != 1 && SettingsHandler.getInstance().isPickpoketEntireSlot())
            qty = new Random().nextInt(qty) + 1;

        damager.getInventory().setItemInMainHand(inv[pick].asQuantity(qty));
        damageTaker.getInventory().setItem(pick,inv[pick].subtract(qty));

        Message.PICKPOKET1.send(damager,inv[pick].getI18NDisplayName());
        Message.PICKPOKET2.send(damageTaker,inv[pick].getI18NDisplayName());
        PartecipantsManager.getInstance().getPartecipantStats(damager).ifPresent(PartecipantStats::addPickpocket);
    }

    private void armorshred(Player damager, Player damageTaker) {
        ItemStack[] inv = damageTaker.getInventory().getContents();
        List<Integer> avaible = new ArrayList<>();
        for (int i = 36; i < inv.length - 1; i++) {
            if (inv[i] != null)
                avaible.add(i);
        }

        if (avaible.isEmpty()) {
            Message.ARMORSHRED3.send(damager,damageTaker.getName());
            return;
        }

        int pick = avaible.get(new Random().nextInt(avaible.size()));

        damager.getInventory().setItemInMainHand(null);
        damageTaker.getInventory().setItem(pick,null);

        Message.ARMORSHRED1.send(damager,inv[pick].getI18NDisplayName());
        Message.ARMORSHRED2.send(damageTaker,inv[pick].getI18NDisplayName());

    }
}
