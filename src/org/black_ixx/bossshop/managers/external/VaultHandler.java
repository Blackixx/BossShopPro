package org.black_ixx.bossshop.managers.external;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.misc.NoEconomy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHandler {

    private Permission perms;
    private Economy economy;

    public VaultHandler(boolean eco, boolean per) {
        BossShop.log("Vault found.");

        if (eco) {
            setupEconomy();
        }
        if (per) {
            setupPermissions();
        }

    }

    ///////////////////////////////////////

    private void setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider == null) {
            ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
            economy = new NoEconomy();
            return;
        }
        economy = economyProvider.getProvider();
    }

    private void setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        if (perms == null) {
            ClassManager.manager.getBugFinder().warn("No Permissions Plugin was found... You need one if you want to work with Permissions or Permission Groups! Get it there: http://plugins.bukkit.org/");
            return;
        }

    }

    ///////////////////////////////////////

    public Permission getPermission() {
        return perms;
    }

    public Economy getEconomy() {
        return economy;
    }

    ///////////////////////////////////////


}
