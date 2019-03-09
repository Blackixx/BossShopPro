package org.black_ixx.bossshop.managers.features;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSMultiplier;
import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.core.rewards.BSRewardType;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.misc.MathTools;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MultiplierHandler {

    private Set<BSMultiplier> multipliers = new HashSet<BSMultiplier>();

    public MultiplierHandler(BossShop plugin) {
        if (plugin.getConfig().getBoolean("MultiplierGroups.Enabled") == false) {
            return;
        }
        List<String> lines = plugin.getConfig().getStringList("MultiplierGroups.List");
        if (lines == null) {
            return;
        }
        setup(plugin, lines);
    }

    public void setup(BossShop plugin, List<String> config_lines) {
        multipliers.clear();
        for (String s : config_lines) {
            BSMultiplier m = new BSMultiplier(s);
            if (m.isValid()) {
                multipliers.add(m);
            }
        }
    }


    public String calculatePriceDisplayWithMultiplier(Player p, BSBuy buy, ClickType clicktype, double d, String message) {
        BSPriceType t = buy.getPriceType(clicktype);
        return calculatePriceDisplayWithMultiplier(p, buy, clicktype, d, message, MathTools.getFormatting(t), MathTools.isIntegerValue(t));
    }

    public String calculatePriceDisplayWithMultiplier(Player p, BSBuy buy, ClickType clicktype, double d, String message, List<String> formatting, boolean integer_value) {
        d = calculatePriceWithMultiplier(p, buy, clicktype, d);

        if (buy.getRewardType(clicktype) == BSRewardType.ItemAll) {
            if (ClassManager.manager.getSettings().getItemAllShowFinalReward() && p != null) {
                ItemStack i = (ItemStack) buy.getReward(clicktype);
                int count = ClassManager.manager.getItemStackChecker().getAmountOfFreeSpace(p, i);

                if (count == 0) {
                    return ClassManager.manager.getMessageHandler().get("Display.ItemAllEach").replace("%value%", message.replace("%number%", MathTools.displayNumber(d, formatting, integer_value)));
                }

                d *= count;
            } else {
                return ClassManager.manager.getMessageHandler().get("Display.ItemAllEach").replace("%value%", message.replace("%number%", MathTools.displayNumber(d, formatting, integer_value)));
            }
        }

        return message.replace("%number%", MathTools.displayNumber(d, formatting, integer_value));
    }

    public double calculatePriceWithMultiplier(Player p, BSBuy buy, ClickType clicktype, double d) {
        return calculatePriceWithMultiplier(p, buy.getPriceType(clicktype), d);
    }

    public double calculatePriceWithMultiplier(Player p, BSPriceType pricetype, double d) { //Used for prices
        for (BSMultiplier m : multipliers) {
            d = m.calculateValue(p, pricetype, d, BSMultiplier.RANGE_PRICE_ONLY);
        }
        return MathTools.round(d, 2);
    }


    public String calculateRewardDisplayWithMultiplier(Player p, BSBuy buy, ClickType clicktype, double d, String message) {
        BSPriceType t = BSPriceType.detectType(buy.getRewardType(clicktype).name());
        return calculateRewardDisplayWithMultiplier(p, buy, clicktype, d, message, MathTools.getFormatting(t), MathTools.isIntegerValue(t));
    }

    public String calculateRewardDisplayWithMultiplier(Player p, BSBuy buy, ClickType clicktype, double d, String message, List<String> formatting, boolean integer_value) {
        d = calculateRewardWithMultiplier(p, buy, clicktype, d);

        if (buy.getPriceType(clicktype) == BSPriceType.ItemAll) {
            if (ClassManager.manager.getSettings().getItemAllShowFinalReward() && p != null) {
                ItemStack i = (ItemStack) buy.getPrice(clicktype);
                int count = ClassManager.manager.getItemStackChecker().getAmountOfSameItems(p, i, buy);

                if (count == 0) {
                    return ClassManager.manager.getMessageHandler().get("Display.ItemAllEach").replace("%value%", message.replace("%number%", MathTools.displayNumber(d, formatting, integer_value)));
                }

                d *= count;
            } else {
                return ClassManager.manager.getMessageHandler().get("Display.ItemAllEach").replace("%value%", message.replace("%number%", MathTools.displayNumber(d, formatting, integer_value)));
            }
        }

        return message.replace("%number%", MathTools.displayNumber(d, formatting, integer_value));
    }

    public double calculateRewardWithMultiplier(Player p, BSBuy buy, ClickType clicktype, double d) { //Used for reward; Works the other way around
        return calculateRewardWithMultiplier(p, buy.getRewardType(clicktype), d);
    }

    public double calculateRewardWithMultiplier(Player p, BSRewardType rewardtype, double d) { //Used for reward; Works the other way around
        for (BSMultiplier m : multipliers) {
            d = m.calculateValue(p, BSPriceType.detectType(rewardtype.name()), d, BSMultiplier.RANGE_REWARD_ONLY);
        }
        return MathTools.round(d, 2);
    }


    public Set<BSMultiplier> getMultipliers() {
        return multipliers;
    }

    public boolean hasMultipliers() {
        if (multipliers == null) {
            return false;
        }
        return !multipliers.isEmpty();
    }


}
