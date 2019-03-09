package org.black_ixx.bossshop.core.rewards;


import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.misc.InputReader;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;


public class BSRewardTypePoints extends BSRewardTypeNumber {


    public Object createObject(Object o, boolean force_final_state) {
        return InputReader.getDouble(o, -1);
    }

    public boolean validityCheck(String item_name, Object o) {
        if ((Double) o != -1) {
            return true;
        }
        ClassManager.manager.getBugFinder().severe("Was not able to create ShopItem " + item_name + "! The reward object needs to be a valid Integer number. Example: '7' or '12'.");
        return false;
    }

    public void enableType() {
        ClassManager.manager.getSettings().setPointsEnabled(true);
    }

    @Override
    public boolean canBuy(Player p, BSBuy buy, boolean message_if_no_success, Object reward, ClickType clickType) {
        return true;
    }

    @Override
    public void giveReward(Player p, BSBuy buy, Object reward, ClickType clickType, int multiplier) {
        double points = ClassManager.manager.getMultiplierHandler().calculateRewardWithMultiplier(p, buy, clickType, ((Double) reward)) * multiplier;
        ClassManager.manager.getPointsManager().givePoints(p, points);
    }

    @Override
    public String getDisplayReward(Player p, BSBuy buy, Object reward, ClickType clickType) {
        return ClassManager.manager.getMultiplierHandler().calculateRewardDisplayWithMultiplier(p, buy, clickType, ((Double) reward), ClassManager.manager.getMessageHandler().get("Display.Points").replace("%points%", "%number%"));
    }

    @Override
    public String[] createNames() {
        return new String[]{"points", "point"};
    }


    @Override
    public boolean mightNeedShopUpdate() {
        return true;
    }

    @Override
    public boolean isIntegerValue() {
        return false;
    }

    @Override
    public boolean allowAsync() {
        return true;
    }

}
