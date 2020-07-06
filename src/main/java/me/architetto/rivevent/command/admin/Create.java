package me.architetto.rivevent.command.admin;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class Create implements CommandExecutor {

    public static HashMap<String,HashMap<String, Location>> riveventPreset = new HashMap<> ();
    public static HashMap<UUID,String> presetName = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)){
            sender.sendMessage ( "Comando non disponibile in console !" );
            return  true;
        }

        Player player = (Player) sender;

        if(args[0].equalsIgnoreCase("create")){
            if(!sender.hasPermission("rivevent.create")){
                sender.sendMessage("Non possiedi i permessi necessari ad eseguire questo comando!");
             return true;
            }

            if(args.length!=2){
                sender.sendMessage("Non hai inserito correttamente i parametri del comando !");
                return true;
            }


            if(riveventPreset.containsKey(args[1])){
                sender.sendMessage("Un preset con questo nome esiste già. Ripetere il comando con un nome diverso !");
                return true;
            }

            riveventPreset.put(args[1],new HashMap<>());
            player.sendMessage("[1/6] Inserisci il primo punto di spawn ...");
            presetName.put(player.getUniqueId(),args[1]);
            return true;


        }





        return false;
    }
}
