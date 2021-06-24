package org.black_ixx.bossshop.core.prices;


import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.misc.InputReader;
import org.black_ixx.bossshop.misc.MathTools;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class BSPriceTypePoints extends BSPriceTypeNumber {


    public Object createObject(Object o, boolean force_final_state) {
        return InputReader.getDouble(o, -1);
    }

    public boolean validityCheck(String item_name, Object o) {
        if ((Double) o != -1) {
            return true;
        }
        ClassManager.manager.getBugFinder().severe("Was not able to create ShopItem " + item_name + "! The price object needs to be a valid number. Example: '7' or '12'.");
        return false;
    }

    public void enableType() {
        ClassManager.manager.getSettings().setPointsEnabled(true);
    }


    @Override
    public boolean hasPrice(Player p, BSBuy buy, Object price, ClickType clickType, int multiplier, boolean messageOnFailure) {
        double points = ClassManager.manager.getMultiplierHandler().calculatePriceWithMultiplier(p, buy, clickType, (Double) price) * multiplier;
        if (ClassManager.manager.getPointsManager().getPoints(p) < points) {
            if (messageOnFailure) {
                ClassManager.manager.getMessageHandler().sendMessage("NotEnough.Points", p);
            }
            return false;
        }
        return true;
    }

    @Override
    public String takePrice(Player p, BSBuy buy, Object price, ClickType clickType, int multiplier) {
        double points = ClassManager.manager.getMultiplierHandler().calculatePriceWithMultiplier(p, buy, clickType, (Double) price) * multiplier;
        ClassManager.manager.getPointsManager().takePoints(p, points);

        return getDisplayBalance(p, buy, price, clickType);
    }

    @Override
    public String getDisplayBalance(Player p, BSBuy buy, Object price, ClickType clickType) {
        double balance_points = ClassManager.manager.getPointsManager().getPoints(p);
        return ClassManager.manager.getMessageHandler().get("Display.Points").replace("%points%", MathTools.displayNumber(balance_points, BSPriceType.Points));
    }

    @Override
    public String getDisplayPrice(Player p, BSBuy buy, Object price, ClickType clickType) {
        return ClassManager.manager.getMultiplierHandler().calculatePriceDisplayWithMultiplier(p, buy, clickType, (Double) price, ClassManager.manager.getMessageHandler().get("Display.Points").replace("%points%", "%number%"));
    }


    @Override
    public String[] createNames() {
        return new String[]{"points", "point"};
    }

    @Override
    public boolean isIntegerValue() {
        return false;
    }

    @Override
    public boolean mightNeedShopUpdate() {
        return true;
    }


}
