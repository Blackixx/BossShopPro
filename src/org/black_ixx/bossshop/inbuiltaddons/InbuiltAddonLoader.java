package org.black_ixx.bossshop.inbuiltaddons;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.inbuiltaddons.advancedshops.AdvancedShops;
import org.black_ixx.bossshop.inbuiltaddons.logictypes.LogicTypes;

public class InbuiltAddonLoader {

    public void load(BossShop plugin) {
        new AdvancedShops().enable(plugin);
        new LogicTypes().enable(plugin);
    }

}
