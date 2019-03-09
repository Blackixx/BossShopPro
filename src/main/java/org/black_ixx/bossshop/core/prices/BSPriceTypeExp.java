package org.black_ixx.bossshop.core.prices;


import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.misc.InputReader;
import org.black_ixx.bossshop.misc.MathTools;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class BSPriceTypeExp extends BSPriceTypeNumber {


    public Object createObject(Object o, boolean force_final_state) {
        return InputReader.getInt(o, -1);
    }

    public boolean validityCheck(String item_name, Object o) {
        if ((Integer) o != -1) {
            return true;
        }
        ClassManager.manager.getBugFinder().severe("Was not able to create ShopItem " + item_name + "! The price object needs to be a valid Integer number. Example: '10' or '30'.");
        return false;
    }

    @Override
    public void enableType() {
    }


    @Override
    public boolean hasPrice(Player p, BSBuy buy, Object price, ClickType clickType, int multiplier, boolean messageOnFailure) {
        int exp = (int) ClassManager.manager.getMultiplierHandler().calculatePriceWithMultiplier(p, buy, clickType, (Integer) price) * multiplier;
        if ((p.getLevel() < (Integer) exp)) {
            if (messageOnFailure) {
                ClassManager.manager.getMessageHandler().sendMessage("NotEnough.Exp", p);
            }
            return false;
        }
        return true;
    }

    @Override
    public String takePrice(Player p, BSBuy buy, Object price, ClickType clickType, int multiplier) {
        int exp = (int) ClassManager.manager.getMultiplierHandler().calculatePriceWithMultiplier(p, buy, clickType, (Integer) price) * multiplier;
        p.setLevel(p.getLevel() - exp);

        return getDisplayBalance(p, buy, price, clickType);
    }

    @Override
    public String getDisplayBalance(Player p, BSBuy buy, Object price, ClickType clickType) {
        int balance_exp = p.getLevel();
        return ClassManager.manager.getMessageHandler().get("Display.Exp").replace("%levels%", MathTools.displayNumber(balance_exp, BSPriceType.Exp));
    }

    @Override
    public String getDisplayPrice(Player p, BSBuy buy, Object price, ClickType clickType) {
        return ClassManager.manager.getMultiplierHandler().calculatePriceDisplayWithMultiplier(p, buy, clickType, (Integer) price, ClassManager.manager.getMessageHandler().get("Display.Exp").replace("%levels%", "%number%"));
    }


    @Override
    public String[] createNames() {
        return new String[]{"exp", "xp", "level", "levels"};
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
