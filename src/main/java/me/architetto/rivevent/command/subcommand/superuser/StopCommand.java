package me.architetto.rivevent.command.subcommand.superuser;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.command.GameHandler;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

public class StopCommand extends SubCommand{
    @Override
    public String getName(){
        return "stop";
    }

    @Override
    public String getDescription(){
        return "Cancel the current event.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent stop";
    }

    GameHandler global = GameHandler.getInstance();


    @Override
    public void perform(Player sender, String[] args){

        if (!sender.hasPermission("rivevent.stop")) {
            sender.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        if (args.length == 2 && args[1].toLowerCase().equals("force")) {
            global.resetEventVariables();
            Bukkit.getScheduler().cancelTasks(RIVevent.plugin);
            return;
        }

        if (global.presetSummon.isEmpty()){
            sender.sendMessage(ChatMessages.RED(Messages.ERR_NO_EVENT));
            return;
        }


        if (global.allPlayerList().isEmpty()){

            global.resetEventVariables();

            Bukkit.getScheduler().cancelTasks(RIVevent.plugin);

            sender.sendMessage(ChatMessages.GREEN(Messages.STOP_EVENT));

            return;

        }

        sender.sendMessage(ChatMessages.GREEN(Messages.STOP_CMD_TP_EVENT));

        if (!global.playerJoined.isEmpty())
            clearJoinedPlayerInventories();

        teleportPlayersToSpawn(global.allPlayerList(),sender);



    }

    public void clearJoinedPlayerInventories() {

        for (UUID key : global.playerJoined) {

            Player p = Bukkit.getPlayer(key);

            if (p != null)
                p.getInventory().clear();

        }

    }

    public void teleportPlayersToSpawn(List<UUID> playerList, Player sender) {

        new BukkitRunnable() {
            @Override
            public void run() {

                if (playerList.size() == 0) {

                    global.resetEventVariables();
                    sender.sendMessage(ChatMessages.GREEN(Messages.STOP_EVENT));
                    Bukkit.getScheduler().cancelTasks(RIVevent.plugin);

                } else {

                    Player target = Bukkit.getPlayer(playerList.get(0));

                    if (target != null){
                        target.teleport(global.endEventRespawnLocation);
                        target.playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 2, 1);
                    }

                    playerList.remove(0);

                }
            }

        }.runTaskTimer(RIVevent.plugin,0L,20L);

    }


}
