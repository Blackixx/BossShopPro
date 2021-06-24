package org.black_ixx.bossshop.managers.item;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemDataPartBanner extends ItemDataPart {

    @SuppressWarnings("deprecation")
    @Override
    public ItemStack transform(ItemStack item, String used_name, String argument) {
        if (Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9")) {  //TODO: ADD Documentation and test this feature out
            if (item.getType() != Material.LEGACY_BANNER) {
                ClassManager.manager.getBugFinder().severe("Mistake in Config: '" + argument + "' is not a valid '" + used_name + "'.");
                return item;
            }
            BannerMeta meta = (BannerMeta) item.getItemMeta();
            String[] bdata = argument.split("\\+");
            DyeColor basecolor = DyeColor.valueOf(bdata[0]);
            if (basecolor != null) {
                List<Pattern> patterns = new ArrayList<>();
                for (int y = 1; y < bdata.length; y++) {
                    try {
                        String[] bpattern = bdata[y].split("-");
                        DyeColor patterncolor = DyeColor.valueOf(bpattern[0]);
                        PatternType patterntype = PatternType.getByIdentifier(bpattern[1]);
                        Pattern pattern = new Pattern(patterncolor, patterntype);
                        patterns.add(pattern);
                    } catch (Exception e) {
                    }
                }
                meta.setBaseColor(basecolor);
                meta.setPatterns(patterns);
            }
            item.setItemMeta(meta);
            return item;
        }
        return item;
    }

    @Override
    public int getPriority() {
        return PRIORITY_NORMAL;
    }

    @Override
    public boolean removeSpaces() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"banner"};
    }


    @Override
    public List<String> read(ItemStack i, List<String> output) {
        //TODO
        return output;
    }

    @Override
    public boolean isSimilar(ItemStack shop_item, ItemStack player_item, BSBuy buy, Player p) {
        return true; //Banner color does not matter
    }

}
