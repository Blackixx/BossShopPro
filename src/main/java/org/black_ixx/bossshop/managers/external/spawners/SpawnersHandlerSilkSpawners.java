package org.black_ixx.bossshop.managers.external.spawners;


import de.dustplanet.util.SilkUtil;
import org.black_ixx.bossshop.managers.misc.InputReader;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;


public class SpawnersHandlerSilkSpawners implements ISpawnerHandler, ISpawnEggHandler {

    private SilkUtil util;


    public SpawnersHandlerSilkSpawners() {
        util = SilkUtil.hookIntoSilkSpanwers();
    }

    public ItemStack transformSpawner(ItemStack i, String entityName) {
        EntityType type = InputReader.readEntityType(entityName);
        if (type == null) {
            return null;
        }
        return util.newSpawnerItem(type.getTypeId(), null, i.getAmount(), false);
    }

    public ItemStack transformEgg(ItemStack i, String entityName) {
        EntityType type = InputReader.readEntityType(entityName);
        if (type == null) {
            return null;
        }
        return util.newEggItem(type.getTypeId(), entityName, i.getAmount());
    }


    public String readSpawner(ItemStack i) {
        short entityid = util.getStoredSpawnerItemEntityID(i);
        String creaturename = util.getCreatureName(entityid);
        return creaturename;
    }

    public String readEgg(ItemStack i) {
        short entityid = util.getStoredEggEntityID(i);
        String creaturename = util.getCreatureName(entityid);
        return creaturename;
    }
}
