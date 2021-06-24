package org.black_ixx.bossshop.managers.item;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemDataPartName extends ItemDataPart {

    @Override
    public ItemStack transform(ItemStack item, String used_name, String argument) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(argument);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public int getPriority() {
        return PRIORITY_NORMAL;
    }

    @Override
    public boolean removeSpaces() {
        return false;
    }

    @Override
    public String[] createNames() {
        return new String[]{"name", "text", "title"};
    }


    @Override
    public List<String> read(ItemStack i, List<String> output) {
        ItemMeta meta = i.getItemMeta();
        if (meta.hasDisplayName()) {
            output.add("name:" + meta.getDisplayName().replaceAll(String.valueOf(ChatColor.COLOR_CHAR), "&"));
        }
        return output;
    }


    @Override
    public boolean isSimilar(ItemStack shop_item, ItemStack player_item, BSBuy buy, Player p) {
        ItemMeta ms = shop_item.getItemMeta();
        ItemMeta mp = player_item.getItemMeta();
        if (ms.hasDisplayName()) {
            if (!mp.hasDisplayName()) {
                return false;
            }
            String shop_item_name = ms.getDisplayName();
            if (ClassManager.manager.getStringManager().checkStringForFeatures(buy == null ? null : buy.getShop(), buy, buy == null ? null : buy.getItem(), shop_item_name)) {
                shop_item_name = ClassManager.manager.getStringManager().transform(shop_item_name, buy, buy == null ? null : buy.getShop(), null, p);
            }
            return shop_item_name.equalsIgnoreCase(mp.getDisplayName());
        }
        return true;
    }


}
