package org.black_ixx.bossshop.core.conditions;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class BSConditionTypePlaceholderMatch extends BSConditionType {


    public boolean matches(Player p, String single_condition, String placeholder) {
        placeholder = ClassManager.manager.getStringManager().transform(placeholder, p);
        single_condition = ClassManager.manager.getStringManager().transform(single_condition, p);
        return ChatColor.stripColor(placeholder).trim().equalsIgnoreCase(ChatColor.stripColor(single_condition.trim()));
    }

    @Override
    public boolean dependsOnPlayer() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"placeholdermatch", "placeholder"};
    }


    @Override
    public void enableType() {
    }


    @Override
    public boolean meetsCondition(BSShopHolder holder, BSBuy shopitem, Player p, String conditiontype, String condition) {
        String parts[] = condition.split(":", 2);
        if (parts.length < 2) {
            ClassManager.manager.getBugFinder().warn("Unable to read placeholdermatch condition " + conditiontype + ":" + condition + " of shopitem " + shopitem.getName() + ". It should look like following: '<Placeholder text>:<match/dontmatch>:<text>'.");
            return false;
        }
        if (parts[0].equalsIgnoreCase("match")) {
            return isCorrect(p, true, parts[1], conditiontype);
        }
        if (parts[0].equalsIgnoreCase("dontmatch")) {
            return isCorrect(p, false, parts[1], conditiontype);
        }
        return false;
    }


    private boolean isCorrect(Player p, boolean has_to_match, String condition, String placeholder) {
        for (String single_condition : condition.split(",")) {
            if (matches(p, single_condition, placeholder) == has_to_match) {
                return true;
            }
        }
        return false;
    }


    @Override
    public String[] showStructure() {
        return new String[]{"[string]:match:[string]", "[string]:dontmatch:[string]"};
    }


}
