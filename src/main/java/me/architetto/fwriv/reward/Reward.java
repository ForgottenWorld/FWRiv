package me.architetto.fwriv.reward;

import org.bukkit.entity.Player;

public abstract class Reward {

    public abstract void give(Player player);

    public abstract String getName();

}
