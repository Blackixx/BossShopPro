package org.black_ixx.bossshop.managers.external.spawners;


import com.songoda.epicspawners.API.EpicSpawners;
import org.bukkit.inventory.ItemStack;


public class SpawnersHandlerEpicSpawners implements ISpawnerHandler {

    private EpicSpawners util;


    public SpawnersHandlerEpicSpawners() {
        util = EpicSpawners.getInstance();
    }

    public ItemStack transformSpawner(ItemStack i, String entityType) {
        return util.getSpawnerManager().getSpawnerData(entityType).toItemStack();
    }

    public String readSpawner(ItemStack i) {
        return util.getSpawnerManager().getSpawnerData(i).getIdentifyingName();
    }

}
