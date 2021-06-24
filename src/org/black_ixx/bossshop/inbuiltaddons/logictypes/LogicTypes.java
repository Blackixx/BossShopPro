package org.black_ixx.bossshop.inbuiltaddons.logictypes;

import org.black_ixx.bossshop.BossShop;
import org.bukkit.Bukkit;

public class LogicTypes {


    public void enable(BossShop plugin) {
        Bukkit.getPluginManager().registerEvents(new TypeRegisterListener(), plugin);
    }

}
