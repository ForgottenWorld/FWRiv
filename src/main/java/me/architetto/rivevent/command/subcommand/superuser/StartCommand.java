package me.architetto.rivevent.command.subcommand.superuser;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.command.GameHandler;
import me.architetto.rivevent.command.SubCommand;

import me.architetto.rivevent.listener.LeftclickListener;
import me.architetto.rivevent.util.ChatMessages;

import me.architetto.rivevent.util.LocSerialization;
import me.architetto.rivevent.util.Messages;
import org.bukkit.Bukkit;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.Player;

import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.*;


public class StartCommand extends SubCommand{
    @Override
    public String getName(){
        return "start";
    }

    @Override
    public String getDescription(){
        return "Start event.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent start";
    }

    @Override
    public void perform(Player player, String[] args){

        if (!player.hasPermission("rivevent.start")) {
            player.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        GameHandler global = GameHandler.getInstance();

        if (global.presetSummon.isEmpty()) {
            player.sendMessage(ChatMessages.RED(Messages.ERR_NO_EVENT));
            return;
        }

        if (!global.setupDone) {
            player.sendMessage(ChatMessages.RED(Messages.ERR_SETUP_NOTREADY));
            return;
        }


        readyAllert();

        int openDoorsDelay = RIVevent.getDefaultConfig().getInt("OPEN_DOORS_DELAY") * 20;
        int closeDoorsDelay = openDoorsDelay + RIVevent.getDefaultConfig().getInt("CLOSE_DOORS_DELAY") * 20;

        openDoors(openDoorsDelay);
        closeDoors(closeDoorsDelay);

        if (RIVevent.plugin.getConfig().getBoolean("ANTI_CAMPER_TOGGLE")) {
            antiCamper();
        }

        if (RIVevent.plugin.getConfig().getBoolean("REWARD_PLAYER_ON_TOP")) {
            rewardPlayerOnTop();
        }



    }

    public void readyAllert () {

        GameHandler global = GameHandler.getInstance();
        for(UUID key : global.playerJoined){

            Player target = Bukkit.getPlayer(key);
            assert target != null;
            target.sendTitle(Messages.START_ALLERT_TITLE,"",20,60,20);

        }

    }

    public void openDoors (long delay) {

        GameHandler global = GameHandler.getInstance();

        new BukkitRunnable() {

            @Override
            public void run() {

                for(org.bukkit.block.Block block : global.doorsToOpen){

                    if (!Tag.DOORS.getValues().contains(block.getType())
                            && !Tag.DOORS.getValues().contains(block.getType())
                            && !Tag.FENCE_GATES.getValues().contains(block.getType())){
                        continue;
                    } //Evita qualche errore strano (Porte che vengono tolte tra il /.. setup ed il /.. start)

                    BlockData data = block.getBlockData();
                    Openable door = (Openable) data;
                    door.setOpen(true);
                    block.setBlockData(door, true);
                    global.startDone = true;

                }

                for (UUID key : global.playerJoined) {

                    Player target = Bukkit.getPlayer(key);
                    assert target != null;
                    target.sendTitle(Messages.START_TITLE,Messages.START_SUBTITLE,20,40,20);

                }


            }
        }.runTaskLater(RIVevent.plugin, delay);
    }

    public void closeDoors (long delay) {

        GameHandler global = GameHandler.getInstance();

        new BukkitRunnable() {

            @Override
            public void run() {

                for(org.bukkit.block.Block block : global.doorsToOpen){

                    if (!Tag.DOORS.getValues().contains(block.getType())
                            && !Tag.DOORS.getValues().contains(block.getType())
                            && !Tag.FENCE_GATES.getValues().contains(block.getType())){
                        continue;
                    }//Evita qualche errore strano (Porte che vengono tolte tra il /.. setup ed il /.. start)

                    BlockData data = block.getBlockData();
                    Openable door = (Openable) data;
                    door.setOpen(false);
                    block.setBlockData(door, true);

                }
            }
        }.runTaskLater(RIVevent.plugin, delay);


    }


    //-------------------- TEST ------------------------//

    public void antiCamper () {

        GameHandler global = GameHandler.getInstance();
        long acDelay = RIVevent.plugin.getConfig().getLong("AC_DELAY") * 20;
        long acPeriod = RIVevent.plugin.getConfig().getLong("AC_PERIOD") * 20;
        int damageValue = Math.abs(RIVevent.plugin.getConfig().getInt("AC_DAMAGE"));//Only positive value
        int minPlayer = Math.max(1, RIVevent.plugin.getConfig().getInt("AC_MIN_PLAYER_ACTIVATION"));

        new BukkitRunnable() {

            @Override
            public void run(){

                if (global.playerJoined.size() < minPlayer) {
                    this.cancel();
                    return;
                }

                sortUUIDbyY(global.playerJoined); //Da testare

                Player target = Bukkit.getPlayer(global.playerJoined.get(global.playerJoined.size()-1));


                assert target != null;
                target.damage(damageValue);
                target.getWorld().strikeLightningEffect(target.getLocation());
                target.getWorld().playSound(target.getLocation(),Sound.ENTITY_LIGHTNING_BOLT_THUNDER,3,1);
                target.sendMessage(ChatMessages.AQUA(Messages.ANTI_CAMPER_MSG));

                Collections.shuffle(global.playerJoined, new Random()); //randomizza la lista per essere il più imparziali possibile

            }
        }.runTaskTimer(RIVevent.plugin,acDelay,acPeriod);


    }

    public void sortUUIDbyY(List<UUID> playerList) {
        playerList.sort((p1, p2) -> {
            int y1 = Objects.requireNonNull(Bukkit.getPlayer(p1)).getLocation().getBlockY();
            int y2 = Objects.requireNonNull(Bukkit.getPlayer(p2)).getLocation().getBlockY();
            if (y1 > y2){
                return 1;
            }else if (y1 == y2){
                return 0;
            }
            return -1;
        });
    }

    public void rewardPlayerOnTop() {

        GameHandler global = GameHandler.getInstance();
        long rewardPeriod = RIVevent.plugin.getConfig().getLong("REWARD_PERIOD") * 20;
        int minNumPlayer = RIVevent.plugin.getConfig().getInt("REWARD_MIN_PLAYERS");

        new BukkitRunnable() {

            private final int towerTopY = LocSerialization.getDeserializedLocation(global.riveventPreset
                    .get(global.presetSummon).get(LeftclickListener.LOC.TOWER)).getBlockY();

            @Override
            public void run(){

                if (global.playerJoined.size() <= minNumPlayer) {
                    this.cancel();
                    return;
                }


                for (UUID key : global.playerJoined) {
                    Player player = Bukkit.getPlayer(key);

                    assert player != null;
                    if (player.getLocation().getBlockY() >= towerTopY) {

                        Material material = global.pickRandomItem();

                        ItemStack itemStack = new ItemStack(material, global.pickRandomAmount(material));

                        player.getInventory().addItem(itemStack);

                        player.sendMessage(ChatMessages.AQUA("Hai ricevuto : " + itemStack.getI18NDisplayName()));

                        player.playSound(player.getLocation(),Sound.BLOCK_NOTE_BLOCK_GUITAR,2,1); //todo: scegliere un suono migliore

                    }
                }
            }
        }.runTaskTimer(RIVevent.plugin,0,rewardPeriod);

    }


    //---------------NOT IMPLEMENTED-------------------//






}
