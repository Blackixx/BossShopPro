package org.black_ixx.bossshop.events;


import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class BSLoadShopItemEvent extends BSEvent {

    private static final HandlerList handlers = new HandlerList();


    private final BSShop shop;
    private final String name;
    private final ConfigurationSection section;

    private BSBuy custom_shopitem;


    public BSLoadShopItemEvent(BSShop shop, String name, ConfigurationSection section) {
        this.shop = shop;
        this.name = name;
        this.section = section;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ConfigurationSection getConfigurationSection() {
        return section;
    }

    public String getShopItemName() {
        return name;
    }

    public BSShop getShop() {
        return shop;
    }

    public void useCustomShopItem(BSBuy buy) {
        this.custom_shopitem = buy;
    }

    public BSBuy getCustomShopItem() {
        return custom_shopitem;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}