package org.black_ixx.bossshop.events;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;

public class BSDisplayItemEvent extends BSEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;

    private final BSBuy buy;

    private final BSShop shop;

    private final Inventory inventory;

    private boolean cancelled = false;

    public BSDisplayItemEvent(Player player, BSShop shop, BSBuy buy, Inventory inventory) {
        this.player = player;
        this.buy = buy;
        this.shop = shop;
        this.inventory = inventory;
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

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}