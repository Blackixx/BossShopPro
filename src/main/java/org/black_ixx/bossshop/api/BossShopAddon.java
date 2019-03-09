package org.black_ixx.bossshop.api;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.misc.ClassTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class BossShopAddon extends JavaPlugin {


    String USER = "%%__USER__%%";
    private BossShop bs;
    private boolean b = false;

    /////// //// //// //// //// ////

    @Override
    public void onEnable() {
        b = false;

        Plugin plugin = Bukkit.getPluginManager().getPlugin(BossShop.NAME);

        if (plugin == null) {
            printSevere(BossShop.NAME + " was not found... you need it in order to run " + getAddonName() + "! Get it here: https://www.spigotmc.org/resources/25699/. Version v" + getRequiredBossShopVersion() + " or newer is required!");
            printInfo("Disabling Addon...");
            b = true;
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        bs = (BossShop) plugin;

        double bs_worth = getWorth(bs.getDescription().getVersion());
        double ao_worth = getWorth(getRequiredBossShopVersion());
        if (bs_worth < ao_worth) {
            printSevere(BossShop.NAME + " was found but it seems to be outdated... you need v" + getRequiredBossShopVersion() + " or newer in order to run " + getAddonName() + "! Get it here: https://www.spigotmc.org/resources/25699/");
            printInfo("Disabling Addon...");
            b = true;
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        bs.getAPI().addEnabledAddon(this);

        BossShop.log("Enabling Addon " + getAddonName());
        enable();

    }

    @Override
    public void onDisable() {
        if (b) {
            return;
        }

        BossShop.log("Disabling Addon " + getAddonName());
        disable();
    }


    protected void enable() { //Can be overwritten
        enableAddon();
    }

    protected void disable() { //Can be overwritten
        disableAddon();
    }

    public void reload(CommandSender sender) { //Can be overwritten
        bossShopReloaded(sender);
        sender.sendMessage(ChatColor.YELLOW + "Reloaded " + BossShop.NAME + " Addon " + ChatColor.GOLD + getAddonName());
    }

    /////// //// //// //// //// ////

    public void printSevere(String msg) {
        Bukkit.getLogger().severe("[" + getAddonName() + "] " + msg);
    }

    public void printWarning(String msg) {
        Bukkit.getLogger().warning("[" + getAddonName() + "] " + msg);
    }

    public void printInfo(String msg) {
        Bukkit.getLogger().info("[" + getAddonName() + "] " + msg);
    }

    public final BossShop getBossShop() {
        return bs;
    }

    /////// //// //// //// //// ////

    protected double getWorth(String s) {
        try {
            if (s == null || s == "" || s.length() < 1) {
                return 0;
            }
            double x = 0;
            String[] parts = s.replace(".", ":").split(":");
            x += Integer.parseInt(parts[0].trim());
            if (parts.length == 2) {
                x += 0.1 * Integer.parseInt(parts[1].trim());
            }
            if (parts.length == 3) {
                x += 0.1 * Integer.parseInt(parts[1].trim());
                x += 0.01 * Integer.parseInt(parts[2].trim());
            }
            return x;
        } catch (Exception e) {
            printWarning("Was not able to get the version of " + s);
            return 1.00;
        }
    }

    public BSAddonStorage createStorage(Plugin plugin, String name) {
        int type = BSAddonStorage.TYPE_LOCAL_FILE; //Maybe add an option to store data elsewhere in future

        switch (type) {
            case BSAddonStorage.TYPE_LOCAL_FILE:
                return new BSAddonConfig(plugin, name);
        }

        return null;
    }

    /////// //// //// //// //// ////

    public abstract String getAddonName();

    public abstract String getRequiredBossShopVersion();

    public abstract void enableAddon();

    public abstract void bossShopFinishedLoading();

    public abstract void disableAddon();

    public abstract void bossShopReloaded(CommandSender sender);
}
