package org.black_ixx.bossshop.managers.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.codec.binary.Base64;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ItemDataPartCustomSkull extends ItemDataPart {

    public static ItemStack transformSkull(ItemStack i, String input) {
        if (input == null || input.isEmpty()) {
            return i;
        }

        ItemMeta skullMeta = i.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        Property property = input.contains("http://textures.minecraft.net/texture") ? getPropertyURL(input) : getProperty(input);
        profile.getProperties().put("textures", property);
        Field profileField = null;
        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        profileField.setAccessible(true);
        try {
            profileField.set(skullMeta, profile);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        i.setItemMeta(skullMeta);
        return i;
    }

    private static Property getProperty(String texture) {
        return new Property("textures", texture);
    }

    private static Property getPropertyURL(String url) {
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        return new Property("textures", new String(encodedData));
    }

    public static String readSkullTexture(ItemStack i) {
        if (i.getType() == Material.PLAYER_HEAD) {
            SkullMeta meta = (SkullMeta) i.getItemMeta();
            Field profileField = null;
            try {
                profileField = meta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);

                GameProfile profile = (GameProfile) profileField.get(meta);
                if (profile != null) {
                    if (profile.getProperties() != null) {
                        Collection<Property> properties = profile.getProperties().get("textures");
                        if (properties != null) {

                            Iterator<Property> iterator = properties.iterator();
                            if (iterator.hasNext()) {
                                Property property = iterator.next();
                                return property.getValue();
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public ItemStack transform(ItemStack item, String used_name, String argument) {
        if (!(item.getItemMeta() instanceof SkullMeta)) {
            ClassManager.manager.getBugFinder().warn("Mistake in Config: Itemdata of type '" + used_name + "' with value '" + argument + "' can not be added to an item with material '" + item.getType().name() + "'. Don't worry I'll automatically transform the material into '" + Material.PLAYER_HEAD + ".");
            item.setType(Material.PLAYER_HEAD);
        }
        item = transformSkull(item, argument);
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
        return new String[]{"customskull", "skull"};
    }

    @Override
    public List<String> read(ItemStack i, List<String> output) {
        String skulltexture = readSkullTexture(i);
        if (skulltexture != null) {
            output.add("customskull:" + skulltexture);
        }
        return output;
    }


    @Override
    public boolean isSimilar(ItemStack shop_item, ItemStack player_item, BSBuy buy, Player p) {
        return true; //Custom skull textures do not matter
    }


}
