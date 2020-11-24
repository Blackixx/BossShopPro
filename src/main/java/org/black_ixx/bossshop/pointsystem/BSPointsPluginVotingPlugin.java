package org.black_ixx.bossshop.pointsystem;

import com.bencodez.votingplugin.user.UserManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;


public class BSPointsPluginVotingPlugin extends BSPointsPlugin {
    public BSPointsPluginVotingPlugin() {
        super("VotingPlugin", "VP");
    }


    @Override
    public double getPoints(OfflinePlayer player) {
        if (player instanceof Player) {
            return UserManager.getInstance().getVotingPluginUser((Player) player).getPoints();
        } else {
            return 0;
        }
    }

    @Override
    public double setPoints(OfflinePlayer player, double points) {
        if (player instanceof Player) {
            UserManager.getInstance().getVotingPluginUser((Player) player).setPoints((int) points);
            return points;
        } else {
            return 0;
        }
    }

    @Override
    public double takePoints(OfflinePlayer player, double points) {
        if (player instanceof Player) {
            UserManager.getInstance().getVotingPluginUser((Player) player).removePoints((int) points);
            return getPoints(player);
        } else {
            return 0;
        }
    }

    @Override
    public double givePoints(OfflinePlayer player, double points) {
        if (player instanceof Player) {
            UserManager.getInstance().getVotingPluginUser((Player) player).addPoints((int) points);
            return getPoints(player);
        } else {
            return 0;
        }
    }


    @Override
    public boolean usesDoubleValues() {
        return false;
    }

}
