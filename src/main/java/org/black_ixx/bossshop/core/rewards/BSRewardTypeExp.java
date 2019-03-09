package org.black_ixx.bossshop.core.rewards;


import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.misc.InputReader;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;


public class BSRewardTypeExp extends BSRewardTypeNumber {


    public Object createObject(Object o, boolean force_final_state) {
        return InputReader.getInt(o, -1);
    }

    public boolean validityCheck(String item_name, Object o) {
        if ((Integer) o != -1) {
            return true;
        }
        ClassManager.manager.getBugFinder().severe("Was not able to create ShopItem " + item_name + "! The reward object needs to be a valid Integer number. Example: '7' or '12'.");
        return false;
    }

    public void enableType() {
    }

    @Override
    public boolean canBuy(Player p, BSBuy buy, boolean message_if_no_success, Object reward, ClickType clickType) {
        return true;
    }

    @Override
    public void giveReward(Player p, BSBuy buy, Object reward, ClickType clickType, int multiplier) {
        int exp = (int) ClassManager.manager.getMultiplierHandler().calculateRewardWithMultiplier(p, buy, clickType, ((Integer) reward)) * multiplier;
        if (ClassManager.manager.getSettings().getExpUseLevel()) {
            p.setLevel(p.getLevel() + exp);
        } else {
            p.giveExp(exp);
        }
    }

    @Override
    public String getDisplayReward(Player p, BSBuy buy, Object reward, ClickType clickType) {
        return ClassManager.manager.getMultiplierHandler().calculateRewardDisplayWithMultiplier(p, buy, clickType, ((Integer) reward), ClassManager.manager.getMessageHandler().get("Display.Exp").replace("%levels%", "%number%"));
    }

    @Override
    public String[] createNames() {
        return new String[]{"exp", "xp"};
    }


    @Override
    public boolean mightNeedShopUpdate() {
        return true;
    }

    @Override
    public boolean isIntegerValue() {
        return true;
    }


}
