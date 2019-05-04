package org.black_ixx.bossshop.events;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BSCheckStringForFeaturesEvent extends BSEvent {

    private static final HandlerList handlers = new HandlerList();


    private String text;
    private BSShop shop;
    private BSBuy buy;

    private boolean contains_feature;


    public BSCheckStringForFeaturesEvent(String text, BSBuy item, BSShop shop) {
        this.text = text;
        this.buy = item;
        this.shop = shop;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public String getText() {
        return text;
    }

    public BSShop getShop() {
        return shop;
    }

    public BSBuy getShopItem() {
        return buy;
    }

    public void approveFeature() {
        contains_feature = true;
    }

    public boolean containsFeature() {
        return contains_feature;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}