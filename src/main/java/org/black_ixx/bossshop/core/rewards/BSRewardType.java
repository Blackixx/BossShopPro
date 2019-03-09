package org.black_ixx.bossshop.core.rewards;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

public abstract class BSRewardType {


    public static BSRewardType
            BungeeCordCommand,
            BungeeCordServer,
            Close,
            Command,
            Custom,
            Enchantment,
            Exp,
            Item,
            ItemAll,
            Money,
            Nothing,
            Permission,
            PlayerCommand,
            PlayerCommandOp,
            Points,
            Shop,
            ShopBack,
            ShopPage;

    private static List<BSRewardType> types;
    private String[] names = createNames();

    public static void loadTypes() {
        types = new ArrayList<>();

        BungeeCordCommand = registerType(new BSRewardTypeBungeeCordCommand());
        BungeeCordServer = registerType(new BSRewardTypeBungeeCordServer());
        Close = registerType(new BSRewardTypeClose());
        Custom = registerType(new BSRewardTypeCustom());
        Command = registerType(new BSRewardTypeCommand());
        Enchantment = registerType(new BSRewardTypeEnchantment());
        Exp = registerType(new BSRewardTypeExp());
        Item = registerType(new BSRewardTypeItem());
        ItemAll = registerType(new BSRewardTypeItemAll());
        Money = registerType(new BSRewardTypeMoney());
        Nothing = registerType(new BSRewardTypeNothing());
        Permission = registerType(new BSRewardTypePermission());
        PlayerCommand = registerType(new BSRewardTypePlayerCommand());
        PlayerCommandOp = registerType(new BSRewardTypePlayerCommandOp());
        Points = registerType(new BSRewardTypePoints());
        Shop = registerType(new BSRewardTypeShop());
        ShopPage = registerType(new BSRewardTypeShopPage());
    }

    public static BSRewardType registerType(BSRewardType type) {
        types.add(type);
        return type;
    }

    public static BSRewardType detectType(String s) {
        if (s != null) {
            for (BSRewardType type : types) {
                if (type.isType(s)) {
                    return type;
                }
            }
        }
        return BSRewardType.Nothing;
    }

    public static List<BSRewardType> values() {
        return types;
    }

    public boolean isType(String s) {
        if (names != null) {
            for (String name : names) {
                if (name.equalsIgnoreCase(s)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void register() {
        BSRewardType.registerType(this);
    }

    public String name() {
        return names[0].toUpperCase();
    }

    public void updateNames() {
        names = createNames();
    }


    public abstract Object createObject(Object o, boolean force_final_state); //Used to transform the config input into a functional object

    public abstract boolean validityCheck(String item_name, Object o); //Used to check if the object is valid

    public abstract void enableType(); //Here you can register classes that the type depends on

    public abstract boolean canBuy(Player p, BSBuy buy, boolean message_if_no_success, Object reward, ClickType clickType);

    public abstract void giveReward(Player p, BSBuy buy, Object reward, ClickType clickType);

    public abstract String getDisplayReward(Player p, BSBuy buy, Object reward, ClickType clickType);

    public abstract String[] createNames();


    public boolean logTransaction() {
        return true; //Can be overwritten
    }

    public boolean isPlayerDependend(BSBuy buy, ClickType clicktype) {
        return supportsMultipliers() && ClassManager.manager.getMultiplierHandler().hasMultipliers() || (buy.getRewardType(clicktype) == BSRewardType.ItemAll && ClassManager.manager.getSettings().getItemAllShowFinalReward());
    }

    public boolean supportsMultipliers() {
        return false; //can be overwritten
    }

    /**
     * If set to true sound will be played when purchasing
     */
    public boolean isActualReward() {
        return true; //can be overwritten
    }

    public boolean allowAsync() {
        return false; //can be overwritten
    }

    public boolean overridesPrice() {
        return false; //Can be overwritten
    }

    public String getPriceReturnMessage(Player p, BSBuy buy, Object price, ClickType clickType) {
        return null; //Can be overwritten in case of overriding the price
    }


    public abstract boolean mightNeedShopUpdate();


}
