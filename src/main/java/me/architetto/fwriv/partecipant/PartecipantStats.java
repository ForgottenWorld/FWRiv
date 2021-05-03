package me.architetto.fwriv.partecipant;

import org.bukkit.entity.Player;

import java.util.UUID;

public class PartecipantStats {

    private final String playerName;
    private final UUID uuid;

    private int towerRewards = 0,kills = 0, targetBlockReward = 0;
    private double damageDealt = 0, damageTaken = 0;

    public PartecipantStats(Player player) {
        this.playerName = player.getDisplayName();
        this.uuid = player.getUniqueId();
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getTargetBlockReward() {
        return targetBlockReward;
    }

    public void addTargetBlockReward() {
        this.targetBlockReward += 1;
    }

    public double getDamageDealt() {
        return damageDealt;
    }

    public void addDamageDealt(double damageDealt) {
        this.damageDealt += damageDealt;
    }

    public double getDamageTaken() {
        return damageTaken;
    }

    public void addDamageTaken(double damageTaken) {
        this.damageTaken += damageTaken;
    }

    public int getTowerRewards() {
        return towerRewards;
    }

    public void addTowerReward() {
        this.towerRewards += 1;
    }

    public int getKills() {
        return kills;
    }

    public void addKill() {
        kills += 1;
    }

    public void reset() {
        this.kills = 0;
        this.towerRewards = 0;
        this.targetBlockReward = 0;
        this.damageDealt = 0;
        this.damageTaken = 0;
    }


}
