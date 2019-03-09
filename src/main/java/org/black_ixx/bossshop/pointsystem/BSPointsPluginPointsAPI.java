package org.black_ixx.bossshop.pointsystem;

import me.BukkitPVP.PointsAPI.PointsAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class BSPointsPluginPointsAPI extends BSPointsPlugin {

    public BSPointsPluginPointsAPI() {
        super("PointsAPI", "PAPI");
    }

    @Override
    public double getPoints(OfflinePlayer player) {
        if (player instanceof Player) {
            return PointsAPI.getPoints((Player) player);
        }
        return PointsAPI.getPoints(player);
    }

    @Override
    public double setPoints(OfflinePlayer player, double points) {
        if (player instanceof Player) {
            PointsAPI.setPoints((Player) player, (int) points);
        } else {
            PointsAPI.setPoints(player, (int) points);
        }
        return points;
    }

    @Override
    public double takePoints(OfflinePlayer player, double points) {
        if (player instanceof Player) {
            PointsAPI.removePoints((Player) player, (int) points);
        } else {
            PointsAPI.removePoints(player, (int) points);
        }
        return getPoints(player);
    }

    @Override
    public double givePoints(OfflinePlayer player, double points) {
        if (player instanceof Player) {
            PointsAPI.addPoints((Player) player, (int) points);
        } else {
            PointsAPI.addPoints(player, (int) points);
        }
        return getPoints(player);
    }

    @Override
    public boolean usesDoubleValues() {
        return false;
    }

}
