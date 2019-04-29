package org.black_ixx.bossshop.core;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.events.BSChoosePageLayoutEvent;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.features.PageLayoutHandler;
import org.black_ixx.bossshop.misc.Misc;
import org.black_ixx.bossshop.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class BSShop {

    public final static int ROWS_LIMIT_CURRENT = ClassManager.manager.getPageLayoutHandler().getMaxRows(); //By default 6
    public final static int ROWS_LIMIT_TOTAL = 6;
    public final static int ROW_ITEMS = 9;

    //////////////////////////// <- Variables

    private String shop_name = "BossShop";
    private String sign_text = "[BossShop]";

    private String displayname;
    private String[] commands;

    private boolean needPermToCreateSign = true;

    private boolean customizable = !ClassManager.manager.getPageLayoutHandler().showIfMultiplePagesOnly(); //Automatically customizable when there special PageLayout components are shown
    private boolean displaying = false; //When displaying custom variables

    private int inventory_size = 9;
    private int manual_inventory_rows;
    private int shop_id = 0;

    private int highest_page; //Might not be correct but is used in case of a fix inventory having multiple pages


    private Set<BSBuy> items = new LinkedHashSet<>();

    //////////////////////////// <- Constructor

    public BSShop(int shop_id, String shop_name, String sign_text, boolean needPermToCreateSign, BossShop plugin, String displayname, int manual_inventory_rows, String[] commands) {
        this.shop_id = shop_id;
        this.shop_name = shop_name;
        this.sign_text = sign_text;
        this.manual_inventory_rows = manual_inventory_rows;
        this.needPermToCreateSign = needPermToCreateSign;
        setCommands(commands);

        setDisplayName(displayname);
    }

    public BSShop(int shop_id) {
        this.shop_id = shop_id;
    }

    //////////////////////////// <- Methods to get main Variables

    public String getShopName() {
        return shop_name;
    }

    public void setShopName(String name) {
        shop_name = name;
    }

    public String getDisplayName() {
        return displayname;
    }

    public void setDisplayName(String displayname) {
        if (displayname != null) {
            this.displayname = ClassManager.manager.getStringManager().transform(displayname, null, this, null, null);
            if (ClassManager.manager.getStringManager().checkStringForFeatures(this, null, null, this.displayname)) {
                customizable = true;
                displaying = true;
            }
        } else {
            this.displayname = shop_name;
        }
    }

    public String getValidDisplayName(Player p, BSShopHolder holder) {
        String displayname = this.displayname;
        displayname = ClassManager.manager.getStringManager().transform(displayname, null, this, holder, p);
        return displayname.length() > 32 ? displayname.substring(0, 32) : displayname;
    }

    public String getSignText() {
        return sign_text;
    }

    public void setSignText(String text) {
        sign_text = text;
    }

    public String[] getCommands() {
        return commands;
    }

    public void setCommands(String[] commands) {
        this.commands = commands;
    }

    public boolean needPermToCreateSign() {
        return needPermToCreateSign;
    }

    /**
     * Checks whether anything within the shop is player-dependent.
     *
     * @return true if shop contains anything player-dependent, like placeholders, conditions, multiple pages and more.
     */
    public boolean isCustomizable() {
        return customizable;
    }


    //////////////////////////// <- Methods to set main Variables

    public void setCustomizable(boolean b) {
        customizable = b;
    }

    /**
     * Checks whether the shop contains player-dependent placeholders.
     *
     * @return true if shop contains player-dependent placeholders.
     */
    public boolean isDisplaying() {
        return displaying;
    }

    public void setDisplaying(boolean b) {
        displaying = b;
    }

    public int getInventorySize() {
        return inventory_size;
    }

    public int getShopId() {
        return shop_id;
    }

    public int getManualInventoryRows() {
        return manual_inventory_rows;
    }

    public void setManualInventoryRows(int i) {
        this.manual_inventory_rows = i;
    }

    public void setNeedPermToCreateSign(boolean b) {
        needPermToCreateSign = b;
    }

    //////////////////////////// <- Methods to get Items

    public Set<BSBuy> getItems() {
        return items;
    }

    public BSBuy getItem(String name) {
        for (BSBuy buy : items) {
            if (buy.getName().equalsIgnoreCase(name)) {
                return buy;
            }
        }
        for (BSBuy buy : items) {
            if (buy.getName().toLowerCase().startsWith(name.toLowerCase())) {
                return buy;
            }
        }
        return null;
    }


    //////////////////////////// <- Other Methods

    public void addShopItem(BSBuy buy, ItemStack menu_item, ClassManager manager) {
        buy.updateShop(this, menu_item, manager, true);
    }

    public void removeShopItem(BSBuy buy) {
        items.remove(buy);
    }


    public Inventory createInventory(Player p, ClassManager manager, int page, int highest_page, BSShopHolder oldshopholder) {
        return manager.getShopCustomizer().createInventory(this, items, p, manager, page, highest_page, oldshopholder);

    }

    public void updateInventory(Inventory i, BSShopHolder holder, Player p, ClassManager manager, int page, int highest_page, boolean auto_refresh) {
        if (holder.getPage() != page) {
            Misc.playSound(p, ClassManager.manager.getSettings().getPropertyString(Settings.SOUND_SHOP_CHANGE_PAGE, this, null));
        }
        holder.setPage(page);
        holder.setHighestPage(highest_page);
        if (ClassManager.manager.getStringManager().checkStringForFeatures(this, null, null, getDisplayName()) & !getValidDisplayName(p, holder).equals(p.getOpenInventory().getTitle()) & !auto_refresh) { //Title is customizable as well but shall only be updated through main thread to prevent errors
            Inventory created = manager.getShopCustomizer().createInventory(this, items, p, manager, page, highest_page, holder.getPreviousShopHolder());
            p.openInventory(created);
            return;
        }
        Inventory inventory = manager.getShopCustomizer().createInventory(this, items, p, manager, i, page, highest_page);
        if (inventory != i) {
            p.openInventory(inventory);
        }
    }

    public void loadInventorySize() {
        PageLayoutHandler layout = ClassManager.manager.getPageLayoutHandler();
        BSChoosePageLayoutEvent event = new BSChoosePageLayoutEvent(this, getShopName(), layout);
        Bukkit.getPluginManager().callEvent(event);
        layout = event.getLayout();

        if (!layout.showIfMultiplePagesOnly()) {
            inventory_size = ROW_ITEMS * layout.getMaxRows();
            return;
        }
        Set<Integer> used_slots = new HashSet<>();
        int highest = 0;
        int different_slots_amount = 0;
        for (BSBuy b : items) {
            if (b != null) {
                if (b.getInventoryLocation() == -1) { //If picking the next slot -> increasing slot number
                    different_slots_amount++;
                } else if (!used_slots.contains(b.getInventoryLocation())) { //if choosing specific slot -> store all different slots and add them in the end
                    used_slots.add(b.getInventoryLocation());
                }
                if (b.getInventoryLocation() > highest) {
                    highest = b.getInventoryLocation();
                }
            }
        }
        different_slots_amount += used_slots.size();
        inventory_size = getInventorySize(Math.max(highest, different_slots_amount - 1)); //Use either the highest slot or the number of different possible slots in order to make sure the inventory is big enough
    }

    @Deprecated
    public int getInventorySize(int i) { //i as highest slot
        i++;
        int rest = i % ROW_ITEMS;
        if (rest > 0) {
            i += ROW_ITEMS - i % ROW_ITEMS;
        }

        int max_slots_per_page = ROWS_LIMIT_CURRENT * ROW_ITEMS;

        if (!ClassManager.manager.getPageLayoutHandler().showIfMultiplePagesOnly() || i > max_slots_per_page) {
            highest_page = i / (ClassManager.manager.getPageLayoutHandler().getReservedSlotsStart() - 1); //Not tested yet!
        } else {
            highest_page = 0;
        }

        return Math.min(max_slots_per_page, Math.max(i, ROW_ITEMS * manual_inventory_rows));
    }

    public void openInventory(Player p) {
        openInventory(p, 0, true);
    }

    public void openInventory(Player p, boolean remember_current_shop) {
        openInventory(p, 0, remember_current_shop);
    }

    public void openInventory(Player p, int page, boolean remember_current_shop) {
        BSShopHolder oldshopholder = null;

        if (remember_current_shop) {
            InventoryView openinventory = p.getOpenInventory();
            if (openinventory != null) {
                if (openinventory.getTopInventory().getHolder() instanceof BSShopHolder) {
                    oldshopholder = (BSShopHolder) openinventory.getTopInventory().getHolder();
                }
            }
        }

        ClassManager.manager.getMessageHandler().sendMessage("Main.OpenShop", p, null, p, this, null, null);
        if (ClassManager.manager.getPlugin().getAPI().isValidShop(p.getOpenInventory())) {
            Misc.playSound(p, ClassManager.manager.getSettings().getPropertyString(Settings.SOUND_SHOP_CHANGE_SHOP, this, null));
        } else {
            Misc.playSound(p, ClassManager.manager.getSettings().getPropertyString(Settings.SOUND_SHOP_OPEN, this, null));
        }
        p.openInventory(createInventory(p, ClassManager.manager, page, highest_page, oldshopholder));
        ClassManager.manager.getPlayerDataHandler().openedShop(p, this);//TODO: only store previous shop, not current shop somehow.
    }

    public void close() {
        for (Player p : Bukkit.getOnlinePlayers()) { //NEW!
            if (ClassManager.manager.getPlugin().getAPI().isValidShop(p.getOpenInventory())) {
                BSShopHolder holder = ((BSShopHolder) p.getOpenInventory().getTopInventory().getHolder());
                if (holder.getShop() == this) {
                    p.closeInventory();
                }
            }
        }
    }

    public boolean isBeingAccessed(Player exclusion) {
        for (Player p : Bukkit.getOnlinePlayers()) { //NEW!
            if (ClassManager.manager.getPlugin().getAPI().isValidShop(p.getOpenInventory())) {
                BSShopHolder holder = ((BSShopHolder) p.getOpenInventory().getTopInventory().getHolder());
                if (holder.getShop() == this) {
                    if (p != exclusion) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //////////////////////////// <- Load Methods

    public void finishedAddingItems() {
        loadInventorySize();
    }

    //////////////////////////// <- Abstract

    public abstract void reloadShop();


}
