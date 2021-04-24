package me.architetto.fwriv.reward;

import me.architetto.fwriv.localization.LocalizationManager;
import me.architetto.fwriv.localization.Message;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ItemReward extends Reward {

    private final ItemStack itemStack;
    private final int maxAmount;
    private final boolean isUnique;

    private List<String> lore;

    public ItemReward(Material material, int maxAmount, boolean isUnique) {

        this.itemStack = new ItemStack(material, 1);

        this.lore = LocalizationManager.getInstance().localizeItemLore(material);

        if (lore != null)
            this.itemStack.setLore(lore);

        this.maxAmount = maxAmount;
        this.isUnique = isUnique;
    }

    @Override
    public void give(Player player) {

        if (isUnique && player.getInventory().contains(itemStack.getType())) {
            Message.ERR_UNIQUE_REWARD.send(player);
            return;
        }

        if (player.getInventory().firstEmpty() == -1) {
            Message.ERR_INVENTORY_FULL.send(player);
            return;
        }

        if (maxAmount != 1)
            itemStack.setAmount(Math.min(itemStack.getMaxStackSize(), ThreadLocalRandom.current().nextInt(1,maxAmount)));

        player.getInventory().addItem(itemStack);

        player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC,1,1);


    }

    @Override
    public String getName() {
        return ChatColor.GOLD + itemStack.getI18NDisplayName();
    }
}
