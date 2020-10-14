package me.architetto.rivevent.command.superuser;

import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.event.MiniGameService;
import me.architetto.rivevent.util.ChatFormatter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Override
    public void perform(Player sender, String[] args) {


        if (!sender.hasPermission("rivevent.minigame")) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Permission Error: you do not have permission to use that command"));
            return;
        }

        EventService eventService = EventService.getInstance();

        if (!eventService.isStarted()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Error: no event is running or started"));
            return;
        }

        MiniGameService miniGameService = MiniGameService.getInstance();

        if (miniGameService.isMiniGameRunning()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Error: a minigame is already running"));
            return;
        }

        if (args.length == 2) {
            switch(args[1].toUpperCase()){
                case "CURSE":
                    miniGameService.startCurseEvent(sender);
                    return;
                default:
                    sender.sendMessage("Nessun minigame con questo nome !");

            }
        }

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){

        if (args.length == 2){

            return new ArrayList<>(
                    Arrays.asList("Curse"));

        }
        return null;
    }

    /*

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

        for (UUID u : global.allPlayerList()) {

            Player p = Bukkit.getPlayer(u);
            if (p == global.cursedPlayer || p == null)
                continue;

            if (global.playerJoined.contains(u))
                p.sendMessage(ChatMessages.AQUA("Attento, uno dei partecipanti è stato maledetto! Non farti toccare!"));

            if (global.playerSpectate.contains(u))
                p.sendMessage(ChatMessages.RED("Uno dei partecipanti è stato maledetto!"));
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

                if (!global.curseEventFlag) {
                    this.cancel();
                    return;
                }

                global.curseEventFlag = false;

                if (!global.playerJoined.contains(global.cursedPlayer.getUniqueId())) {
                    return;
                }

                global.cursedPlayer.damage(30);
                global.cursedPlayer.sendMessage(ChatMessages.AQUA("Sei morto a causa della maledizione!")); //todo: messaggio da rivedere


                for (UUID u : global.allPlayerList()) {

                    Player p = Bukkit.getPlayer(u);

                    if (p ==  null)
                        continue;

                    p.sendTitle( ChatColor.DARK_RED + global.cursedPlayer.getDisplayName() + ChatColor.RESET +  "e' morto!",
                            "La maledizione si e' compiuta!",20,60,20);

                }


            }
        }.runTaskLater(RIVevent.plugin,2400L);

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
                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 300, 3));
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

        global.openDoors();

        new BukkitRunnable() {

            @Override
            public void run() {

                global.closeDoors();

            }
        }.runTaskLater(RIVevent.plugin, 2400L);

    }

    // -- ALLFALLDOWN-Minigame methods -- // }

    // -- DEATHRACE-Minigame methods -- // {

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

                animatedTitle();

                if (global.playerJoined.size() <= 1) {
                    global.deathRaceEventFlag = false;
                    this.cancel();
                }


            }
        }.runTaskTimer(RIVevent.plugin,settings.deathRacePeriod - 200,settings.deathRacePeriod - 200);

    }

    public void animatedTitle() {

        new BukkitRunnable() {

            private int countdown = 10;

            @Override
            public void run(){

                if (countdown == 0){
                    Player target = takeLastPlayer();

                    if (target != null){
                        target.setHealth(0);
                        target.sendMessage(ChatMessages.RED("A questo giro sei l'ultimo.. bon voyage!"));
                    }

                    this.cancel();
                    return;
                }


                for (UUID u : global.playerJoined) {
                    Player p = Bukkit.getPlayer(u);

                    if (p != null)
                        p.sendTitle("","" + ChatColor.RED + countdown,0,20,0);

                }

                countdown -= 1;


            }
        }.runTaskTimer(RIVevent.plugin,0,20);

    }

    public Player takeLastPlayer(){

        Player p = Bukkit.getPlayer(global.playerJoined.get(0));

        for (UUID u : global.playerJoined) {

            Player p1 = Bukkit.getPlayer(u);

            if (p1 == null || p == null)
                return null;

            if (p1.getLocation().getY() < p.getLocation().getY())
                p = Bukkit.getPlayer(u);

        }

       return p;
    }

    // -- DEATHRACE-Minigame methods -- // }




*/





}
