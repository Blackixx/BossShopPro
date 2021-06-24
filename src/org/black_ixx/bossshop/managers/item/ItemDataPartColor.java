package org.black_ixx.bossshop.managers.item;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.misc.InputReader;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.material.Colorable;

import java.util.List;

public class ItemDataPartColor extends ItemDataPart {

    @Override
    public ItemStack transform(ItemStack item, String used_name, String argument) {
        String[] parts = argument.split("#");
        if (parts.length != 3) {
            ClassManager.manager.getBugFinder().severe("Mistake in Config: '" + argument + "' is not a valid '" + used_name + "'. It has to look like this: '<red part>#<green part>#<blue part>'. You can find a list of RGB Colors here: http://www.farb-tabelle.de/de/farbtabelle.htm.");
            return item;
        }

        int red = InputReader.getInt(parts[0].trim(), 0);
        int green = InputReader.getInt(parts[1].trim(), 0);
        int blue = InputReader.getInt(parts[2].trim(), 0);
        Color c = Color.fromRGB(red, green, blue);

        if (item.getItemMeta() instanceof Colorable) {
            Colorable meta = (Colorable) item.getItemMeta();
            DyeColor color = DyeColor.getByColor(c);
            meta.setColor(color);
            item.setItemMeta((ItemMeta) meta);
            return item;
        }

        if (item.getItemMeta() instanceof LeatherArmorMeta) {
            LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
            meta.setColor(c);
            item.setItemMeta(meta);
            return item;
        }

        if (item.getItemMeta() instanceof PotionMeta) {
            PotionMeta meta = (PotionMeta) item.getItemMeta();
            meta.setColor(c);
            item.setItemMeta(meta);
            return item;
        }

        ClassManager.manager.getBugFinder().severe("Mistake in Config: Unable to color items of material '" + item.getType().name() + "'!");
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
        return new String[]{"color", "dye"};
    }


    @Override
    public List<String> read(ItemStack i, List<String> output) {
        if (i.getItemMeta() instanceof Colorable) {
            Colorable c = (Colorable) (i.getItemMeta());
            Color color = c.getColor().getColor();
            output.add("color:" + color.getRed() + "#" + color.getGreen() + "#" + color.getBlue());

        } else if (i.getItemMeta() instanceof LeatherArmorMeta) {
            LeatherArmorMeta m = (LeatherArmorMeta) i.getItemMeta();
            Color color = m.getColor();
            output.add("color:" + color.getRed() + "#" + color.getGreen() + "#" + color.getBlue());

        } else if (i.getItemMeta() instanceof PotionMeta) {
            PotionMeta m = (PotionMeta) i.getItemMeta();
            Color color = m.getColor();
            output.add("color:" + color.getRed() + "#" + color.getGreen() + "#" + color.getBlue());
        }
        return output;
    }

    @Override
    public boolean isSimilar(ItemStack shop_item, ItemStack player_item, BSBuy buy, Player p) {
        return true; //Color does not matter
    }


}
