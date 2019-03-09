package org.black_ixx.bossshop.managers;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MessageHandler {
    private final BossShop plugin;
    private final String fileName = "messages.yml";
    private final File file;
    private FileConfiguration config = null;

    public MessageHandler(final BossShop plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder().getAbsolutePath(), fileName);
        config = YamlConfiguration.loadConfiguration(this.file);
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
            ClassManager.manager.getBugFinder().warn("Could not save message config to " + file);
        }
    }

    public void sendMessage(String node, CommandSender sender) {
        sendMessage(node, sender, null, null, null, null, null);
    }

    public void sendMessage(String node, CommandSender sender, String offline_target) {
        sendMessage(node, sender, offline_target, null, null, null, null);
    }

    public void sendMessage(String node, CommandSender sender, Player target) {
        sendMessage(node, sender, null, target, null, null, null);
    }

    public void sendMessage(String node, CommandSender sender, String offline_target, Player target, BSShop shop, BSShopHolder holder, BSBuy item) {
        if (sender != null) {

            if (node == null || node == "") {
                return;
            }

            String message = get(node, target, shop, holder, item);

            if (message == null || message.isEmpty() || message.length() < 2) {
                return;
            }

            if (offline_target != null) {
                message = message.replace("%player%", offline_target).replace("%name%", offline_target).replace("%target%", offline_target);
            }

            sendMessageDirect(message, sender);
        }
    }

    public void sendMessageDirect(String message, CommandSender sender) {
        if (sender != null) {

            if (message == null || message.isEmpty() || message.length() < 2) {
                return;
            }

            String colors = "";
            for (String line : message.split("\\\\n")) {
                sender.sendMessage(colors + line);
                colors = ChatColor.getLastColors(line);
            }
        }
    }


    public String get(String node) {
        return get(node, null, null, null, null);
    }

    public String getRaw(String node) {
        return config.getString(node, node);
    }

    private String get(String node, Player target, BSShop shop, BSShopHolder holder, BSBuy item) {
        return replace(config.getString(node, node), target, shop, holder, item);
    }

    private String replace(String message, Player target, BSShop shop, BSShopHolder holder, BSBuy item) {
        return ClassManager.manager.getStringManager().transform(message, item, shop, holder, target);
    }


}