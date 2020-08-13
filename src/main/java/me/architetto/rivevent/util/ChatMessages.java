package me.architetto.rivevent.util;

import org.bukkit.ChatColor;

public class ChatMessages{

    public static String RED(String message) {
        return ChatColor.RED + "[RIVe] " + ChatColor.RESET + message;
    }

    public static String GREEN(String message) {
        return ChatColor.DARK_GREEN + "[RIVe] " + ChatColor.RESET + message;
    }

    public static String GOLD(String message) {
        return ChatColor.GOLD + "[RIVe] " + ChatColor.RESET + message;
    }

    public static String AQUA(String message) {
        return ChatColor.AQUA + "[RIVe] " + ChatColor.RESET + message;
    }

    public static String RIVallert(String message) { return ChatColor.LIGHT_PURPLE + "[RIVe]" + ChatColor.RESET
            + ChatColor.ITALIC + ChatColor.AQUA + message; }

    public static String PosMessage(String message, Enum LOC) {

        return ChatColor.GOLD + "["+ message + "] " + ChatColor.RESET + "Inserisci la posizione di " + ChatColor.DARK_AQUA
                + ChatColor.ITALIC +  LOC + ChatColor.RESET + " ...";
    }

}
