package me.architetto.rivevent.command.subcommand.superuser;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.command.SettingsHandler;
import me.architetto.rivevent.command.GameHandler;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.listener.RightClickListener;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.LocSerialization;
import me.architetto.rivevent.util.Messages;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

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

    GameHandler global = GameHandler.getInstance();
    SettingsHandler settings = SettingsHandler.getInstance();

    public enum TargetSpawn {Sp1,Sp2,Sp3,Sp4}

    @Override
    public void perform(Player sender, String[] args){

        if (!sender.hasPermission("rivevent.setup")) {
            sender.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        if (global.setupStartFlag) {
            sender.sendMessage(ChatMessages.RED(Messages.ERR_SETUP_DONE));
            return;
        }

        if (global.presetSummon.isEmpty()) {
            sender.sendMessage(ChatMessages.RED(Messages.ERR_NO_EVENT));
            return;
        }

        if (global.playerJoined.isEmpty()) {
            sender.sendMessage(ChatMessages.RED(Messages.NO_PLAYER_JOINED));
            return;
        }

        global.setupStartFlag = true;

        getSpawnDoors();

        teleportToSpawn(sender);

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }

    public void teleportToSpawn(Player sender) {

        new BukkitRunnable() {

            private String loc = TargetSpawn.Sp1.toString();
            private final List<UUID> playerJoinedCopy = global.playerJoined;
            private int index = 0;

            @Override
            public void run(){

                if (index >= playerJoinedCopy.size()) {
                    sender.sendMessage(ChatMessages.GREEN(Messages.OK_SETUP));
                    global.setupDoneFlag = true;
                    this.cancel();
                    return;
                }

                Player p = Bukkit.getPlayer(playerJoinedCopy.get(index));

                if (p != null) {

                    if (!p.isOnline()) {
                        global.playerJoined.remove(p.getUniqueId());
                        return;
                    }

                    p.getInventory().clear();
                    equipLoadout(p);
                    index++;

                    switch(loc.toUpperCase()){
                        case "SP1":
                            p.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(RightClickListener.Step.SPAWN1)));
                            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 2, 1);
                            loc = TargetSpawn.Sp2.toString();
                            return;

                        case "SP2":
                            p.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(RightClickListener.Step.SPAWN2)));
                            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 2, 1);
                            loc = TargetSpawn.Sp3.toString();
                            return;

                        case "SP3":
                            p.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(RightClickListener.Step.SPAWN3)));
                            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 2, 1);
                            loc = TargetSpawn.Sp4.toString();
                            return;

                        case "SP4":
                            p.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(RightClickListener.Step.SPAWN4)));
                            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 2, 1);
                            loc = TargetSpawn.Sp1.toString();

                    }
                }

            }
        }.runTaskTimer(RIVevent.plugin,0,20);

    }

    public void getSpawnDoors() {

        doorDetector(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(RightClickListener.Step.SPAWN1)), settings.doorsDetectorRange);
        doorDetector(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(RightClickListener.Step.SPAWN2)), settings.doorsDetectorRange);
        doorDetector(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(RightClickListener.Step.SPAWN3)), settings.doorsDetectorRange);
        doorDetector(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(RightClickListener.Step.SPAWN4)), settings.doorsDetectorRange);

    }

    public void doorDetector(Location loc,int radius) {

        Block middle = loc.getBlock();
        for (int x = radius; x >= -radius; x--) {
            for (int y = radius; y >= -radius; y--){
                for(int z = radius; z >= -radius; z--){
                    if (Tag.DOORS.getValues().contains(middle.getRelative(x, y, z).getType())
                            ||Tag.FENCE_GATES.getValues().contains(middle.getRelative(x, y, z).getType())){

                        global.doorsToOpen.add(middle.getRelative(x, y, z).getLocation().getBlock());

                    }
                }
            }
        }
    }

    public void equipLoadout(Player target) {

        target.setHealth(20);
        target.setFoodLevel(20);

        for (Material material : global.startLoadOut.keySet()) {

            target.getInventory().addItem(new ItemStack(material,global.startLoadOut.get(material)));

        }

    }


}






