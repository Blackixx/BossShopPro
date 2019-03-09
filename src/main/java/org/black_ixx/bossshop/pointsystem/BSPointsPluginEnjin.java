package org.black_ixx.bossshop.pointsystem;

import com.enjin.core.EnjinServices;
import com.enjin.rpc.mappings.mappings.general.RPCData;
import com.enjin.rpc.mappings.services.PointService;
import org.bukkit.OfflinePlayer;


public class BSPointsPluginEnjin extends BSPointsPlugin {

    public BSPointsPluginEnjin() {
        super("EnjinMinecraftPlugin");
    }

    public double getPoints(OfflinePlayer player) {
        RPCData<Integer> data = EnjinServices.getService(PointService.class).get(player.getName());
        if (data == null || data.getResult() == null) {
            return 0;
        }
        return data.getResult();
    }

    public double setPoints(OfflinePlayer player, double points) {
        EnjinServices.getService(PointService.class).set(player.getName(), (int) points);
        return points;
    }

    public double takePoints(OfflinePlayer player, double points) {
        EnjinServices.getService(PointService.class).remove(player.getName(), (int) points);
        return getPoints(player);
    }

    public double givePoints(OfflinePlayer player, double points) {
        EnjinServices.getService(PointService.class).add(player.getName(), (int) points);
        return getPoints(player);
    }

    @Override
    public boolean usesDoubleValues() {
        return false;
    }
}
