package me.architetto.rivevent.command.subcommand.superuser;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.command.GlobalVar;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.listener.LeftClickOnBlock;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.LocSerialization;
import me.architetto.rivevent.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
        return null;
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
        }else{
            global.setupStart = true;

            new BukkitRunnable(){

                private int i = 1;

                public void run(){
                    //Tippa tutti i player joinati nei vari punti di spawn.
                    for(UUID key : global.playerJoined.keySet()){

                        randomNum = ThreadLocalRandom.current().nextInt(1, 4 + 1);
                        Player target = Bukkit.getPlayer(key);

                        if (target.isOnline()){

                            target.getInventory().clear();

                            switch(randomNum){
                                case 1:
                                    target.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LeftClickOnBlock.LOC.SPAWN1)));
                                    continue;
                                case 2:
                                    target.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LeftClickOnBlock.LOC.SPAWN2)));
                                    continue;
                                case 3:
                                    target.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LeftClickOnBlock.LOC.SPAWN3)));
                                    continue;
                                case 4:
                                    target.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LeftClickOnBlock.LOC.SPAWN4)));
                                    continue;
                                default:
                                    target.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LeftClickOnBlock.LOC.SPAWN1)));

                            }
                        }
                    }
                    if(i==global.playerJoined.keySet().size()){
                        cancel();
                    }
                    i++;
                }
            }.runTaskTimer(RIVevent.plugin, 0L, 20L);

            doorDetector(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LeftClickOnBlock.LOC.SPAWN1)));
            doorDetector(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LeftClickOnBlock.LOC.SPAWN2)));
            doorDetector(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LeftClickOnBlock.LOC.SPAWN3)));
            doorDetector(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LeftClickOnBlock.LOC.SPAWN4)));

            global.setupDone = true;

        }
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


