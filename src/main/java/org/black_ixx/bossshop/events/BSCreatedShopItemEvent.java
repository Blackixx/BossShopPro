package org.black_ixx.bossshop.events;


import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

public class BSCreatedShopItemEvent extends BSEvent {

    private static final HandlerList handlers = new HandlerList();


    private final BSShop shop;
    private final BSBuy item;
    private final ConfigurationSection section;


    public BSCreatedShopItemEvent(BSShop shop, BSBuy item, ConfigurationSection section) {
        this.shop = shop;
        this.item = item;
        this.section = section;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ConfigurationSection getConfigurationSection() {
        return section;
    }

    public BSBuy getShopItem() {
        return item;
    }

    public BSShop getShop() {
        return shop;
    }

    public void putSpecialInformation(Plugin plugin, Object information) {
        item.putSpecialInformation(plugin, information);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}