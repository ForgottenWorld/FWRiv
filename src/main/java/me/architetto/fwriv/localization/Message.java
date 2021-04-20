package me.architetto.fwriv.localization;

import com.sun.org.apache.bcel.internal.generic.FALOAD;
import me.architetto.fwriv.utils.MessageUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.command.CommandSender;

public enum Message {

    PREFIX("fwriv_prefix", false),

    CREATION_MODE_INFO("creation_mode_info", true),
    CREATION_MODE_STEP("creation_mode_step", true),

    JOIN_STARTED_EVENT("join_started_event", true),
    JOIN_READY_EVENT("join_ready_event", true),

    SUCCESS_ARENA_CREATION("success_arena_creation", true),
    SUCCESS_ARENA_DELETED("success_arena_deleted", true),
    SUCCESS_CONFIG_RELOAD("success_config_reloaded", true),

    ERR_PERMISSION("err_permission",true),
    ERR_SYNTAX("err_syntax",true),

    ERR_ARENA_NAME_UNAVAIBLE("err_arena_name_unavailable",true),
    ERR_ARENA_NAME_NOT_EXIST("err_arena_name_not_exist", true),
    ERR_CREATION_MODE("err_creation_mode",true),
    ERR_ARENA_CREATION("err_arena_creation", true),

    ERR_EVENT_IS_RUNNING("err_event_is_running", true),
    ERR_NO_EVENT_IS_RUNNING("err_no_event_is_running", true),

    //BROADCAST
    BROADCAST_PLAYERJOINEVENT("broadcast_playerjoinevent", true),

    //COMPONENT
    COMP_EVENT_JOIN("comp_event_join", true),
    COMP_EVENT_JOIN_HOVER("comp_event_join_hover", false),
    COMP_EVENT_STARTSTOP("comp_event_startstop", true),
    COMP_EVENT_START_HOVER("comp_event_start_hover", false),
    COMP_EVENT_STOP_HOVER("comp_event_stop_hover", false),



    //COMMAND DESCRIPTION
    INIT_COMMAND("init_command", false),
    JOIN_COMMAND("join_command", false),
    RESTART_COMMAND("restart_command", false),
    DELETE_COMMAND("delete_command", false),
    RELOAD_COMMAND("reload_command", false),
    CREATE_COMMAND("create_command", false);

    private final String message;
    private final boolean showPrefix;
    private final LocalizationManager localizationManager;

    Message(String message, boolean showPrefix) {
        this.message = MessageUtil.rewritePlaceholders(message);
        this.showPrefix = showPrefix;
        this.localizationManager = LocalizationManager.getInstance();
    }

    public void send(CommandSender sender, Object... objects) {
        sender.sendMessage(asString(objects));
    }

    public void broadcast(Object... objects) {
        Bukkit.getServer().broadcastMessage(asString(objects));
    }

    public void broadcast(String permission, Object... objects) {
        Bukkit.getServer().broadcast(asString(objects),permission);
    }

    public String asString(Object... objects) {
        return format(objects);
    }

    private String format(Object... objects) {
        String string = localizationManager.localizeString(this.message);
        if(this.showPrefix) {
            string = localizationManager.localizeString(PREFIX.message) + " " + string;
        }

        for (int i = 0; i < objects.length; i++) {
            Object o = objects[i];
            string = string.replace("{" + i + "}", String.valueOf(o));
        }
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public void sendComponent(CommandSender sender, Object... objects) {
        sender.sendMessage(asComponent(objects));
    }

    public BaseComponent asComponent(Object... objects) {
        return formatComponent(objects);
    }

    private BaseComponent formatComponent(Object... objects) {

        String[] parts = localizationManager.localizeString(this.message).split("§");

        TextComponent textComponent = new TextComponent();

        if(this.showPrefix)
            textComponent.addExtra(ChatColor.translateAlternateColorCodes('&',
                    localizationManager.localizeString(PREFIX.message)));

        int j = 0;

        for (String part : parts) {
            if (part.matches("^\\*$")) {
                if (objects.length <= j + 1) {
                    if (objects[j] instanceof BaseComponent)
                        textComponent.addExtra((BaseComponent) objects[j]);
                    else
                        textComponent.addExtra(String.valueOf(objects[j]));
                    j++;
                } else
                    textComponent.addExtra(part);
            } else
                textComponent.addExtra(ChatColor.translateAlternateColorCodes('&',
                        part));
        }

        return textComponent;

    }

    public void broadcastComponent(Object... objects) {
        Bukkit.broadcast(asComponent(objects));
    }

    public void specialBroadcastComponent(Object... objects) {
        BaseComponent baseComponent = asComponent(objects);
        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            player.sendMessage(baseComponent);
            player.playNote(player.getLocation(), Instrument.BANJO, Note.flat(0, Note.Tone.A));
        });
    }
}
