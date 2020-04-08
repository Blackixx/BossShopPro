package com.songoda.epicspawners.API;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class EpicSpawners {

    public int getSpawnerMultiplier(Location location) {
        throw new UnsupportedOperationException();
    }

    public ItemStack newSpawnerItem(EntityType type, int amount) {
        throw new UnsupportedOperationException();
    }

    public EntityType getType(ItemStack stack) {
        throw new UnsupportedOperationException();
    }

    public static EpicSpawners getInstance() {
        throw new UnsupportedOperationException();
    }

    public EpicSpawnersManager getSpawnerManager() {
        throw new UnsupportedOperationException();
    }
}