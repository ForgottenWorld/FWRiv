package me.architetto.rivevent.command.subcommand.superuser;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.command.GlobalVar;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.listener.LClickListener;
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

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

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

    public int randomNum ;

    @Override
    public void perform(Player player, String[] args){

        if (!player.hasPermission("rivevent.setup")) {
            player.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        GlobalVar global = GlobalVar.getInstance();

        if (global.setupStart) {
            player.sendMessage(ChatMessages.RED(Messages.ERR_SETUP_DONE));
            return;
        }

        if (global.presetSummon.isEmpty()) {
            player.sendMessage(ChatMessages.RED(Messages.ERR_NO_EVENT));
            return;
        }

        global.setupStart = true;

        for(UUID key : global.playerJoined){

            new BukkitRunnable(){

                int count =1;

                @Override
                public void run(){

                    randomNum = ThreadLocalRandom.current().nextInt(1, 4 + 1);


                    Player target = Bukkit.getPlayer(key);

                    if (target.isOnline()){

                        target.getInventory().clear();

                        switch(randomNum){
                            case 1:
                                target.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LClickListener.LOC.SPAWN1)));
                                target.playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,3,1);
                            case 2:
                                target.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LClickListener.LOC.SPAWN2)));
                                target.playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,3,1);
                            case 3:
                                target.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LClickListener.LOC.SPAWN3)));
                                target.playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,3,1);
                            case 4:
                                target.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LClickListener.LOC.SPAWN4)));
                                target.playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,3,1);
                            default:
                                target.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LClickListener.LOC.SPAWN1)));
                                target.playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,3,1);
                        }
                    }

                    if (count == global.playerJoined.size()) {
                        global.setupDone = true;
                        player.sendMessage(ChatMessages.GREEN(Messages.OK_SETUP));
                    } else
                        count++;
                }
            }.runTaskLater(RIVevent.plugin, 20);
        }

        doorDetector(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LClickListener.LOC.SPAWN1)));
        doorDetector(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LClickListener.LOC.SPAWN2)));
        doorDetector(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LClickListener.LOC.SPAWN3)));
        doorDetector(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LClickListener.LOC.SPAWN4)));

    }


    public void doorDetector(Location loc) {

        //TEST DETECT DOOR

        int raduis = 6;
        Block middle = loc.getBlock();
        GlobalVar global = GlobalVar.getInstance();
        for (int x = raduis; x >= -raduis; x--) {
            for (int y = raduis; y >= -raduis; y--){
                for(int z = raduis; z >= -raduis; z--){
                    if (Tag.DOORS.getValues().contains(middle.getRelative(x, y, z).getType())
                            ||Tag.FENCE_GATES.getValues().contains(middle.getRelative(x, y, z).getType())){

                        Block block = middle.getRelative(x, y, z).getLocation().getBlock();

                        global.doorsToOpen.add(block);


                    }
                }
            }
        }
    }



}






