package org.black_ixx.bossshop.events;


import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.managers.features.PageLayoutHandler;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class BSChoosePageLayoutEvent extends BSEvent {

    private static final HandlerList handlers = new HandlerList();


    private final BSShop shop;
    private final String name;
    private PageLayoutHandler layout;


    public BSChoosePageLayoutEvent(BSShop shop, String name, PageLayoutHandler layout) {
        this.shop = shop;
        this.name = name;
        this.layout = layout;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public String getShopItemName() {
        return name;
    }

    public BSShop getShop() {
        return shop;
    }

    public PageLayoutHandler getLayout() {
        return layout;
    }

    public void setLayout(PageLayoutHandler layout) {
        this.layout = layout;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}