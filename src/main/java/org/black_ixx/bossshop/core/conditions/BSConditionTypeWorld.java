package org.black_ixx.bossshop.core.conditions;


import org.bukkit.entity.Player;

public class BSConditionTypeWorld extends BSConditionTypeMatch {

    @Override
    public boolean matches(Player p, String single_condition) {
        return p.getWorld().getName().equalsIgnoreCase(single_condition);
    }

    @Override
    public boolean dependsOnPlayer() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"world", "worldname"};
    }


    @Override
    public void enableType() {
    }


}
