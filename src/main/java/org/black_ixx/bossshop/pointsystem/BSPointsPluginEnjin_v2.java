package org.black_ixx.bossshop.pointsystem;

import com.enjin.officialplugin.points.ErrorConnectingToEnjinException;
import com.enjin.officialplugin.points.PlayerDoesNotExistException;
import com.enjin.officialplugin.points.PointsAPI;
import com.enjin.officialplugin.points.PointsAPI.Type;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.OfflinePlayer;


public class BSPointsPluginEnjin_v2 extends BSPointsPlugin {

    public BSPointsPluginEnjin_v2() {
        super("EnjinMinecraftPlugin");
    }

    public double getPoints(OfflinePlayer player) {
        try {
            return PointsAPI.getPointsForPlayer(player.getName());
        } catch (PlayerDoesNotExistException e) {
            ClassManager.manager.getBugFinder().warn("[Enjin Minecraft Plugin] Not able to get Player " + player.getName() + ". Not existing!");
        } catch (ErrorConnectingToEnjinException e) {
            ClassManager.manager.getBugFinder().warn("[Enjin Minecraft Plugin] Not able to connect to Enjin!");
        }
        return 0;
    }

    public double setPoints(OfflinePlayer player, double points) {
        PointsAPI.modifyPointsToPlayerAsynchronously(player.getName(), (int) points, Type.SetPoints);
        return points;
    }

    public double takePoints(OfflinePlayer player, double points) {
        String name = player.getName();
        try {
            return PointsAPI.modifyPointsToPlayer(name, (int) points, Type.RemovePoints);
        } catch (NumberFormatException e) {
            ClassManager.manager.getBugFinder().warn("[Enjin Minecraft Plugin] Not able to take Points... \"NumberFormatException\" Tried to take " + points + " Points from " + name + ".");
        } catch (PlayerDoesNotExistException e) {
            ClassManager.manager.getBugFinder().warn("[Enjin Minecraft Plugin] Not able to get Player " + name + ". Not existing!");
        } catch (ErrorConnectingToEnjinException e) {
            ClassManager.manager.getBugFinder().warn("[Enjin Minecraft Plugin] Not able to connect to Enjin!");
        }
        return getPoints(player);
    }

    public double givePoints(OfflinePlayer player, double points) {
        String name = player.getName();
        try {
            return PointsAPI.modifyPointsToPlayer(name, (int) points, Type.AddPoints);
        } catch (NumberFormatException e) {
            ClassManager.manager.getBugFinder().warn("[Enjin Minecraft Plugin] Not able to take Points... \"NumberFormatException\" Tried to take " + points + " Points from " + name + ".");
        } catch (PlayerDoesNotExistException e) {
            ClassManager.manager.getBugFinder().warn("[Enjin Minecraft Plugin] Not able to get Player " + name + ". Not existing!");
        } catch (ErrorConnectingToEnjinException e) {
            ClassManager.manager.getBugFinder().warn("[Enjin Minecraft Plugin] Not able to connect to Enjin!");
        }
        return getPoints(player);
    }

    @Override
    public boolean usesDoubleValues() {
        return false;
    }
}
