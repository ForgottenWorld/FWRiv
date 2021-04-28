package me.architetto.fwriv.command.user;

import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.command.CommandName;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;

public class InfoCommand extends SubCommand {
    @Override
    public String getName(){
        return CommandName.INFO_COMMAND;
    }

    @Override
    public String getDescription(){
        return "placeholder";
    }

    @Override
    public String getSyntax(){
        return "/fwriv " + CommandName.INFO_COMMAND;
    }

    @Override
    public String getPermission() {
        return "fwriv.info";
    }

    @Override
    public int getArgsRequired() {
        return 0;
    }

    @Override
    public void perform(Player sender, String[] args) {

        //wip, idea interessante

        ItemStack writtenBook = new ItemStack(Material.WRITTEN_BOOK,1);
        BookMeta bookMeta = (BookMeta) writtenBook.getItemMeta();
        bookMeta.setTitle("GUIDA EVENTO RIV");
        bookMeta.setAuthor("Architetto");
        List<String> pages = new ArrayList<>();
        pages.add(ChatColor.BLUE + "" + ChatColor.BOLD + "Guida 'Resta in vetta'\n" + ChatColor.RESET +
                "\n" +
                "Lo scopo del riv e' scalare la torre e sconfiggere tutti gli avversari.\n" +
                "\n" +
                "Raggiungendo la cima e' possibile ottenere dei premi estremamente utili.");
        pages.add("Alcuni items hanno abilita' particolari:\n" +
                "\n" +
                "Le " + ChatColor.BLUE + "" + ChatColor.BOLD + "snowballs" + ChatColor.RESET
                + " applicano un lieve knockback quando colpiscono un giocatore.\n" +
                "\n" +
                "Le " + ChatColor.BLUE + "" + ChatColor.BOLD + "ender pearls" + ChatColor.RESET
                + " permettono di teletrasportarsi sulla cima della torre.");
        pages.add("Mangiando il " + ChatColor.BLUE + "" + ChatColor.BOLD + "miele" + ChatColor.RESET
                + " si recupera un po di punti salute.\n" +
                "\n" +
                "La " + ChatColor.BLUE + "" + ChatColor.BOLD + "canna da pesca" + ChatColor.RESET
                + " puo' tirare i players.\n" +
                "\n" +
                "I " + ChatColor.BLUE + "" + ChatColor.BOLD + "fuochi d'artificio" + ChatColor.RESET
                + " permettono di volare per qualche metro in altezza.");

        pages.add("All'attivazione, il " + ChatColor.BLUE + "" + ChatColor.BOLD + "totem" + ChatColor.RESET
                + " oltre a salvarti la vita ti teletrasporta sulla cima della torre.");

        pages.add("Ci sono altre meccaniche utili da sfruttare durante l'evento:\n" +
                "\n" +
                "Colpire i " + ChatColor.BLUE + "" + ChatColor.BOLD + "targhet block" + ChatColor.RESET
                + " ti fa guadagnare dei premi utili durante l'evento.\n" +
                "\n" +
                "I " + ChatColor.BLUE + "" + ChatColor.BOLD + "punti fame" + ChatColor.RESET
                + " non scendono mai a zero.");

        bookMeta.setPages(pages);
        writtenBook.setItemMeta(bookMeta);
        sender.openBook(writtenBook);

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }
}
