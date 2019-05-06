package org.black_ixx.bossshop.managers.item;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemDataPartBanner extends ItemDataPart {

    @Override
    public ItemStack transform(ItemStack item, String used_name, String argument) {
        if(item.getItemMeta() instanceof BannerMeta){
            BannerMeta meta = (BannerMeta) item.getItemMeta();
            String[] bData = argument.split("\\+");
            List<Pattern> patterns = new ArrayList<>();
            for (String patternString : bData) {
                try {
                    String[] bPattern = patternString.split("-");
                    DyeColor color = DyeColor.valueOf(bPattern[0]);
                    PatternType patterntype = PatternType.getByIdentifier(bPattern[1]);
                    Pattern pattern = new Pattern(color, patterntype);
                    patterns.add(pattern);
                } catch (Exception e) {
                }
            }
            meta.setPatterns(patterns);
            item.setItemMeta(meta);
            return item;
        }
        ClassManager.manager.getBugFinder().severe("Mistake in Config: '" + argument + "' is not a valid '" + used_name + "'.");
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
        return new String[]{"banner", "patterns"};
    }


    @Override
    public List<String> read(ItemStack i, List<String> output) {
        if(i.getItemMeta() instanceof BannerMeta){
             List<Pattern> patterns = ((BannerMeta)i.getItemMeta()).getPatterns();
            if(!patterns.isEmpty()){
                StringBuilder builder = new StringBuilder("banner:");
                for (Pattern pattern : patterns) {
                    builder.append(pattern.getColor().name() + "-" + pattern.getPattern().getIdentifier());
                    builder.append("+");
                }
                output.add(builder.delete(builder.length() -1, builder.length()).toString());
            }
        }
        return output;
    }

    @Override
    public boolean isSimilar(ItemStack shop_item, ItemStack player_item, BSBuy buy, Player p) {
        return true; //Banner color does not matter
    }

}
