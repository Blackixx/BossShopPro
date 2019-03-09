package org.black_ixx.bossshop.settings;

import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.config.BSConfigShop;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class ShopProperty extends SettingsProperty {


    private Map<BSShop, Object> shop_settings;

    public ShopProperty(ConfigurationSection config, String path, Class<?> type) {
        super(config, path, type);
    }


    @Override
    public void load(ConfigurationSection config) {
        super.load(config);
        if (ClassManager.manager.getShops() != null && ClassManager.manager.getShops().getShops() != null) {
            readShops(type);
        }
    }

    public void readShops(Class<?> type) {
        for (BSShop shop : ClassManager.manager.getShops().getShops().values()) {
            readShop(shop, type);
        }
    }

    @Override
    public void update(Object o) {
        if (o instanceof BSShop) {
            BSShop shop = (BSShop) o;
            readShop(shop, type);
        }
        super.update(o);
    }

    public void readShop(BSShop shop, Class<?> type) {
        if (shop instanceof BSConfigShop) {
            BSConfigShop configshop = (BSConfigShop) shop;
            ConfigurationSection config = configshop.getConfig();
            if (config.contains(path)) {
                if (shop_settings == null) {
                    shop_settings = new HashMap<BSShop, Object>();
                }
                shop_settings.put(shop, read(config));
            }
        }
    }


    public boolean containsValue(Object input, Object value) {
        if (input instanceof BSShop) {
            BSShop shop = (BSShop) input;
            if (containsValueShop(shop, value)) {
                return true;
            }
        }
        return super.containsValue(input, value);
    }

    public boolean containsValueShop(BSShop shop, Object value) {
        if (shop_settings != null && shop_settings.containsKey(shop)) {
            if (isIdentical(shop_settings.get(shop), value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsValueAny(Object value) {
        if (shop_settings != null) {
            for (BSShop shop : shop_settings.keySet()) {
                if (containsValueShop(shop, value)) {
                    return true;
                }
            }
        }
        return super.containsValueAny(value);
    }


    public Object getObject(Object input) {
        if (input instanceof BSShop) {
            BSShop shop = (BSShop) input;
            if (shop_settings != null && shop_settings.containsKey(shop)) {
                return shop_settings.get(shop);
            }
        }
        return super.getObject(input);
    }

}
