package org.black_ixx.bossshop.managers.item;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.tags.ItemTagType;

import java.util.List;

public class ItemDataPartPlayerhead extends ItemDataPart {

    @Override
    public ItemStack transform(ItemStack item, String used_name, String argument) {
        if (!(item.getItemMeta() instanceof SkullMeta)) {
            ClassManager.manager.getBugFinder().warn("Mistake in Config: Itemdata of type '" + used_name + "' with value '" + argument + "' can not be added to an item with material '" + item.getType().name() + "'. Don't worry I'll automatically transform the material into '" + Material.PLAYER_HEAD.name() + ".");
            item.setType(Material.PLAYER_HEAD);
        }

        SkullMeta meta = (SkullMeta) item.getItemMeta();

        if (ClassManager.manager.getStringManager().checkStringForFeatures(null, null, null, argument)) {
            NamespacedKey key = new NamespacedKey(ClassManager.manager.getPlugin(), "skullOwnerPlaceholder");
            meta.getCustomTagContainer().setCustomTag(key, ItemTagType.STRING, argument); //argument = placeholder
        } else {
            OfflinePlayer player = Bukkit.getOfflinePlayer(argument);
            if (player != null) {
                meta.setOwningPlayer(player);
            } else {
                meta.setOwner(argument);
            }
        }

        item.setItemMeta(meta);
        return item;
    }

    @Override
    public int getPriority() {
        return PRIORITY_EARLY;
    }

    @Override
    public boolean removeSpaces() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"playerhead", "head", "owner"};
    }


    @Override
    public List<String> read(ItemStack i, List<String> output) {
        if (i.getItemMeta() instanceof SkullMeta) {
            SkullMeta meta = (SkullMeta) i.getItemMeta();
            if (i.getType() == Material.PLAYER_HEAD) {
                if (meta.hasOwner()) {
                    output.add("playerhead:" + meta.getOwner());
                }
            }
        }
        return output;
    }


    @Override
    public boolean isSimilar(ItemStack shop_item, ItemStack player_item, BSBuy buy, Player p) {
        if (shop_item.getType() == Material.PLAYER_HEAD) {
            if (player_item.getType() != Material.PLAYER_HEAD) {
                return false;
            }

            SkullMeta ms = (SkullMeta) shop_item.getItemMeta();
            SkullMeta mp = (SkullMeta) player_item.getItemMeta();

            if (ms.hasOwner()) {

                if (!mp.hasOwner()) {
                    return false;
                }

                return ms.getOwner().equalsIgnoreCase(mp.getOwner());

            }
        }
        return true;
    }


}
