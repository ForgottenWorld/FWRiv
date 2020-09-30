package me.architetto.rivevent.command.subcommand.superuser;

import me.architetto.rivevent.command.GameHandler;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class EventInfoCommand extends SubCommand{
    @Override
    public String getName(){
        return "eventinfo";
    }

    @Override
    public String getDescription(){
        return "Info about current event.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent eventinfo";
    }

    GameHandler global = GameHandler.getInstance();

    @Override
    public void perform(Player sender, String[] args){

        if (!sender.hasPermission("rivevent.eventinfo")) {
            sender.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        if (global.presetSummon.isEmpty()){
            sender.sendMessage(ChatMessages.RED(Messages.ERR_NO_EVENT));
            return;
        }

        sender.sendTitle("", ChatColor.RED + "Partecipanti : " + ChatColor.RESET + global.playerJoined.size()
               + "  ||  " + ChatColor.AQUA + "Spettatori : " + ChatColor.RESET + global.playerSpectate.size(),0,100,0);

        sender.sendMessage(ChatMessages.GREEN("\n====== PARTECIPANTI EVENTO RIV ======\n" + ChatColor.GREEN
                + "GIOCATORI : " + ChatColor.RESET + getJoinedPlayerList() + ChatColor.AQUA + "\nSPETTATORI : "
                + ChatColor.RESET + getSpectatorPlayerList()) + "\n==================================");

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }

    public List<String> getJoinedPlayerList() {

        if (global.playerJoined.isEmpty())
            return null;

        List<String> playerNameList = new ArrayList<>();

        for (UUID u : global.playerJoined) {
            playerNameList.add(Objects.requireNonNull(Bukkit.getPlayer(u)).getName());
        }

        return playerNameList;
    }

    public List<String> getSpectatorPlayerList() {

        if (global.playerSpectate.isEmpty())
            return null;

        List<String> playerNameList = new ArrayList<>();

        for (UUID u : global.playerSpectate) {
            playerNameList.add(Objects.requireNonNull(Bukkit.getPlayer(u)).getName());
        }

        return playerNameList;
    }

}
