package org.black_ixx.bossshop.core.conditions;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.misc.InputReader;
import org.bukkit.entity.Player;

public abstract class BSConditionTypeNumber extends BSConditionType {

    @Override
    public boolean meetsCondition(BSShopHolder holder, BSBuy shopitem, Player p, String conditiontype, String condition) {
        double n = getNumber(shopitem, holder, p);

        if (condition.contains("#") && condition.contains("%")) {
            String parts[] = condition.split("#", 2);
            condition = parts[0];
            int divisor = InputReader.getInt(parts[1].replace("%", ""), 1);
            n %= divisor;
        }

        if (ClassManager.manager.getStringManager().checkStringForFeatures(shopitem.getShop(), shopitem, shopitem.getItem(), condition)) {
            condition = ClassManager.manager.getStringManager().transform(condition, shopitem, shopitem.getShop(), holder, p);
        }

        //Basic operations
        if (conditiontype.equalsIgnoreCase("over") || conditiontype.equalsIgnoreCase(">")) {
            double d = InputReader.getDouble(condition, -1);
            return n > d;
        }
        if (conditiontype.equalsIgnoreCase("under") || conditiontype.equalsIgnoreCase("<") || conditiontype.equalsIgnoreCase("below")) {
            double d = InputReader.getDouble(condition, -1);
            return n < d;
        }
        if (conditiontype.equalsIgnoreCase("equals") || conditiontype.equalsIgnoreCase("=")) {
            return equals(n, condition.split(","));
        }

        if (conditiontype.equalsIgnoreCase("between") || conditiontype.equalsIgnoreCase("inbetween")) {
            String separator = condition.contains(":") ? ":" : "-";
            String[] parts = condition.split(separator);
            if (parts.length == 2) {
                double start = InputReader.getDouble(parts[0], -1);
                double end = InputReader.getDouble(parts[1], -1);
                return n >= start && n <= end;
            } else {
                ClassManager.manager.getBugFinder().warn("Unable to read condition '" + condition + "' of conditiontype 'between'. It has to look like following: '<number1>-<number2>'.");
                return false;
            }
        }


        return false;
    }

    private boolean equals(double n, String[] options) {
        for (String option : options) {
            double d = InputReader.getDouble(option, -1);
            if (d == n) {
                return true;
            }
        }
        return false;
    }


    @Override
    public String[] showStructure() {
        return new String[]{"over:[double]", "under:[double]", "equals:[double]", "between:[double]:[double]"};
    }

    public abstract double getNumber(BSBuy shopitem, BSShopHolder holder, Player p);

}
