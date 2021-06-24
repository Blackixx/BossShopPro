package org.black_ixx.bossshop.managers.features;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.config.ConfigLoader;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PageLayoutHandler {

    private List<BSBuy> items;
    private int max_rows = 6; //Default!
    private int reservedslots_start;
    private boolean show_if_multiple_pages_only;

    public PageLayoutHandler(List<BSBuy> items, int reservedslots_start, boolean show_if_multiple_pages_only) {
        this.items = items;
        this.reservedslots_start = reservedslots_start;
        this.show_if_multiple_pages_only = show_if_multiple_pages_only;
    }


    public PageLayoutHandler(BossShop plugin) {
        File f = new File(ClassManager.manager.getPlugin().getDataFolder().getAbsolutePath() + "/pagelayout.yml");
        try {
            FileConfiguration config = ConfigLoader.loadConfiguration(f, false);
            setup(config);

        } catch (InvalidConfigurationException e) {
            plugin.getClassManager().getBugFinder().severe("Unable to load '/plugins/" + BossShop.NAME + "/pagelayout.yml'. Reason: " + e.getMessage());
        }
    }

    public PageLayoutHandler(ConfigurationSection section) {
        setup(section);
    }


    public void setup(ConfigurationSection section) {
        max_rows = Math.max(1, section.getInt("MaxRows"));
        reservedslots_start = section.getInt("ReservedSlotsStart");
        show_if_multiple_pages_only = section.getBoolean("ShowIfMultiplePagesOnly");

        items = new ArrayList<BSBuy>();
        if (section.isConfigurationSection("items")) {
            for (String key : section.getConfigurationSection("items").getKeys(false)) {
                BSBuy buy = ClassManager.manager.getBuyItemHandler().loadItem(section.getConfigurationSection("items"), null, key);
                if (buy != null) {
                    items.add(buy);
                }
            }
        }
    }


    public List<BSBuy> getItems() {
        return items;
    }

    public int getMaxRows() {
        return max_rows;
    }

    public boolean showIfMultiplePagesOnly() {
        return show_if_multiple_pages_only;
    }

    /**
     * @return display slot start: Starts with slot 1.
     */
    public int getReservedSlotsStart() {
        return reservedslots_start;
    }

}
