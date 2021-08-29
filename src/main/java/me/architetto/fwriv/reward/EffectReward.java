package me.architetto.fwriv.reward;

import me.architetto.fwriv.localization.Message;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.ThreadLocalRandom;

public class EffectReward extends Reward {

    private final PotionEffectType potionEffectType;
    private final int minDuration, maxDuration, maxAmplifier;


    public EffectReward(PotionEffectType potionEffectType, int minDuration, int maxDuration, int maxAmplifier) {
        this.potionEffectType = potionEffectType;
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
        this.maxAmplifier = maxAmplifier;
    }


    @Override
    public void give(Player player) {

        int amplifier = 1;
        int duration = this.minDuration;

        if (this.maxAmplifier != 1)
         amplifier = ThreadLocalRandom.current().nextInt(1, maxAmplifier);

        if (minDuration != maxDuration)
            duration = ThreadLocalRandom.current().nextInt(minDuration, maxDuration);

        new PotionEffect(potionEffectType, duration, amplifier).apply(player);
        Message.REWARD_OBTAINED.send(player, getName());

        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_DRINK,1,1);

    }

    @Override
    public String getName() {
        return StringUtils.replace(this.potionEffectType.getName().toUpperCase(), "_", " ") + " EFFECT";
    }
}
