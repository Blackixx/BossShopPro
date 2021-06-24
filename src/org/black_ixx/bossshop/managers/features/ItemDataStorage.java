package org.black_ixx.bossshop.managers.features;

import org.black_ixx.bossshop.BossShop;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ItemDataStorage {


    private final BossShop plugin;
    private final String fileName = "ItemDataStorage.yml";
    private final File file;
    private FileConfiguration config = null;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy dd-MM 'at' hh:mm:ss a (E)");

    public ItemDataStorage(final BossShop plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder().getAbsolutePath(), fileName);
        reloadConfig();
    }

    public FileConfiguration getConfig() {
        if (config == null)
            reloadConfig();

        return config;
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(file);
        InputStream defConfigStream = plugin.getResource(fileName);
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
            config.setDefaults(defConfig);
        }
    }

    public void saveConfig() {
        try {
            getConfig().save(file);
        } catch (IOException e) {
            BossShop.log("Could not save " + fileName + " to " + file);
        }
    }

    public String getDate() {
        Date dNow = new Date();
        return formatter.format(dNow);
    }

    public void addItemData(String playername, List<String> itemdata) {
        config.set(playername + "." + getDate(), itemdata);
        saveConfig();
    }


}
