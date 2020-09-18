package me.architetto.rivevent.command.subcommand.superuser;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.command.GameHandler;
import me.architetto.rivevent.command.SettingsHandler;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.listener.RightClickListener;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.LocSerialization;
import me.architetto.rivevent.util.Messages;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.security.SecureRandom;
import java.util.UUID;

public class MiniGameCommand extends SubCommand{
    @Override
    public String getName(){
        return "minigame";
    }

    @Override
    public String getDescription(){
        return "Starts a minigame.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent minigame [event name]";
    }

    GameHandler global = GameHandler.getInstance();
    SettingsHandler settings = SettingsHandler.getInstance();

    @Override
    public void perform(Player player, String[] args){

        //WIP


        if (!player.hasPermission("rivevent.minigame")) {
            player.sendMessage(ChatMessages.RED(Messages.NO_PERM));
            return;
        }

        if (!global.startDoneFlag) {
            player.sendMessage(ChatMessages.RED(Messages.ERR_START));
            return;
        }

        if (global.isMinigameInProgress()) {
            player.sendMessage(ChatMessages.RED(Messages.ERR_MINIEVENT));
            return;
        }

        if (args.length >= 2) {
            switch(args[1].toUpperCase()){
                case "CURSE":
                    curseEvent(player);
                    return;
                case "BACKTOLIFE":
                    backToLifeEvent();
                    return;
                case "FALLDOWN":
                    allFallDownEvent();
                    return;
                case "DEATHRACE":
                    deathRaceEvent(player);
                    return;
                default:
                    player.sendMessage("Nessun minigame con questo nome !");

            }
        }
    }


    // -- CurseMinigame methods -- // {

    public void curseEvent(Player sender) {

        if (global.playerJoined.size() < 2) {
            sender.sendMessage(ChatMessages.RED("Non ci sono abbastanza giocatori per iniziare questo minigioco!"));
            return;
        }

        cursePickRandomPlayer();

        if (global.cursedPlayer == null){
            sender.sendMessage(ChatMessages.RED("Errore nella scelta del player maledetto."));
            return;
        }

        global.curseEventFlag = true;

        global.cursedPlayer.sendTitle(ChatColor.RED  + "Sei stato maledetto !",ChatColor.ITALIC + "Presto, colpisci qualcuno per sbarazzarti della maledizione!",20,120,20);
        global.cursedPlayer.getWorld().playSound(global.cursedPlayer.getLocation(), Sound.ENTITY_GHAST_HURT,5,2);

        for (UUID u : global.playerJoined) {
            Player p = Bukkit.getPlayer(u);

            if (p == global.cursedPlayer)
                continue;

            assert p != null;
            p.sendMessage(ChatMessages.AQUA("Attento, uno dei partecipanti è stato maledetto! Non farti toccare!"));
        }

        execution();

    }

    public void cursePickRandomPlayer() {

        SecureRandom random = new SecureRandom();
        int randomPlayerIndex = random.nextInt(global.playerJoined.size()-1);

        global.cursedPlayer = Bukkit.getPlayer(global.playerJoined.get(randomPlayerIndex));

    }

    public void execution() {

        new BukkitRunnable() {


            @Override
            public void run(){

                global.curseEventFlag = false;

                if (!global.playerJoined.contains(global.cursedPlayer.getUniqueId())) {
                    return;
                }

                global.cursedPlayer.damage(30);
                global.cursedPlayer.sendMessage(ChatMessages.AQUA("Sei morto a causa della maledizione!")); //todo: messaggio da rivedere


                for (UUID u : global.allPlayerList()) {

                    Player p = Bukkit.getPlayer(u);

                    assert p != null;
                    p.sendTitle( ChatColor.DARK_RED + global.cursedPlayer.getDisplayName() + ChatColor.RESET +  "e' morto!",
                            "La maledizione si e' compiuta!",20,60,20);

                }


            }
        }.runTaskLater(RIVevent.plugin,3600L);

    }

    // -- CurseMinigame methods -- // }

    // -- BackToLife-Minigame methods -- // {

    public void backToLifeEvent() {

        global.backToLifeEventFlag = true;

        //Fa tornare in gioco tutti i player eliminati - WIP

        regenEffectForNotRevivedPlayer();

        revivePlayerRunnable();


    }

    public void regenEffectForNotRevivedPlayer() {

        Player player;

        for (UUID u : global.playerJoined) {
            player = Bukkit.getPlayer(u);

            if (player != null)
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 10));

        }
    }

    public void revivePlayerRunnable() {

        new BukkitRunnable() {

            Player target;

            @Override
            public void run(){

                if (global.playerOut.isEmpty()){
                    global.backToLifeEventFlag = false;
                    this.cancel();
                    return;
                }

                target = Bukkit.getPlayer(global.playerOut.get(0));

                global.playerSpectate.remove(global.playerOut.get(0));
                global.playerJoined.add(global.playerOut.get(0));

                if (target != null){

                    target.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon)
                            .get(RightClickListener.Step.TOWER)));

                    target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 80, 10));

                }

                global.playerOut.remove(0);

            }
        }.runTaskTimer(RIVevent.plugin,0,25);

    }

    // -- BackToLife-Minigame methods -- // }

    // -- ALLFALLDOWN-Minigame methods -- // {

    public void allFallDownEvent() {

        global.fallDownEventFlag = true;

        for (UUID u : global.playerJoined) {
            Player p = Bukkit.getPlayer(u);

            if (p != null)
            p.sendMessage(ChatMessages.AQUA("Eento All Fall Down : Tutti i players vengono teletrasportati al punto di partenza!"));
        }

        new BukkitRunnable(){

            @Override
            public void run(){

                nauseaEffect();

                fallDownTeleport();

                manageDoors();

            }

        }.runTaskLater(RIVevent.plugin,100L);


    }

    public void nauseaEffect() {
        Player p;
        for (UUID u : global.allPlayerList()) {
            p = Bukkit.getPlayer(u);

            if (p != null) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 140, 10));
                p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 20));
                p.sendTitle("","Tutti giu' per terra!",20,80,20);
            }

        }
    }

    public void fallDownTeleport() {

                Player p;
                int i = 1;
                for (UUID u : global.playerJoined) {
                    p = Bukkit.getPlayer(u);
                    if (p != null) {
                        p.playSound(p.getLocation(),Sound.ENTITY_ENDERMAN_TELEPORT,2,1);
                        switch(i){
                            case 1:
                                p.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(RightClickListener.Step.SPAWN1)));
                                i++;
                                continue;
                            case 2:
                                p.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(RightClickListener.Step.SPAWN2)));
                                i++;
                                continue;
                            case 3:
                                p.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(RightClickListener.Step.SPAWN3)));
                                i++;
                                continue;
                            case 4:
                                p.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(RightClickListener.Step.SPAWN4)));
                                i=1;
                        }
                    }
                }

                global.fallDownEventFlag = false;

    }

    public void manageDoors() {

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
        }.runTaskLater(RIVevent.plugin, 2400L);

    }

    // -- ALLFALLDOWN-Minigame methods -- // }

    // -- DEATHRACE-Minigame methods -- // {       //Work in progress

    public void deathRaceEvent(Player player) {

        if (global.playerJoined.size() < 2) {
            player.sendMessage(ChatMessages.RED("Non ci sono abbastanza giocatori"));
            return;
        }

        global.deathRaceEventFlag = true;

        Player p;

        for (UUID u : global.playerJoined) {
            p = Bukkit.getPlayer(u);
            if (p != null){
                p.sendMessage(ChatMessages.AQUA("Evento Death Race : Ogni " + ChatColor.RED + settings.deathRacePeriod / 20
                        + " secondi" + ChatColor.RESET + " il player più in basso MUORE!"));
                p.sendTitle(ChatColor.DARK_RED + "Death Race", "",20,60,20);
            }
        }

        deathRaceRunnable();


    }

    public void deathRaceRunnable() {

        new BukkitRunnable() {

            @Override
            public void run(){

                Player target = takeLastPlayer();
                target.setHealth(0);
                target.sendMessage(ChatMessages.RED("A questo giro sei l'ultimo.. bon voyage!"));

                if (global.playerJoined.size() <= 1) {
                    global.deathRaceEventFlag = false;
                    this.cancel();
                }


            }
        }.runTaskTimer(RIVevent.plugin,settings.deathRacePeriod,settings.deathRacePeriod);

    }

    public Player takeLastPlayer(){

        Player p = Bukkit.getPlayer(global.playerJoined.get(0));

        for (UUID u : global.playerJoined) {

            Player p1 = Bukkit.getPlayer(u);

                if (p1.getLocation().getY() < p.getLocation().getY())
                    p = Bukkit.getPlayer(u);

        }

       return p;
    }








}
