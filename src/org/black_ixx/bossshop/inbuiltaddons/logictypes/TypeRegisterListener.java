package org.black_ixx.bossshop.inbuiltaddons.logictypes;

import org.black_ixx.bossshop.events.BSRegisterTypesEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TypeRegisterListener implements Listener {


    @EventHandler
    public void onCreate(BSRegisterTypesEvent event) {
        new BSPriceTypeAnd().register();
        new BSPriceTypeOr().register();
        new BSRewardTypeAnd().register();
    }

}
