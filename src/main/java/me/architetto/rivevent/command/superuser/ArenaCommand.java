package me.architetto.rivevent.command.superuser;

import me.architetto.rivevent.RIVevent;
import me.architetto.rivevent.arena.Arena;
import me.architetto.rivevent.arena.ArenaManager;
import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.util.ChatFormatter;
import me.architetto.rivevent.util.CommandName;
import me.architetto.rivevent.util.Messages;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ArenaCommand extends SubCommand{
    @Override
    public String getName(){
        return CommandName.ARENA_COMMAND;
    }

    @Override
    public String getDescription(){
        return "Info about selected arena";
    }

    @Override
    public String getSyntax(){
        return "/rivevent arena <arena_name>";
    }

    @Override
    public void perform(Player sender, String[] args){

        if (!sender.hasPermission("rivevent.eventmanager")) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_PERMISSION));
            return;
        }

        ArenaManager arenaManager = ArenaManager.getInstance();
        if (args.length < 2) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_ARENA_CMD_SYNTAX));
            return;
        }

        String presetName = args[1];
        Optional<Arena> arena = arenaManager.getArena(presetName);

        if (arenaManager.getArenaContainer().isEmpty()){
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_NO_ARENA));
            return;
        }

        if (arena.isPresent()) {
            Arena selectedArena = arena.get();
            sender.sendMessage(ChatFormatter.chatHeaderPresetInfo());
            sender.sendMessage(ChatFormatter.formatPresetName(selectedArena.getName()));
            sender.sendMessage(ChatFormatter.formatLocation("SPAWN #1", selectedArena.getSpawn1()));
            particleEffect(selectedArena.getSpawn1());
            sender.sendMessage(ChatFormatter.formatLocation("SPAWN #2", selectedArena.getSpawn2()));
            particleEffect(selectedArena.getSpawn2());
            sender.sendMessage(ChatFormatter.formatLocation("SPAWN #3", selectedArena.getSpawn3()));
            particleEffect(selectedArena.getSpawn3());
            sender.sendMessage(ChatFormatter.formatLocation("SPAWN #4", selectedArena.getSpawn4()));
            particleEffect(selectedArena.getSpawn4());
            sender.sendMessage(ChatFormatter.formatLocation("TOWER POINT", selectedArena.getTower()));
            particleEffect(selectedArena.getTower());
            sender.sendMessage(ChatFormatter.chatFooter());



        } else {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_NO_ARENA_NAME));
        }

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {

        if (args.length == 2){

            return new ArrayList<>(ArenaManager.getInstance().getArenaContainer().keySet());

        }

        return null;
    }

    public void particleEffect(Location loc) {
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(255, 69, 0), 5);

        new BukkitRunnable() {
            int times = 20;
            @Override
            public void run(){
                loc.getWorld().spawnParticle(Particle.REDSTONE, loc,10,dustOptions);
                times--;
                if (times == 0)
                    this.cancel();
            }
        }.runTaskTimer(RIVevent.plugin,0,20);

    }
}
