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


    /**
     * Load a section from the config
     * @param config the config to load from
     */
    @Override
    public void load(ConfigurationSection config) {
        super.load(config);
        if (ClassManager.manager.getShops() != null && ClassManager.manager.getShops().getShops() != null) {
            readShops(type);
        }
    }

    /**
     * Read in the shops
     * @param type the class type
     */
    public void readShops(Class<?> type) {
        for (BSShop shop : ClassManager.manager.getShops().getShops().values()) {
            readShop(shop, type);
        }
    }

    /**
     * Update the shops
     * @param o shop
     */
    @Override
    public void update(Object o) {
        if (o instanceof BSShop) {
            BSShop shop = (BSShop) o;
            readShop(shop, type);
        }
        super.update(o);
    }

    /**
     * Read in the shops from the config
     * @param shop the shop to read
     * @param type the type
     */
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


    /**
     * Check if a shop contains something
     * @param input where to check it
     * @param value what to check
     * @return contains or not
     */
    public boolean containsValue(Object input, Object value) {
        if (input instanceof BSShop) {
            BSShop shop = (BSShop) input;
            if (containsValueShop(shop, value)) {
                return true;
            }
        }
        return super.containsValue(input, value);
    }

    /**
     * Check if a setting is contained in a shop
     * @param shop the shop to check
     * @param value the value to check
     * @return contained or not
     */
    public boolean containsValueShop(BSShop shop, Object value) {
        if (shop_settings != null && shop_settings.containsKey(shop)) {
            if (isIdentical(shop_settings.get(shop), value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if a shop contains any value
     * @param value the value to check
     * @return contained or not
     */
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


    /**
     * Get an object from a shop
     * @param input the shop to check
     * @return object
     */
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
