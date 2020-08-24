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
    public void perform(Player player, String[] args){

        if (!player.hasPermission("rivevent.stop")) {
            player.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        if (args.length == 2 && args[1].toLowerCase().equals("force")) {
            global.clearEventVariables();
            return;
        }

        if (global.presetSummon.isEmpty()){
            player.sendMessage(ChatMessages.RED(Messages.ERR_NO_EVENT));
            return;
        }

        if (!global.playerJoined.isEmpty()) {
            clearInventories(global.playerJoined);
        }

        if (!global.allPlayerList().isEmpty()){
            tpBackToConfigPosition(global.allPlayerList());
        }

        global.clearEventVariables();

        player.sendMessage(ChatMessages.GREEN(Messages.STOP_EVENT));

    }

    public void clearInventories (List<UUID> playerJoined) {

        for (UUID key : playerJoined) {

            Player player = Bukkit.getPlayer(key);
            assert player != null;
            player.getInventory().clear();

        }

    }

    public void tpBackToConfigPosition(List<UUID> playerList) {

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
