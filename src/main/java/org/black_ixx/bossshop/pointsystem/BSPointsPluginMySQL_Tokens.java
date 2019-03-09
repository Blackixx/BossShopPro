package org.black_ixx.bossshop.pointsystem;

import me.bukkit.mTokens.Inkzzz.Tokens;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class BSPointsPluginMySQL_Tokens extends BSPointsPlugin {
    private Tokens pp;

    public BSPointsPluginMySQL_Tokens() {
        super("MySQL-Tokens", "MySQL-Token");

        Plugin tokens = ClassManager.manager.getPlugin().getServer().getPluginManager().getPlugin("MySQL-Tokens");
        if (tokens != null) {
            pp = ((Tokens) tokens);
        }
    }

    @Override
    public double getPoints(OfflinePlayer player) {
        Player p = player.getPlayer();
        if (p == null) {
            return 0;
        }
        return pp.getAPI().getTokens(p);
    }

    @Override
    public double setPoints(OfflinePlayer player, double points) {
        Player p = player.getPlayer();
        if (p == null) {
            return 0;
        }
        pp.getAPI().resetTokens(p);
        pp.getAPI().addTokens(p, (int) points);

        return points;
    }

    @Override
    public double takePoints(OfflinePlayer player, double points) {
        Player p = player.getPlayer();
        if (p == null) {
            return 0;
        }
        pp.getAPI().takeTokens(p, (int) points);

        return getPoints(player);
    }

    @Override
    public double givePoints(OfflinePlayer player, double points) {
        Player p = player.getPlayer();
        if (p == null) {
            return 0;
        }
        pp.getAPI().addTokens(p, (int) points);

        return getPoints(player);
    }

    @Override
    public boolean usesDoubleValues() {
        return false;
    }

}
