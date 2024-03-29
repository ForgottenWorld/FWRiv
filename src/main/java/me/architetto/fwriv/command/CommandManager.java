package me.architetto.fwriv.command;

import me.architetto.fwriv.command.admin.CreateCommand;
import me.architetto.fwriv.command.admin.DeleteCommand;
import me.architetto.fwriv.command.admin.ReloadCommand;
import me.architetto.fwriv.command.superuser.*;
import me.architetto.fwriv.command.user.GameCommand;
import me.architetto.fwriv.command.user.JoinCommand;
import me.architetto.fwriv.command.user.LeaveCommand;
import me.architetto.fwriv.localization.Message;
import me.architetto.fwriv.utils.MessageUtil;
import me.architetto.fwriv.utils.NameUtil;
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
        subcommands.add(new DeleteCommand());
        subcommands.add(new InitCommand());
        subcommands.add(new RestartCommand());
        subcommands.add(new StopCommand());
        subcommands.add(new JoinCommand());
        subcommands.add(new StartCommand());
        subcommands.add(new LeaveCommand());
        subcommands.add(new GameCommand());
        subcommands.add(new ArenaCommand());
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Error: can't run commands from console");
            return true;
        }

        Player p = (Player) sender;

        if (args.length > 0) {
            for (int i = 0; i < getSubcommands().size(); i++) {
                if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())) {

                    SubCommand subCommand = getSubcommands().get(i);

                    if (!sender.hasPermission(subCommand.getPermission())) {
                        Message.ERR_PERMISSION.send(sender);
                        return true;
                    }

                    if (args.length < subCommand.getArgsRequired()) {
                        Message.ERR_SYNTAX.send(sender);
                        return true;
                    }

                    subCommand.perform(p, args);

                }
            }
        }else{
            p.sendMessage(MessageUtil.commandsInfoHeader());

            for (int i = 0; i < getSubcommands().size(); i++) {

                SubCommand subCommand = getSubcommands().get(i);

                if (!sender.hasPermission(subCommand.getPermission()))
                    continue;

                p.sendMessage(MessageUtil.formatListMessage(subCommand.getSyntax()
                        + " - " + subCommand.getDescription()));
            }

            p.sendMessage(MessageUtil.chatFooter());
        }

        return true;
    }

    public ArrayList<SubCommand> getSubcommands(){
        return subcommands;
    }


    @SuppressWarnings("NullableProblems")
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        String argChar;

        if (args.length == 1) {
            argChar = args[0];

            ArrayList<String> subcommandsArguments = new ArrayList<>();

            for (int i = 0; i < getSubcommands().size(); i++) {
                SubCommand subCommand = getSubcommands().get(i);

                if (!sender.hasPermission(subCommand.getPermission()))
                    continue;

                subcommandsArguments.add(subCommand.getName());
            }

            return NameUtil.filterByStart(subcommandsArguments,argChar);

        }else if (args.length >= 2) {
            argChar = args[args.length - 1];

            for (int i = 0; i < getSubcommands().size(); i++) {
                if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())) {
                    return NameUtil.filterByStart(getSubcommands().get(i).getSubcommandArguments((Player) sender, args),argChar);
                }
            }
        }

        return null;
    }

}
