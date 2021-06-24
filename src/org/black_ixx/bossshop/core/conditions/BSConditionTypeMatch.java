package org.black_ixx.bossshop.core.conditions;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.bukkit.entity.Player;

public abstract class BSConditionTypeMatch extends BSConditionType {

    @Override
    public boolean meetsCondition(BSShopHolder holder, BSBuy shopitem, Player p, String conditiontype, String condition) {
        if (conditiontype.equalsIgnoreCase("match")) {
            return isCorrect(p, true, condition);
        }
        if (conditiontype.equalsIgnoreCase("dontmatch")) {
            return isCorrect(p, false, condition);
        }
        return false;
    }


    private boolean isCorrect(Player p, boolean has_to_match, String condition) {
        for (String single_condition : condition.split(",")) {
            if (matches(p, single_condition) == has_to_match) {
                return true;
            }
        }
        return false;
    }


    @Override
    public String[] showStructure() {
        return new String[]{"match:[string]", "dontmatch:[string]"};
    }


    public abstract boolean matches(Player p, String single_condition);

}
