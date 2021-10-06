package me.architetto.fwriv.localization;

import me.architetto.fwriv.partecipant.PartecipantStatus;
import me.architetto.fwriv.partecipant.PartecipantsManager;
import me.architetto.fwriv.utils.MessageUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public enum Message {

    PREFIX("fwriv_prefix", false),

    CREATION_MODE_INFO("creation_mode_info", true),
    CREATION_MODE_STEP("creation_mode_step", true),
    CREATION_MODE_LOCATION("creation_mode_location", true),

    JOIN_STARTED_EVENT("join_started_event", true),
    JOIN_READY_EVENT("join_ready_event", true),

    START_MESSAGE("start_message", true),
    COUNTDOWN_START("countdown_start", true),

    PARTECIPANT_INFO("partecipants_info", true),

    REWARD_OBTAINED("reward_obtained", true),

    VICTORY_SUBTITLE("victory_subtitle",false),
    VICTORY_SERVER_BROADCAST("victory_server_broadcast",true),

    TARGETBLOCK_READY("targetblock_ready", true),
    TARGETBLOCK_COOLDOWN("targetblock_cooldown", true),

    PLAYER_DEATH1("player_death1", true),
    PLAYER_DEATH2("player_death2", true),

    FISHINGROD_1("fishingrod_1", true),
    FISHINGROD_2("fishingrod_2", true),

    PICKPOKET1("pickpoket1", true),
    PICKPOKET2("pickpoket2", true),
    PICKPOKET3("pickpoket3", true),

    ARMORSHRED1("armorshred1", true),
    ARMORSHRED2("armorshred2", true),
    ARMORSHRED3("armorshred3", true),


    ANTICAMPER_START("anticamper_start", true),
    ANTICAMPER_HEIGHT_CHANGE("anticamper_height_change", true),
    ANTICAMPER_MAX_HEIGHT("anticamper_max_height", true),

    PARTECIPANT_RESTART_MESSAGE("partecipant_restart_message",true),
    PARTECIPANT_LEAVE("partecipant_leave",true),

    SUCCESS_ARENA_CREATION("success_arena_creation", true),
    SUCCESS_ARENA_DELETED("success_arena_deleted", true),
    SUCCESS_CONFIG_RELOAD("success_config_reloaded", true),

    //ERROR

    ERR_PERMISSION("err_permission",true),
    ERR_SYNTAX("err_syntax",true),

    ERR_ARENA_NAME_UNAVAIBLE("err_arena_name_unavailable",true),
    ERR_ARENA_NAME_NOT_EXIST("err_arena_name_not_exist", true),
    ERR_CREATION_MODE("err_creation_mode",true),
    ERR_ARENA_CREATION("err_arena_creation", true),

    ERR_EVENT_IS_RUNNING("err_event_is_running", true),
    ERR_NO_EVENT_IS_RUNNING("err_no_event_is_running", true),
    ERR_EVENT_NOT_READY("err_event_not_ready", true),

    ERR_NOT_ENOUGH_PLAYERS("err_not_enough_players", true),
    ERR_ALREADY_JOINED("err_already_joined", true),
    ERR_NO_EVENT_JOINED("err_no_event_joined", true),
    ERR_ECHELON_MUTEXACTIVITY("err_echelon_mutexactivity", true),

    ERR_INVENTORY_FULL("err_inventory_full",true),
    ERR_UNIQUE_REWARD("err_unique_reward", true),

    ERR_RESTART_DONE("err_restart_done", true),

    ERR_TPDISABLED("err_tpdisabled", true),

    //STATS
    STATS_KILLS("stats_kills",false),
    STATS_DAMAGEDONE("stats_damagedone", false),
    STATS_DAMAGETAKEN("stats_damagetaken", false),
    STATS_TOWERREWARDS("stats_towerrewards", false),
    STATS_PICKPOKET("stats_pickpoket", false),

    //BROADCAST
    BROADCAST_PLAYERJOINEVENT("broadcast_playerjoinevent", true),
    BROADCAST_PLAYERLEAVEEVENT("broadcast_playerleaveevent", true),
    BROADCAST_EVENT_ENDED("broadcast_event_ended",true),

    //COMPONENT
    COMP_EVENT_JOIN("comp_event_join", true),
    COMP_EVENT_START("comp_event_start", true),
    COMP_EVENT_ENDED_BROADCAST("comp_event_ended_broadcast", true),
    COMP_ARENA_POINTS("comp_arenacmd_points", true),

    //COMMAND DESCRIPTION
    INIT_COMMAND("init_command", false),
    JOIN_COMMAND("join_command", false),
    RESTART_COMMAND("restart_command", false),
    START_COMMAND("start_command", false),
    DELETE_COMMAND("delete_command", false),
    RELOAD_COMMAND("reload_command", false),
    STOP_COMMAND("stop_command",false),
    ARENA_COMMAND("arena_command", false),
    LEAVE_COMMAND("leave_command",false),
    GAME_COMMAND("game_command",false),
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

    public void broadcastWithPermission(String permission, Object... objects) {
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
                if (objects.length >= j + 1) {
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

    public void broadcastComponent(String permission, Object... objects) {
        TextComponent txt = new TextComponent(asComponent(objects));
        Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.hasPermission(permission))
                .forEach(p -> p.sendMessage(txt));
    }

    public void specialBroadcastComponent(Object... objects) {
        TextComponent txt = new TextComponent(asComponent(objects));
        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            player.sendMessage(txt);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1 ,1);
        });
    }

    public void sendSpecialComponent(Player sender, Object... objects) {
        sender.sendMessage(asComponent(objects));
        sender.playSound(sender.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1 ,1);
    }

    public void sendToPartecipants(Object...objects) {
        String msg = asString(objects);
        PartecipantsManager.getInstance().getPartecipantsUUID(PartecipantStatus.ALL).stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .filter(Player::isOnline)
                .forEach(player -> player.sendMessage(msg));
    }
}
