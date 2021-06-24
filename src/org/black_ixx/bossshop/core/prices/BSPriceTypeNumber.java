package org.black_ixx.bossshop.core.prices;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.rewards.BSRewardType;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public abstract class BSPriceTypeNumber extends BSPriceType {


    public abstract boolean isIntegerValue();

    @Override
    public boolean isPlayerDependend(BSBuy buy, ClickType clicktype) {
        return super.isPlayerDependend(buy, clicktype) || (buy.getRewardType(clicktype) == BSRewardType.ItemAll && ClassManager.manager.getSettings().getItemAllShowFinalReward());
    }

    @Override
    public boolean supportsMultipliers() {
        return true;
    }

    @Override
    public String takePrice(Player p, BSBuy buy, Object price, ClickType clickType) {
        return takePrice(p, buy, price, clickType, 1);
    }

    @Override
    public boolean hasPrice(Player p, BSBuy buy, Object price, ClickType clickType, boolean messageOnFailure) {
        return hasPrice(p, buy, price, clickType, 1, messageOnFailure);
    }


    public abstract String takePrice(Player p, BSBuy buy, Object price, ClickType clickType, int multiplier);

    public abstract boolean hasPrice(Player p, BSBuy buy, Object price, ClickType clickType, int multiplier, boolean messageOnFailure);

    public abstract String getDisplayBalance(Player p, BSBuy buy, Object price, ClickType clickType);


}
