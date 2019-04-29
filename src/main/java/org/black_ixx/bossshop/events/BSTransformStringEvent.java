package org.black_ixx.bossshop.events;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BSTransformStringEvent extends BSEvent {

    private static final HandlerList handlers = new HandlerList();


    private String text;
    private Player target;
    private BSShop shop;
    private BSBuy buy;
    private BSShopHolder holder;


    public BSTransformStringEvent(String text, BSBuy item, BSShop shop, BSShopHolder holder, Player target) {
        this.text = text;
        this.buy = item;
        this.shop = shop;
        this.holder = holder;
        this.target = target;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Player getTarget() {
        return target;
    }

    public BSShop getShop() {
        return shop;
    }

    public BSBuy getShopItem() {
        return buy;
    }

    public BSShopHolder getShopHolder() {
        return holder;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}