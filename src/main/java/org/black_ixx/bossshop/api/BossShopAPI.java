package org.black_ixx.bossshop.api;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.*;
import org.black_ixx.bossshop.core.conditions.BSConditionType;
import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.core.rewards.BSRewardType;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.config.BSConfigShop;
import org.black_ixx.bossshop.managers.item.ItemDataPart;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BossShopAPI {


    private BossShop plugin;
    private List<BossShopAddon> enabled_addons;


    public BossShopAPI(BossShop plugin) {
        this.plugin = plugin;
    }


    public BossShopAddon getAddon(String addonname) {
        if (enabled_addons != null) {
            for (BossShopAddon addon : enabled_addons) {
                if (addon.getAddonName().equalsIgnoreCase(addonname)) {
                    return addon;
                }
            }
        }
        return null;
    }

    //For single shop
    public boolean isValidShop(InventoryView v) {
        if (v != null) {
            if (v.getTopInventory() != null) {
                return isValidShop(v.getTopInventory());
            }
        }
        return false;
    }

    public boolean isValidShop(Inventory i) {
        if (i == null) {
            return false;
        }
        return (i.getHolder() instanceof BSShopHolder);
    }

    public BSShop getShop(String name) {
        if (plugin.getClassManager() == null) {
            return null;
        }
        if (plugin.getClassManager().getShops() == null) {
            return null;
        }
        return plugin.getClassManager().getShops().getShop(name.toLowerCase());
    }

    public void openShop(Player p, String name) {
        BSShop shop = getShop(name);
        if (shop == null) {
            BossShop.log("[API] Error: Tried to open Shop " + name + " but it was not found...");
            return;
        }
        openShop(p, shop);
    }

    public void openShop(Player p, BSShop shop) {
        plugin.getClassManager().getShops().openShop(p, shop);
    }

    public void updateInventory(Player p) {
        updateInventory(p, false);
    }

    public void updateInventory(Player p, boolean force_new_creation) {
        if (isValidShop(p.getOpenInventory())) {
            BSShopHolder holder = (BSShopHolder) p.getOpenInventory().getTopInventory().getHolder();
            if (force_new_creation) {
                holder.getShop().openInventory(p, holder.getPage(), false);
            } else {
                holder.getShop().updateInventory(p.getOpenInventory().getTopInventory(), holder, p, ClassManager.manager, holder.getPage(), holder.getHighestPage(), false);
            }
        }
    }


    //Get managers
    public BSShops getShopHandler() {
        return plugin.getClassManager().getShops();
    }


    //Modify Shop/Shops
    public void addShop(BSShop shop) {
        getShopHandler().addShop(shop);
    }

    public int createNextShopId() {
        return getShopHandler().createId();
    }

    public void addItemToShop(ItemStack menu_item, BSBuy shop_item, BSShop shop) {
        shop.addShopItem(shop_item, menu_item, ClassManager.manager);
    }

    public void finishedAddingItemsToShop(BSShop shop) {
        shop.finishedAddingItems();
    }


    //Register things
    public void registerConditionType(BSConditionType type) {
        type.register();
    }

    public void registerPriceType(BSPriceType type) {
        type.register();
    }

    public void registerRewardType(BSRewardType type) {
        type.register();
    }

    public void registerItemDataPart(ItemDataPart part) {
        part.register();
    }


    //Create things
    public BSBuy createBSBuy(String name, BSRewardType reward_type, BSPriceType price_type, Object reward, Object price, String msg, int location, String permission, String displayPermission) {
        return new BSBuy(reward_type, price_type, reward, price, msg, location, permission, displayPermission, name);
    }


    public BSBuy createBSBuyCustom(String name, BSRewardType reward_type, BSPriceType price_type, BSCustomLink reward, Object price, String msg, int location, String permission, String displayPermission) {
        return new BSBuy(reward_type, price_type, reward, price, msg, location, permission,displayPermission, name);
    }

    public BSBuy createBSBuy(BSRewardType reward_type, BSPriceType price_type, Object reward, Object price, String msg, int location, String permission, String displayPermission) {
        return new BSBuy(reward_type, price_type, reward, price, msg, location, permission, displayPermission,  "");
    }


    public BSBuy createBSBuyCustom(BSRewardType reward_type, BSPriceType price_type, BSCustomLink reward, Object price, String msg, int location, String permission, String displayPermission) {
        return new BSBuy(reward_type, price_type, reward, price, msg, location, permission, displayPermission, "");
    }

    public BSCustomLink createBSCustomLink(BSCustomActions actions, int action_id) {
        return new BSCustomLink(action_id, actions);
    }


    //Get shop items
    public HashMap<BSShop, List<BSBuy>> getAllShopItems() {
        HashMap<BSShop, List<BSBuy>> all = new HashMap<BSShop, List<BSBuy>>();
        for (int i : plugin.getClassManager().getShops().getShops().keySet()) {
            BSShop shop = plugin.getClassManager().getShops().getShop(i);
            if (shop == null) {
                continue;
            }
            List<BSBuy> items = new ArrayList<BSBuy>();
            for (BSBuy buy : shop.getItems()) {
                if (buy == null || buy.getItem() == null) {
                    continue;
                }
                items.add(buy);
            }
            all.put(shop, items);
        }

        return all;
    }

    public HashMap<BSConfigShop, List<BSBuy>> getAllShopItems(String config_option) {
        HashMap<BSConfigShop, List<BSBuy>> all = new HashMap<BSConfigShop, List<BSBuy>>();
        for (int i : plugin.getClassManager().getShops().getShops().keySet()) {
            BSShop shop = plugin.getClassManager().getShops().getShop(i);
            if (shop == null | !(shop instanceof BSConfigShop)) {
                continue;
            }
            BSConfigShop sho = (BSConfigShop) shop;
            List<BSBuy> items = new ArrayList<BSBuy>();
            for (BSBuy buy : shop.getItems()) {
                if (buy == null || buy.getItem() == null) {
                    continue;
                }
                if (buy.getConfigurationSection(sho).getBoolean(config_option) == false && buy.getConfigurationSection(sho).getInt(config_option) == 0) {
                    continue;
                }
                items.add(buy);
            }
            all.put(sho, items);
        }

        return all;
    }

    //Addon API
    protected void addEnabledAddon(BossShopAddon addon) {
        Plugin addonplugin = Bukkit.getPluginManager().getPlugin(addon.getAddonName());
        if (addonplugin == null) {
            return;
        }
        if (enabled_addons == null) {
            enabled_addons = new ArrayList<BossShopAddon>();
        }
        if (enabled_addons.contains(addon)) {
            return;
        }
        enabled_addons.add(addon);
    }

    public List<BossShopAddon> getEnabledAddons() {
        return enabled_addons;
    }


}
