package org.black_ixx.bossshop.managers.item;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;


public class ItemDataPartWrittenBookInformation extends ItemDataPart {

    @Override
    public ItemStack transform(ItemStack item, String used_name, String argument) {
        if (!(item.getItemMeta() instanceof BookMeta)) {
            ClassManager.manager.getBugFinder().severe("Mistake in Config: You can not add book information to an item with material '" + item.getType().name() + "'! Following line is invalid: '" + used_name + ":" + argument + "'.");
            return item;
        }

        String parts[] = argument.split("#", 2);
        if (parts.length != 2) {
            ClassManager.manager.getBugFinder().severe("Mistake in Config: Following line is invalid: '" + used_name + ":" + argument + "'. It should look like this: 'book:<title>#<author>'.");
            return item;
        }

        BookMeta meta = (BookMeta) item.getItemMeta();
        meta.setTitle(ClassManager.manager.getStringManager().transform(parts[0]));
        meta.setAuthor(ClassManager.manager.getStringManager().transform(parts[1]));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public int getPriority() {
        return PRIORITY_NORMAL;
    }

    @Override
    public boolean removeSpaces() {
        return false;
    }

    @Override
    public String[] createNames() {
        return new String[]{"book"};
    }


    @Override
    public List<String> read(ItemStack i, List<String> output) {
        if ((i.getItemMeta() instanceof BookMeta)) {
            BookMeta meta = (BookMeta) i.getItemMeta();
            if (meta.hasAuthor() || meta.hasTitle()) {
                output.add("book:" + meta.getTitle().replaceAll(String.valueOf(ChatColor.COLOR_CHAR), "&") + "#" + meta.getAuthor().replaceAll(String.valueOf(ChatColor.COLOR_CHAR), "&"));
            }
        }
        return output;
    }


    @Override
    public boolean isSimilar(ItemStack shop_item, ItemStack player_item, BSBuy buy, Player p) {
        if (shop_item.getItemMeta() instanceof BookMeta) {
            if (!(player_item.getItemMeta() instanceof BookMeta)) {
                return false;
            }

            BookMeta ms = (BookMeta) shop_item.getItemMeta();
            BookMeta mp = (BookMeta) player_item.getItemMeta();

            if (ms.hasAuthor()) {
                if (!mp.hasAuthor()) {
                    return false;
                }

                if (!mp.getAuthor().equals(ms.getAuthor())) {
                    return false;
                }

            }

            if (ms.hasTitle()) {
                if (!mp.hasTitle()) {
                    return false;
                }

                if (!mp.getTitle().equals(ms.getTitle())) {
                    return false;
                }

            }

        }
        return true;
    }


}
