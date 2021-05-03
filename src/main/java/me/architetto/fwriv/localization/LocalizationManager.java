package me.architetto.fwriv.localization;

import me.architetto.fwriv.config.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.*;

public class LocalizationManager {

    private static LocalizationManager localizationManager;

    private final Map<String, String> strings;

    private final Map<Material, List<String>> specialItemLore;

    private final ItemStack infoBook;

    private LocalizationManager() {
        if (localizationManager != null)
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        this.strings = new HashMap<>();
        this.specialItemLore = new HashMap<>();
        this.infoBook = new ItemStack(Material.WRITTEN_BOOK,1);
    }

    public static LocalizationManager getInstance() {
        if(localizationManager == null)
            localizationManager = new LocalizationManager();
        return localizationManager;
    }

    public void loadLanguageFile() {

        FileConfiguration localization = ConfigManager.getInstance().getConfig("Strings.yml");

        ConfigurationSection strings = Objects.requireNonNull(localization.getConfigurationSection("strings"));
        for (String key : strings.getKeys(false)) {
            this.strings.put(key, localization.getString("strings." + key));
        }
        ConfigurationSection itemsLore = Objects.requireNonNull(localization.getConfigurationSection("special_item_lore"));
        for (String key : itemsLore.getKeys(false)) {
            this.specialItemLore.put(Material.getMaterial(key), coloredStringList(localization.getStringList("special_item_lore." + key)));
        }

        BookMeta bookMeta = (BookMeta) this.infoBook.getItemMeta();
        bookMeta.setTitle("FWRiv guide");
        bookMeta.setAuthor("Architetto");
        bookMeta.setPages(coloredStringList(localization.getStringList("gameplay_info_strings")));
        this.infoBook.setItemMeta(bookMeta);

    }

    public void reload() {
        this.strings.clear();
        this.specialItemLore.clear();
        loadLanguageFile();
    }

    public String localizeString(String key) {
        return this.strings.containsKey(key) ? this.strings.get(key) : ChatColor.RED + "No translation present for " + key;
    }

    public List<String> localizeItemLore(Material material) {
        return this.specialItemLore.get(material);
    }

    private List<String> coloredStringList(List<String> stringList) {
        for (ListIterator<String> iter = stringList.listIterator(); iter.hasNext();)
            iter.set(ChatColor.translateAlternateColorCodes('&',iter.next()));
        return stringList;
    }

    public ItemStack getInfoBook() {
        return this.infoBook;
    }

}
