package me.architetto.rivevent.command;

import me.architetto.rivevent.RIVevent;

public class SettingsHandler{

    private static SettingsHandler instance;

    public int doorsDetectorRange;
    public int openDoorsDelay;
    public int closeDoorsDelay;



    public boolean rewardPlayersOnTopToggle;
    public long rewardPlayerPeriod;
    public int rewardMinPlayer;

    public boolean antiCamperToggle;
    public long antiCamperStartDelay;
    public long antiCamperPeriod;
    public int antiCamperDamage;
    public int antiCamperMaxY;
    public int antiCamperRedLineGrowPeriod;
    public int antiCamperRedLineGrowValue;

    public int minFoodLevel;
    public int resetFoodLevel;

    public boolean snowballDamageToggle;
    public boolean snowballKnockbackToggle;
    public double snowballKnockbackPower;
    public double snowballHitDamage;

    public long deathRacePeriod;

    private SettingsHandler(){

    }

    public static SettingsHandler getInstance(){
        if(instance==null)
            instance = new SettingsHandler();

        return instance;
    }

    public void load(){

        doorsDetectorRange = RIVevent.plugin.getConfig().getInt("DOORS_RADIUS_DETECTOR",5);
        openDoorsDelay = RIVevent.plugin.getConfig().getInt("OPEN_DOORS_DELAY",5) * 20;
        closeDoorsDelay = RIVevent.plugin.getConfig().getInt("CLOSE_DOORS_DELAY",15) * 20 + openDoorsDelay;

        antiCamperToggle = RIVevent.plugin.getConfig().getBoolean("ANTI_CAMPER_TOGGLE",true);
        antiCamperStartDelay = RIVevent.plugin.getConfig().getLong("AC_DELAY",120) * 20;
        antiCamperPeriod = RIVevent.plugin.getConfig().getLong("AC_DAMAGE_PERIOD",2) * 20;
        antiCamperDamage = Math.abs(RIVevent.plugin.getConfig().getInt("AC_DAMAGE",1));
        antiCamperMaxY = RIVevent.plugin.getConfig().getInt("AC_MIN_REDLINE_DIFFERENCE",4);
        antiCamperRedLineGrowPeriod = RIVevent.plugin.getConfig().getInt("RED_LINE_GROW_PERIOD",60) * 20;
        antiCamperRedLineGrowValue = RIVevent.plugin.getConfig().getInt("RED_LINE_GROW_VALUE",1);

        rewardPlayersOnTopToggle = RIVevent.plugin.getConfig().getBoolean("REWARD_PLAYER_ON_TOP",true);
        rewardPlayerPeriod = RIVevent.plugin.getConfig().getLong("REWARD_PERIOD") * 20;
        rewardMinPlayer = RIVevent.plugin.getConfig().getInt("REWARD_MIN_PLAYERS");

        minFoodLevel = RIVevent.plugin.getConfig().getInt("MIN_FOOD_LEVEL",15);
        resetFoodLevel = RIVevent.plugin.getConfig().getInt("RESET_FOOD_LEVEL",20);

        snowballDamageToggle = RIVevent.plugin.getConfig().getBoolean("SNOWBALL_DOES_DAMAGE",true);
        snowballKnockbackToggle = RIVevent.plugin.getConfig().getBoolean("SNOWBALL_DOES_KNOCKBACK",true);
        snowballKnockbackPower = RIVevent.plugin.getConfig().getDouble("SNOWBALL_KNOCKBACK_POWER",1);
        snowballHitDamage = RIVevent.plugin.getConfig().getDouble("SNOWBALL_DAMAGE",0.1);

        deathRacePeriod = RIVevent.plugin.getConfig().getLong("DEATH_RACE_PERIOD",120) * 20;

    }

}
