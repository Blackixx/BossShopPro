package org.black_ixx.bossshop.pointsystem;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import pgDev.bukkit.CommandPoints.CommandPoints;

public class BSPointsPluginCommandPoints extends BSPointsPlugin {
    private pgDev.bukkit.CommandPoints.CommandPointsAPI pp;
    private BossShop plugin;

    public BSPointsPluginCommandPoints() {
        super("CommandPoints", "CP");

        plugin = ClassManager.manager.getPlugin();
        Plugin commandPoints = plugin.getServer().getPluginManager().getPlugin("CommandPoints");
        if (commandPoints != null) {
            pp = ((CommandPoints) commandPoints).getAPI();
        }
    }

    @Override
    public double getPoints(OfflinePlayer player) {
        return pp.getPoints(player.getName(), plugin);
    }

    @Override
    public double setPoints(OfflinePlayer player, double points) {
        pp.setPoints(player.getName(), (int) points, plugin);
        return points;
    }

    @Override
    public double takePoints(OfflinePlayer player, double points) {
        pp.removePoints(player.getName(), (int) points, "Purchase", plugin);
        return getPoints(player);
    }

    @Override
    public double givePoints(OfflinePlayer player, double points) {
        pp.addPoints(player.getName(), (int) points, "Reward", plugin);
        return getPoints(player);
    }

    @Override
    public boolean usesDoubleValues() {
        return false;
    }

}
