package org.black_ixx.bossshop.core.conditions;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.bukkit.entity.Player;

public class BSSingleCondition implements BSCondition {

    private BSConditionType type;
    private String conditiontype, condition;


    public BSSingleCondition(BSConditionType type, String conditiontype, String condition) {
        this.type = type;
        this.conditiontype = conditiontype;
        this.condition = condition;
    }

    public boolean meetsCondition(BSShopHolder holder, BSBuy buy, Player p) {
        return type.meetsCondition(holder, buy, p, conditiontype, condition);
    }

    public BSConditionType getType() {
        return type;
    }

    public String getConditionType() {
        return conditiontype;
    }

    public String getCondition() {
        return condition;
    }

}
