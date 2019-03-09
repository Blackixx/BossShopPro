package org.black_ixx.bossshop.pointsystem;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public abstract class BSPointsPlugin {
    private final String name;
    private final String[] nicknames;
    private final boolean available;

    public BSPointsPlugin(String main_name, String... nicknames) {
        this.name = main_name;
        this.nicknames = nicknames;
        this.available = Bukkit.getPluginManager().getPlugin(name) != null;
    }

    public String getName() {
        return name;
    }

    public String[] getNicknames() {
        return nicknames;
    }

    public boolean isAvailable() {
        return available;
    }

    public abstract double getPoints(OfflinePlayer player);

    public abstract double setPoints(OfflinePlayer player, double points);

    public abstract double takePoints(OfflinePlayer player, double points);

    public abstract double givePoints(OfflinePlayer player, double points);

    public abstract boolean usesDoubleValues();


    public void register() {
        BSPointsAPI.register(this);
    }
}
