package me.architetto.rivevent.command.subcommand.superuser;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.command.GlobalVar;
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

    int playerCount = 0;


    @Override
    public void perform(Player player, String[] args){

        if (!player.hasPermission("rivevent.stop")) {
            player.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        GlobalVar global = GlobalVar.getInstance();

        if (global.presetSummon.isEmpty()) {
            player.sendMessage(ChatMessages.RED(Messages.ERR_NO_EVENT));
        }else{


            if (!global.playerJoined.isEmpty()) {
                clearInventories(global.playerJoined);
            }

            List<UUID> mergedList = new ArrayList<>(global.playerJoined);
            mergedList.addAll(global.playerSpectate);
            global.clearVar();

            if (!mergedList.isEmpty()){
                tpBackToSpes(mergedList);
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

    public void tpBackToSpes (List<UUID> playerList) {

        GlobalVar global = GlobalVar.getInstance();

        new BukkitRunnable(){

            @Override
            public void run(){

                Player target = Bukkit.getPlayer(playerList.get(playerCount));
                assert target != null;
                target.teleport(global.respawnLoc);
                target.playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 3, 1);

                playerCount++;
                if (playerList.size() - 1 <= playerCount) {
                    playerCount = 0;
                    this.cancel();
                }
            }

        }.runTaskTimer(RIVevent.plugin,0L,20L);

    }



}
