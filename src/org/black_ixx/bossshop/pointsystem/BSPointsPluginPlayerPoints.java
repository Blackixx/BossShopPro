package org.black_ixx.bossshop.pointsystem;

import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

public class BSPointsPluginPlayerPoints extends BSPointsPlugin {
    private PlayerPoints pp;

    public BSPointsPluginPlayerPoints() {
        super("PlayerPoints", "PlayerPoint", "PP");

        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("PlayerPoints");
        if (plugin != null) {
            pp = (PlayerPoints.class.cast(plugin));
        }
    }

    @Override
    public double getPoints(OfflinePlayer player) {
        return pp.getAPI().look(player.getUniqueId());
    }

    @Override
    public double setPoints(OfflinePlayer player, double points) {
        pp.getAPI().set(player.getUniqueId(), (int) points);
        return points;
    }

    @Override
    public double takePoints(OfflinePlayer player, double points) {
        pp.getAPI().take(player.getUniqueId(), (int) points);
        return getPoints(player);
    }

    @Override
    public double givePoints(OfflinePlayer player, double points) {
        pp.getAPI().give(player.getUniqueId(), (int) points);
        return getPoints(player);
    }

    @Override
    public boolean usesDoubleValues() {
        return false;
    }

}
