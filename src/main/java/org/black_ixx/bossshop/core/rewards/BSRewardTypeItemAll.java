package org.black_ixx.bossshop.core.rewards;


import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.prices.BSPriceTypeNumber;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.misc.InputReader;
import org.black_ixx.bossshop.misc.CurrencyTools.BSCurrency;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class BSRewardTypeItemAll extends BSRewardType {


    public Object createObject(Object o, boolean force_final_state) {
        if (force_final_state) {
            ItemStack i = InputReader.readItem(o, false);
            i.setAmount(1);
            return i;
        } else {
            return InputReader.readStringList(o);
        }
    }

    public boolean validityCheck(String item_name, Object o) {
        if (o != null) {
            return true;
        }
        ClassManager.manager.getBugFinder().severe("Was not able to create ShopItem " + item_name + "! The reward object needs to be a valid list of ItemData (https://www.spigotmc.org/wiki/bossshoppro-rewardtypes/).");
        return false;
    }

    @Override
    public void enableType() {
    }

    @Override
    public boolean canBuy(Player p, BSBuy buy, boolean messageOnFailure, Object reward, ClickType clickType) {
        ItemStack item = (ItemStack) reward;
        if (!ClassManager.manager.getItemStackChecker().hasFreeSpace(p, item)) {
            if (messageOnFailure) {
                ClassManager.manager.getMessageHandler().sendMessage("Main.InventoryFull", p, null, p, buy.getShop(), null, buy);
            }
            return false;
        }

        int items_amount_possible_space = ClassManager.manager.getItemStackChecker().getAmountOfFreeSpace(p, item);
        BSCurrency price_currency = BSCurrency.detectCurrency(buy.getPriceType(clickType).name());
        double price_per_item = (double) buy.getPrice(clickType);
        int items_amount_possible_money = (int) (price_currency.getBalance(p) / price_per_item);
        int items_amount = Math.max(1, Math.min(items_amount_possible_space, items_amount_possible_money));

        BSPriceTypeNumber pricetype = (BSPriceTypeNumber) buy.getPriceType(clickType);
        if (!pricetype.hasPrice(p, buy, buy.getPrice(clickType), clickType, items_amount, messageOnFailure)) {
            return false;
        }


        return true;
    }

    @Override
    public void giveReward(Player p, BSBuy buy, Object reward, ClickType clickType) {
        ItemStack item = (ItemStack) reward;

        int items_amount_possible_space = ClassManager.manager.getItemStackChecker().getAmountOfFreeSpace(p, item);
        BSCurrency price_currency = BSCurrency.detectCurrency(buy.getPriceType(clickType).name());
        double price_per_item = (double) buy.getPrice(clickType);
        int items_amount_possible_money = (int) (price_currency.getBalance(p) / price_per_item);
        int items_amount = Math.max(1, Math.min(items_amount_possible_space, items_amount_possible_money));

        BSPriceTypeNumber pricetype = (BSPriceTypeNumber) buy.getPriceType(clickType);
        pricetype.takePrice(p, buy, buy.getPrice(clickType), clickType, items_amount);


        ClassManager.manager.getItemStackCreator().giveItem(p, buy, item, items_amount, true);
    }

    @Override
    public String getDisplayReward(Player p, BSBuy buy, Object reward, ClickType clickType) {
        ItemStack item = (ItemStack) reward;
        String item_name = ClassManager.manager.getItemStackTranslator().readMaterial(item);
        return ClassManager.manager.getMessageHandler().get("Display.ItemAllBuy").replace("%item%", item_name);
    }

    @Override
    public String[] createNames() {
        return new String[]{"itemall", "buyall"};
    }

    @Override
    public boolean mightNeedShopUpdate() {
        return true;
    }

    @Override
    public boolean allowAsync() {
        return false;
    }


    @Override
    public boolean overridesPrice() {
        return true;
    }

    @Override
    public String getPriceReturnMessage(Player p, BSBuy buy, Object price, ClickType clickType) {
        BSPriceTypeNumber pricetype = (BSPriceTypeNumber) buy.getPriceType(clickType);
        return pricetype.getDisplayBalance(p, buy, price, clickType);
    }
}
