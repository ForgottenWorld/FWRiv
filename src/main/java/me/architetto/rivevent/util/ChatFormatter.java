package me.architetto.rivevent.util;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.Collections;

public class ChatFormatter{

    public static String pluginPrefix() {
        return  ChatColor.AQUA + "[" +
                ChatColor.DARK_AQUA + "RIVevent" +
                ChatColor.AQUA + "] " +
                ChatColor.RESET;
    }


    public static String chatHeaderPresetInfo() {
        return  ChatColor.AQUA + ">>>---------------[ " +
                ChatColor.DARK_AQUA + ChatColor.BOLD + "PRESET INFO" +
                ChatColor.AQUA + " ]---------------<<<";
    }

    public static String chatFooter() {
        return  ChatColor.AQUA + String.join("", Collections.nCopies(53, "-"));
    }

    public static String formatSuccessMessage(String message) {
        message = ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "RIVe >> " + ChatColor.RESET + message;
        return message;
    }

    public static String formatErrorMessage(String message) {
        message = ChatColor.DARK_RED + "" + ChatColor.BOLD + "RIVe >> " + ChatColor.RESET + message;
        return message;
    }

    public static String formatInitializationMessage(String message) {
        message = ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "RIVe >> " + ChatColor.RESET + message;
        return message;
    }

    public static String formatEventMessage(String message) {
        message = ChatColor.GOLD + "" + ChatColor.BOLD + "RIVe >> " + ChatColor.RESET + message;
        return message;
    }

    public static String formatEventAllert(String message) {
        message = ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "RIVe >> " + ChatColor.RESET + message;
        return message;
    }

    public static  String formatPresetName(String arenaName) {
        return ChatColor.AQUA + "" + ChatColor.BOLD + "PRESET NAME" + " >> " + ChatColor.RESET + arenaName;
    }

    public static String formatLocation(String pointName, Location loc) {

        return ChatColor.AQUA + "" + ChatColor.BOLD + pointName + " >> " + ChatColor.RESET +
                ChatColor.YELLOW + "" + ChatColor.BOLD + "WORLD : " + ChatColor.RESET + loc.getWorld().getName() +
                ChatColor.YELLOW + "" + ChatColor.BOLD + " X : " + ChatColor.RESET + loc.getBlockX() +
                ChatColor.YELLOW + "" + ChatColor.BOLD + " Z : " + ChatColor.RESET + loc.getBlockZ() +
                ChatColor.YELLOW + "" + ChatColor.BOLD + " Y : " + ChatColor.RESET + loc.getBlockY();
    }

}
