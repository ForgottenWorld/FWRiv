package me.architetto.fwriv.reward;

import me.architetto.fwriv.localization.Message;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.ThreadLocalRandom;

public class PotionReward extends Reward {

    private ItemStack itemStack;

    private final PotionEffectType potionEffectType;
    private final int minDuration, maxDuration, maxAmplifier;
    private final Color potionColor;

    public PotionReward(PotionEffectType potionEffectType, int minDuration, int maxDuration, int maxAmplifier, DyeColor color) {
        this.itemStack = new ItemStack(Material.SPLASH_POTION,1);
        this.potionEffectType = potionEffectType;
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
        this.maxAmplifier = maxAmplifier;
        this.potionColor = color.getColor();
    }


    @Override
    public void give(Player player) {

        int amplifier = 1;
        int duration = this.minDuration;

        if (this.maxAmplifier != 1)
            amplifier = ThreadLocalRandom.current().nextInt(1, maxAmplifier);

        if (minDuration != maxDuration)
            duration = ThreadLocalRandom.current().nextInt(minDuration, maxDuration);



        PotionMeta potionMeta = (PotionMeta) this.itemStack.getItemMeta();

        potionMeta.addCustomEffect(new PotionEffect(potionEffectType, duration, amplifier), true);
        potionMeta.setColor(this.potionColor);
        potionMeta.setDisplayName(ChatColor.YELLOW + this.potionEffectType.getName());
        this.itemStack.setItemMeta(potionMeta);

        player.getInventory().addItem(itemStack);
        Message.REWARD_OBTAINED.send(player, getName());
        player.playSound(player.getLocation(), Sound.BLOCK_BREWING_STAND_BREW,1,1);

    }

    @Override
    public String getName() {
        return StringUtils.capitalize(this.potionEffectType.getName().toLowerCase()) + " potion";
    }
}
