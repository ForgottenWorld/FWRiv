package me.architetto.rivevent.command.subcommand.superuser;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.command.GameHandler;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.listener.RightClickListener;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.LocSerialization;
import me.architetto.rivevent.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
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
                default:
                    player.sendMessage("Nessun minigame con questo nome !");

            }
        }

        /* Esempi di Eventi

        -BLACKOUT : Blind player
        -BACKTOLIFE : Tutti i player tornano in vita (OK)
        -ROLLBACK : Clear inventario di tutti i partecipanti (OK ma non mi piace molto)
        -SNOWBALL SHOWDOWN : Ogni spettatore riceve palle di neve per bersagliare i giocatori
        -NAUSEA : i giocatori ricevono effetto nausea
        -INVISIBILITA' : tutti i giocatori diventano invisibili
        -TITANFALL : un giocatore potenziato... non so
        -CURSE : una maledizione che si passa tramite pugno, al termine del cooldown chi ha la malattia muore all'istante  (OK) [curseEvent]
        -WIP : un player ha la corona colpendolo viene sottratta

        -LUCKYBOY : un player che se colpito da premi

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

                    target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 10));

                }

                global.playerOut.remove(0);

            }
        }.runTaskTimer(RIVevent.plugin,0,25);

    }

    // -- BackToLife-Minigame methods -- // }

    //-----------------------------------//






}
