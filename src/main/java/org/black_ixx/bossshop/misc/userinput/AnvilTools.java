package org.black_ixx.bossshop.misc.userinput;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AnvilTools {


    //WARNING: MIGHT NOT WORK WHEN USING OLDER MC BUILDS.
    public static Inventory openAnvilGui(String title, ItemStack item, BSAnvilHolder holder, Player p) {
        Inventory i = createAnvilGui(title, item, holder);
        p.openInventory(i);
        return i;
    }

    public static Inventory createAnvilGui(String title, ItemStack item, BSAnvilHolder holder) {
        Inventory inventory = Bukkit.createInventory(holder, InventoryType.ANVIL);
        item = item.clone();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(title);
        item.setItemMeta(meta);
        inventory.setItem(0, item);
        holder.setInventory(inventory);
        return inventory;
    }

}
