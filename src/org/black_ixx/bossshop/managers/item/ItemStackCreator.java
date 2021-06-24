package org.black_ixx.bossshop.managers.item;


import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.config.BSConfigShop;
import org.black_ixx.bossshop.managers.misc.InputReader;
import org.black_ixx.bossshop.managers.misc.StringManipulationLib;
import org.black_ixx.bossshop.misc.Misc;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;


public class ItemStackCreator {


    public ItemStack createItemStack(List<String> itemData, BSBuy buy, BSShop shop, boolean final_version) { //This allows to work with %rewarditem_<id>% and %priceitem_<id>%

        if (shop instanceof BSConfigShop) {
            BSConfigShop cshop = (BSConfigShop) shop;

            List<String> new_list = null;
            for (String line : itemData) {

                String reward_line = StringManipulationLib.figureOutVariable(line, "rewarditem", 0);
                if (reward_line != null) {
                    if (new_list == null) {
                        new_list = cloneList(itemData);
                    }
                    int i = InputReader.getInt(reward_line, -1) - 1;
                    new_list = transform(line, i, new_list, buy, cshop, "Reward");
                }

                String price_line = StringManipulationLib.figureOutVariable(line, "priceitem", 0);
                if (price_line != null) {
                    if (new_list == null) {
                        new_list = cloneList(itemData);
                    }
                    int i = InputReader.getInt(reward_line, -1) - 1;
                    new_list = transform(line, i, new_list, buy, cshop, "Price");
                }

                if (new_list != null) {
                    return createItemStack(new_list, final_version);
                }

            }


        }

        return createItemStack(itemData, final_version);
    }

    private List<String> transform(String line, int index, List<String> new_list, BSBuy buy, BSConfigShop shop, String path) {
        if (index != -1) {
            new_list.remove(line);

            List<List<String>> list = InputReader.readStringListList(buy.getConfigurationSection(shop).get(path));
            if (list != null) {
                if (list.size() > index) {
                    for (String entry : list.get(index)) {
                        new_list.add(entry);
                    }
                } else {
                    ClassManager.manager.getBugFinder().warn("Was trying to import the item look for MenuItem of shopitem '" + buy.getName() + "' in shop '" + shop.getShopName() + "' but your " + path + " does not contain a " + index + ". item!");
                }
            } else {
                ClassManager.manager.getBugFinder().warn("Was trying to import the item look for MenuItem of shopitem '" + buy.getName() + "' in shop '" + shop.getShopName() + "' but your " + path + " is not an item list!");
            }
        }
        return new_list;
    }

    private List<String> cloneList(List<String> list) {
        if (list != null & !list.isEmpty()) {
            List<String> clone = new ArrayList<String>();
            for (String line : list) {
                clone.add(line);
            }
            return clone;
        }
        return list;
    }

    public ItemStack createItemStack(List<String> itemData, boolean final_version) {
        ItemStack i = new ItemStack(Material.STONE);

        itemData = Misc.fixLore(itemData);

        i = ItemDataPart.transformItem(i, itemData);
        ClassManager.manager.getItemStackTranslator().translateItemStack(null, null, null, i, null, final_version);
        return i;
    }

    /**
     * Gives the selected item to the player.
     *
     * @param p          Player to give the item to.
     * @param buy        Shopitem linked to the item.
     * @param i          Item to add to the player.
     * @param clone_item Whether the item selected can be modified or if a clone of the selected item should be used instead, keeping the original item unchanged.
     * @post If clone_item = false the item is modified (placeholders adapted to player and amount changed).
     */
    public void giveItem(Player p, BSBuy buy, ItemStack i, boolean clone_item) {
        giveItem(p, buy, i, i.getAmount(), clone_item);
    }


    /**
     * Gives the selected item to the player.
     *
     * @param p          Player to give the item to.
     * @param buy        Shopitem linked to the item.
     * @param i          Item to add to the player.
     * @param amount     Amount of the item to add to the player.
     * @param clone_item Whether the item selected can be modified or if a clone of the selected item should be used instead, keeping the original item unchanged.
     * @post If clone_item = false the item is modified (placeholders adapted to player and amount changed).
     */
    public void giveItem(Player p, BSBuy buy, ItemStack i, int amount, boolean clone_item) {
        if (clone_item) {
            i = i.clone();
        }

        int to_give = amount;
        int stacksize = ClassManager.manager.getItemStackChecker().getMaxStackSize(i);

        //First of all translate item
        i = ClassManager.manager.getItemStackTranslator().translateItemStack(buy, null, null, (clone_item ? i.clone() : i), p, true);

        while (to_give > 0) {
            i.setAmount(Math.min(stacksize, to_give));
            giveItem(p, i.clone(), stacksize);
            to_give -= i.getAmount();
        }
    }


    /**
     * Gives an unmodified version of the item to the player.
     *
     * @param p         Player to give the item to.
     * @param i         ItemStack to give to the player.
     * @param stacksize Maximum stack size determined using {@link ItemStackChecker#getMaxStackSize(ItemStack)}.
     * @pre The item needs to be translated and adapted to the player already.
     */
    private void giveItem(Player p, ItemStack i, int stacksize) {
        if (i.getAmount() > stacksize) {
            throw new RuntimeException("Can not give an itemstack with a higher amount than the maximum stack size at once to a player.");
        }
        int free = ClassManager.manager.getItemStackChecker().getAmountOfFreeSpace(p, i);

        if (free < i.getAmount()) { //Not enough space?
            dropItem(p, i, stacksize);
        } else {
            p.getInventory().addItem(i);
        }
    }

    private void dropItem(Player p, ItemStack i, int stacksize) {
        int toTake = i.getAmount();
        int amount;
        i = i.clone();

        while (toTake > 0) {
            amount = Math.min(toTake, stacksize);
            i.setAmount(amount);
            p.getWorld().dropItem(p.getLocation(), i);// Drop Item!
            toTake -= amount;
        }
    }

}
