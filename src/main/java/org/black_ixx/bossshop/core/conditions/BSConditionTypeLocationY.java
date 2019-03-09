package org.black_ixx.bossshop.core.conditions;


import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.bukkit.entity.Player;

public class BSConditionTypeLocationY extends BSConditionTypeNumber {

    @Override
    public double getNumber(BSBuy shopitem, BSShopHolder holder, Player p) {
        return p.getLocation().getY();
    }

    @Override
    public boolean dependsOnPlayer() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"locationy", "y"};
    }


    @Override
    public void enableType() {
    }


}
