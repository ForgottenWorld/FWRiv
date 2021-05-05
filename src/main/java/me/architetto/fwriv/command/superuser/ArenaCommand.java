package me.architetto.fwriv.command.superuser;

import me.architetto.fwriv.arena.Arena;
import me.architetto.fwriv.arena.ArenaManager;
import me.architetto.fwriv.command.CommandName;
import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.localization.Message;
import me.architetto.fwriv.particles.ParticlesManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ArenaCommand extends SubCommand {
    @Override
    public String getName() {
        return CommandName.ARENA_COMMAND;
    }

    @Override
    public String getDescription() {
        return Message.ARENA_COMMAND.asString();
    }

    @Override
    public String getSyntax() {
        return "/fwriv " + CommandName.ARENA_COMMAND + " <arena_name> [-p] [-id]";
    }

    @Override
    public String getPermission() {
        return "fwriv.arena";
    }

    @Override
    public int getArgsRequired() {
        return 3;
    }

    private final List<String> params = Arrays.asList(
            "tp",
            "points",
            "showpoint"
    );

    private final List<String> id = Arrays.asList(
            "1",
            "2",
            "3",
            "4",
            "5",
            "all"
    );

    private final List<String> tpid = Arrays.asList(
            "1",
            "2",
            "3",
            "4",
            "5"
    );

    @Override
    public void perform(Player sender, String[] args) {
        ArenaManager arenaManager = ArenaManager.getInstance();
        Optional<Arena> arenaOpt = arenaManager.getArena(args[1]);

        if (!arenaOpt.isPresent())
            return;

        Arena arena = arenaOpt.get();

        switch (args[2].toLowerCase()) {
            case "tp":
                if (args.length != 4)
                    return;
                switch (args[3].toLowerCase()) {
                    case "1":
                        sender.teleport(arena.getSpawn1());
                        break;
                    case "2":
                        sender.teleport(arena.getSpawn2());
                        break;
                    case "3":
                        sender.teleport(arena.getSpawn3());
                        break;
                    case "4":
                        sender.teleport(arena.getSpawn4());
                        break;
                    case "5":
                        sender.teleport(arena.getTower());
                        break;
                    default:
                        return;
                }
                break;
            case "points":
                Message.COMP_ARENA_POINTS.sendComponent(sender,
                        component(1,args[1]),arena.formattedLoc(1),tpcomp(1,args[1]),
                        component(2,args[1]),arena.formattedLoc(2),tpcomp(2,args[1]),
                        component(3,args[1]),arena.formattedLoc(3),tpcomp(3,args[1]),
                        component(4,args[1]),arena.formattedLoc(4),tpcomp(4,args[1]),
                        component(5,args[1]),arena.formattedLoc(5),tpcomp(5,args[1]));
                break;
            case "showpoint":
                if (args.length != 4)
                    return;
                switch (args[3].toLowerCase()) {
                    case "1":
                        ParticlesManager.getInstance().arenaPointEffect(arena.getSpawn1(),100);
                        break;
                    case "2":
                        ParticlesManager.getInstance().arenaPointEffect(arena.getSpawn2(),100);
                        break;
                    case "3":
                        ParticlesManager.getInstance().arenaPointEffect(arena.getSpawn3(),100);
                        break;
                    case "4":
                        ParticlesManager.getInstance().arenaPointEffect(arena.getSpawn4(),100);
                        break;
                    case "5":
                        ParticlesManager.getInstance().arenaPointEffect(arena.getTower(),100);
                        break;
                    case "all":
                        ParticlesManager particlesManager = ParticlesManager.getInstance();
                        particlesManager.arenaPointEffect(arena.getSpawn1(),300);
                        particlesManager.arenaPointEffect(arena.getSpawn2(),300);
                        particlesManager.arenaPointEffect(arena.getSpawn3(),300);
                        particlesManager.arenaPointEffect(arena.getSpawn4(),300);
                        particlesManager.arenaPointEffect(arena.getTower(),300);
                        break;
                    default:
                }
        }


    }

    private TextComponent component(int id, String arenaname) {
        switch (id) {
            case 1:
                return new TextComponent( new ComponentBuilder("[")
                        .color(net.md_5.bungee.api.ChatColor.GRAY)
                        .append("SPAWN1")
                        .color(ChatColor.GREEN)
                        .event(new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/fwriv arena "+ arenaname +" showpoint 1"))
                        .append("]")
                        .color(net.md_5.bungee.api.ChatColor.GRAY).create());
            case 2:
                return new TextComponent( new ComponentBuilder("[")
                        .color(net.md_5.bungee.api.ChatColor.GRAY)
                        .append("SPAWN2")
                        .color(ChatColor.GREEN)
                        .event(new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/fwriv arena " + arenaname + " showpoint 2"))
                        .append("]")
                        .color(net.md_5.bungee.api.ChatColor.GRAY).create());
            case 3:
                return new TextComponent( new ComponentBuilder("[")
                        .color(net.md_5.bungee.api.ChatColor.GRAY)
                        .append("SPAWN3")
                        .color(ChatColor.GREEN)
                        .event(new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/fwriv arena " + arenaname + " showpoint 3"))
                        .append("]")
                        .color(net.md_5.bungee.api.ChatColor.GRAY).create());
            case 4:
                return new TextComponent( new ComponentBuilder("[")
                        .color(net.md_5.bungee.api.ChatColor.GRAY)
                        .append("SPAWN4")
                        .color(ChatColor.GREEN)
                        .event(new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/fwriv arena " + arenaname + " showpoint 4"))
                        .append("]")
                        .color(net.md_5.bungee.api.ChatColor.GRAY).create());
            case 5:
                return new TextComponent( new ComponentBuilder("[")
                        .color(net.md_5.bungee.api.ChatColor.GRAY)
                        .append("TOWER/")
                        .color(ChatColor.GREEN)
                        .event(new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/fwriv arena " + arenaname + " showpoint 5"))
                        .append("]")
                        .color(net.md_5.bungee.api.ChatColor.GRAY).create());
        }
        return null;
    }

    private TextComponent tpcomp(int id, String arenaname) {
        return new TextComponent( new ComponentBuilder("[")
                .color(net.md_5.bungee.api.ChatColor.GRAY)
                .append("TP")
                .color(ChatColor.BLUE)
                .event(new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/fwriv arena " + arenaname + " tp " + id))
                .append("]")
                .color(net.md_5.bungee.api.ChatColor.GRAY).create());
    }


    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        if (args.length == 2)
            return ArenaManager.getInstance().getArenaNameList();
        else if(args.length == 3)
            return params;
        else if (args.length == 4) {
            if (args[2].equals("showpoint"))
                return id;
            else if (args[2].equals("tp"))
                return tpid;
            else
                return null;
        } else
            return null;
    }
}
