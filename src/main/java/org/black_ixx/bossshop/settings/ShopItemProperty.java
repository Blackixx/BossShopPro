package org.black_ixx.bossshop.settings;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.config.BSConfigShop;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class ShopItemProperty extends ShopProperty {


    private Map<BSBuy, Object> shopitem_settings;

    public ShopItemProperty(ConfigurationSection config, String path, Class<?> type) {
        super(config, path, type);
    }


    @Override
    public void load(ConfigurationSection config) {
        super.load(config);
        if (ClassManager.manager.getShops() != null && ClassManager.manager.getShops().getShops() != null) {
            readShopItems(type);
        }
    }

    public void readShopItems(Class<?> type) {
        for (BSShop shop : ClassManager.manager.getShops().getShops().values()) {
            if (shop instanceof BSConfigShop) {
                BSConfigShop configshop = (BSConfigShop) shop;

                for (BSBuy buy : shop.getItems()) {
                    readShopItem(buy, configshop, type);
                }

            }
        }
    }

    @Override
    public void update(Object o) {
        if (o instanceof BSBuy) {
            BSBuy buy = (BSBuy) o;
            readShopItem(buy, type);
        }
        super.update(o);
    }

    public void readShopItem(BSBuy buy, Class<?> type) {
        if (buy.getShop() instanceof BSConfigShop) {
            BSConfigShop configshop = (BSConfigShop) buy.getShop();
            readShopItem(buy, configshop, type);
        }
    }

    public void readShopItem(BSBuy buy, BSConfigShop configshop, Class<?> type) {
        ConfigurationSection section = buy.getConfigurationSection(configshop);
        if (section != null && section.contains(path)) {
            if (shopitem_settings == null) {
                shopitem_settings = new HashMap<>();
            }
            shopitem_settings.put(buy, read(section));
        }
    }

    @Override
    public void readShop(BSShop shop, Class<?> type) {
        super.readShop(shop, type);
        for (BSBuy buy : shop.getItems()) {
            readShopItem(buy, type);
        }
    }


    public boolean containsValue(Object input, Object value) {
        if (input instanceof BSBuy) {
            BSBuy buy = (BSBuy) input;
            if (containsValueShopItem(buy, value)) {
                return true;
            }
            return super.containsValue(buy.getShop(), value);
        }

        return super.containsValue(input, value);
    }

    public boolean containsValueShopItem(BSBuy buy, Object value) {
        if (shopitem_settings != null && shopitem_settings.containsKey(buy)) {
            if (isIdentical(shopitem_settings.get(buy), value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsValueAny(Object value) {
        if (shopitem_settings != null) {
            for (BSBuy buy : shopitem_settings.keySet()) {
                if (containsValueShopItem(buy, value)) {
                    return true;
                }
            }
        }
        return super.containsValueAny(value);
    }


    @Override
    public Object getObject(Object input) {
        if (input instanceof BSBuy) {
            BSBuy buy = (BSBuy) input;
            if (shopitem_settings != null && shopitem_settings.containsKey(buy)) {
                return shopitem_settings.get(buy);
            }
            return super.getObject(buy.getShop());
        }

        return super.getObject(input);
    }

}
