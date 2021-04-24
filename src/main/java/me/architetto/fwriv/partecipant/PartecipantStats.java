package me.architetto.fwriv.partecipant;

public class PartecipantStats {

    private int rewards = 0,kills = 0, resurrection = 0;
    private double damageDealt = 0, damageTaken = 0;

    public PartecipantStats() {
    }

    public int getResurrection() {
        return resurrection;
    }

    public void setResurrection(int resurrection) {
        this.resurrection = resurrection;
    }

    public void addResurrection() {
        this.resurrection += 1;
    }

    public double getDamageDealt() {
        return damageDealt;
    }

    public void setDamageDealt(double damageDealt) {
        this.damageDealt = damageDealt;
    }

    public void addDamageDealt(double damageDealt) {
        this.damageDealt += damageDealt;
    }

    public double getDamageTaken() {
        return damageTaken;
    }

    public void setDamageTaken(double damageTaken) {
        this.damageTaken = damageTaken;
    }

    public void addDamageTaken(double damageTaken) {
        this.damageTaken += damageTaken;
    }

    public int getRewards() {
        return rewards;
    }

    public void setRewards(int rewards) {
        this.rewards = rewards;
    }

    public void addReward() {
        this.rewards += 1;
    }

    public int getKills() {
        return kills;
    }

    public void addKill() {
        kills += 1;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

}
