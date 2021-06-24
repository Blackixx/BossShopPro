package org.black_ixx.bossshop.managers.item;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;


public class ItemDataPartMobspawner extends ItemDataPart {

    @Override
    public ItemStack transform(ItemStack item, String used_name, String argument) {
        if (ClassManager.manager.getSpawnerHandler() == null) {
            ClassManager.manager.getBugFinder().warn("Unable to work with ItemData of type " + createNames()[0] + ": Requires the plugin SilkSpawners or EpicSpawners.");
            return item;
        }

        ItemStack spawner = ClassManager.manager.getSpawnerHandler().transformSpawner(item, argument);
        if (spawner == null) {
            ClassManager.manager.getBugFinder().severe("Mistake in Config: '" + argument + "' is not a valid mob type ('" + used_name + "').");
            return item;
        }

        return spawner;
    }

    @Override
    public int getPriority() {
        return PRIORITY_MOST_EARLY;
    }

    @Override
    public boolean removeSpaces() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"mobspawner", "monsterspawner", "spawner"};
    }


    @Override
    public List<String> read(ItemStack i, List<String> output) {
        if (i.getType() == Material.SPAWNER) {
            if (ClassManager.manager.getSpawnerHandler() == null) {
                output.add("(You need the plugin SilkSpawners or EpicSpawners in order to read/create pre-set mobspawners)");
            } else {
                output.add("mobspawner:" + ClassManager.manager.getSpawnerHandler().readSpawner(i));
            }
        }
        return output;
    }


    @Override
    public boolean isSimilar(ItemStack shop_item, ItemStack player_item, BSBuy buy, Player p) {
        if (shop_item.getType() == Material.SPAWNER) {
            if (player_item.getType() != Material.SPAWNER) {
                return false;
            }

            if (ClassManager.manager.getSpawnerHandler() != null) {
                String spawners = ClassManager.manager.getSpawnerHandler().readSpawner(shop_item);
                String spawnerp = ClassManager.manager.getSpawnerHandler().readSpawner(player_item);
                return spawners.equalsIgnoreCase(spawnerp);
            }
        }
        return true;
    }
}
