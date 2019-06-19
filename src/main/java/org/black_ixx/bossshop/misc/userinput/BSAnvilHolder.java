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


    /**
     * Get the inventory
     * @return inventory
     */
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Set the inventory
     * @param i inventory
     */
    public void setInventory(Inventory i) {
        this.inventory = i;
    }

    /**
     * Get the output text from the inventory
     * @return output text
     */
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


    /**
     * Check if player clicked result
     * @param p player to check
     */
    public abstract void userClickedResult(Player p);


}
