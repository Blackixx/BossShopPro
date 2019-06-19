package org.black_ixx.bossshop.core.conditions;

import org.bukkit.entity.Player;

public class BSConditionTypeWeather extends BSConditionTypeMatch {


    @Override
    public boolean matches(Player p, String single_condition) {
        return p.getWorld().hasStorm();
    }


    @Override
    public boolean dependsOnPlayer() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"storming", "raining"};
    }


    @Override
    public void enableType() {
    }


}
