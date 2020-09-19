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

    GameHandler global = GameHandler.getInstance();
    SettingsHandler settings = SettingsHandler.getInstance();

    List<Material> noDoubleItem = new ArrayList<>(Arrays.asList(Material.LEATHER_BOOTS,
            Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET, Material.LEATHER_LEGGINGS, Material.FISHING_ROD));

    public int redLineY;

    @Override
    public void perform(Player sender, String[] args){

        if (!sender.hasPermission("rivevent.start")) {
            sender.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        if (global.presetSummon.isEmpty()) {
            sender.sendMessage(ChatMessages.RED(Messages.ERR_NO_EVENT));
            return;
        }

        if (!global.setupDoneFlag) {
            sender.sendMessage(ChatMessages.RED(Messages.ERR_SETUP_NOTREADY));
            return;
        }


        readyAllert();

        openDoors(settings.openDoorsDelay);
        closeDoors(settings.closeDoorsDelay);


        if (settings.antiCamperToggle) {
            antiCamperSystem();
        }


        if (settings.rewardPlayersOnTopToggle) {
            rewardPlayerOnTop();
        }



    }

    public void readyAllert () {

        Player target;
        for(UUID key : global.playerJoined){

            target = Bukkit.getPlayer(key);

            if (target != null)
                target.sendTitle(Messages.START_ALLERT_TITLE,"",20,60,20);

        }

    }

    public void openDoors (long delay) {


        new BukkitRunnable() {

            @Override
            public void run() {

                global.startDoneFlag = true;

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

                }

                for (UUID key : global.playerJoined) {

                    Player target = Bukkit.getPlayer(key);

                    if (target != null)
                        target.sendTitle(Messages.START_TITLE,Messages.START_SUBTITLE,20,40,20);

                }


            }
        }.runTaskLater(RIVevent.plugin, delay);
    }

    public void closeDoors (long delay) {

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


    //-------------------- REWARD PLAYER ------------------------//

    public void rewardPlayerOnTop() {

        new BukkitRunnable() {

            private final int towerTopY = LocSerialization.getDeserializedLocation(global.riveventPreset
                    .get(global.presetSummon).get(RightClickListener.Step.TOWER)).getBlockY();

            @Override
            public void run(){

                if (global.playerJoined.size() < settings.rewardMinPlayer || !global.setupStartFlag) {
                    this.cancel();
                    return;
                }


                for (UUID key : global.playerJoined) {
                    Player player = Bukkit.getPlayer(key);


                    assert player != null;
                    if (player.getInventory().firstEmpty() == -1){
                        player.sendMessage(ChatMessages.AQUA("Non hai ricevuto nulla perchè il tuo inventario è pieno!"));
                        continue;
                    }

                    if (player.getLocation().getBlockY() >= towerTopY) {

                        Material material = global.pickRandomItem();

                        if (noDoubleItem.contains(material) && player.getInventory().contains(material)){
                            player.sendMessage(ChatMessages.AQUA("Nulla di utile, sarai piu' fortunato al prossimo giro!"));
                            continue;
                        }

                        ItemStack itemStack = new ItemStack(material, global.pickRandomAmount(material));

                        player.getInventory().addItem(itemStack);

                        player.sendMessage(ChatMessages.AQUA("Hai ricevuto : " + itemStack.getI18NDisplayName()));

                        player.playSound(player.getLocation(),Sound.ENTITY_PLAYER_LEVELUP,1,1);

                    }
                }
            }
        }.runTaskTimer(RIVevent.plugin,0,settings.rewardPlayerPeriod);

    }

    //--------------------  ANTI CAMPER  ------------------------//

    public void antiCamperSystem() {

        redLineY = takeLowestSpawnYCoord();

        checkPlayersPosition();

        redLineManager();

    }

    public void checkPlayersPosition() {
        new BukkitRunnable() {

            @Override
            public void run(){

                if (global.playerJoined.size() <= 1){
                    this.cancel();
                    return;
                }

                for (UUID u : global.playerJoined) {
                    Player p = Bukkit.getPlayer(u);

                    if (p != null)
                        if (p.getLocation().getY() <= redLineY)
                            p.damage(settings.antiCamperDamage);
                }

            }
        }.runTaskTimer(RIVevent.plugin,settings.antiCamperStartDelay,settings.antiCamperPeriod);
    }

    public int takeLowestSpawnYCoord() {

        int y = 300;

        y = Math.min(y, LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(RightClickListener.Step.SPAWN1)).getBlockY());
        y = Math.min(y, LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(RightClickListener.Step.SPAWN2)).getBlockY());
        y = Math.min(y, LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(RightClickListener.Step.SPAWN3)).getBlockY());
        y = Math.min(y, LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(RightClickListener.Step.SPAWN4)).getBlockY());

        return y;
    }

    public void redLineManager() {

        new BukkitRunnable() {

            private final int maxYredLine = LocSerialization.getDeserializedLocation(global.riveventPreset
                    .get(global.presetSummon).get(RightClickListener.Step.TOWER)).getBlockY();

            @Override
            public void run(){
                redLineY += settings.antiCamperRedLineGrowValue;

                for (UUID u : global.playerJoined) {
                    Player p = Bukkit.getPlayer(u);
                    if (p != null){
                        p.sendMessage(ChatMessages.RED("Altezza minima consentita : " + ChatColor.GOLD + redLineY));
                    }
                }

                if (redLineY == maxYredLine - settings.antiCamperMaxY || global.playerJoined.size() <= 1)
                    this.cancel();

            }
        }.runTaskTimer(RIVevent.plugin,settings.antiCamperStartDelay + settings.antiCamperRedLineGrowPeriod,settings.antiCamperRedLineGrowPeriod);


    }







}
