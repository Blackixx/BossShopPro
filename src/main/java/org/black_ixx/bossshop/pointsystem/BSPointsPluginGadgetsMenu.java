package org.black_ixx.bossshop.pointsystem;

import com.yapzhenyie.GadgetsMenu.api.GadgetsMenuAPI;
import com.yapzhenyie.GadgetsMenu.player.PlayerManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;


public class BSPointsPluginGadgetsMenu extends BSPointsPlugin {


    public BSPointsPluginGadgetsMenu() {
        super("GadgetsMenu");
    }


    @Override
    public double getPoints(OfflinePlayer player) {
        Player p = player.getPlayer();
        if (p == null) {
            return 0;
        }
        PlayerManager playerManager = GadgetsMenuAPI.getPlayerManager(p);
        return playerManager.getMysteryDust();
    }

    @Override
    public double setPoints(OfflinePlayer player, double points) {
        Player p = player.getPlayer();
        if (p == null) {
            return -1;
        }
        PlayerManager playerManager = GadgetsMenuAPI.getPlayerManager(p);
        playerManager.setMysteryDust((int) points);
        return points;
    }

    @Override
    public double takePoints(OfflinePlayer player, double points) {
        Player p = player.getPlayer();
        if (p == null) {
            return -1;
        }
        PlayerManager playerManager = GadgetsMenuAPI.getPlayerManager(p);
        playerManager.removeMysteryDust((int) points);
        return playerManager.getMysteryDust();
    }

    @Override
    public double givePoints(OfflinePlayer player, double points) {
        Player p = player.getPlayer();
        if (p == null) {
            return -1;
        }
        PlayerManager playerManager = GadgetsMenuAPI.getPlayerManager(p);
        playerManager.addMysteryDust((int) points);
        return playerManager.getMysteryDust();
    }

    @Override
    public boolean usesDoubleValues() {
        return false;
    }

}
