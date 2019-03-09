package org.black_ixx.bossshop.managers.external.spawners;


import com.songoda.epicspawners.api.EpicSpawners;
import com.songoda.epicspawners.api.EpicSpawnersAPI;
import org.bukkit.inventory.ItemStack;


public class SpawnersHandlerEpicSpawners implements ISpawnerHandler {

    private EpicSpawners util;


    public SpawnersHandlerEpicSpawners() {
        util = EpicSpawnersAPI.getImplementation();
    }

    public ItemStack transformSpawner(ItemStack i, String entityType) {
        return util.newSpawnerItem(util.createSpawnerData(entityType).build(), 1);
    }

    public String readSpawner(ItemStack i) {
        return util.getSpawnerDataFromItem(i).getIdentifyingName();
    }

}
