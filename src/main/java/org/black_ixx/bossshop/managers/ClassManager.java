package org.black_ixx.bossshop.managers;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.api.BossShopAddon;
import org.black_ixx.bossshop.core.BSShops;
import org.black_ixx.bossshop.core.conditions.BSConditionType;
import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.core.rewards.BSRewardType;
import org.black_ixx.bossshop.events.BSRegisterTypesEvent;
import org.black_ixx.bossshop.managers.config.ConfigHandler;
import org.black_ixx.bossshop.managers.config.FileHandler;
import org.black_ixx.bossshop.managers.external.BungeeCordManager;
import org.black_ixx.bossshop.managers.external.LanguageManager;
import org.black_ixx.bossshop.managers.external.PlaceholderAPIHandler;
import org.black_ixx.bossshop.managers.external.VaultHandler;
import org.black_ixx.bossshop.managers.external.spawners.ISpawnEggHandler;
import org.black_ixx.bossshop.managers.external.spawners.ISpawnerHandler;
import org.black_ixx.bossshop.managers.external.spawners.SpawnersHandlerEpicSpawners;
import org.black_ixx.bossshop.managers.external.spawners.SpawnersHandlerSilkSpawners;
import org.black_ixx.bossshop.managers.features.*;
import org.black_ixx.bossshop.managers.item.ItemDataPart;
import org.black_ixx.bossshop.managers.item.ItemStackChecker;
import org.black_ixx.bossshop.managers.item.ItemStackCreator;
import org.black_ixx.bossshop.managers.item.ItemStackTranslator;
import org.black_ixx.bossshop.managers.misc.StringManager;
import org.black_ixx.bossshop.managers.serverpinging.ServerPingingManager;
import org.black_ixx.bossshop.misc.MathTools;
import org.black_ixx.bossshop.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class ClassManager {

    public static ClassManager manager;
    String USER = "%%__USER__%%";
    private ItemStackChecker itemstackChecker;
    private StringManager stringmanager;


    ///////////////////////////////
    private PointsManager pointsmanager;
    private VaultHandler vaulthandler;
    private PlaceholderAPIHandler placeholderhandler;
    private MessageHandler messagehandler;
    private ItemStackCreator itemstackCreator;
    private ItemStackTranslator itemstackTranslator;
    private BuyItemHandler buyItemHandler;
    private ConfigHandler configHandler;
    private BugFinder bugfinder;
    private BossShop plugin;
    private Settings settings;
    private BSShops shops;
    private PageLayoutHandler pagelayoutHandler;
    private BungeeCordManager bungeeCordManager;
    private ShopCustomizer customizer;
    private TransactionLog transactionLog;
    private ServerPingingManager serverPingingManager;
    private AutoRefreshHandler autoRefreshHandler;
    private MultiplierHandler multiplierHandler;
    private StorageManager storageManager;
    private ISpawnEggHandler spawnEggHandler;
    private ISpawnerHandler spawnerHandler;
    private LanguageManager languageManager;
    private ItemDataStorage itemdataStorage;
    private PlayerDataHandler playerdataHandler;
    public ClassManager(BossShop plugin) {
        this.plugin = plugin;
        manager = this;
        settings = new Settings();

        new FileHandler().exportConfigs(plugin);

        BSRewardType.loadTypes();
        BSPriceType.loadTypes();
        BSConditionType.loadTypes();
        ItemDataPart.loadTypes();

        //////////////// <- Independent Classes

        playerdataHandler = new PlayerDataHandler();
        configHandler = new ConfigHandler(plugin);
        MathTools.init(settings.getNumberLocale(), settings.getNumberGroupingSize());
        storageManager = new StorageManager(plugin);
        bugfinder = new BugFinder(plugin);
        itemdataStorage = new ItemDataStorage(plugin);
        multiplierHandler = new MultiplierHandler(plugin);
        stringmanager = new StringManager();
        itemstackCreator = new ItemStackCreator();
        itemstackTranslator = new ItemStackTranslator();
        buyItemHandler = new BuyItemHandler();
        itemstackChecker = new ItemStackChecker();
        messagehandler = new MessageHandler(plugin);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            placeholderhandler = new PlaceholderAPIHandler();
        }

        if (Bukkit.getPluginManager().isPluginEnabled("LangUtils")) {
            languageManager = new LanguageManager();
        }

        if (Bukkit.getPluginManager().isPluginEnabled("SilkSpawners")) {
            try {
                Class.forName("de.dustplanet.util.SilkUtil");
                SpawnersHandlerSilkSpawners h = new SpawnersHandlerSilkSpawners();
                spawnerHandler = h;
                spawnEggHandler = h;
            } catch (ClassNotFoundException e) {
                getBugFinder().warn("It seems like you have 'SilkSpawners' installed, but BossShopPro does not recognize the API of the plugin. " +
                        "Note: There are different SilkSpawners plugins around. The one BossShopPro can hook into is https://www.spigotmc.org/resources/7811/. " +
                        "Others are simply ignored.");
            }
        }

        if (Bukkit.getPluginManager().isPluginEnabled("EpicSpawners")) {
            spawnerHandler = new SpawnersHandlerEpicSpawners();
        }

    }

    /**
     * Setup the dependent classes
     */
    public void setupDependentClasses() {
        Bukkit.getPluginManager().callEvent(new BSRegisterTypesEvent());

        FileConfiguration config = plugin.getConfig();
        plugin.getInventoryListener().init(config.getInt("ClickDelay"), config.getInt("ClickSpamKick.ClickDelay"), config.getInt("ClickSpamKick.Warnings"), config.getInt("ClickSpamKick.ForgetTime"));

        pagelayoutHandler = new PageLayoutHandler(plugin);

        //if (settings.getPointsEnabled()){ Is not known because shops are not yet loaded. But is required before shops are loaded in order to be able to display items properly.
        pointsmanager = new PointsManager();
        //}

        shops = new BSShops(plugin, settings);

        if (settings.getVaultEnabled()) {
            Plugin VaultPlugin = Bukkit.getServer().getPluginManager().getPlugin("Vault");
            if (VaultPlugin == null) {
                ClassManager.manager.getBugFinder().warn("Vault was not found... You need it if you want to work with Permissions, Permission Groups or Money! Get it there: http://dev.bukkit.org/server-mods/vault/");
            } else {
                vaulthandler = new VaultHandler(settings.getMoneyEnabled(), settings.getPermissionsEnabled());
            }
        }

        if (settings.getBalanceVariableEnabled() || settings.getBalancePointsVariableEnabled() || settings.getProperty(Settings.HIDE_ITEMS_PLAYERS_DONT_HAVE_PERMISSIONS_FOR).containsValueAny(true)) {
            customizer = new ShopCustomizer();
        }

        if (settings.getTransactionLogEnabled()) {
            transactionLog = new TransactionLog(plugin);
        }

        if (settings.getServerPingingEnabled(false)) {
            serverPingingManager = new ServerPingingManager(plugin);
            getServerPingingManager().getServerPingingRunnableHandler().start(settings.getServerPingingSpeed(), plugin, getServerPingingManager());
            getServerPingingManager().setReadyToTransform(true);
        }

        if (settings.getBungeeCordServerEnabled()) { //Depends on ServerPinging
            bungeeCordManager = new BungeeCordManager();
            Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
            Bukkit.getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", bungeeCordManager);
        }

        if (settings.getAutoRefreshSpeed() > 0) {
            autoRefreshHandler = new AutoRefreshHandler();
            autoRefreshHandler.start(settings.getAutoRefreshSpeed(), plugin);
        }


        if (plugin.getAPI().getEnabledAddons() != null) {
            for (BossShopAddon addon : plugin.getAPI().getEnabledAddons()) {
                addon.bossShopFinishedLoading();
            }
        }
    }

    ///////////////////////////////

    public Settings getSettings() {
        return settings;
    }

    public ItemStackChecker getItemStackChecker() {
        return itemstackChecker;
    }

    public StringManager getStringManager() {
        return stringmanager;
    }

    public PointsManager getPointsManager() {
        return pointsmanager;
    }

    public VaultHandler getVaultHandler() {
        if (vaulthandler == null) {
            return new VaultHandler(ClassManager.manager.getSettings().getMoneyEnabled(), ClassManager.manager.getSettings().getPointsEnabled());
        }
        return vaulthandler;
    }

    public PlaceholderAPIHandler getPlaceholderHandler() {
        return placeholderhandler;
    }

    public MessageHandler getMessageHandler() {
        return messagehandler;
    }

    public ItemStackCreator getItemStackCreator() {
        return itemstackCreator;
    }

    public ItemStackTranslator getItemStackTranslator() {
        return itemstackTranslator;
    }

    public BuyItemHandler getBuyItemHandler() {
        return buyItemHandler;
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public BugFinder getBugFinder() {
        return bugfinder;
    }

    public BossShop getPlugin() {
        return plugin;
    }

    public BSShops getShops() {
        return shops;
    }

    public PageLayoutHandler getPageLayoutHandler() {
        return pagelayoutHandler;
    }

    public PlayerDataHandler getPlayerDataHandler() {
        return playerdataHandler;
    }

    public BungeeCordManager getBungeeCordManager() {
        if (bungeeCordManager == null) {
            bungeeCordManager = new BungeeCordManager();
        }
        return bungeeCordManager;
    }

    public ShopCustomizer getShopCustomizer() {
        if (customizer == null) {
            customizer = new ShopCustomizer();
        }
        return customizer;
    }

    public TransactionLog getTransactionLog() {
        return transactionLog;
    }

    public ServerPingingManager getServerPingingManager() {
        return serverPingingManager;
    }

    public MultiplierHandler getMultiplierHandler() {
        return multiplierHandler;
    }

    public AutoRefreshHandler getAutoRefreshHandler() {
        return autoRefreshHandler;
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public ISpawnerHandler getSpawnerHandler() {
        return spawnerHandler;
    }

    public ISpawnEggHandler getSpawnEggHandler() {
        return spawnEggHandler;
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public ItemDataStorage getItemDataStorage() {
        return itemdataStorage;
    }


}
