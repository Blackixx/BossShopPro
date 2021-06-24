package org.black_ixx.bossshop.core.conditions;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.misc.InputReader;
import org.black_ixx.bossshop.managers.serverpinging.ServerInfo;
import org.black_ixx.bossshop.managers.serverpinging.ServerPingingManager;
import org.bukkit.entity.Player;

public class BSConditionTypeServerpinging extends BSConditionTypeNumber {


    @Override
    public boolean meetsCondition(BSShopHolder holder, BSBuy shopitem, Player p, String conditiontype, String condition) {
        if (conditiontype.equalsIgnoreCase("online")) {
            ServerPingingManager m = ClassManager.manager.getServerPingingManager();
            if (m != null) {
                ServerInfo connector = m.getFirstInfo(shopitem);
                boolean b = InputReader.getBoolean(condition, true);
                if (connector != null) {
                    return connector.isOnline() == b;
                }
            }
            return false;
        }

        return super.meetsCondition(holder, shopitem, p, conditiontype, condition);
    }


    @Override
    public double getNumber(BSBuy shopitem, BSShopHolder holder, Player p) {
        ServerPingingManager m = ClassManager.manager.getServerPingingManager();
        if (m != null) {
            ServerInfo connector = ClassManager.manager.getServerPingingManager().getFirstInfo(shopitem);
            if (connector != null) {
                if (connector.isOnline()) {
                    return connector.getPlayers();
                }
            }
        }
        return 0;
    }

    @Override
    public boolean dependsOnPlayer() {
        return false;
    }

    @Override
    public String[] createNames() {
        return new String[]{"serverpinging", "serverping", "pinging", "ping"};
    }


    @Override
    public String[] showStructure() {
        return new String[]{"online", "over:[double]", "under:[double]", "equals:[double]", "between:[double]-[double]"};
    }


    @Override
    public void enableType() {
    }


}
