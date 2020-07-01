package org.black_ixx.bossshop.pointsystem;

import me.justeli.coins.Coins;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

public class BSPointsPluginCoins extends BSPointsPlugin {
    public BSPointsPluginCoins() {
        super("Coins");
    }


    @Override
    public double getPoints(OfflinePlayer player) {
        return Coins.getEconomy().getBalance(player.getName());
    }

    @Override
    public double setPoints(OfflinePlayer player, double points) {
        double current = Coins.getEconomy().getBalance(player.getName());
        if (current > points) {
            Coins.getEconomy().withdrawPlayer(player.getName(), current - points);
        } else {
            Coins.getEconomy().depositPlayer(player.getName(), points - current);
        }
        return points;
    }

    @Override
    public double takePoints(OfflinePlayer player, double points) {
        EconomyResponse response = Coins.getEconomy().withdrawPlayer(player.getName(), points);
        return response.balance;
    }

    @Override
    public double givePoints(OfflinePlayer player, double points) {
        EconomyResponse response = Coins.getEconomy().depositPlayer(player.getName(), points);
        return response.balance;
    }

    @Override
    public boolean usesDoubleValues() {
        return false;
    }

}
