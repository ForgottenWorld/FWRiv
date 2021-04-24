package me.architetto.fwriv.utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.Collections;

public class ChatFormatter{

    public static String pluginPrefix() {
        return  ChatColor.AQUA + "[" +
                ChatColor.DARK_AQUA + "FWriv" +
                ChatColor.AQUA + "] " +
                ChatColor.RESET;
    }

    public static String chatHeaderPresetInfo() {
        return  ChatColor.AQUA + "[*]----------------[ " +
                ChatColor.DARK_AQUA + ChatColor.BOLD + "ARENA INFO" +
                ChatColor.AQUA + " ]----------------[*]";
    }

    public static String chatHeaderGameplayInfo() {
        return  ChatColor.AQUA + "[*]----------------[ " +
                ChatColor.DARK_AQUA + ChatColor.BOLD + "GAMEPLAY INFO" +
                ChatColor.AQUA + " ]----------------[*]";
    }

    public static String chatFooter() {
        return  ChatColor.AQUA + String.join("", Collections.nCopies(53, "-"));
    }

    public static String formatSuccessMessage(String message) {
        message = ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "FWRiv >> " + ChatColor.RESET + message;
        return message;
    }

    public static String formatErrorMessage(String message) {
        message = ChatColor.DARK_RED + "" + ChatColor.BOLD + "FWRiv >> " + ChatColor.RESET + message;
        return message;
    }

    public static String formatInitializationMessage(String message) {
        message = ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "FWRiv >> " + ChatColor.RESET + message;
        return message;
    }

    public static String formatEventMessage(String message) {
        message = ChatColor.GOLD + "" + ChatColor.BOLD + "FWRiv >> " + ChatColor.RESET + message;
        return message;
    }

    public static String formatEventAllert(String message) {
        message = ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "FWRiv >> " + ChatColor.RESET + message;
        return message;
    }

}
