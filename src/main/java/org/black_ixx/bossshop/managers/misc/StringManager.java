package org.black_ixx.bossshop.managers.misc;


import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.core.rewards.BSRewardType;
import org.black_ixx.bossshop.events.BSCheckStringForFeaturesEvent;
import org.black_ixx.bossshop.events.BSTransformStringEvent;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.serverpinging.ConnectedBuyItem;
import org.black_ixx.bossshop.misc.MathTools;
import org.black_ixx.bossshop.misc.Misc;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringManager {

    private static final Pattern hexPattern = Pattern.compile("(#[a-fA-F0-9]{6})");

    /**
     * Transform specific strings from one thing to another
     * @param s input string
     * @return transformed string
     */
    public String transform(String s) {
        if (s == null) {
            return null;
        }
        Matcher matcher = hexPattern.matcher(s);
        while (matcher.find()) {
            String color = s.substring(matcher.start(), matcher.end());
            s = s.replace(color, "" + net.md_5.bungee.api.ChatColor.of(color));
        }

        s = s.replace("[<3]", "❤");
        s = s.replace("[*]", "★");
        s = s.replace("[**]", "✹");
        s = s.replace("[o]", "●");
        s = s.replace("[v]", "✔");
        s = s.replace("[+]", "♦");
        s = s.replace("[x]", "✦");
        s = s.replace("[%]", "♠");
        s = s.replace("[%%]", "♣");
        s = s.replace("[radioactive]", "☢");
        s = s.replace("[peace]", "☮");
        s = s.replace("[moon]", "☾");
        s = s.replace("[crown]", "♔");
        s = s.replace("[snowman]", "☃");
        s = s.replace("[tools]", "⚒");
        s = s.replace("[swords]", "⚔");
        s = s.replace("[note]", "♩ ");
        s = s.replace("[block]", "█");
        s = s.replace("[triangle]", "▲");
        s = s.replace("[warn]", "⚠");
        s = s.replace("[left]", "←");
        s = s.replace("[right]", "→");
        s = s.replace("[up]", "↑");
        s = s.replace("[down]", "↓");

        s = ChatColor.translateAlternateColorCodes('&', s);

        if (ClassManager.manager.getSettings().getServerPingingEnabled(true)) {
            s = ClassManager.manager.getServerPingingManager().transform(s);
        }

        s = MathTools.transform(s);

        s = s.replace("[and]", "&");
        s = s.replace("[colon]", ":");
        s = s.replace("[hashtag]", "#");
        return s;
    }


    public String transform(String s, BSBuy item, BSShop shop, BSShopHolder holder, Player target) {
        if (s == null) {
            return null;
        }

        if (s.contains("%")) {
            if (item != null) {
                s = item.transformMessage(s, shop, target);
            }
            if (shop != null) {
                if (shop.getShopName() != null) {
                    s = s.replace("%shop%", shop.getShopName());
                }
                if (shop.getDisplayName() != null) {
                    s = s.replace("%shopdisplayname%", shop.getDisplayName());
                }
            }
            if (holder != null) {
                s = s.replace("%page%", String.valueOf(holder.getDisplayPage()));
                s = s.replace("%maxpage%", String.valueOf(holder.getDisplayHighestPage()));
            }

            BSTransformStringEvent event = new BSTransformStringEvent(s, item, shop, holder, target);
            Bukkit.getPluginManager().callEvent(event);
            s = event.getText();
        }

        return transform(s, target);
    }

    public String transform(String s, Player target) {
        if (s == null) {
            return null;
        }

        if (target != null && s.contains("%")) {
            s = s.replace("%name%", target.getName()).replace("%player%", target.getName()).replace("%target%", target.getName());
            s = s.replace("%displayname%", target.getDisplayName());
            s = s.replace("%uuid%", target.getUniqueId().toString());

            if (s.contains("%balance%") && ClassManager.manager.getVaultHandler() != null) {
                if (ClassManager.manager.getVaultHandler().getEconomy() != null) {
                    double balance = ClassManager.manager.getVaultHandler().getEconomy().getBalance(target.getName());
                    s = s.replace("%balance%", MathTools.displayNumber(balance, BSPriceType.Money));
                }
            }
            if (s.contains("%balancepoints%") && ClassManager.manager.getPointsManager() != null) {
                double balance_points = ClassManager.manager.getPointsManager().getPoints(target);
                s = s.replace("%balancepoints%", MathTools.displayNumber(balance_points, BSPriceType.Points));
            }

            if (s.contains("%world%")) {
                s = s.replace("%world%", target.getWorld().getName());
            }

            if (s.contains("%item_in_hand%")) {
                s = s.replace("%item_in_hand%", Misc.getItemInMainHand(target).getType().name());
            }

            if (s.contains("%input%")) {
                s = s.replace("%input%", ClassManager.manager.getPlayerDataHandler().getInput(target));
            }

            if (ClassManager.manager.getPlaceholderHandler() != null) {
                s = ClassManager.manager.getPlaceholderHandler().transformString(s, target);
            }
        }

        return transform(s);
    }


    public boolean checkStringForFeatures(BSShop shop, BSBuy buy, ItemStack menu_item, String s) { //Returns true if this would make a shop customizable
        boolean b = false;


        if (s.matches(hexPattern.pattern())) {
            b = true;
        }

        if (s.contains("%")) {

            if (s.contains("%balance%")) {
                ClassManager.manager.getSettings().setBalanceVariableEnabled(true);
                ClassManager.manager.getSettings().setVaultEnabled(true);
                ClassManager.manager.getSettings().setMoneyEnabled(true);
                b = true;
            }

            if (s.contains("%balancepoints%")) {
                ClassManager.manager.getSettings().setBalancePointsVariableEnabled(true);
                ClassManager.manager.getSettings().setPointsEnabled(true);
                b = true;
            }

            if (s.contains("%name%") || s.contains("%player%") || s.contains("%uuid%")) {
                b = true;
            }

            if (s.contains("%page%") || s.contains("%maxpage%")) {
                b = true;
            }

            if (s.contains("%world%")) {
                b = true;
            }

            if (buy != null && shop != null && ClassManager.manager.getSettings().getServerPingingEnabled(true)) {
                String server_names = StringManipulationLib.figureOutVariable(s, 0, "players", "motd");
                if (server_names != null) {
                    b = true;
                    if (buy.getItem() != null) {
                        String[] servers = server_names.split(":");
                        ClassManager.manager.getServerPingingManager().registerShopItem(servers[0].trim(), new ConnectedBuyItem(buy, menu_item));
                    }
                }
            }

            if (s.contains("%reward%") || s.contains("%price%")) {
                if (ClassManager.manager.getMultiplierHandler().hasMultipliers()) {
                    b = true;
                }
                if (buy.getPriceType(null) == BSPriceType.ItemAll || buy.getRewardType(null) == BSRewardType.ItemAll) {
                    b = true;
                }
            }

            if (s.contains("%input%")) {
                b = true;
            }

            if (ClassManager.manager.getPlaceholderHandler() != null) {
                if (ClassManager.manager.getPlaceholderHandler().containsPlaceholder(s)) {
                    b = true;
                }
            }

            BSCheckStringForFeaturesEvent event = new BSCheckStringForFeaturesEvent(s, buy, shop);
            Bukkit.getPluginManager().callEvent(event);
            if (event.containsFeature()) {
                b = true;
            }
        }

        if (s.contains("{") && s.contains("}")) {
            b = true;
        }
        return b;
    }


}
