package org.black_ixx.bossshop.core.prices;


import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.misc.InputReader;
import org.black_ixx.bossshop.misc.MathTools;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class BSPriceTypeMoney extends BSPriceTypeNumber {


    public Object createObject(Object o, boolean force_final_state) {
        return InputReader.getDouble(o, -1);
    }

    public boolean validityCheck(String item_name, Object o) {
        if ((Double) o != -1) {
            return true;
        }
        ClassManager.manager.getBugFinder().severe("Was not able to create ShopItem " + item_name + "! The price object needs to be a valid number. Example: '4.0' or '10'.");
        return false;
    }

    public void enableType() {
        ClassManager.manager.getSettings().setMoneyEnabled(true);
        ClassManager.manager.getSettings().setVaultEnabled(true);
    }


    @Override
    public boolean hasPrice(Player p, BSBuy buy, Object price, ClickType clickType, int multiplier, boolean messageOnFailure) {
        double money = (double) ClassManager.manager.getMultiplierHandler().calculatePriceWithMultiplier(p, buy, clickType, (Double) price) * multiplier;
        if (ClassManager.manager.getVaultHandler() == null) {
            return false;
        }
        if (ClassManager.manager.getVaultHandler().getEconomy() == null) {
            return false;
        }
        if (!ClassManager.manager.getVaultHandler().getEconomy().hasAccount(p.getName())) {
            if (messageOnFailure) {
                ClassManager.manager.getMessageHandler().sendMessage("Economy.NoAccount", p);
            }
            return false;
        }
        if (ClassManager.manager.getVaultHandler().getEconomy().getBalance(p.getName()) < money) {
            if (messageOnFailure) {
                ClassManager.manager.getMessageHandler().sendMessage("NotEnough.Money", p);
            }
            return false;
        }

        return true;
    }

    @Override
    public String takePrice(Player p, BSBuy buy, Object price, ClickType clickType, int multiplier) {
        double money = (double) ClassManager.manager.getMultiplierHandler().calculatePriceWithMultiplier(p, buy, clickType, (Double) price) * multiplier;

        if (!ClassManager.manager.getVaultHandler().getEconomy().hasAccount(p.getName())) {
            ClassManager.manager.getBugFinder().severe("Unable to take money! No economy account existing! (" + p.getName() + ", " + money + ")");
            return "";
        }

        ClassManager.manager.getVaultHandler().getEconomy().withdrawPlayer(p.getName(), money);
        return getDisplayBalance(p, buy, price, clickType);
    }

    @Override
    public String getDisplayBalance(Player p, BSBuy buy, Object price, ClickType clickType) {
        double balance = ClassManager.manager.getVaultHandler().getEconomy().getBalance(p.getName());
        return ClassManager.manager.getMessageHandler().get("Display.Money").replace("%money%", MathTools.displayNumber(balance, BSPriceType.Money));
    }

    @Override
    public String getDisplayPrice(Player p, BSBuy buy, Object price, ClickType clickType) {
        return ClassManager.manager.getMultiplierHandler().calculatePriceDisplayWithMultiplier(p, buy, clickType, (Double) price, ClassManager.manager.getMessageHandler().get("Display.Money").replace("%money%", "%number%"));
    }

    @Override
    public String[] createNames() {
        return new String[]{"money"};
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
