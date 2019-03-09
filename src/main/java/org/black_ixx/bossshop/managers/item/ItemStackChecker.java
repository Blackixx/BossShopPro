package org.black_ixx.bossshop.managers.item;


import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemStackChecker {

    public final static int INVENTORY_SLOT_START = 0;
    public final static int INVENTORY_SLOT_END = 35;

    private List<String> tools_suffixes;
    private List<Material> tools_complete;

    public ItemStackChecker() {
        tools_suffixes = new ArrayList<>();
        tools_suffixes.add("AXE");
        tools_suffixes.add("HOE");
        tools_suffixes.add("PICKAXE");
        tools_suffixes.add("SPADE");
        tools_suffixes.add("SWORD");

        tools_suffixes.add("BOOTS");
        tools_suffixes.add("CHESTPLATE");
        tools_suffixes.add("HELMET");
        tools_suffixes.add("LEGGINGS");

        tools_suffixes.add("BARDING");

        tools_complete = new ArrayList<>();
        tools_complete.add(Material.BOW);
        tools_complete.add(Material.FLINT_AND_STEEL);
        tools_complete.add(Material.SHEARS);
        tools_complete.add(Material.FISHING_ROD);
    }


    public boolean inventoryContainsItem(Player p, ItemStack i, BSBuy buy) {
        if (getAmountOfSameItems(p, i, buy) >= i.getAmount()) {
            return true;
        }
        return false;
    }

    public void takeItem(ItemStack shop_item, Player p, BSBuy buy) {
        int a = 0;
        int slot = -1;

        for (ItemStack player_item : p.getInventory().getContents()) {
            slot++;
            if (player_item != null && player_item.getType() != Material.AIR) {
                if (canSell(p, player_item, shop_item, slot, buy)) {

                    player_item = player_item.clone(); //New
                    player_item.setAmount(Math.min(player_item.getAmount(), shop_item.getAmount() - a)); //New

                    a += player_item.getAmount();
                    //remove(p, player_item); //Old
                    p.getInventory().removeItem(player_item); //New

                    if (a >= shop_item.getAmount()) { //Reached amount. Can stop!
                        break;
                    }
                }
            }
        }

        a -= shop_item.getAmount();
        if (a > 0) {
            ClassManager.manager.getBugFinder().warn("Player " + p.getName() + " lost " + a + " items of type " + shop_item.getType().name() + ". How would that happen?");
        }
        return;
    }

    public int getAmountOfSameItems(Player p, ItemStack shop_item, BSBuy buy) {
        int a = 0;
        int slot = -1;

        for (ItemStack player_item : p.getInventory().getContents()) {
            slot++;
            if (player_item != null) {
                if (canSell(p, player_item, shop_item, slot, buy)) {
                    a += player_item.getAmount();
                }
            }
        }
        return a;
    }

    public boolean hasFreeSpace(Player p, ItemStack item) {
        return getAmountOfFreeSpace(p, item) >= item.getAmount();
    }

    public int getAmountOfFreeSpace(Player p, ItemStack item) {
        int freeSlots = 0;
        for (int slot = INVENTORY_SLOT_START; slot <= INVENTORY_SLOT_END; slot++) {
            ItemStack slotItem = p.getInventory().getItem(slot);
            if (slotItem == null || slotItem.getType() == Material.AIR) {
                freeSlots += item.getMaxStackSize();
            } else {
                if (slotItem.isSimilar(item)) {
                    freeSlots += Math.max(0, slotItem.getMaxStackSize() - slotItem.getAmount());
                }
            }
        }
        return freeSlots;
    }

    public boolean hasFreeSpace(Player p, List<ItemStack> items) {
        HashMap<ItemStack, Integer> amounts = new HashMap<>();

        //Make amounts ready
        for (ItemStack item : items) {
            int amount = item.getAmount();
            if (amounts.containsKey(item)) {
                amount += amounts.get(item);
            }
            amounts.put(item, amount);
        }

        //Decrease amounts using already filled inventory slots
        for (int slot = INVENTORY_SLOT_START; slot <= INVENTORY_SLOT_END; slot++) {
            ItemStack slotItem = p.getInventory().getItem(slot);
            if (slotItem != null) {

                for (ItemStack item : amounts.keySet()) {
                    if (slotItem.isSimilar(item)) {
                        int spaceLeft = slotItem.getMaxStackSize() - slotItem.getAmount();
                        int amountLeft = Math.max(0, amounts.get(item) - spaceLeft);
                        if (amountLeft == 0) {
                            amounts.remove(item);
                        } else {
                            amounts.put(item, amountLeft);
                        }
                        break; //break for loop because slot is already used
                    }
                }

            }
        }

        //Decrease amounts using empty inventory slots
        for (int slot = INVENTORY_SLOT_START; slot <= INVENTORY_SLOT_END; slot++) {
            ItemStack slotItem = p.getInventory().getItem(slot);
            if (slotItem == null) {

                for (ItemStack item : amounts.keySet()) {
                    int amountLeft = amounts.get(item);
                    if (amountLeft > 0) {
                        amountLeft = Math.max(0, amountLeft - item.getMaxStackSize());
                        if (amountLeft == 0) {
                            amounts.remove(item);
                        } else {
                            amounts.put(item, amountLeft);
                        }
                        break; //break for loop because slot is already used
                    }
                }

            }
        }

        return amounts.isEmpty();
    }


    private boolean canSell(Player p, ItemStack player_item, ItemStack shop_item, int slot, BSBuy buy) {
        if (slot < INVENTORY_SLOT_START || slot > INVENTORY_SLOT_END) { //Has to be inside normal inventory
            return false;
        }

        ItemDataPart exception_durability = isTool(player_item) && ClassManager.manager.getSettings().getAllowSellingDamagedItems() ? ItemDataPart.DURABILITY : null;
        ItemDataPart[] exceptions = new ItemDataPart[]{exception_durability};

        return ItemDataPart.isSimilar(shop_item, player_item, exceptions, buy, false, p);
    }


    public boolean isEqualShopItemAdvanced(ItemStack a, ItemStack b, boolean compare_text, Player p) {
        return isEqualShopItemAdvanced(a, b, compare_text, true, true, p);
    }

    public boolean isEqualShopItemAdvanced(ItemStack a, ItemStack b, boolean compare_text, boolean compare_amount, boolean compare_itemmeta_existence, Player p) {
        if (a != null && b != null) {
            if (compare_itemmeta_existence && a.hasItemMeta() != b.hasItemMeta()) {
                return false;
            }

            ItemDataPart[] exceptions;
            ItemDataPart exception_durability = isTool(a) && ClassManager.manager.getSettings().getAllowSellingDamagedItems() ? ItemDataPart.DURABILITY : null;
            if (!compare_text) {
                exceptions = new ItemDataPart[]{exception_durability, ItemDataPart.NAME, ItemDataPart.LORE, ItemDataPart.PLAYERHEAD};
            } else {
                exceptions = new ItemDataPart[]{exception_durability};
            }


            return ItemDataPart.isSimilar(a, b, exceptions, null, compare_amount, p);
        }
        return false;
    }

    public boolean isTool(ItemStack i) {
        if (tools_complete.contains(i.getType())) {
            return true;
        }
        for (String tool_suffix : tools_suffixes) {
            if (i.getType().name().endsWith(tool_suffix)) {
                return true;
            }
        }
        return false;
    }

    public int getMaxStackSize(ItemStack i) {
        if (ClassManager.manager.getSettings().getCheckStackSize()) {
            return i.getMaxStackSize();
        }
        return 64;
    }

}
