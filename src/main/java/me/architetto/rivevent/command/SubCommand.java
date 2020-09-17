package me.architetto.rivevent.command;

import org.bukkit.entity.Player;

public abstract class SubCommand {

    //name of the command
    public abstract String getName();

    
    public abstract String getDescription();

    //How to use command ex. /rivevent create <preset_name>
    public abstract String getSyntax();

    //code for the subcommand
    public abstract void perform(Player sender, String[] args);

}
