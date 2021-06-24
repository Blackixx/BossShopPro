package org.black_ixx.bossshop.pointsystem;

import com.masterderpydoge.tokens.TokenCore;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public class BSPointsPluginMySQLTokens extends BSPointsPlugin {
    private TokenCore pp;

    public BSPointsPluginMySQLTokens() {
        super("MySQLTokens", "MySQLToken");

        Plugin tokens = ClassManager.manager.getPlugin().getServer().getPluginManager().getPlugin("MySQLTokens");
        if (tokens != null) {
            pp = ((TokenCore) tokens);
        }
    }

    @Override
    public double getPoints(OfflinePlayer player) {
        Player p = player.getPlayer();
        if (p == null) {
            return 0;
        }
        return pp.getTokens(p);
    }

    @Override
    public double setPoints(OfflinePlayer player, double points) {
        Player p = player.getPlayer();
        if (p == null) {
            return 0;
        }
        pp.setTokens(p, (int) points);

        return points;
    }

    @Override
    public double takePoints(OfflinePlayer player, double points) {
        Player p = player.getPlayer();
        if (p == null) {
            return 0;
        }
        pp.removeTokens(p, (int) points);

        return getPoints(player);
    }

    @Override
    public double givePoints(OfflinePlayer player, double points) {
        Player p = player.getPlayer();
        if (p == null) {
            return 0;
        }
        pp.addTokens(p, (int) points);

        return getPoints(player);
    }

    @Override
    public boolean usesDoubleValues() {
        return false;
    }

}
