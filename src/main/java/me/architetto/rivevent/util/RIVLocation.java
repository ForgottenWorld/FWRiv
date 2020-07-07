package me.architetto.rivevent.util;

import org.bukkit.ChatColor;
import org.bukkit.Location;

public class RIVLocation{

    public static String getStringFromLocation(Location loc) {
        if (loc == null) {
            return "";
        }
        return "\n" + ChatColor.GOLD + "Mondo : " + ChatColor.RESET + loc.getWorld().getName() +
                "\n" + ChatColor.GREEN + "X : " + ChatColor.RESET + loc.getX() +
                "\n" + ChatColor.GREEN + "Y : " + ChatColor.RESET + loc.getY() +
                "\n" + ChatColor.GREEN + "Z : " + ChatColor.RESET + loc.getZ() ;
    }


}
