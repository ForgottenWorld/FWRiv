package me.architetto.rivevent.command.subcommand.superuser;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.command.GameHandler;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.listener.LeftclickListener;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.LocSerialization;
import me.architetto.rivevent.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
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

        if (global.miniEventFlag) {
            player.sendMessage(ChatMessages.RED(Messages.ERR_MINIEVENT));
            return;
        }

        if (args.length >= 2) {
            switch(args[1].toLowerCase()){
                case "backtolife":
                    backToLifeEvent();
                    return;
                case "rollback":
                    rollbackEvent();
                    return;
                case "curse":
                    curseEvent();
                    return;
                default:
                    player.sendMessage("Cos ?");

            }
        }

        /* EVENTI
        -BLACKOUT : Blind dei player e scambio di posizione
        -BACKTOLIFE : Tutti i player tornano in vita (OK)
        -ROLLBACK : Clear inventario di tutti i partecipanti (OK)
        -SNOWBALL SHOWDOWN : Ogni spettatore riceve palle di neve per bersagliare i giocatori
        -NAUSEA : i giocatori ricevono effetto nausea
        -INVISIBILITA' : tutti i giocatori diventano invisibili
        -TITANFALL : un giocatore potenziato... non so
        -CURSE : una maledizione che si passa tramite pugno, al termine del cooldown chi ha la malattia muore all'istante  (OK) [curseEvent]
        -WIP : un player ha la corona colpendolo viene sottratta

         */

    }

    public void backToLifeEvent() {

        //Fa tornare in gioco tutti i player eliminati - WIP

        global.playerSpectate.removeAll(global.playerOut);
        global.playerJoined.addAll(global.playerOut);

        for (UUID player : global.playerOut) {
            Player target = Bukkit.getPlayer(player);
            assert target != null;
            target.teleport(LocSerialization.getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LeftclickListener.LOC.TOWER)));

        }

        global.playerOut.clear();

    }

    public void rollbackEvent() {

        //Clear degli inventari di tutti i partecipanti - WIP

        for (UUID player : global.playerJoined) {

            Player target = Bukkit.getPlayer(player);
            assert target != null;
            target.getInventory().clear();

        }

    }

    public void curseEvent() {

        global.curseEventFlag = true;

        SecureRandom random = new SecureRandom();
        int randomPlayerIndex;

        if (global.playerJoined.size() == 1) //Situazione che non dovrebbe capitare mai ovviamente
            randomPlayerIndex = 0;
        else
            randomPlayerIndex = random.nextInt(global.playerJoined.size()-1);

        global.cursedPlayer = Bukkit.getPlayer(global.playerJoined.get(randomPlayerIndex));

        assert global.cursedPlayer != null;
        global.cursedPlayer.sendTitle(ChatColor.RED  + "Sei stato maledetto !",ChatColor.ITALIC + "Presto, colpisci qualcuno per sbarazzarti della maledizione!",20,120,20);
        global.cursedPlayer.getWorld().playSound(global.cursedPlayer.getLocation(), Sound.ENTITY_GHAST_HURT,5,2);

        for (UUID u : global.playerJoined) {
            Player p = Bukkit.getPlayer(u);

            if (p == global.cursedPlayer)
                continue;

            assert p != null;
            p.sendMessage(ChatMessages.AQUA("Attento, uno dei partecipanti è stato maledetto! Non farti toccare!"));
        }

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



}
