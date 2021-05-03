package me.architetto.fwriv.utils;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;

import java.util.Collections;

public class MessageUtil {

    public static String commandsInfo() {
        return  ChatColor.YELLOW + "[*]----------------[ " +
                ChatColor.DARK_AQUA + ChatColor.BOLD + "COMMANDS INFO" +
                ChatColor.YELLOW + " ]----------------[*]";
    }

    public static TextComponent joinComponent() {
        return new TextComponent( new ComponentBuilder("[")
                .color(net.md_5.bungee.api.ChatColor.GRAY)
                .append("JOIN")
                .bold(true)
                .color(net.md_5.bungee.api.ChatColor.DARK_GREEN)
                .event(new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/fwriv join"))
                .append("]")
                .bold(false)
                .color(net.md_5.bungee.api.ChatColor.GRAY).create());
    }

    public static TextComponent startComponent() {
        return new TextComponent( new ComponentBuilder("[")
                .color(net.md_5.bungee.api.ChatColor.GRAY)
                .append("START")
                .bold(true)
                .color(net.md_5.bungee.api.ChatColor.DARK_GREEN)
                .event(new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/fwriv start"))
                .append("]")
                .bold(false)
                .color(net.md_5.bungee.api.ChatColor.GRAY).create());
    }

    public static TextComponent infoComponent() {
        return new TextComponent( new ComponentBuilder("[")
                .color(net.md_5.bungee.api.ChatColor.GRAY)
                .append("INFO")
                .bold(true)
                .color(net.md_5.bungee.api.ChatColor.LIGHT_PURPLE)
                .event(new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/fwriv gameplay"))
                .append("]")
                .bold(false)
                .color(net.md_5.bungee.api.ChatColor.GRAY).create());
    }

    public static TextComponent restartComponent() {
        return new TextComponent( new ComponentBuilder("[")
                .color(net.md_5.bungee.api.ChatColor.GRAY)
                .append("RESTART")
                .bold(true)
                .color(net.md_5.bungee.api.ChatColor.DARK_GREEN)
                .event(new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/fwriv restart"))
                .append("]")
                .bold(false)
                .color(net.md_5.bungee.api.ChatColor.GRAY).create());
    }

    public static TextComponent stopComponent() {
        return new TextComponent( new ComponentBuilder("[")
                .color(net.md_5.bungee.api.ChatColor.GRAY)
                .append("STOP")
                .bold(true)
                .color(net.md_5.bungee.api.ChatColor.RED)
                .event(new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/fwriv stop"))
                .append("]")
                .bold(false)
                .color(net.md_5.bungee.api.ChatColor.GRAY).create());
    }

    public static String chatFooter() {
        return  ChatColor.YELLOW + String.join("",
                Collections.nCopies(53, "-"));
    }

    public static String formatListMessage(String message) {
        message = ChatColor.GRAY + "[] " + ChatColor.RESET + message;
        return message;
    }

    public static String rewritePlaceholders(String input) {
        int i = 0;
        while (input.contains("{}")) {
            input = input.replaceFirst("\\{}", "{" + i++ + "}");
        }
        return input;
    }

}
