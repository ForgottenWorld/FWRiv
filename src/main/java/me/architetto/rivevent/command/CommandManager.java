package me.architetto.rivevent.command;

import me.architetto.rivevent.command.admin.CreateCommand;
import me.architetto.rivevent.command.admin.DeleteCommand;
import me.architetto.rivevent.command.admin.ReloadCommand;
import me.architetto.rivevent.command.superuser.*;
import me.architetto.rivevent.command.user.InfoCommand;
import me.architetto.rivevent.command.user.JoinCommand;
import me.architetto.rivevent.command.user.LeaveCommand;
import me.architetto.rivevent.util.ChatFormatter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements TabExecutor{

    private final ArrayList<SubCommand> subcommands = new ArrayList<>();

    public CommandManager(){
        subcommands.add(new CreateCommand());
        subcommands.add(new ReloadCommand());
        subcommands.add(new ArenaCommand());
        subcommands.add(new DeleteCommand());
        subcommands.add(new InitCommand());
        subcommands.add(new RestartCommand());
        subcommands.add(new StopCommand());
        subcommands.add(new SetCommand());
        subcommands.add(new JoinCommand());
        subcommands.add(new StartCommand());
        subcommands.add(new MinigameCommand());
        subcommands.add(new LeaveCommand());
        subcommands.add(new EventCommand());
        subcommands.add(new InfoCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Error: can't run commands from console"));
            return true;
        }

        Player p = (Player) sender;

        if (args.length > 0) {
            for (int i = 0; i < getSubcommands().size(); i++){
                if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())){
                        getSubcommands().get(i).perform(p, args);
                }
            }
        }else{
            p.sendMessage("--------------------------------");
            for (int i = 0; i < getSubcommands().size(); i++){
                p.sendMessage(getSubcommands().get(i).getSyntax() + " - " + getSubcommands().get(i).getDescription());
            }
            p.sendMessage("--------------------------------");
        }

        return true;
    }

    public ArrayList<SubCommand> getSubcommands(){
        return subcommands;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (args.length == 1) {
            ArrayList<String> subcommandsArguments = new ArrayList<>();

            for (int i = 0; i < getSubcommands().size(); i++){
                subcommandsArguments.add(getSubcommands().get(i).getName());
            }

            return subcommandsArguments;

        }else if (args.length >= 2) {
            for (int i = 0; i < getSubcommands().size(); i++) {
                if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())) {
                    return getSubcommands().get(i).getSubcommandArguments((Player) sender, args);
                }
            }
        }

        return null;
    }

}
