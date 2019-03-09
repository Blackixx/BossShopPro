package org.black_ixx.bossshop.core.prices;


import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class BSPriceTypeNothing extends BSPriceType {


    public Object createObject(Object o, boolean force_final_state) {
        return null;
    }

    public boolean validityCheck(String item_name, Object o) {
        return true;
    }

    public void enableType() {
    }


    @Override
    public boolean hasPrice(Player p, BSBuy buy, Object price, ClickType clickType, boolean messageOnFailure) {
        return true;
    }

    @Override
    public String takePrice(Player p, BSBuy buy, Object price, ClickType clickType) {
        return "";
    }

    @Override
    public String getDisplayPrice(Player p, BSBuy buy, Object price, ClickType clickType) {
        return ClassManager.manager.getMessageHandler().get("Display.Nothing");
    }


    @Override
    public String[] createNames() {
        return new String[]{"nothing", "free"};
    }

    @Override
    public boolean mightNeedShopUpdate() {
        return false;
    }

}
