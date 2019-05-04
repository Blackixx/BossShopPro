package org.black_ixx.bossshop.events;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;

public class BSPlayerPurchasedEvent extends BSEvent {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final BSBuy buy;
    private final BSShop shop;
    private final ClickType clickType;


    public BSPlayerPurchasedEvent(Player player, BSShop shop, BSBuy buy, ClickType clickType) {
        this.player = player;
        this.buy = buy;
        this.shop = shop;
        this.clickType = clickType;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public BSBuy getShopItem() {
        return buy;
    }

    public BSShop getShop() {
        return shop;
    }

    public ClickType getClickType() {
        return clickType;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}