package me.architetto.rivevent.command.subcommand.superuser;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.command.GameHandler;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.listener.LeftclickListener;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.LocSerialization;
import me.architetto.rivevent.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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
    public void perform(Player player, String[] args){

        if (!player.hasPermission("rivevent.setup")) {
            player.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        GameHandler global = GameHandler.getInstance();

        if (global.setupStart) {
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

        global.setupStart = true;

        int doorsRadius = RIVevent.getDefaultConfig().getInt("DOORS_RADIUS_DETECTOR");

        doorDetector(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LeftclickListener.LOC.SPAWN1)),doorsRadius);
        doorDetector(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LeftclickListener.LOC.SPAWN2)),doorsRadius);
        doorDetector(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LeftclickListener.LOC.SPAWN3)),doorsRadius);
        doorDetector(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LeftclickListener.LOC.SPAWN4)),doorsRadius);



        new BukkitRunnable(){

            private int playerCount = 0;
            private int spawnNum = 1;

            @Override
            public void run(){

                Player target = Bukkit.getPlayer(global.playerJoined.get(playerCount));

                if (spawnNum == 5)
                    spawnNum = 1;

                assert target != null;
                target.getInventory().clear();

                eventSpawnPointTeleport(target,spawnNum);

                playerCount++;
                spawnNum++;

                if (global.playerJoined.size() - 1 < playerCount) {
                    player.sendMessage(ChatMessages.GREEN(Messages.OK_SETUP));
                    global.setupDone = true;
                    playerCount = 0;
                    this.cancel();
                }

            }
        }.runTaskTimer(RIVevent.plugin,0L,20L);

    }


    public void doorDetector(Location loc,int radius) {

        Block middle = loc.getBlock();
        GameHandler global = GameHandler.getInstance();
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

    public void eventSpawnPointTeleport(Player target, int spawnNum) {

        GameHandler global = GameHandler.getInstance();

        switch(spawnNum) {
            case 1:
                target.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LeftclickListener.LOC.SPAWN1)));
                target.playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,2,1);
            case 2:
                target.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LeftclickListener.LOC.SPAWN2)));
                target.playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,2,1);
            case 3:
                target.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LeftclickListener.LOC.SPAWN3)));
                target.playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,2,1);
            case 4:
                target.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LeftclickListener.LOC.SPAWN4)));
                target.playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,2,1);
        }


    }


}






