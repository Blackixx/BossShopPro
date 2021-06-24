package org.black_ixx.bossshop.core.rewards;


import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.misc.InputReader;
import org.black_ixx.bossshop.misc.Enchant;
import org.black_ixx.bossshop.misc.Misc;
import org.black_ixx.bossshop.settings.Settings;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;


public class BSRewardTypeEnchantment extends BSRewardType {


    public Object createObject(Object o, boolean force_final_state) {
        if (force_final_state) {
            return InputReader.readEnchant(o);
        } else {
            return InputReader.readString(o, false);
        }
    }

    public boolean validityCheck(String item_name, Object o) {
        if (o != null) {
            return true;
        }
        ClassManager.manager.getBugFinder().severe("Was not able to create ShopItem " + item_name + "! The reward object needs to be a text line looking like this: '<enchantmenttype/enchantmentid>#<level>'.");
        return false;
    }

    @Override
    public void enableType() {
    }

    @Override
    public boolean canBuy(Player p, BSBuy buy, boolean message_if_no_success, Object reward, ClickType clickType) {
        Enchant enchant = (Enchant) reward;

        ItemStack item = Misc.getItemInMainHand(p);
        if (item == null || item.getType() == Material.AIR) {
            if (message_if_no_success) {
                ClassManager.manager.getMessageHandler().sendMessage("Enchantment.EmptyHand", p);
            }
            return false;
        }

        if (!ClassManager.manager.getSettings().getPropertyBoolean(Settings.ALLOW_UNSAFE_ENCHANTMENTS, buy)) {
            if (!(enchant.getType().canEnchantItem(item))) {
                if (message_if_no_success) {
                    ClassManager.manager.getMessageHandler().sendMessage("Enchantment.Invalid", p);
                }
                return false;
            }
        }
        return true;
    }

    @Override
    public void giveReward(Player p, BSBuy buy, Object reward, ClickType clickType) {
        Enchant enchant = (Enchant) reward;
        ItemStack item = Misc.getItemInMainHand(p);
        if (item != null && item.getType() != Material.AIR) {
            item.addUnsafeEnchantment(enchant.getType(), enchant.getLevel());
        }
    }

    @Override
    public String getDisplayReward(Player p, BSBuy buy, Object reward, ClickType clickType) {
        Enchant enchant = (Enchant) reward;
        return ClassManager.manager.getMessageHandler().get("Display.Enchantment").replace("%type%", ClassManager.manager.getItemStackTranslator().readEnchantment(enchant.getType())).replace("%level%", String.valueOf(enchant.getLevel()));
    }

    @Override
    public String[] createNames() {
        return new String[]{"enchantment", "enchant"};
    }

    @Override
    public boolean mightNeedShopUpdate() {
        return false;
    }

}
