package org.black_ixx.bossshop;


import org.black_ixx.bossshop.api.BossShopAPI;
import org.black_ixx.bossshop.api.BossShopAddon;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.events.BSReloadedEvent;
import org.black_ixx.bossshop.inbuiltaddons.InbuiltAddonLoader;
import org.black_ixx.bossshop.listeners.InventoryListener;
import org.black_ixx.bossshop.listeners.PlayerListener;
import org.black_ixx.bossshop.listeners.SignListener;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BossShop extends JavaPlugin {

    public final static String NAME = "BossShopPro";
    String USER = "%%__USER__%%";
    private ClassManager manager;
    private InventoryListener il;

    /////////////////////////////////////////////////
    private SignListener sl;
    private PlayerListener pl;
    private BossShopAPI api;

    public static void log(String s) {
        Bukkit.getLogger().info("[" + NAME + "] " + s);
    }

    public static void debug(String s) {
        if (ClassManager.manager.getSettings().isDebugEnabled()) {
            log(s);
        }
    }

    /////////////////////////////////////////////////

    @Override
    public void onEnable() {
        log("Loading data...");
        manager = new ClassManager(this);
        api = new BossShopAPI(this);

        CommandManager commander = new CommandManager();

        if (getCommand("bs") != null) {
            getCommand("bs").setExecutor(commander);
        }
        if (getCommand("bossshop") != null) {
            getCommand("bossshop").setExecutor(commander);
        }
        if (getCommand("shop") != null) {
            getCommand("shop").setExecutor(commander);
        }


        ////////////////<- Listeners

        il = new InventoryListener(this);
        getServer().getPluginManager().registerEvents(il, this);

        sl = new SignListener(manager.getSettings().getSignsEnabled(), this);
        getServer().getPluginManager().registerEvents(sl, this);

        pl = new PlayerListener(this);
        getServer().getPluginManager().registerEvents(pl, this);


        new BukkitRunnable() {
            @Override
            public void run() {
                new InbuiltAddonLoader().load(BossShop.this);
                getClassManager().setupDependentClasses();
            }
        }.runTaskLaterAsynchronously(this, 5);
    }

    @Override
    public void onDisable() {
        closeShops();
        unloadClasses();
        log("Disabling... bye!");
    }

    public ClassManager getClassManager() {
        return manager;
    }

    public SignListener getSignListener() {
        return sl;
    }

    public InventoryListener getInventoryListener() {
        return il;
    }

    /////////////////////////////////////////////////

    public PlayerListener getPlayerListener() {
        return pl;
    }

    public BossShopAPI getAPI() {
        return api;
    }

    public void reloadPlugin(CommandSender sender) {
        closeShops();

        reloadConfig();
        manager.getMessageHandler().reloadConfig();

        if (manager.getShops() != null) {
            for (String s : manager.getShops().getShopIds().keySet()) {
                BSShop shop = manager.getShops().getShops().get(s);
                if (shop != null) {
                    shop.reloadShop();
                }
            }
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (api.isValidShop(p.getOpenInventory())) {
                p.closeInventory();
            }
        }

        sl.setSignsEnabled(false); // Wird durch ConfigHandler umgesetzt (ClassManager laedt ConfigHandler)

        unloadClasses();

        manager = new ClassManager(this);

        if (api.getEnabledAddons() != null) {
            for (BossShopAddon addon : api.getEnabledAddons()) {
                addon.reload(sender);
            }
        }

        manager.setupDependentClasses();


        BSReloadedEvent event = new BSReloadedEvent(this);
        Bukkit.getPluginManager().callEvent(event);
    }

    private void unloadClasses() {
        Bukkit.getScheduler().cancelTasks(this);

        if (manager == null) {
            return;
        }

        if (manager.getSettings() == null) {
            return;
        }

        if (manager.getStorageManager() != null) {
            manager.getStorageManager().saveConfig();
        }
        if (manager.getItemDataStorage() != null) {
            manager.getItemDataStorage().saveConfig();
        }

        if (manager.getSettings().getTransactionLogEnabled()) {
            manager.getTransactionLog().saveConfig();
        }

        if (manager.getSettings().getServerPingingEnabled(true)) {
            manager.getServerPingingManager().getServerPingingRunnableHandler().stop();
            manager.getServerPingingManager().clear();
        }

        if (manager.getAutoRefreshHandler() != null) {
            manager.getAutoRefreshHandler().stop();
        }
    }

    private void closeShops() {
        if (manager == null || manager.getShops() == null || manager.getShops().getShops() == null) {
            return;
        }
        for (int i : manager.getShops().getShops().keySet()) {
            BSShop shop = manager.getShops().getShops().get(i);
            if (shop != null) {
                shop.close();
            }
        }
    }


}
