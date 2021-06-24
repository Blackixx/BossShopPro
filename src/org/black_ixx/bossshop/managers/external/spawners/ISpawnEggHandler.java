package org.black_ixx.bossshop.managers.external.spawners;

import org.bukkit.inventory.ItemStack;

public interface ISpawnEggHandler {


    ItemStack transformEgg(ItemStack i, String entityName);

    String readEgg(ItemStack i);
}
