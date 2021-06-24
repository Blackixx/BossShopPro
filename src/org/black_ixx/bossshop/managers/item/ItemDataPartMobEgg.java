package org.black_ixx.bossshop.managers.item;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;


public class ItemDataPartMobEgg extends ItemDataPart {

    @Override
    public ItemStack transform(ItemStack item, String used_name, String argument) {
        if (ClassManager.manager.getSpawnEggHandler() == null) {
            ClassManager.manager.getBugFinder().warn("Unable to work with ItemData of type " + createNames()[0] + ": Requires the plugin SilkSpawners, which was not found.");
            return item;
        }

        ItemStack egg = ClassManager.manager.getSpawnEggHandler().transformEgg(item, argument);
        if (egg == null) {
            ClassManager.manager.getBugFinder().severe("Mistake in Config: '" + argument + "' is not a valid mob type ('" + used_name + "').");
            return item;
        }

        return egg;
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
        return new String[]{"monsteregg", "mobegg", "spawnegg"};
    }


    @Override
    public List<String> read(ItemStack i, List<String> output) {
        if (i.getType().name().endsWith("SPAWN_EGG")) {
            if (ClassManager.manager.getSpawnEggHandler() != null) {
                output.add("monsteregg:" + ClassManager.manager.getSpawnEggHandler().readEgg(i));
            } else {
                //no custom output needed: Knowing the material type is enough
            }
        }
        return output;
    }


    @Override
    public boolean isSimilar(ItemStack shop_item, ItemStack player_item, BSBuy buy, Player p) {
        if (!shop_item.getType().equals(player_item.getType())) {
            return false;
        }

        if (ClassManager.manager.getSpawnEggHandler() != null) { //just necessary for custom mob types: General ones are determined by material name
            String mobs = ClassManager.manager.getSpawnEggHandler().readEgg(shop_item);
            String mobp = ClassManager.manager.getSpawnEggHandler().readEgg(player_item);
            return mobs.equalsIgnoreCase(mobp);
        }
        return true;
    }

}
