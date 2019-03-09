package org.black_ixx.bossshop.managers.item;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.misc.InputReader;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ItemDataPartMaterial extends ItemDataPart {

    @Override
    public ItemStack transform(ItemStack item, String used_name, String argument) {
        short durability = 0;
        Material m = Material.STONE;

        if (argument.contains(":")) { //Can be used for durability
            String[] parts = argument.split(":");
            if (parts.length > 1) {
                durability = (short) InputReader.getInt(parts[1].trim(), 0);
            }
            argument = parts[0].trim();
        }


        m = InputReader.readMaterial(argument);

        if (m == null) {
            ClassManager.manager.getBugFinder().severe("Mistake in Config: '" + argument + "' is not a valid '" + used_name + "'. Unable to find a fitting material.");
            return item;
        }

        item.setType(m);
        item.setDurability(durability);
        return item;
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
        return new String[]{"type", "id", "material"};
    }


    @Override
    public List<String> read(ItemStack i, List<String> output) {
        output.add("type:" + i.getType().name());
        return output;
    }


    @Override
    public boolean isSimilar(ItemStack shop_item, ItemStack player_item, BSBuy buy, Player p) {
        return shop_item.getType() == player_item.getType();
    }
}
