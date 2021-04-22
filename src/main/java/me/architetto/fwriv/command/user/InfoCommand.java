package me.architetto.fwriv.command.user;

import me.architetto.fwriv.command.SubCommand;
import me.architetto.fwriv.utils.ChatFormatter;
import me.architetto.fwriv.command.CommandName;
import me.architetto.fwriv.utils.Messages;
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
        return "rivevent.info";
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
        pages.add(ChatColor.BLUE + "Guida 'Resta in vetta'\n" + ChatColor.RESET +
                "\n" +
                "L'evento 'Resta In Vetta' (RIV) decreta come vincitore l'ultimo giocatore che rimane in vita." +
                " A cosa serve scalare la torre ? E' molto semplice , raggiungendo la cima della torre ricevi " +
                "dei premi (Rewards) estremamente utili per sopravvivere come");
        pages.add("cibo, pezzi di armatura e  oggetti con abilita' particolari.\n" +
                "La barra della fame non schende mai a zero ma per recuperare la vita che potresti perdere" +
                " e' indispensabile tenerla sempre al massimo.\n" +
                ChatColor.BLUE + "     ANTI CAMPER\n" + ChatColor.RESET +
                "Per evitare atteggiamenti furbi, ad");
        pages.add("un certo momento si attiva " +
                ChatColor.GREEN + "l'anti camper" + ChatColor.RESET +
                ", che consiste in un altezza minima da dover raggiungere " +
                "sotto la quale si riceve continuamente danno. L'altezza minima e' indicata tramite un effetto " +
                "particellare impossibile da non notare oltre ad essere indicata in chat.");
        bookMeta.setPages(pages);
        writtenBook.setItemMeta(bookMeta);

        sender.openBook(writtenBook);

        /*

        sender.sendMessage(ChatFormatter.chatHeaderGameplayInfo());
        sender.sendMessage(Messages.RIV_GAMEPLAY_INFO);
        sender.sendMessage(ChatFormatter.chatFooter());

         */

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }
}
