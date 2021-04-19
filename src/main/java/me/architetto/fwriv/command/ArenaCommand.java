package me.architetto.fwriv.command;

import me.architetto.fwriv.arena.ArenaManager;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class ArenaCommand extends SubCommand{
    @Override
    public String getName() {
        return CommandName.ARENA_COMMAND;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getSyntax() {
        return null;
    }

    @Override
    public String getPermission() {
        return "frwriv.arena";
    }

    @Override
    public int getArgsRequired() {
        return 3;
    }

    private static final List<String> arenaParam = Arrays.asList(
            "new",
            "remove",
            "show",
            "info",
            "tp"
    );

    @Override
    public void perform(Player sender, String[] args) {

        String arenaName = String.join("_", Arrays.copyOfRange(args, 2, args.length));

        switch (args[1].toLowerCase()) {
            case "new":

                ArenaManager arenaManager = ArenaManager.getInstance();

                if (arenaManager.isPlayerInCreationMode(sender))
                    return;

                if (arenaManager.isArenaName(arenaName))
                    return;

                arenaManager.addPlayerToArenaCreation(sender, arenaName);

            case "remove":
            case "show":
            case "info":
            case "tp":
        }



    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
