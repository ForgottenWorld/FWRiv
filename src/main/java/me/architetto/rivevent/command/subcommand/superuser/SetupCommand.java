package me.architetto.rivevent.command.subcommand.superuser;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.command.GameHandler;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.listener.LeftclickListener;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.LocSerialization;
import me.architetto.rivevent.util.Messages;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
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


    int doorsRadiusDetector = RIVevent.getDefaultConfig().getInt("DOORS_RADIUS_DETECTOR");
    GameHandler global = GameHandler.getInstance();

    @Override
    public void perform(Player player, String[] args){

        if (!player.hasPermission("rivevent.setup")) {
            player.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        if (global.setupStartFlag) {
            player.sendMessage(ChatMessages.RED(Messages.ERR_SETUP_DONE));
            return;
        }

        if (global.presetSummon.isEmpty()) {
            player.sendMessage(ChatMessages.RED(Messages.ERR_NO_EVENT));
            return;
        }

        if (global.playerJoined.isEmpty()) {
            player.sendMessage(ChatMessages.RED("Impossibile avviare il setup, nessun player sta' partecipando all'evento !")); //TODO
            return;
        }

        global.setupStartFlag = true;

        doorDetector(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LeftclickListener.LOC.SPAWN1)), doorsRadiusDetector);
        doorDetector(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LeftclickListener.LOC.SPAWN2)), doorsRadiusDetector);
        doorDetector(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LeftclickListener.LOC.SPAWN3)), doorsRadiusDetector);
        doorDetector(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LeftclickListener.LOC.SPAWN4)), doorsRadiusDetector);

        playersSpawnSetup(player);

    }


    public void doorDetector(Location loc,int radius) {

        Block middle = loc.getBlock();
        for (int x = radius; x >= -radius; x--) {
            for (int y = radius; y >= -radius; y--){
                for(int z = radius; z >= -radius; z--){
                    if (Tag.DOORS.getValues().contains(middle.getRelative(x, y, z).getType())
                            ||Tag.FENCE_GATES.getValues().contains(middle.getRelative(x, y, z).getType())){

                        Block block = middle.getRelative(x, y, z).getLocation().getBlock();

                        global.doorsToOpen.add(block);

                    }
                }
            }
        }
    }

    public void chooseSpawnPoint(Player target, int spawnNum) {

        switch(spawnNum) {
            case 1:
                target.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LeftclickListener.LOC.SPAWN1)));
                target.playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,2,1);
                return;
            case 2:
                target.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LeftclickListener.LOC.SPAWN2)));
                target.playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,2,1);
                return;
            case 3:
                target.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LeftclickListener.LOC.SPAWN3)));
                target.playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,2,1);
                return;
            case 4:
                target.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LeftclickListener.LOC.SPAWN4)));
                target.playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,2,1);
        }


    }

    public void playersSpawnSetup(Player player) {

        new BukkitRunnable() {

            private final List<UUID> playerJoinedCopy = new ArrayList<>(global.playerJoined);
            private int spawnPointIndex = 1;


            @Override
            public void run(){

                Player target = Bukkit.getPlayer(playerJoinedCopy.get(0));
                assert target != null;
                target.getInventory().clear();

                chooseSpawnPoint(target,spawnPointIndex);
                equipLoadout(target);

                playerJoinedCopy.remove(0);
                spawnPointIndex++;

                if (playerJoinedCopy.isEmpty()) {
                    player.sendMessage(ChatMessages.GREEN(Messages.OK_SETUP));
                    global.setupDoneFlag = true;
                    this.cancel();
                }

                if (spawnPointIndex == 5)
                    spawnPointIndex = 1;

            }
        }.runTaskTimer(RIVevent.plugin,0L,20L);

    }

    public void equipLoadout(Player target) {

        for (Material material : global.startLoadOut.keySet()) {

            target.getInventory().addItem(new ItemStack(material,global.startLoadOut.get(material)));

        }

    }


}






