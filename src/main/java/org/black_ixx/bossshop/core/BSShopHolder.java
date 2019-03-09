package org.black_ixx.bossshop.core;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;

public class BSShopHolder implements InventoryHolder {

    private BSShopHolder previous_shopholder;
    private BSShop shop;
    private int page, highest_page;
    private HashMap<Integer, BSBuy> items;
    public BSShopHolder(BSShop shop, HashMap<Integer, BSBuy> items) {
        this.shop = shop;
        this.items = items;
    }
    public BSShopHolder(BSShop shop, BSShopHolder previous_shopholder) {
        this(shop);
        this.previous_shopholder = previous_shopholder;
    }
    public BSShopHolder(BSShop shop) {
        this.shop = shop;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }

    public BSBuy getShopItem(int i) {
        return items.get(i);
    }

    public int getSlot(BSBuy buy) {
        for (int slot : items.keySet()) {
            BSBuy value = items.get(slot);
            if (value == buy) {
                return slot;
            }
        }
        return -1;
    }


    public BSShop getShop() {
        return shop;
    }

    public BSShopHolder getPreviousShopHolder() {
        return previous_shopholder;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getHighestPage() {
        return highest_page;
    }

    public void setHighestPage(int page) {
        this.highest_page = page;
    }

    public int getDisplayPage() {
        return page + 1;
    }

    public int getDisplayHighestPage() {
        return highest_page + 1;
    }

    public void setItems(HashMap<Integer, BSBuy> items, int page, int highest_page) {
        this.items = items;
        this.page = page;
        this.highest_page = highest_page;
    }


}
