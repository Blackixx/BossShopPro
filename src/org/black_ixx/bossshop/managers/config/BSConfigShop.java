package org.black_ixx.bossshop.managers.config;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.BSShops;
import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.core.rewards.BSRewardType;
import org.black_ixx.bossshop.events.BSLoadShopItemsEvent;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class BSConfigShop extends BSShop {

    private String ymlName;
    private File f;
    private FileConfiguration config;
    private ConfigurationSection section;

    //////////////////////////////////

    public BSConfigShop(int shop_id, String ymlName, BSShops shophandler) {
        this(shop_id, new File(ClassManager.manager.getPlugin().getDataFolder().getAbsolutePath() + "/shops/" + ymlName), shophandler);
    }

    public BSConfigShop(int shop_id, File f, BSShops shophandler) {
        this(shop_id, f, shophandler, null);
    }

    public BSConfigShop(int shop_id, File f, BSShops shophandler, ConfigurationSection sectionOptional) {
        super(shop_id);

        this.f = f;

        try {
            config = ConfigLoader.loadConfiguration(f, true);

        } catch (InvalidConfigurationException e) {
            ClassManager.manager.getBugFinder().severe("Invalid Configuration! File: /shops/" + ymlName + " Cause: " + e.getMessage());
            String name = ymlName.replace(".yml", "");
            setSignText("[" + name + "]");
            setNeedPermToCreateSign(true);
            setShopName(name);

            ItemStack i = new ItemStack(Material.WHITE_WOOL, 1);
            ItemMeta m = i.getItemMeta();
            m.setDisplayName(ChatColor.RED + "Your Config File contains mistakes! (" + ymlName + ")");
            List<String> lore = new ArrayList<String>();
            lore.add(ChatColor.YELLOW + "For more information check /plugins/" + BossShop.NAME + "/BugFinder.yml out!");
            m.setLore(lore);
            i.setItemMeta(m);
            addShopItem(new BSBuy(BSRewardType.Command, BSPriceType.Nothing, "tell %player% the config file (" + ymlName + ") contains mistakes...", null, "", 0, "", name), i, ClassManager.manager);
            finishedAddingItems();
            return;
        }

        setup(shophandler, sectionOptional == null ? config : sectionOptional);

    }

    public void setup(BSShops shophandler, ConfigurationSection section) {
        this.section = section;

        //Add defaults if not existing already
        addDefaults();

        setShopName(section.getString("ShopName"));
        setDisplayName(section.getString("DisplayName"));
        setSignText(section.getString("signs.text"));
        setNeedPermToCreateSign(section.getBoolean("signs.NeedPermissionToCreateSign"));
        setManualInventoryRows(section.getInt("InventoryRows", -1));

        String commands = section.getString("Command");
        if (commands != null) {
            setCommands(commands.split(":"));
        }

        ClassManager.manager.getSettings().update(this);

        //Load Items
        loadItems();
        BSLoadShopItemsEvent event = new BSLoadShopItemsEvent(shophandler, this);
        Bukkit.getPluginManager().callEvent(event);
        finishedAddingItems();
    }

    //////////////////////////////////

    public FileConfiguration getConfig() {
        return config;
    }

    public ConfigurationSection getConfigurationSection() {
        return section;
    }

    //////////////////////////////////

    public void saveConfig() {
        try {
            config.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void reloadConfig() {
        f = new File(ClassManager.manager.getPlugin().getDataFolder().getAbsolutePath() + "/shops/" + ymlName);
        config = YamlConfiguration.loadConfiguration(f);
        InputStream defConfigStream = ClassManager.manager.getPlugin().getResource(f.getName());
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
            config.setDefaults(defConfig);
        }
    }

    //////////////////////////////////

    public void addDefault(String name, String rewardType, String priceType, Object reward, Object price, List<String> menuitem, String message, int loc, String permission) {
        ConfigurationSection c = section.getConfigurationSection("shop").createSection(name);
        c.set("RewardType", rewardType);
        c.set("PriceType", priceType);
        c.set("Price", price);
        c.set("Reward", reward);
        c.set("MenuItem", menuitem);
        c.set("Message", message);
        c.set("InventoryLocation", loc);
        c.set("ExtraPermission", permission);
    }

    public void addDefaults() {
        section.addDefault("ShopName", "ExtraShop");
        section.addDefault("signs.text", "[ExtraShop]");
        section.addDefault("signs.NeedPermissionToCreateSign", false);

        if (section.getConfigurationSection("shop") == null && section.getConfigurationSection("itemshop") == null) {
            section.createSection("shop");

            List<String> menuItem = new ArrayList<String>();
            menuItem.add("type:STONE");
            menuItem.add("amount:1");
            menuItem.add("name:&8Example");
            List<String> cmd = new ArrayList<String>();
            cmd.add("tell %name% Example");
            addDefault("Example", "command", "money", cmd, 5000, menuItem, "", 1, "");
            config.options().copyDefaults(true);
            saveConfig();
        }
    }

    //////////////////////////////////

    @Override
    public int getInventorySize() {
        if (section.getInt("InventorySize") != 0) {
            return section.getInt("InventorySize");
        }
        return super.getInventorySize();
    }

    ///////////////////////////////////////// <- Load Config-Items


    public void loadItems() {
        ConfigurationSection c = section.getConfigurationSection("shop");
        if (c != null) {
            for (String key : c.getKeys(false)) {
                ClassManager.manager.getBuyItemHandler().loadItem(c, this, key);
            }
        }
    }

    @Override
    public void reloadShop() {
        reloadConfig();
    }

}
