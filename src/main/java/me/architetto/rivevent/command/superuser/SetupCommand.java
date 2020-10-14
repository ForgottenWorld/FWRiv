package me.architetto.rivevent.command.superuser;

import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.config.SettingsHandler;
import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.util.ChatFormatter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class SetupCommand extends SubCommand{
    @Override
    public String getName(){
        return "setup";
    }

    @Override
    public String getDescription(){
        return "Teleport players in spawns position.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent setup";
    }

    @Override
    public void perform(Player sender, String[] args){

        if (!sender.hasPermission("rivevent.setup")) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Permission Error: you do not have permission to use that command"));
            return;
        }

        EventService eventService = EventService.getInstance();

        if (!eventService.isRunning()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Error: no event in progress"));
            return;
        }
        if (eventService.isSetupDone()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Error: setup already done"));
            return;
        }

        if (eventService.getParticipantsPlayers().isEmpty()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Error: insufficient players to start event"));
            return;
        }

        eventService.setDoorsStatus(false);

        int spawnIndex = 1;

        for (UUID u : Collections.unmodifiableList(eventService.getParticipantsPlayers())) {
            Player p = Bukkit.getPlayer(u);

            if (p == null)
                return;

            switch(spawnIndex) {
                case 1:
                    teleportEvent(p,eventService.getSummonedArena().getSpawn1());
                    spawnIndex++;
                    break;
                case 2:
                    teleportEvent(p,eventService.getSummonedArena().getSpawn2());
                    spawnIndex++;
                    break;
                case 3:
                    teleportEvent(p,eventService.getSummonedArena().getSpawn3());
                    spawnIndex++;
                    break;
                case 4:
                    teleportEvent(p,eventService.getSummonedArena().getSpawn4());
                    spawnIndex = 1;
            }
        }

        eventService.setSetupDone(true);

        TextComponent startCmd = new TextComponent(ChatColor.YELLOW + "" + ChatColor.BOLD + "HERE" );
        startCmd.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rivevent start"));
        startCmd.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click here to start event")));


        sender.sendMessage(new TextComponent(ChatFormatter.formatInitializationMessage("setup done. To start click "))
                ,startCmd,new TextComponent(" or use " + ChatColor.AQUA + "/rivevent start"));


    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }

    public void teleportEvent(Player p, Location loc) {
        SettingsHandler settingsHandler = SettingsHandler.getInstance();

        p.teleport(loc);
        p.playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT,1,1);

        p.getInventory().clear();
        p.setFireTicks(0);
        p.setFoodLevel(20);
        p.setHealth(20);

        for (Material material : settingsHandler.startEquipItems.keySet()) {

            p.getInventory().addItem(new ItemStack(material,settingsHandler.startEquipItems.get(material)));

        }

        for (PotionEffect effect : p.getActivePotionEffects())
            p.removePotionEffect(effect.getType());
    }

}






