package org.black_ixx.bossshop.core.conditions;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.entity.Player;

public class BSConditionTypeMoney extends BSConditionTypeNumber {

    @Override
    public double getNumber(BSBuy shopitem, BSShopHolder holder, Player p) {
        if (ClassManager.manager.getVaultHandler() == null) {
            return 0;
        }
        if (ClassManager.manager.getVaultHandler().getEconomy() == null) {
            return 0;
        }
        if (ClassManager.manager.getVaultHandler().getEconomy().hasAccount(p.getName())) {
            return ClassManager.manager.getVaultHandler().getEconomy().getBalance(p.getName());
        }
        return 0;
    }

    @Override
    public boolean dependsOnPlayer() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"money"};
    }


    @Override
    public void enableType() {
        ClassManager.manager.getSettings().setVaultEnabled(true);
        ClassManager.manager.getSettings().setMoneyEnabled(true);
    }


}
