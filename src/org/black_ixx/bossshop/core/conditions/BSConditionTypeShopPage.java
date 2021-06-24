package org.black_ixx.bossshop.core.conditions;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.bukkit.entity.Player;

public class BSConditionTypeShopPage extends BSConditionTypeNumber {

    @Override
    public boolean meetsCondition(BSShopHolder holder, BSBuy shopitem, Player p, String conditiontype, String condition) {
        return super.meetsCondition(holder, shopitem, p, conditiontype, transformLine(holder, shopitem, p, condition));
    }

    @Override
    public double getNumber(BSBuy shopitem, BSShopHolder holder, Player p) {
        return holder.getDisplayPage();
    }

    @Override
    public boolean dependsOnPlayer() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"shoppage", "page"};
    }


    @Override
    public void enableType() {
    }


    public String transformLine(BSShopHolder holder, BSBuy shopitem, Player p, String s) {
        s = s.replace("%maxpage%", String.valueOf(holder.getDisplayHighestPage()));
        s = s.replace("%maxshoppage%", String.valueOf(holder.getDisplayHighestPage()));
        return s;
    }


}
