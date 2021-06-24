package org.black_ixx.bossshop.managers.serverpinging;

import org.black_ixx.bossshop.core.BSBuy;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ConnectedBuyItem {

    private List<String> original_lore;
    private String original_name;
    private BSBuy buy;

    public ConnectedBuyItem(BSBuy original_buy, ItemStack menu_item) {
        this.buy = original_buy;
        if (menu_item != null) {
            ItemMeta meta = menu_item.getItemMeta();
            if (meta != null) {
                this.original_lore = meta.getLore();
                this.original_name = meta.getDisplayName();
            }
        }
    }

    public ConnectedBuyItem(List<String> original_lore, String original_name, BSBuy buy) {
        this.original_lore = original_lore;
        this.original_name = original_name;
        this.buy = buy;
    }


    public List<String> getOriginalLore() {
        return original_lore;
    }

    public String getOriginalName() {
        return original_name;
    }

    public BSBuy getShopItem() {
        return buy;
    }

}
