package me.architetto.rivevent.command.subcommand.superuser;

import me.architetto.rivevent.RIVevent;
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

    @Override
    public void perform(Player player, String[] args){

        //WIP WIP WIP WIP WIP WIP WIP WIP WIP WIP WIP


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
                case "THEBOOGEYMAN":
                    theBoogeymanEvent(player);
                    return;
                case "FALLDOWN":
                    allFallDownEvent();
                    return;
                default:
                    player.sendMessage("Nessun minigame con questo nome !");

            }
        }

        /* Esempi di Eventi

        -BACKTOLIFE : Tutti i player tornano in vita (OK)
        -FALLDOWN : I partecipanti tornano allo start (OK)
        -THEBOOGEYMAN : un player invisibile, il primo che lo colpisce vince l'evento. (OK, da perfezionare)
        -CURSE : una maledizione che si passa tramite pugno, al termine del cooldown chi ha la malattia muore all'istante  (OK) [curseEvent]


        -SNOWBALL SHOWDOWN : Ogni spettatore riceve palle di neve per bersagliare i giocatori


        -WIP : un player ha la corona colpendolo viene sottratta


         */

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


    //-----------------------------------//


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

    //-----------------------------------//

    // -- Boogeyman-Minigame methods -- // {

    public void theBoogeymanEvent(Player sender) {


        if (global.playerJoined.size() < 2) {
            sender.sendMessage(ChatMessages.RED("Non ci sono abbastanza giocatori per iniziare questo minigioco!"));
            return;
        }

        global.boogeymanEventFlag = true;

        global.boogeymanPlayer = chooseBoogeyman();

        if (global.boogeymanPlayer == null){
            global.boogeymanEventFlag = false;
            return;
        }

        blindAllPlayer();

        global.boogeymanPlayer.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 2400, 10));
        discontinuousInvisibleEffect();

        boogeymanSparkEffect();

        endBoogeymanEvent(2400);



        //il boogeyman muore se toccato
        //se non viene toccato ottiene qualche bonus o item

    }

    public void blindAllPlayer() {

        Player p;
        for (UUID u : global.allPlayerList()) {
            p = Bukkit.getPlayer(u);

            if (p != null && p != global.boogeymanPlayer)
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 160, 10));

        }

    }

    public Player chooseBoogeyman() {

        if (global.playerJoined.size() == 1)
            return Bukkit.getPlayer(global.playerJoined.get(0));

        SecureRandom random = new SecureRandom();
        int randomPlayerIndex = random.nextInt(global.playerJoined.size()-1);

        return Bukkit.getPlayer(global.playerJoined.get(randomPlayerIndex));

    }

    public void boogeymanSparkEffect() {

        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(64, 64, 64), 2);

        new BukkitRunnable() {

            @Override
            public void run(){

                if (!global.boogeymanEventFlag) {
                    this.cancel();
                    return;
                }

                global.boogeymanPlayer.getWorld().spawnParticle(Particle.REDSTONE, global.boogeymanPlayer.getLocation().add(0,1,0), 10, dustOptions);
                global.boogeymanPlayer.getWorld().playSound(global.boogeymanPlayer.getLocation(),Sound.ENTITY_SLIME_SQUISH,1,1);


            }
        }.runTaskTimer(RIVevent.plugin,200, 15);


    }

    public void discontinuousInvisibleEffect() {
        new BukkitRunnable() {

            @Override
            public void run(){

                if (!global.boogeymanEventFlag) {
                    global.boogeymanPlayer.removePotionEffect(PotionEffectType.INVISIBILITY);
                    this.cancel();
                    return;
                }

                global.boogeymanPlayer.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 15, 10));
                global.boogeymanPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 30, 2));

            }
        }.runTaskTimer(RIVevent.plugin,100,240);
    }

    public void  endBoogeymanEvent(long eventEndTimer) {

        new BukkitRunnable() {

            @Override
            public void run(){

                if (!global.boogeymanEventFlag) {
                    this.cancel();
                    return;
                }

                global.boogeymanEventFlag = false;

                if (global.playerJoined.contains(global.boogeymanPlayer.getUniqueId())) {

                    //Premio per il boogeyman
                    global.boogeymanPlayer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 3600, 2));
                    global.boogeymanPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 3600, 2));

                }

            }
        }.runTaskLater(RIVevent.plugin,eventEndTimer);

    }

    // -- Boogeyman-Minigame methods -- // }

    // -- ALLFALLDOWN-Minigame methods -- // {

    public void allFallDownEvent() {

        global.fallDownEventFlag = true;

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
        new BukkitRunnable() {

            @Override
            public void run(){

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
        }.runTaskLater(RIVevent.plugin,60);
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







}
