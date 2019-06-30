package org.black_ixx.bossshop.managers.item;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class ItemDataPart {

    public static int
            PRIORITY_MOST_EARLY = 0,
            PRIORITY_EARLY = 10,
            PRIORITY_NORMAL = 50,
            PRIORITY_LATE = 80,
            PRIORITY_VERY_LATE = 100;

    public static ItemDataPart
            MATERIAL,
            AMOUNT,
            DURABILITY,
            NAME,
            LORE,
            ENCHANTMENT,
            PLAYERHEAD,
            CUSTOMSKULL,
            CUSTOMMODELDATA,
            MOBSPAWNER,
            MOBEGG,
            ITEMFLAGS,
            COLOR,
            POTIONEFFECT,
            POTION,
            UNBREAKING,
            BOOK,
            BOOKPAGE,
            BANNER; //UNSUPPORTED BY READITEM

    private static List<ItemDataPart> types;
    private String[] names = createNames();

    public static void loadTypes() {
        types = new ArrayList<>();

        MATERIAL = registerType(new ItemDataPartMaterial());
        AMOUNT = registerType(new ItemDataPartAmount());
        DURABILITY = registerType(new ItemDataPartDurability());
        NAME = registerType(new ItemDataPartName());
        LORE = registerType(new ItemDataPartLore());
        ENCHANTMENT = registerType(new ItemDataPartEnchantment());
        PLAYERHEAD = registerType(new ItemDataPartPlayerhead());
        CUSTOMMODELDATA = registerType(new ItemDataPartCustomModelData());
        MOBSPAWNER = registerType(new ItemDataPartMobspawner());
        MOBEGG = registerType(new ItemDataPartMobEgg());
        CUSTOMSKULL = registerType(new ItemDataPartCustomSkull());
        ITEMFLAGS = registerType(new ItemDataPartItemflags());
        COLOR = registerType(new ItemDataPartColor());
        POTIONEFFECT = registerType(new ItemDataPartPotionEffect());
        POTION = registerType(new ItemDataPartPotion());
        UNBREAKING = registerType(new ItemDataPartUnbreaking());
        BANNER = registerType(new ItemDataPartBanner());
        BOOK = registerType(new ItemDataPartWrittenBookInformation());
        BOOKPAGE = registerType(new ItemDataPartWrittenBookPage());
    }

    public static ItemDataPart registerType(ItemDataPart type) {
        types.add(type);
        return type;
    }

    public static ItemDataPart detectTypeSpecial(String whole_line) {
        if (whole_line == null) {
            return null;
        }
        String[] parts = whole_line.split(":", 2);
        String name = parts[0].trim();
        return detectType(name);
    }

    public static ItemDataPart detectType(String s) {
        for (ItemDataPart type : types) {
            if (type.isType(s)) {
                return type;
            }
        }
        return null;
    }

    public static ItemStack transformItem(ItemStack item, List<String> itemdata) {
        Collections.sort(itemdata, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {//TODO: test sorting out
                ItemDataPart type1 = detectTypeSpecial(s1);
                ItemDataPart type2 = detectTypeSpecial(s2);
                if (type1 != null && type2 != null) {
                    return Integer.compare(type1.getPriority(), type2.getPriority());
                }
                return 0;
            }
        });
        for (String line : itemdata) {
            item = transformItem(item, line);
        }
        return item;
    }

    public static ItemStack transformItem(ItemStack item, String line) {
        if (line == null) {
            return item;
        }
        String[] parts = line.split(":", 2);
        String name = parts[0].trim();
        String argument = null;
        if (parts.length == 2) {
            argument = parts[1].trim();
        }

        ItemDataPart part = detectType(name);
        if (part == null) {
            ClassManager.manager.getBugFinder().severe("Mistake in Config: Unable to read itemdata '" + name + ":" + argument);
            return item;
        }

        return part.transformItem(item, name, argument);
    }

    public static List<String> readItem(ItemStack item) {
        if (item == null) {
            return null;
        }
        List<String> output = new ArrayList<>();
        for (ItemDataPart part : types) {
            try {
                output = part.read(item, output);
            } catch (Exception e) { //Seems like that ItemDataPart is not supported yet
            } catch (NoSuchMethodError e) { //Seems like that ItemDataPart is not supported yet
            }
        }
        return output;
    }

    public static boolean isSimilar(ItemStack shop_item, ItemStack player_item, ItemDataPart[] exceptions, BSBuy buy, boolean compare_amount, Player p) {
        if (shop_item == null || player_item == null) {
            return false;
        }
        for (ItemDataPart part : types) {
            if (isException(exceptions, part)) {
                continue;
            }
            if (!compare_amount && part == AMOUNT) {
                continue;
            }
            try {
                if (!part.isSimilar(shop_item, player_item, buy, p)) {
                    return false;
                }
            } catch (Exception e) { //Seems like that ItemDataPart is not supported yet
            } catch (NoSuchMethodError e) { //Seems like that ItemDataPart is not supported yet
            }
        }
        return true;
    }

    private static boolean isException(ItemDataPart[] exceptions, ItemDataPart part) {
        if (exceptions != null) {
            for (ItemDataPart exception : exceptions) {
                if (exception == part) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<ItemDataPart> values() {
        return types;
    }

    public boolean isType(String s) {
        if (names != null) {
            for (String name : names) {
                if (name.equalsIgnoreCase(s)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void register() {
        ItemDataPart.registerType(this);
    }

    public String name() {
        return names[0].toUpperCase();
    }


    public ItemStack transformItem(ItemStack item, String used_name, String argument) { //Return true in case of success
        if (argument == null && needsArgument()) {
            return item;
        }

        if (removeSpaces() && argument != null) {
            argument = argument.replaceAll(" ", "");
        }

        try {
            return transform(item, used_name.toLowerCase(), argument);
        } catch (NoClassDefFoundError e) { //Seems like that ItemDataPart is not supported yet
            ClassManager.manager.getBugFinder().severe("Unable to work with itemdata '" + used_name.toLowerCase() + ":" + argument + ". Seems like it is not supported by your server version yet.");
            return item;
        } catch (NoSuchMethodError e) { //Seems like that ItemDataPart is not supported yet
            ClassManager.manager.getBugFinder().severe("Unable to work with itemdata '" + used_name.toLowerCase() + ":" + argument + ". Seems like it is not supported by your server version yet.");
            return item;
        }
    }


    @Deprecated
    public abstract ItemStack transform(ItemStack item, String used_name, String argument); //Return true in case of success

    public abstract boolean isSimilar(ItemStack shop_item, ItemStack player_item, BSBuy buy, Player p);

    public abstract List<String> read(ItemStack i, List<String> output);

    public abstract int getPriority(); //Parts with a lower priority (like material) are triggered before parts with higher priority are

    public abstract boolean removeSpaces();

    public abstract String[] createNames();

    public boolean needsArgument() {
        return true; //Can be overriden
    }


}
