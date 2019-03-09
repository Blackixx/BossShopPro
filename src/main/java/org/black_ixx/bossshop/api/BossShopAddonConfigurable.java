package org.black_ixx.bossshop.api;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class BossShopAddonConfigurable extends BossShopAddon {


    private BSAddonConfig config;


    protected void enable() {
        config = new BSAddonConfig(this, "config");
        super.enable();
    }

    protected void disable() {
        super.disable();
        if (saveConfigOnDisable()) {
            config.save();
        }
    }

    public void reload(CommandSender sender) {
        config.reload();
        super.reload(sender);
    }


    @Override
    public FileConfiguration getConfig() {
        return config.getConfig();
    }

    public BSAddonConfig getAddonConfig() {
        return config;
    }

    @Override
    public void reloadConfig() {
        config.reload();
    }

    @Override
    public void saveConfig() {
        config.save();
    }

    public abstract boolean saveConfigOnDisable();


}
