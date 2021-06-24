package org.black_ixx.bossshop.core.rewards;


import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.misc.InputReader;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;


public class BSRewardTypeShopPage extends BSRewardType {


    public Object createObject(Object o, boolean force_final_state) {
        return InputReader.readString(o, true);
    }

    public boolean validityCheck(String item_name, Object o) {
        if (o != null) {
            return true;
        }
        ClassManager.manager.getBugFinder().severe("Was not able to create ShopItem " + item_name + "! The reward object needs to be either 'next', 'previous' or a page number like '0' (first page), '1' or '2'.");
        return false;
    }

    public void enableType() {
    }

    @Override
    public boolean canBuy(Player p, BSBuy buy, boolean message_if_no_success, Object reward, ClickType clickType) {
        return true;
    }

    @Override
    public void giveReward(Player p, BSBuy buy, Object reward, ClickType clickType) {
        int page = calculatePage(p, (String) reward);

        if (page != -1) {
            Inventory inventory = p.getOpenInventory().getTopInventory();
            BSShopHolder holder = (BSShopHolder) inventory.getHolder();
            holder.getShop().updateInventory(inventory, holder, p, ClassManager.manager, page, holder.getHighestPage(), false);
        }
    }

    @Override
    public String getDisplayReward(Player p, BSBuy buy, Object reward, ClickType clickType) {
        int page = calculatePage(p, (String) reward);
        return ClassManager.manager.getMessageHandler().get("Display.Page").replace("%page%", String.valueOf(page + 1));
    }


    private int calculatePage(Player p, String reward) {
        InventoryView inventoryview = p.getOpenInventory();
        if (inventoryview != null) {
            Inventory inventory = inventoryview.getTopInventory();
            InventoryHolder holder = inventory.getHolder();

            if (holder instanceof BSShopHolder) {
                BSShopHolder shopholder = (BSShopHolder) holder;
                if (reward.equalsIgnoreCase("next") || reward.equalsIgnoreCase("+")) {
                    int page = Math.min(shopholder.getPage() + 1, shopholder.getHighestPage());
                    return page;
                }
                if (reward.equalsIgnoreCase("previous") || reward.equalsIgnoreCase("-")) {
                    int page = Math.max(shopholder.getPage() - 1, 0);
                    return page;
                }

                try {
                    int page = Math.max(0, Math.min(Integer.valueOf(reward), shopholder.getHighestPage()));
                    return page - 1;

                } catch (NumberFormatException e) {
                    ClassManager.manager.getBugFinder().warn("Was not able to detect shop page. Unable to read Reward '" + reward + "'. Please use either 'next', 'previous' or a page number like '1' or '2'.");
                }

            }
        }
        return -1;
    }


    @Override
    public String[] createNames() {
        return new String[]{"shoppage", "page", "openpage"};
    }

    public boolean logTransaction() {
        return false;
    }

    @Override
    public boolean mightNeedShopUpdate() {
        return false;
    }

    @Override
    public boolean isPlayerDependend(BSBuy buy, ClickType clicktype) {
        return true;
    }

    @Override
    public boolean isActualReward() {
        return false;
    }

}
