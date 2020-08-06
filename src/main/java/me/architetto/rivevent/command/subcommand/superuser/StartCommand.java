package me.architetto.rivevent.command.subcommand.superuser;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.command.GlobalVar;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;


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

        GlobalVar global = GlobalVar.getInstance();

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



    }

    public void readyAllert () {

        GlobalVar global = GlobalVar.getInstance();
        for(UUID key : global.playerJoined){

            Player target = Bukkit.getPlayer(key);
            assert target != null;
            target.sendTitle(Messages.START_ALLERT_TITLE,"",20,60,20);

        }

    }

    public void openDoors (long delay) {

        GlobalVar global = GlobalVar.getInstance();

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

        GlobalVar global = GlobalVar.getInstance();

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

    //--------------------------------------------------//

    public void antiCamper () {

        GlobalVar global = GlobalVar.getInstance();
        int acDelay = RIVevent.plugin.getConfig().getInt("AC_DELAY");
        int acPeriod = RIVevent.plugin.getConfig().getInt("AC_PERIOD");

        new BukkitRunnable() {

            @Override
            public void run(){

                if (global.playerJoined.size()<=1) {
                    this.cancel();
                }

                Player target = Bukkit.getPlayer(global.playerJoined.get(0));

                for (UUID key : global.playerJoined) {

                    if (target.getLocation().getBlockY() > Bukkit.getPlayer(key).getLocation().getBlockY()) {

                        target = Bukkit.getPlayer(key);

                    }

                }

                target.setHealth(target.getHealth()-1);
                target.sendMessage(ChatMessages.AQUA("Pizzicato !")); //Migliorabile

            }
        }.runTaskTimer(RIVevent.plugin,acDelay,acPeriod);


    }

    //--------------------------------------------------//





    //---------------NOT IMPLEMENTED-------------------//

    public void rewardTopTowerPlayerEvent(int towerY) {

        GlobalVar global = GlobalVar.getInstance();

        for(UUID key : global.playerJoined){

            Player target = Bukkit.getPlayer(key);

            assert target != null;
            if (target.getLocation().getY() >= towerY) {

                Material material = randomItem();

                ItemStack itemStack = new ItemStack(material,randomAmount(material));

                target.getInventory().addItem(itemStack);

                target.sendMessage(ChatMessages.AQUA("Hai ricevuto : " + itemStack.getI18NDisplayName()));

            }


        }

    }

    public Material randomItem() {

        GlobalVar global = GlobalVar.getInstance();
        int randomNum = global.itemList.size();

        randomNum = ThreadLocalRandom.current().nextInt(1, randomNum);

        return global.itemList.get(randomNum);

    }

    public int randomAmount(Material material) {

        if (material.getMaxStackSize()==1 || material.equals(Material.GOLDEN_APPLE))
            return 1;


        int randomNum = 10;
        randomNum = ThreadLocalRandom.current().nextInt(1, randomNum+1);

        return randomNum;

    }




}
