package org.black_ixx.bossshop.managers.item;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.misc.InputReader;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.List;

public class ItemDataPartPotion extends ItemDataPart {

    @Override
    public ItemStack transform(ItemStack item, String used_name, String argument) {
        String[] parts = argument.split("#");
        if (parts.length != 3) {
            ClassManager.manager.getBugFinder().severe("Mistake in Config: '" + argument + "' is not a valid '" + used_name + "'. It has to look like this: '<potion name/id>#<extended? true/false><upgraded? true/false>'. For example 'potion:POISON#true#true'.");
            return item;
        }

        if (!(item.getItemMeta() instanceof PotionMeta)) {
            ClassManager.manager.getBugFinder().severe("Mistake in Config: You can not make potions out of an item with material '" + item.getType().name() + "'! Following line is invalid: '" + used_name + ":" + argument + "'.");
            return item;
        }

        PotionMeta meta = (PotionMeta) item.getItemMeta();

        String potiontype = parts[0].trim();
        boolean extended = InputReader.getBoolean(parts[1].trim(), false);
        boolean upgraded = InputReader.getBoolean(parts[2].trim(), false);

        PotionType type = null;
        for (PotionType t : PotionType.values()) {
            if (potiontype.equalsIgnoreCase(t.name())) {
                type = t;
                break;
            }
        }

        if (type == null) {
            ClassManager.manager.getBugFinder().severe("Mistake in Config: '" + argument + "' is not a valid '" + used_name + "'. The name of the potion is not known.");
            return item;
        }

        meta.setBasePotionData(new PotionData(type, extended, upgraded));
        item.setItemMeta(meta);

        return item;
    }

    @Override
    public int getPriority() {
        return PRIORITY_LATE;
    }

    @Override
    public boolean removeSpaces() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"potion"};
    }

    @Override
    public List<String> read(ItemStack i, List<String> output) {
        if (i.getItemMeta() instanceof PotionMeta) {
            PotionMeta meta = (PotionMeta) i.getItemMeta();
            if (meta.getBasePotionData() != null) {
                output.add("potion:" + meta.getBasePotionData().getType().name() + "#" + meta.getBasePotionData().isExtended() + "#" + meta.getBasePotionData().isUpgraded());
            }
        }
        return output;
    }

    @Override
    public boolean isSimilar(ItemStack shop_item, ItemStack player_item, BSBuy buy, Player p) {
        if (shop_item.getItemMeta() instanceof PotionMeta) {


            if (!(player_item.getItemMeta() instanceof PotionMeta)) {
                return false;
            }

            PotionMeta ms = (PotionMeta) shop_item.getItemMeta();
            PotionMeta mp = (PotionMeta) player_item.getItemMeta();

            if (ms.getBasePotionData().getType() == PotionType.WATER || ms.getBasePotionData().getType() == PotionType.UNCRAFTABLE) {
                return true;
            }

            if (ms.getBasePotionData().getType() != mp.getBasePotionData().getType()) {
                return false;
            }

            if (ms.getBasePotionData().isExtended() & !mp.getBasePotionData().isExtended()) {
                return false;
            }
            if (ms.getBasePotionData().isUpgraded() & !mp.getBasePotionData().isUpgraded()) {
                return false;
            }

        }
        return true;
    }

}
