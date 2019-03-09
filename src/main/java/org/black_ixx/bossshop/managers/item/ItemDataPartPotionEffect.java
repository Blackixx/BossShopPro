package org.black_ixx.bossshop.managers.item;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.misc.InputReader;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class ItemDataPartPotionEffect extends ItemDataPart {

    @Override
    public ItemStack transform(ItemStack item, String used_name, String argument) {
        String[] parts = argument.split("#", 4);
        if (parts.length < 3) {
            ClassManager.manager.getBugFinder().severe("Mistake in Config: '" + argument + "' is not a valid '" + used_name + "'. It has to look like this: '<potioneffect name/id>#<level><duration>[#<r>#<g>#<b>]'. For example 'potioneffect:CONFUSION#1#60' or 'potioneffect:CONFUSION#1#60#255#0#0'.");
            return item;
        }

        if (!(item.getItemMeta() instanceof PotionMeta)) {
            ClassManager.manager.getBugFinder().severe("Mistake in Config: You can not add Potioneffects to an item with material '" + item.getType().name() + "'! Following line is invalid: '" + used_name + ":" + argument + "'.");
            return item;
        }

        PotionMeta meta = (PotionMeta) item.getItemMeta();

        String potioneffecttype = parts[0].trim();
        int level = InputReader.getInt(parts[1].trim(), -1);
        double duration = InputReader.getDouble(parts[2].trim(), -1); //Duration in seconds. Needs to be multiplied by 20 later.

        if (level == -1) {
            ClassManager.manager.getBugFinder().severe("Mistake in Config: '" + argument + "' is not a valid '" + used_name + "'. The level of the enchantment is invalid.");
            return item;
        }
        if (duration == -1) {
            ClassManager.manager.getBugFinder().severe("Mistake in Config: '" + argument + "' is not a valid '" + used_name + "'. The duration of the enchantment is invalid.");
            return item;
        }

        PotionEffectType type = PotionEffectType.getByName(potioneffecttype);

        if (type == null) {
            ClassManager.manager.getBugFinder().severe("Mistake in Config: '" + argument + "' is not a valid '" + used_name + "'. The name/id of the potioneffect is not known.");
            return item;
        }


        if (parts.length == 4) {
            String colorparts[] = parts[3].split("#");
            if (colorparts.length == 3) {
                Color c = Color.fromRGB(InputReader.getInt(colorparts[0], 1), InputReader.getInt(colorparts[1], 1), InputReader.getInt(colorparts[2], 1));
                meta.setColor(c);
            } else {
                ClassManager.manager.getBugFinder().severe("Mistake in Config: '" + argument + "' of type '" + used_name + "': Unable to read color.");
            }
        }

        PotionEffect effect = new PotionEffect(type, (int) (duration * 20), level);
        meta.addCustomEffect(effect, true);
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
        return new String[]{"potioneffect", "potioneffectid"};
    }

    @Override
    public List<String> read(ItemStack i, List<String> output) {
        if (i.getItemMeta() instanceof PotionMeta) {
            PotionMeta meta = (PotionMeta) i.getItemMeta();
            if (meta.hasCustomEffects()) {
                for (PotionEffect effect : meta.getCustomEffects()) {
                    output.add("potioneffect:" + effect.getType().getName() + "#" + effect.getDuration() / 20 + "#" + effect.getAmplifier());
                }
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

            if (ms.hasCustomEffects()) {

                for (PotionEffect effect : ms.getCustomEffects()) {
                    if (!mp.hasCustomEffect(effect.getType())) {
                        return false;
                    }

                    for (PotionEffect playereffect : mp.getCustomEffects()) {
                        if (playereffect.getType() == effect.getType()) {

                            if (playereffect.getAmplifier() < effect.getAmplifier()) {
                                return false;
                            }
                            if (playereffect.getDuration() < effect.getDuration()) {
                                return false;
                            }

                            break;
                        }
                    }

                }


            }

        }
        return true;
    }

}
