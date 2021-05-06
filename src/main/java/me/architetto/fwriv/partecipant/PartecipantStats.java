package me.architetto.fwriv.partecipant;

import org.bukkit.entity.Player;

import java.util.UUID;

@SuppressWarnings("unused")
public class PartecipantStats {

    private final String playerName;
    private final UUID uuid;

    private int towerRewards = 0,kills = 0, pickpocket = 0;
    private double damageDealt = 0, damageTaken = 0;

    public PartecipantStats(Player player) {
        this.playerName = player.getDisplayName();
        this.uuid = player.getUniqueId();
    }

    public int getPickpocket() {
        return pickpocket;
    }

    public void addPickpocket() {
        this.pickpocket += 1;
    }

    public String getPlayerName() {
        return playerName;
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

    public UUID getUUID() {
        return uuid;
    }

    public void reset() {
        this.kills = 0;
        this.towerRewards = 0;
        this.damageDealt = 0;
        this.damageTaken = 0;
        this.pickpocket = 0;
    }


}
