package org.black_ixx.bossshop.managers.item;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.misc.StringManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagType;

import java.util.ArrayList;
import java.util.List;


public class ItemStackTranslator {


    public ItemStack translateItemStack(BSBuy buy, BSShop shop, BSShopHolder holder, ItemStack item, Player target, boolean final_version) {
        if (item != null) {
            if (item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();

                //Normal itemdata
                if (meta.hasDisplayName()) {
                    meta.setDisplayName(ClassManager.manager.getStringManager().transform(meta.getDisplayName(), buy, shop, holder, target));
                }

                if (meta.hasLore()) {
                    List<String> lore = meta.getLore();
                    for (int i = 0; i < lore.size(); i++) {
                        lore.set(i, ClassManager.manager.getStringManager().transform(lore.get(i), buy, shop, holder, target));
                    }
                    meta.setLore(splitLore(lore, ClassManager.manager.getSettings().getMaxLineLength(), final_version));
                }


                //Skull itemdata
                if (meta instanceof SkullMeta) {
                    SkullMeta skullmeta = (SkullMeta) meta;
                    NamespacedKey key = new NamespacedKey(ClassManager.manager.getPlugin(), "skullOwnerPlaceholder");
                    CustomItemTagContainer tagContainer = meta.getCustomTagContainer();
                    if (tagContainer.hasCustomTag(key, ItemTagType.STRING)) {
                        String placeholder = tagContainer.getCustomTag(key, ItemTagType.STRING);
                        if (placeholder != null) {
                            String playerName = ClassManager.manager.getStringManager().transform(placeholder, target);
                            OfflinePlayer transformedPlayer = Bukkit.getOfflinePlayer(playerName);
                            if (transformedPlayer != null) {
                                skullmeta.setOwningPlayer(transformedPlayer);
                            } else {
                                skullmeta.setOwner(playerName);
                            }
                        }
                    }
                }

                item.setItemMeta(meta);


                if (meta instanceof SkullMeta) {
                    transformCustomSkull(buy, shop, item, holder, target);
                }
            }


        }
        return item;
    }


    private void transformCustomSkull(BSBuy buy, BSShop shop, ItemStack item, BSShopHolder holder, Player target) {
        String skulltexture = ItemDataPartCustomSkull.readSkullTexture(item);
        if (skulltexture != null) {
            if (ClassManager.manager.getStringManager().checkStringForFeatures(shop, buy, item, skulltexture)) {
                item = ItemDataPartCustomSkull.transformSkull(item, ClassManager.manager.getStringManager().transform(skulltexture, buy, shop, holder, target));
            }
        }
    }

    private List<String> splitLore(List<String> lore, int max_line_length, boolean final_version) {
        if (max_line_length > 0 && final_version) {
            List<String> goal = new ArrayList<>();
            for (String line : lore) {

                String[] words = line.split(" ");
                String current = null;

                for (String word : words) {
                    if (current == null) {
                        current = word;
                        continue;
                    }
                    String next = current + " " + word;
                    if (ChatColor.stripColor(next).length() > max_line_length) {
                        goal.add(current);
                        String last_colors = current == null ? "" : ChatColor.getLastColors(current);
                        current = last_colors + word;
                    } else {
                        current = next;
                    }
                }
                goal.add(current);

            }

            return goal;
        } else {
            return lore;
        }
    }


    public String getFriendlyText(List<ItemStack> items) {
        if (items != null) {
            String msg = "";
            int x = 0;
            for (ItemStack i : items) {
                x++;
                msg += readItemStack(i) + (x < items.size() ? ", " : "");
            }
            return msg;
        }
        return null;
    }

    public String readItemStack(ItemStack i) {
        if (ClassManager.manager.getLanguageManager() != null) {
            return i.getAmount() + " " + ClassManager.manager.getLanguageManager().getDisplayNameItem(i);
        }
        String material = readMaterial(i);
        return i.getAmount() + " " + material;
    }

    public String readEnchantment(Enchantment e) {
        if (ClassManager.manager.getLanguageManager() != null) {
            return ClassManager.manager.getLanguageManager().getDisplayNameEnchantment(e);
        }
        return e.getName().toLowerCase().replace("_", "");
    }


    public boolean checkItemStackForFeatures(BSShop shop, BSBuy buy, ItemStack item) { //Returns true if this would make a shop customizable
        boolean b = false;
        if (item != null) {
            if (item.hasItemMeta()) {
                StringManager s = ClassManager.manager.getStringManager();
                ItemMeta meta = item.getItemMeta();

                //Normal itemdata
                if (meta.hasDisplayName()) {
                    if (s.checkStringForFeatures(shop, buy, item, meta.getDisplayName())) {
                        b = true;
                    }
                }

                if (meta.hasLore()) {
                    List<String> lore = meta.getLore();
                    for (int i = 0; i < lore.size(); i++) {
                        if (s.checkStringForFeatures(shop, buy, item, lore.get(i))) {
                            b = true;
                        }
                    }
                }

                //Skull itemdata
                if (meta instanceof SkullMeta) {
                    SkullMeta skullmeta = (SkullMeta) meta;
                    if (skullmeta.hasOwner()) {
                        if (s.checkStringForFeatures(shop, buy, item, skullmeta.getOwner())) {
                            b = true;
                        }
                    }
                }
            }
        }
        return b;
    }

    public String readItemName(ItemStack item) {
        if (item != null) {
            if (item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta.hasDisplayName()) {
                    return meta.getDisplayName();
                }
            }
            return readItemStack(item);
        }
        return null;
    }

    public String readMaterial(ItemStack item) {
        if (ClassManager.manager.getLanguageManager() != null) {
            ItemStack i = new ItemStack(item.getType());
            return ClassManager.manager.getLanguageManager().getDisplayNameItem(i);
        }
        String material = item.getType().name().toLowerCase().replace("_", " ");
        material = material.replaceFirst(material.substring(0, 1), material.substring(0, 1).toUpperCase());
        return material;
    }

    public void copyTexts(ItemStack receiver, ItemStack source) {
        if (source.hasItemMeta()) {
            ItemMeta meta_source = source.getItemMeta();
            ItemMeta meta_receiver = receiver.getItemMeta();

            if (meta_source.hasDisplayName()) {
                meta_receiver.setDisplayName(meta_source.getDisplayName());
            }
            if (meta_source.hasLore()) {
                meta_receiver.setLore(meta_source.getLore());
            }

            if (meta_source instanceof SkullMeta && meta_receiver instanceof SkullMeta) {
                SkullMeta sm_source = (SkullMeta) meta_source;
                SkullMeta sm_receiver = (SkullMeta) meta_receiver;

                if (sm_source.hasOwner()) {
                    sm_receiver.setOwner(sm_source.getOwner());
                }
            }

            receiver.setItemMeta(meta_receiver);
        }
    }


    public boolean isItemList(Object o) {
        if (o instanceof List<?>) {
            return isItemList((List<?>) o);
        }
        return false;
    }

    public boolean isItemList(List<?> list) {
        if (list != null) {
            if (list.size() >= 1) {
                Object first = list.get(0);
                if (first instanceof ItemStack) {
                    return true;
                }
            }
        }
        return false;
    }
}
