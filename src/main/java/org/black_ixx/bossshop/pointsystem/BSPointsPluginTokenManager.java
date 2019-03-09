package org.black_ixx.bossshop.pointsystem;

import me.realized.tokenmanager.TokenManagerPlugin;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.OptionalLong;


public class BSPointsPluginTokenManager extends BSPointsPlugin {


    private TokenManagerPlugin tm;

    public BSPointsPluginTokenManager() {
        super("TokenManager", "TM");
        tm = TokenManagerPlugin.getInstance();
    }


    @Override
    public double getPoints(OfflinePlayer player) {
        if (player instanceof Player) {
            OptionalLong l = tm.getTokens((Player) player);
            return l.isPresent() ? l.getAsLong() : 0;
        }
        return -1;
    }

    @Override
    public double setPoints(OfflinePlayer player, double points) {
        if (player instanceof Player) {
            tm.setTokens((Player) player, (long) points);
            return points;
        } else {
            return -1;
        }
    }

    @Override
    public double takePoints(OfflinePlayer player, double points) {
        if (player instanceof Player) {
            return this.setPoints(player, this.getPoints(player) - points);
        } else {
            return -1;
        }
    }

    @Override
    public double givePoints(OfflinePlayer player, double points) {
        if (player instanceof Player) {
            return this.setPoints(player, this.getPoints(player) + points);
        } else {
            return -1;
        }
    }


    @Override
    public boolean usesDoubleValues() {
        return false;
    }

}
