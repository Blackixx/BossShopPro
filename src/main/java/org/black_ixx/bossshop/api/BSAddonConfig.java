package org.black_ixx.bossshop.api;

import com.google.common.io.Files;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class BSAddonConfig implements BSAddonStorage {

    boolean isSaving = false;
    private Plugin plugin;
    private File file;
    private YamlConfiguration config;

    public BSAddonConfig(Plugin plugin, String file_name) {
        this.plugin = plugin;
        file = new File(ClassManager.manager.getPlugin().getDataFolder().getAbsolutePath() + "/addons/" + plugin.getName() + "/" + file_name + ".yml");
        config = YamlConfiguration.loadConfiguration(file);
    }

    public BSAddonConfig(Plugin plugin, File file) {
        this.plugin = plugin;
        this.file = file;
        config = YamlConfiguration.loadConfiguration(file);
    }

    public boolean save() {
        if (isSaving)
            return false;

        isSaving = true;

        try {
            config.save(file);
        } catch (IOException e1) {
            plugin.getLogger().warning("File I/O Exception on saving " + file.getName());
            e1.printStackTrace();
            return false;
        }

        isSaving = false;
        return true;
    }

    public void saveAsync() {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {

            @Override
            public void run() {
                save();
            }

        });
    }

    public boolean reload() {
        try {
            config = YamlConfiguration.loadConfiguration(file);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public File getFile() {
        return file;
    }


    @Override
    public void set(String path, Object value) {
        config.set(path, value);
    }

    @Override
    public String getString(String path, String def) {
        return config.getString(path, def);
    }

    @Override
    public int getInt(String path, int def) {
        return config.getInt(path, def);
    }

    @Override
    public double getDouble(String path, double def) {
        return config.getDouble(path, def);
    }

    @Override
    public boolean getBoolean(String path, boolean def) {
        return config.getBoolean(path, def);
    }

    @Override
    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }


    public void addDefault(String path, Object value) {
        if (!config.contains(path)) {
            config.set(path, value);
        }
    }

    @Override
    public boolean containsPath(String key) {
        return config.contains(key);
    }

    @Override
    public Set<String> listKeys(String section, boolean deep) {
        ConfigurationSection s = config;
        if (section != null) {
            s = s.getConfigurationSection(section);
        }
        if (s != null) {
            return s.getKeys(deep);
        }
        return null;
    }

    @Override
    public void deleteAll(String section) {
        ConfigurationSection s = config;
        if (section != null) {
            s = s.getConfigurationSection(section);
        }
        if (s != null) {
            for (String key : s.getKeys(true)) {
                s.set(key, null);
            }
            config.set(s.getCurrentPath(), null);
        }
    }

    @Override
    public boolean pasteContentFrom(BSAddonStorage source) {
        if (source instanceof BSAddonConfig) {
            BSAddonConfig c = (BSAddonConfig) source;
            try {
                source.save();
                Files.copy(c.getFile(), file);
                reload();
            } catch (IOException e) {
                ClassManager.manager.getBugFinder().warn("Unable to copy storage file from '" + c.getFile().getPath() + "' to '" + file.getPath() + "'.");
                return false;
            }
            return true;
        }

        //New storage types might be added in future
        return false;
    }

}
