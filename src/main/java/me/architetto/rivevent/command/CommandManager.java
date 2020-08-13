package me.architetto.rivevent.command;

import me.architetto.rivevent.command.subcommand.admin.CreateCommand;
import me.architetto.rivevent.command.subcommand.admin.DeleteCommand;
import me.architetto.rivevent.command.subcommand.superuser.*;
import me.architetto.rivevent.command.subcommand.user.JoinCommand;
import me.architetto.rivevent.command.subcommand.user.LeaveCommand;
import me.architetto.rivevent.command.subcommand.user.SpectateCommand;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandManager implements CommandExecutor{

    private final ArrayList<SubCommand> subcommands = new ArrayList<>();

    public CommandManager(){
        subcommands.add(new CreateCommand());
        subcommands.add(new ListCommand());
        subcommands.add(new DeleteCommand());
        subcommands.add(new InitCommand());
        subcommands.add(new StopCommand());
        subcommands.add(new SetupCommand());
        subcommands.add(new JoinCommand());
        subcommands.add(new StartCommand());
        subcommands.add(new SpectateCommand());
        subcommands.add(new LeaveCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatMessages.RED(Messages.NO_CONSOLE));
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

}
