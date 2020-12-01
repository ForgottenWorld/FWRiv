package me.architetto.fwriv.reward;

import org.bukkit.Material;

import java.security.SecureRandom;

public class ItemReward{

    private Material materialReward;

    private Integer rewardMaxAmount;
    private Double rewardWeight;

    private boolean unique;

    public ItemReward(Material materialReward, Double rewardWeight, Integer rewardMaxAmount, boolean unique) {

        this.materialReward = materialReward;
        this.rewardWeight = rewardWeight;
        this.rewardMaxAmount = rewardMaxAmount;

        this.unique = unique;
    }

    public Material item() { return this.materialReward;}

    public Double weight() { return this.rewardWeight; }

    public Integer amount() {

        if (rewardMaxAmount == 1)
            return 1;

        return new SecureRandom().nextInt(rewardMaxAmount) + 1;
    }

    public boolean isUnique() { return this.unique; }

}
