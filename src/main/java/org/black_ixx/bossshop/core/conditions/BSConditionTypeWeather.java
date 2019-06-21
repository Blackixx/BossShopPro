package org.black_ixx.bossshop.core.conditions;

import org.bukkit.entity.Player;

public class BSConditionTypeWeather extends BSConditionTypeMatch {


    @Override
    public boolean matches(Player p, String single_condition) {
        if (single_condition.equalsIgnoreCase("storm")) {
            return p.getWorld().hasStorm();
        } else if (single_condition.equalsIgnoreCase("clear")) {
            return !p.getWorld().hasStorm();
        }
        return false;
    }


    @Override
    public boolean dependsOnPlayer() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"weather"};
    }


    @Override
    public void enableType() {
    }


}
