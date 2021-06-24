package org.black_ixx.bossshop.core.conditions;

import org.black_ixx.bossshop.managers.misc.InputReader;
import org.black_ixx.bossshop.misc.Misc;
import org.bukkit.entity.Player;

public class BSConditionTypeHandItem extends BSConditionTypeMatch {


    @Override
    public boolean matches(Player p, String single_condition) {
        return Misc.getItemInMainHand(p).getType().equals(InputReader.readMaterial(single_condition));
    }


    @Override
    public boolean dependsOnPlayer() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"handitem", "itemhand", "mainitem", "iteminhand"};
    }


    @Override
    public void enableType() {
    }


}
