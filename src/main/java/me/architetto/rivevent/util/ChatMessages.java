package me.architetto.rivevent.util;

import org.bukkit.ChatColor;

public class ChatMessages{

    public static String pluginGreenPrefix() {
        return  ChatColor.DARK_GREEN + "[RIVe] " +
                ChatColor.RESET;
    }

    public static String pluginRedPrefix() {
        return  ChatColor.RED + "[RIVe] " +
                ChatColor.RESET;
    }

    public static String pluginAquaPrefix() {
        return  ChatColor.AQUA + "[RIVe] " +
                ChatColor.RESET;
    }

    public static String pluginGoldPrefix() {
        return  ChatColor.GOLD + "[RIVe] " +
                ChatColor.RESET;
    }

    public static String pluginGrayPrefix() {
        return  ChatColor.GRAY + "[RIVe] " +
                ChatColor.RESET;
    }


    public static String RED(String message) {
        message = pluginRedPrefix () + message;
        return message;
    }

    public static String GREEN(String message) {
        message = pluginGreenPrefix() + message;
        return message;
    }

    public static String PosMessage(String message, Enum LOC) {
        message = ChatColor.GOLD + "["+ message + "] " + ChatColor.RESET + "Inserisci la posizione di " + ChatColor.DARK_AQUA
                + ChatColor.ITALIC +  LOC + ChatColor.RESET + " ...";
        return message;
    }




}
