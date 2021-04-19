package me.architetto.fwriv.reward;

import me.architetto.fwriv.localization.LocalizationManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ItemReward extends Reward {

    private final ItemStack itemStack;
    private final int maxAmount;
    private final boolean isUnique;

    public ItemReward(Material material, int maxAmount, boolean isUnique) {
        this.itemStack = new ItemStack(material, 1);

        List<String> lore = LocalizationManager.getInstance().localizeItemLore(material);
        if (lore != null)
            this.itemStack.setLore(lore);

        this.maxAmount = maxAmount;
        this.isUnique = isUnique;
    }

    @Override
    public void give(Player player) {

        if (isUnique && player.getInventory().contains(itemStack.getType()))
            return; //todo: aggiungere messaggio

        if (player.getInventory().firstEmpty() == -1)
            return; //todo: message

        if (maxAmount != 1)
            itemStack.setAmount(Math.min(itemStack.getMaxStackSize(), ThreadLocalRandom.current().nextInt(1,maxAmount)));

        player.getInventory().addItem(itemStack);

        player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC,1,1);


    }

    @Override
    public String getName() {
        return itemStack.getI18NDisplayName();
    }
}
