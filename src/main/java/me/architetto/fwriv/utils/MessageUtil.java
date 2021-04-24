package me.architetto.fwriv.utils;

import me.architetto.fwriv.localization.Message;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;

import java.util.Collections;

public class MessageUtil {

    public static String commandsInfo() {
        return  ChatColor.YELLOW + "[*]----------------[ " +
                ChatColor.DARK_AQUA + ChatColor.BOLD + "COMMANDS INFO" +
                ChatColor.YELLOW + " ]----------------[*]";
    }

    public static TextComponent joinCmponent() {
        return new TextComponent( new ComponentBuilder("[")
                .color(net.md_5.bungee.api.ChatColor.GRAY)
                .append("JOIN")
                .bold(true)
                .color(net.md_5.bungee.api.ChatColor.DARK_GREEN)
                .event(new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/fwriv join"))
                .event(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new Text(Message.COMP_EVENT_JOIN_HOVER.asString())))
                .append("]")
                .bold(false)
                .color(net.md_5.bungee.api.ChatColor.GRAY).create());
    }

    public static TextComponent startCmponent() {
        return new TextComponent( new ComponentBuilder("[")
                .color(net.md_5.bungee.api.ChatColor.GRAY)
                .append("START")
                .bold(true)
                .color(net.md_5.bungee.api.ChatColor.DARK_GREEN)
                .event(new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/fwriv start"))
                .event(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new Text(Message.COMP_EVENT_START_HOVER.asString())))
                .append("]")
                .bold(false)
                .color(net.md_5.bungee.api.ChatColor.GRAY).create());
    }

    public static TextComponent infoCmponent() {
        return new TextComponent( new ComponentBuilder("[")
                .color(net.md_5.bungee.api.ChatColor.GRAY)
                .append("INFO")
                .bold(true)
                .color(net.md_5.bungee.api.ChatColor.LIGHT_PURPLE)
                .event(new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/fwriv info"))
                .append("]")
                .bold(false)
                .color(net.md_5.bungee.api.ChatColor.GRAY).create());
    }

    public static TextComponent restartCmponent() {
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

    public static TextComponent stopCmponent() {
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
