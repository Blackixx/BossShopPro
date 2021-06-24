package org.black_ixx.bossshop.pointsystem;

import org.bukkit.OfflinePlayer;
import org.kingdoms.constants.kingdom.Kingdom;
import org.kingdoms.constants.player.OfflineKingdomPlayer;
import org.kingdoms.manager.game.GameManagement;

public class BSPointsPluginKingdoms extends BSPointsPlugin {


    public BSPointsPluginKingdoms() {
        super("Kingdoms");
    }


    @Override
    public double getPoints(OfflinePlayer player) {
        OfflineKingdomPlayer p = GameManagement.getPlayerManager().getOfflineKingdomPlayer(player);
        if (p.getKingdomName() != null) {
            Kingdom kingdom = GameManagement.getKingdomManager().getOrLoadKingdom(p.getKingdomName());
            if (kingdom != null) {
                return kingdom.getResourcepoints();
            }
        }
        return 0;
    }

    @Override
    public double setPoints(OfflinePlayer player, double points) {
        OfflineKingdomPlayer p = GameManagement.getPlayerManager().getOfflineKingdomPlayer(player);
        if (p.getKingdomName() != null) {
            Kingdom kingdom = GameManagement.getKingdomManager().getOrLoadKingdom(p.getKingdomName());
            if (kingdom != null) {
                kingdom.setResourcepoints((int) points);
                return points;
            }
        }
        return 0;
    }

    @Override
    public double takePoints(OfflinePlayer player, double points) {
        OfflineKingdomPlayer p = GameManagement.getPlayerManager().getOfflineKingdomPlayer(player);
        if (p.getKingdomName() != null) {
            Kingdom kingdom = GameManagement.getKingdomManager().getOrLoadKingdom(p.getKingdomName());
            if (kingdom != null) {
                int current = kingdom.getResourcepoints();
                int result = current - (int) points;
                kingdom.setResourcepoints(result);
                return result;
            }
        }
        return 0;
    }

    @Override
    public double givePoints(OfflinePlayer player, double points) {
        OfflineKingdomPlayer p = GameManagement.getPlayerManager().getOfflineKingdomPlayer(player);
        if (p.getKingdomName() != null) {
            Kingdom kingdom = GameManagement.getKingdomManager().getOrLoadKingdom(p.getKingdomName());
            if (kingdom != null) {
                int current = kingdom.getResourcepoints();
                int result = current + (int) points;
                kingdom.setResourcepoints(result);
                return result;
            }
        }
        return 0;
    }


    @Override
    public boolean usesDoubleValues() {
        return false;
    }

}
