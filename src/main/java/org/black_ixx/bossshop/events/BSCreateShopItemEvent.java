package org.black_ixx.bossshop.events;


import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSInputType;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.conditions.BSCondition;
import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.core.rewards.BSRewardType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class BSCreateShopItemEvent extends BSEvent {

    private static final HandlerList handlers = new HandlerList();


    private final BSShop shop;
    private final String name;
    private final ConfigurationSection section;
    private final BSRewardType rewardT;
    private final BSPriceType priceT;
    private final Object reward, price;
    private final String msg, permission;
    private final int inventorylocation;
    private final BSCondition condition;
    private final BSInputType inputtype;
    private final String inputtext;
    private BSBuy custom_shopitem;


    public BSCreateShopItemEvent(BSShop shop, String name, ConfigurationSection section, BSRewardType rewardT, BSPriceType priceT, Object reward, Object price, String msg, int location, String permission, BSCondition condition, BSInputType inputtype, String inputtext) {
        this.shop = shop;
        this.name = name;
        this.section = section;
        this.rewardT = rewardT;
        this.priceT = priceT;
        this.reward = reward;
        this.price = price;
        this.msg = msg;
        this.inventorylocation = location;
        this.permission = permission;
        this.condition = condition;
        this.inputtype = inputtype;
        this.inputtext = inputtext;
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

    public BSRewardType getRewardType() {
        return rewardT;
    }

    public BSPriceType getPriceType() {
        return priceT;
    }

    public Object getReward() {
        return reward;
    }

    public Object getPrice() {
        return price;
    }

    public String getMessage() {
        return msg;
    }

    public int getInventoryLocation() {
        return inventorylocation;
    }

    public String getExtraPermission() {
        return permission;
    }

    public BSCondition getCondition() {
        return condition;
    }

    public BSInputType getInputType() {
        return inputtype;
    }

    public String getInputText() {
        return inputtext;
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