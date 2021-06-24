package org.black_ixx.bossshop.managers.external.spawners;

import org.bukkit.inventory.ItemStack;

public interface ISpawnerHandler {


    ItemStack transformSpawner(ItemStack i, String entityName);

    String readSpawner(ItemStack i);
}
