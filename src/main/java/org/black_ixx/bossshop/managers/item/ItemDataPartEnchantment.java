package org.black_ixx.bossshop.managers.item;

import com.vk2gpz.tokenenchant.api.TokenEnchantAPI;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.misc.InputReader;
import org.black_ixx.bossshop.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.List;
import java.util.Map;


public class ItemDataPartEnchantment extends ItemDataPart {

    @Override
    public ItemStack transform(ItemStack item, String used_name, String argument) {
        String[] parts = argument.split("#");
        if (parts.length != 2) {
            ClassManager.manager.getBugFinder().severe("Mistake in Config: '" + argument + "' is not a valid '" + used_name + "'. It has to look like this: '<enchantment name/id>#<level>'. For example 'DAMAGE_ALL#5'.");
            return item;
        }

        String enchantment = parts[0].trim();
        int level = InputReader.getInt(parts[1].trim(), -1);

        if (level == -1) {
            ClassManager.manager.getBugFinder().severe("Mistake in Config: '" + argument + "' is not a valid '" + used_name + "'. The level of the enchantment is invalid.");
            return item;
        }

        Enchantment e = InputReader.readEnchantment(enchantment);

        if (e == null && Bukkit.getPluginManager().isPluginEnabled("TokenEnchant")) {
            TokenEnchantAPI te = TokenEnchantAPI.getInstance();
            item = te.enchant(null, item, enchantment, level, true, 0, false);
            return item;
        }

        if (e == null) {
            ClassManager.manager.getBugFinder().severe("Mistake in Config: '" + enchantment + "' is not a valid '" + used_name + "'. The name/id of the enchantment is not known.");
            return item;
        }

        if (item.getItemMeta() instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
            meta.addStoredEnchant(e, level, true);
            item.setItemMeta(meta);
        } else {
            item.addUnsafeEnchantment(e, level);
        }

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
        return new String[]{"enchantment", "enchantmentid"};
    }


    @Override
    public List<String> read(ItemStack i, List<String> output) {
        if (i.getEnchantments() != null) {
            Map<Enchantment, Integer> enchantments = i.getEnchantments();
            for (Enchantment enchantment : enchantments.keySet()) {
                output.add("enchantment:" + enchantment.getKey().getKey() + "#" + enchantments.get(enchantment));
            }
        }
        if (i.getItemMeta() instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) i.getItemMeta();
            Map<Enchantment, Integer> enchantments = meta.getStoredEnchants();
            for (Enchantment enchantment : enchantments.keySet()) {
                output.add("enchantment:" + enchantment.getKey().getKey() + "#" + enchantments.get(enchantment));
            }
        }
        return output;
    }


    @Override
    public boolean isSimilar(ItemStack shop_item, ItemStack player_item, BSBuy buy, Player p) {
        //normal enchantments
        if (!containsEnchantments(shop_item.getEnchantments(), player_item.getEnchantments(), buy)) {
            return false;
        }


        //enchantmentbook enchantments
        if (shop_item.getItemMeta() instanceof EnchantmentStorageMeta) {

            if (!(player_item.getItemMeta() instanceof EnchantmentStorageMeta)) {
                return false;
            }

            EnchantmentStorageMeta ms = (EnchantmentStorageMeta) shop_item.getItemMeta();
            EnchantmentStorageMeta mp = (EnchantmentStorageMeta) player_item.getItemMeta();
            if (!containsEnchantments(ms.getStoredEnchants(), mp.getStoredEnchants(), buy)) {
                return false;
            }

        }


        return true;
    }


    private boolean containsEnchantments(Map<Enchantment, Integer> es, Map<Enchantment, Integer> ep, BSBuy buy) {
        for (Enchantment e : es.keySet()) {
            if (!ep.containsKey(e)) {
                return false;
            }
            if (ep.get(e) < es.get(e)) {
                return false;
            }
        }
        if (!ClassManager.manager.getSettings().getPropertyBoolean(Settings.ALLOW_SELLING_GREATER_ENCHANTS, buy)) {
            for (Enchantment e : ep.keySet()) {
                if (!es.containsKey(e)) {
                    return false;
                }
                if (es.get(e) < ep.get(e)) {
                    return false;
                }
            }
        }

        return true;
    }


}
