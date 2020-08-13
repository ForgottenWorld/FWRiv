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

import java.util.ArrayList;
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



    @Override
    public void perform(Player player, String[] args){

        if (!player.hasPermission("rivevent.stop")) {
            player.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        GameHandler global = GameHandler.getInstance();

        if (global.presetSummon.isEmpty()) {
            player.sendMessage(ChatMessages.RED(Messages.ERR_NO_EVENT));
        }else{


            if (!global.playerJoined.isEmpty()) {
                clearInventories(global.playerJoined);
            }

            List<UUID> mergedList = new ArrayList<>(global.playerJoined);
            mergedList.addAll(global.playerSpectate);
            global.clearEventVariables();

            if (!mergedList.isEmpty()){
                tpBackToConfigPosition(mergedList); //DA TESTARE
            }

            player.sendMessage(ChatMessages.GREEN(Messages.STOP_EVENT));

        }
    }

    public void clearInventories (List<UUID> playerJoined) {

        for (UUID key : playerJoined) {

            Player player = Bukkit.getPlayer(key);
            assert player != null;
            player.getInventory().clear();

        }

    }



    public void tpBackToConfigPosition(List<UUID> playerList) {

        GameHandler global = GameHandler.getInstance();

        new BukkitRunnable() {
            @Override
            public void run() {

                if (playerList.size() == 0) {
                    this.cancel();

                } else {
                    Player target = Bukkit.getPlayer(playerList.get(0));
                    assert target != null;
                    target.teleport(global.endEventRespawnLocation);
                    target.playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 2, 1);
                    playerList.remove(0);

                }
            }

        }.runTaskTimer(RIVevent.plugin,0L,20L);

    }


}
