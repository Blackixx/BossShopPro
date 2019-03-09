package org.black_ixx.bossshop.inbuiltaddons.advancedshops;

import org.black_ixx.bossshop.BossShop;
import org.bukkit.Bukkit;

public class AdvancedShops {

    public void enable(BossShop plugin) {
        Bukkit.getPluginManager().registerEvents(new ShopItemCreationListener(), plugin);
    }

}
