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
import org.bukkit.event.player.PlayerQuitEvent;
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

        doorDetector(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LClickListener.LOC.SPAWN1)),6); //L'intenzione e di settare il raggio da config
        doorDetector(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LClickListener.LOC.SPAWN2)),6);
        doorDetector(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LClickListener.LOC.SPAWN3)),6);
        doorDetector(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LClickListener.LOC.SPAWN4)),6);

        for(UUID key : global.playerJoined){

            new BukkitRunnable(){

                @Override
                public void run(){

                    Player target = Bukkit.getPlayer(key);

                    assert target != null;
                    target.getInventory().clear();

                    randomTeleport(target);


                    if (key.equals(global.playerJoined.get(global.playerJoined.size()-1))) {
                        player.sendMessage(ChatMessages.GREEN(Messages.OK_SETUP));
                        global.setupDone = true;
                    }
                }
            }.runTaskLater(RIVevent.plugin, 20);
        }

    }


    public void doorDetector(Location loc,int radius) {

        Block middle = loc.getBlock();
        GlobalVar global = GlobalVar.getInstance();
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

    public void randomTeleport(Player target) {

        int randomValue = ThreadLocalRandom.current().nextInt(1, 4 + 1);

        GlobalVar global = GlobalVar.getInstance();

        switch(randomValue){
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



}






