package org.black_ixx.bossshop.core.conditions;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.entity.Player;

public class BSConditionTypePoints extends BSConditionTypeNumber {

    @Override
    public double getNumber(BSBuy shopitem, BSShopHolder holder, Player p) {
        return ClassManager.manager.getPointsManager().getPoints(p);
    }

    @Override
    public boolean dependsOnPlayer() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"points"};
    }


    @Override
    public void enableType() {
        ClassManager.manager.getSettings().setPointsEnabled(true);
    }


}
