package org.black_ixx.bossshop.misc.userinput;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class BSAnvilHolder implements InventoryHolder {

    private Inventory inventory;

    public BSAnvilHolder() {
    }

    public BSAnvilHolder(Inventory inventory) {
        this.inventory = inventory;
    }


    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory i) {
        this.inventory = i;
    }

    public String getOutputText() {
        if (inventory != null) {
            ItemStack item = inventory.getItem(2);
            if (item != null) { //Somehow the item in the result slot (slot 3) always is null?! Even when one is displayed by client
                if (item.hasItemMeta()) {
                    return item.getItemMeta().getDisplayName();
                }
            }
        }
        return null;
    }


    public abstract void userClickedResult(Player p);


}
