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

        openDoorsRunnable();
        closeDoorsRunnable();

        if (settings.antiCamperToggle) {
            antiCamperSystem();
        }


        if (settings.rewardPlayersOnTopToggle) {
            rewardPlayerOnTop();
        }



    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }

    public void readyAllert () {

        Player target;
        for(UUID key : global.playerJoined){

            target = Bukkit.getPlayer(key);

            if (target != null)
                target.sendTitle(Messages.START_ALLERT_TITLE,"",20,60,20);

        }

    }

    public void openDoorsRunnable() {

        new BukkitRunnable() {

            @Override
            public void run() {

                global.startDoneFlag = true;

                global.openDoors();

                for (UUID key : global.playerJoined) {

                    Player target = Bukkit.getPlayer(key);

                    if (target != null)
                        target.sendTitle(Messages.START_TITLE,Messages.START_SUBTITLE,20,40,20);

                }


            }
        }.runTaskLater(RIVevent.plugin, settings.openDoorsDelay);
    }

    public void closeDoorsRunnable() {

        new BukkitRunnable() {

            @Override
            public void run() {

                global.closeDoors();

            }
        }.runTaskLater(RIVevent.plugin, settings.closeDoorsDelay);


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

                    if (player == null)
                        continue;

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

        redLineParticleEffect(LocSerialization.getDeserializedLocation(global.riveventPreset
                .get(global.presetSummon).get(RightClickListener.Step.TOWER)).clone(),12);

    }

    public void checkPlayersPosition() {
        new BukkitRunnable() {

            @Override
            public void run(){

                if (global.playerJoined.size() < 1){
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

        return y+2;
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

                if (redLineY == maxYredLine - settings.antiCamperMaxY || global.playerJoined.size() < 1)
                    this.cancel();

            }
        }.runTaskTimer(RIVevent.plugin,settings.antiCamperStartDelay + settings.antiCamperRedLineGrowPeriod,settings.antiCamperRedLineGrowPeriod);


    }

    public void redLineParticleEffect(Location xzLoc,int radius) {

         xzLoc.setY(redLineY);

        new BukkitRunnable() {

            final Particle.DustOptions dustOptions = new Particle.DustOptions(org.bukkit.Color.fromRGB(200, 0, 0),10);
            final Particle.DustOptions dustOptions2 = new Particle.DustOptions(org.bukkit.Color.fromRGB(0, 0, 0),2);
            final int startYRedLine = redLineY;
            boolean deathLineAnimation = true;

            @Override
            public void run(){

                xzLoc.setY(redLineY);
                Block middle = xzLoc.getBlock();
                for (int x = radius; x >= -radius; x--) {

                    for (int z = radius; z >= -radius; z--) {

                        if (middle.getRelative(x, 0, z).getType().isAir()) {

                            if (z < radius - 1 && z > -radius + 1 && x < radius - 1 && x > -radius + 1)
                                continue;

                           if (deathLineAnimation)
                               xzLoc.getWorld().spawnParticle(Particle.REDSTONE, middle.getRelative(x, 0, z).getLocation(), 2, dustOptions);
                           else
                               xzLoc.getWorld().spawnParticle(Particle.REDSTONE, middle.getRelative(x, 0, z).getLocation(), 1, dustOptions2);


                           if (redLineY >= startYRedLine + 3)
                               xzLoc.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, middle.getRelative(x, -1, z).getLocation(), 0, 0,-0.02,0);

                        }
                    }
                }

                deathLineAnimation= !deathLineAnimation;


            }
        }.runTaskTimer(RIVevent.plugin,settings.antiCamperStartDelay,10);







    }







}
