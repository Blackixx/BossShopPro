package org.black_ixx.bossshop.core;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.config.BSConfigShop;
import org.black_ixx.bossshop.managers.config.FileHandler;
import org.black_ixx.bossshop.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.io.File;
import java.util.HashMap;

public class BSShops {

    private HashMap<Integer, BSShop> shops;
    private HashMap<String, Integer> shopsIds;

    /////////////////////////////// <- Variables
    private int id = 0;
    public BSShops(BossShop plugin, Settings settings) {
        shops = new HashMap<Integer, BSShop>();
        shopsIds = new HashMap<String, Integer>();

        File folder = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "shops" + File.separator);
        new FileHandler().exportShops(plugin);

        boolean enable_shop_commands = loadShops(folder, settings, "");
        ClassManager.manager.getSettings().setShopCommandsEnabled(enable_shop_commands);

        BossShop.log("Loaded " + shops.size() + " Shops!");
    }

    private boolean loadShops(File folder, Settings settings, String parent_path) {
        boolean enable_shop_commands = false;

        for (File f : folder.listFiles()) {
            if (f != null) {
                if (f.isDirectory()) {
                    if (settings.getLoadSubfoldersEnabled()) {
                        if (loadShops(f, settings, f.getName() + File.separator)) {
                            enable_shop_commands = true;
                        }
                    }
                    continue;
                }

                if (f.isFile()) {
                    if (f.getName().contains(".yml")) {
                        BSShop shop = loadShop(f, parent_path);

                        if (shop.getCommands() != null) {
                            enable_shop_commands = true;
                        }
                    }

                }
            }
        }
        return enable_shop_commands;
    }

    /////////////////////////////// <- Load Shop

    public void addShop(BSShop shop) {
        shops.put(shop.getShopId(), shop);

        if (shopsIds.containsKey(shop.getShopName().toLowerCase())) {
            ClassManager.manager.getBugFinder().warn("Two Shops with the same Name (" + shop.getShopName().toLowerCase() + ") are loaded. When opening a Shop via Name, only one of this Shops will be opened!");
        }

        shopsIds.put(shop.getShopName().toLowerCase(), shop.getShopId());
    }

    public BSShop loadShop(File f, String parent_path) {
        String name = parent_path + f.getName();
        BSShop shop = new BSConfigShop(createId(), name, this);

        addShop(shop);

        return shop;
    }

    public void unloadShop(BSShop shop) {
        int id = getShopId(shop.getShopName());
        shopsIds.remove(shop.getShopName());
        shops.remove(id);
        shop.close();
    }


    /////////////////////////////// <- Simple Methods

    public void openShop(Player p, String name) {
        if (!isShop(name)) {
            ClassManager.manager.getMessageHandler().sendMessage("Main.ShopNotExisting", p);
            return;
        }
        openShop(p, getShopFast(name));
    }

    public void openShop(Player p, BSShop shop) {
        int page = 0;
        boolean remember_current_shop = true;

        InventoryView view = p.getOpenInventory();
        if (view != null) {
            if (view.getTopInventory().getHolder() instanceof BSShopHolder) {
                BSShopHolder holder = (BSShopHolder) view.getTopInventory().getHolder();
                BSShopHolder old_shopholder = holder.getPreviousShopHolder();
                if (old_shopholder != null) {
                    //Going back to previous shop
                    if (old_shopholder.getShop() == shop) {
                        page = old_shopholder.getPage();

                        /* If going back to parent shop, children shop should not be remembered
                         *  That way it can be prevented that all previous shops are kept in memory when players keep switching between shops
                         *  Note: This might cause confusion in some causes because some pages are restored and some are not.
                         */
                        remember_current_shop = false;
                    }
                }
            }
        }


        shop.openInventory(p, page, remember_current_shop);
    }

    public BSShop getShop(String name) {
        return getShop(getShopId(name));
    }

    public BSShop getShopFast(String name) {
        return getShopFast(getShopId(name));
    }

    public BSShop getShopByCommand(String player_command) {
        if (player_command != null && player_command.length() > 0) {
            for (BSShop shop : shops.values()) {
                String[] commands = shop.getCommands();
                if (commands != null) {
                    for (String command : commands) {
                        if (command.equalsIgnoreCase(player_command)) {
                            return shop;
                        }
                    }
                }
            }
        }
        return null;
    }

    public BSShop getShop(int id) {
        return shops.containsKey(id) ? shops.get(id) : null;
    }

    public BSShop getShopFast(int id) {
        return shops.get(id);
    }

    public int getShopId(String name) {
        name = name.toLowerCase();
        if (!shopsIds.containsKey(name)) {
            //ClassManager.manager.getBugFinder().warn("Was not able to get the Id of the "+name+" Shop.");
            return -1; //Was return 0 before. Changed because I think then it returns no shop for sure!
        }
        return shopsIds.get(name);
    }

    public boolean isShop(String name) {
        return shopsIds.containsKey(name);
    }

    public boolean isShop(int id) {
        return shops.containsKey(id);
    }

    public HashMap<Integer, BSShop> getShops() {
        return shops;
    }

    public HashMap<String, Integer> getShopIds() {
        return shopsIds;
    }

    public int createId() {
        id++;
        return id;
    }


    ////////////////////////////////////////////////////////////////////////////

    public void refreshShops(boolean mode_serverpinging) {
        for (Player p : Bukkit.getOnlinePlayers()) { //If players have a customizable inventory open it needs an update
            if (ClassManager.manager.getPlugin().getAPI().isValidShop(p.getOpenInventory())) {
                Inventory open_inventory = p.getOpenInventory().getTopInventory();
                BSShopHolder h = (BSShopHolder) open_inventory.getHolder();

                if (h.getShop().isCustomizable()) {
                    if (!mode_serverpinging) {
                        if (ClassManager.manager.getSettings().getServerPingingEnabled(true)) {
                            if (ClassManager.manager.getServerPingingManager().containsServerpinging(h.getShop())) {
                                continue;
                            }
                        }
                        h.getShop().updateInventory(open_inventory, h, p, ClassManager.manager, h.getPage(), h.getHighestPage(), true);
                    }
                }
            }
        }
    }

}
