package org.black_ixx.bossshop.pointsystem;

import org.bukkit.OfflinePlayer;

public class BSPointsPluginNone extends BSPointsPlugin {

    public BSPointsPluginNone() {
        super("None");
    }

    public double getPoints(OfflinePlayer player) {
        return -1;
    }

    public double setPoints(OfflinePlayer player, double points) {
        return -1;
    }

    public double takePoints(OfflinePlayer player, double points) {
        return -1;
    }

    public double givePoints(OfflinePlayer player, double points) {
        return -1;
    }

    @Override
    public boolean usesDoubleValues() {
        return false;
    }
}
