package org.black_ixx.bossshop.core.conditions;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.bukkit.entity.Player;

public interface BSCondition {

    boolean meetsCondition(BSShopHolder holder, BSBuy buy, Player p);

}
