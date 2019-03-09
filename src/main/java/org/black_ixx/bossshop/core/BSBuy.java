package org.black_ixx.bossshop.core;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.conditions.BSCondition;
import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.core.rewards.BSRewardType;
import org.black_ixx.bossshop.events.BSPlayerPurchaseEvent;
import org.black_ixx.bossshop.events.BSPlayerPurchasedEvent;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.config.BSConfigShop;
import org.black_ixx.bossshop.misc.Misc;
import org.black_ixx.bossshop.misc.ShopItemPurchaseTask;
import org.black_ixx.bossshop.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;


public class BSBuy {


    private BSShop shop;
    private HashMap<Plugin, Object> special_information;

    private boolean fix_item; // In order for an item to not be fix it must contain a player-dependent placeholder (detected by StringManager.checkStringForFeatures)
    private ItemStack item;
    private String name;
    private BSInputType inputtype; // null by default. A value if players need to enter input before they can purchase the item.
    private String inputtext;
    private BSRewardType rewardT;
    private BSPriceType priceT;
    private Object reward;
    private Object price;
    private BSCondition condition;
    private String permission;
    private boolean perm_is_group = false;
    private String msg;
    private int location;

    public BSBuy(BSRewardType rewardT, BSPriceType priceT, Object reward, Object price, String msg, int location, String permission, String name, BSCondition condition, BSInputType inputtype, String inputtext) {
        this(rewardT, priceT, reward, price, msg, location, permission, name);
        this.condition = condition;
        this.inputtype = inputtype;
        this.inputtext = ClassManager.manager.getStringManager().transform(inputtext, this, null, null, null);
    }
    public BSBuy(BSRewardType rewardT, BSPriceType priceT, Object reward, Object price, String msg, int location, String permission, String name) {
        this.priceT = priceT;
        this.rewardT = rewardT;

        if (permission != null && permission != "") {
            this.permission = permission;
            if (permission.startsWith("[") && permission.endsWith("]")) {
                if (permission.length() > 2) {
                    String group = permission.substring(1, permission.length() - 1);
                    if (group != null) {
                        ClassManager.manager.getSettings().setVaultEnabled(true);
                        this.permission = group;
                        perm_is_group = true;
                    }
                }
            }
        }

        this.reward = reward;
        this.price = price;
        this.name = name;
        this.msg = ClassManager.manager.getStringManager().transform(msg, this, null, null, null);
        this.location = location;
    }



    public BSShop getShop() {
        return shop;
    }

    public void setShop(BSShop shop) {
        this.shop = shop;
    }

    public BSRewardType getRewardType(ClickType clicktype) {
        return rewardT;
    }

    public BSPriceType getPriceType(ClickType clicktype) {
        return priceT;
    }

    public Object getReward(ClickType clicktype) {
        return reward;
    }

    public Object getPrice(ClickType clicktype) {
        return price;
    }

    public String getMessage(ClickType clicktype) {
        return msg;
    }

    public BSInputType getInputType(ClickType clicktype) {
        return inputtype;
    }

    public String getInputText(ClickType clicktype) {
        return inputtext;
    }

    /**
     * @return real inventory location with 0 as first possible one and -1 as "next available slot".
     */
    public int getInventoryLocation() {
        return location;
    }

    @Deprecated
    public void setInventoryLocation(int i) {
        location = i;
    }

    public String getName() {
        return name;
    }

    public ItemStack getItem() {
        return item;
    }

    public boolean isItemFix() {
        return fix_item;
    }

    public BSCondition getCondition() {
        return condition;
    }

    public boolean meetsCondition(BSShopHolder holder, Player p) {
        if (condition != null) {
            return condition.meetsCondition(holder, this, p);
        }
        return true;
    }

    public boolean containsConditions() {
        return condition != null;
    }

    public ConfigurationSection getConfigurationSection(BSConfigShop shop) {
        return shop.getConfig().getConfigurationSection("shop").getConfigurationSection(name);
    }

    public boolean hasPermission(Player p, boolean msg, ClickType clicktype) {
        if (!isExtraPermissionExisting(clicktype)) {
            return true;
        }
        String permission = getExtraPermission(clicktype);

        if (isExtraPermissionGroup(clicktype)) {
            boolean no_group = true;
            for (String group : ClassManager.manager.getVaultHandler().getPermission().getPlayerGroups(p)) {
                no_group = false;
                if (group.equalsIgnoreCase(permission)) {
                    return true;
                }
            }
            if (no_group && permission.equalsIgnoreCase("default")) {
                return true;
            }
            if (msg) {
                ClassManager.manager.getMessageHandler().sendMessage("Main.NoPermission", p);
            }
            return false;
        }

        if (p.hasPermission(permission)) {
            return true;
        }
        if (msg) {
            ClassManager.manager.getMessageHandler().sendMessage("Main.NoPermission", p);
        }
        return false;
    }

    public boolean isExtraPermissionExisting(ClickType clicktype) {
        String permission = getExtraPermission(clicktype);
        if (permission == null) {
            return false;
        }
        if (permission.equalsIgnoreCase("")) {
            return false;
        }
        return true;
    }

    public boolean isExtraPermissionGroup(ClickType clicktype) {
        return perm_is_group;
    }



    public String getExtraPermission(ClickType clicktype) {
        return permission;
    }

    public Object readSpecialInformation(Plugin plugin) {
        if (special_information != null) {
            return special_information.get(plugin);
        }
        return null;
    }



    /**
     * Transforms the selected message by replacing price and reward placeholders with descriptions of price or reward.
     * If player is null only player-independent placeholders are replaced.
     * Additionally item placeholders like amount, material and more are replaced.
     * <br>
     * The method needs to be executed once the item is created (player=null):
     * <br>- to replace all player-independent placeholders
     * <br>- to mark the related shop as customizable if it contains player-dependent placeholders
     * <br>- to detect the inputtype of this shopitem. If it is not null (default is null) players need to enter input before purchases are made
     * <br>
     * <br>The method also needs to be executed when a certain player accesses the item (player!=null):
     * <br>- to replace all player-dependent placeholders
     *
     * @param msg  Message to transform.
     * @param shop Shop this shopitem belongs to.
     * @param p    Related player. Can be null.
     * @return transformed message.
     */
    public String transformMessage(String msg, BSShop shop, Player p) {
        if (msg == null) {
            return null;
        }
        if (msg.length() == 0) {
            return msg;
        }

        if (p == null) {
            //Player null -> first item creation
            if (msg.contains("%input_text%")) {
                inputtype = BSInputType.TEXT;
            } else if (msg.contains("%input_player%")) {
                inputtype = BSInputType.PLAYER;
            }
            if (inputtype != null && shop != null) {
                shop.setCustomizable(true);
                shop.setDisplaying(true);
            }
        }

        //Handle reward and price variables
        if (msg.contains("%price%") || msg.contains("%reward%")) {
            String rewardMessage = rewardT.isPlayerDependend(this, null) ? null : rewardT.getDisplayReward(p, this, reward, null);
            String priceMessage = priceT.isPlayerDependend(this, null) ? null : priceT.getDisplayPrice(p, this, price, null);


            if (shop != null) { //Does shop need to be customizable and is not already?
                if (!shop.isCustomizable()) {
                    boolean has_pricevariable = (msg.contains("%price%") && (priceT.isPlayerDependend(this, null)));
                    boolean has_rewardvariable = (msg.contains("%reward%") && (rewardT.isPlayerDependend(this, null)));
                    if (has_pricevariable || has_rewardvariable) {
                        shop.setCustomizable(true);
                        shop.setDisplaying(true);
                    }
                }
            }

            boolean possibly_customizable = shop == null ? true : shop.isCustomizable();
            if (possibly_customizable) {
                if (p != null) { //When shop is customizable, the variables needs to be adapted to the player
                    rewardMessage = rewardT.getDisplayReward(p, this, reward, null);
                    priceMessage = priceT.getDisplayPrice(p, this, price, null);
                }
            }

            if (priceMessage != null && priceMessage != "" && priceMessage.length() > 0) {
                msg = msg.replace("%price%", priceMessage);
            }
            if (rewardMessage != null && rewardMessage != "" && rewardMessage.length() > 0) {
                msg = msg.replace("%reward%", rewardMessage);
            }
        }

        //Not working with these variables anymore. They are still included and set to "" in order to make previous shops still look good and stay compatible.
        if (priceT != null && priceT.name() != "" && priceT.name().length() > 0) {
            msg = msg.replace(" %pricetype%", "");
            msg = msg.replace("%pricetype%", "");
        }
        if (rewardT != null && rewardT.name() != "" && rewardT.name().length() > 0) {
            msg = msg.replace(" %rewardtype%", "");
            msg = msg.replace("%rewardtype%", "");
        }

        //Handle rest
        msg = msg.replace("%shopitemname%", this.name);

        String name = this.name;
        if (shop != null && item != null) {
            String item_title = ClassManager.manager.getItemStackTranslator().readItemName(item);
            if (item_title != null) {
                name = item_title;
                msg = msg.replace("%itemname%", name);
            }

            if (msg.contains("%amount%")) {
                msg = msg.replace("%amount%", String.valueOf(item.getAmount()));
            }
            if (msg.contains("%material%")) {
                msg = msg.replace("%material%", ClassManager.manager.getItemStackTranslator().readMaterial(item));
            }
            if (msg.contains("%rewardraw%")) {
                msg = msg.replace("%rewardraw%", String.valueOf(reward));
            }
            if (msg.contains("%priceraw%")) {
                msg = msg.replace("%priceraw%", String.valueOf(price));
            }
        }
        return msg;
    }

    public void updateShop(BSShop shop, ItemStack menuitem, ClassManager manager, boolean add_item) {
        if (manager.getSettings().getPropertyBoolean(Settings.HIDE_ITEMS_PLAYERS_DONT_HAVE_PERMISSIONS_FOR, this)) {
            if (!shop.isCustomizable()) {
                if (isExtraPermissionExisting(null)) {
                    shop.setCustomizable(true);
                }
            }
        }

        if (!shop.isCustomizable()) {
            for (BSBuy b : shop.getItems()) {
                if (b != null) {
                    if (b.getInventoryLocation() == getInventoryLocation() || b.containsConditions()) {
                        shop.setCustomizable(true);
                        break;
                    }
                }
            }
        }

        if (menuitem.hasItemMeta()) {
            if (ClassManager.manager.getItemStackTranslator().checkItemStackForFeatures(shop, this, menuitem)) {
                shop.setCustomizable(true);
                shop.setDisplaying(true);
            }

            if (!isItemFix()) { //Addons can make items fix instantly. In normal cases this check is not needed.
                ClassManager.manager.getItemStackTranslator().translateItemStack(this, shop, null, menuitem, null, false);
            }
        }
        setItem(menuitem, !ClassManager.manager.getItemStackTranslator().checkItemStackForFeatures(shop, this, menuitem));
        if (isItemFix()) { //When all placeholders are replaced the plugin can finally cut the lore and do final stuff
            ClassManager.manager.getItemStackTranslator().translateItemStack(null, null, null, getItem(), null, true);
        }
        if (add_item) {
            shop.getItems().add(this);
        }
    }

    public void putSpecialInformation(Plugin plugin, Object information) {
        if (plugin != null && information != null) {
            if (special_information == null) {
                special_information = new HashMap<>();
            }
            special_information.put(plugin, information);
        }
    }

    public void setItem(ItemStack item, boolean fix_item) {
        this.item = item;
        this.fix_item = fix_item;
    }

    public void click(Player p, BSShop shop, BSShopHolder holder, ClickType clicktype, InventoryClickEvent event, BossShop plugin) {
        if (!hasPermission(p, true, clicktype)) {
            Misc.playSound(p, ClassManager.manager.getSettings().getPropertyString(Settings.SOUND_SHOPITEM_NOPERMISSION, this, null));
            return;
        }
        if (!meetsCondition(holder, p)) {
            return; //Can only happen when player click-spams item before it is refreshed
        }

        this.purchaseTry(p, shop, holder, clicktype, event, plugin);
    }

    /**
     * Tries executing a purchase action. Fails if the player is unable to buy the item or unable to pay the price.
     *
     * @param p         Player to click purchase.
     * @param shop      Shop this shopitem belongs to.
     * @param holder    Holder of the shop.
     * @param clicktype Clicktype.
     * @param event     Click event which caused purchase. Can be null (for example when click is simulated).
     * @param plugin    Bossshop plugin.
     */
    public void purchaseTry(Player p, BSShop shop, BSShopHolder holder, ClickType clicktype, InventoryClickEvent event, BossShop plugin) {
        BSPlayerPurchaseEvent e1 = new BSPlayerPurchaseEvent(p, shop, this, clicktype);//Custom Event
        Bukkit.getPluginManager().callEvent(e1);
        if (e1.isCancelled()) {
            return;
        }//Custom Event end

        BSRewardType rewardtype = getRewardType(clicktype);
        BSPriceType pricetype = getPriceType(clicktype);

        if (!rewardtype.canBuy(p, this, true, getReward(clicktype), clicktype)) {
            return;
        }
        if (!pricetype.hasPrice(p, this, getPrice(clicktype), clicktype, true)) {
            Misc.playSound(p, ClassManager.manager.getSettings().getPropertyString(Settings.SOUND_SHOPITEM_NOTENOUGHMONEY, this, null));
            return;
        }

        purchaseTask(p, shop, holder, clicktype, rewardtype, pricetype, event, plugin);
    }

    /**
     * Triggers {@link BSBuy#purchase(Player, BSShop, BSShopHolder, ClickType, BSRewardType, BSPriceType, InventoryClickEvent, BossShop, boolean)}.
     * This is done asynchronously if {@link Settings#getPurchaseAsync()} = true.
     *
     * @param p          Player to purchase the item.
     * @param shop       Shop this shopitem belongs to.
     * @param holder     Holder of the shop.
     * @param clicktype  Clicktype.
     * @param rewardtype Rewardtype.
     * @param pricetype  Pricetype.
     * @param event      Click event which caused purchase. Can be null (for example when click is simulated).
     * @param plugin     Bossshop plugin.
     */
    @Deprecated
    public void purchaseTask(final Player p, final BSShop shop, final BSShopHolder holder, final ClickType clicktype, final BSRewardType rewardtype, final BSPriceType pricetype, final InventoryClickEvent event, final BossShop plugin) {
        if (inputtype != null) {
            inputtype.forceInput(p, shop, this, holder, clicktype, rewardtype, pricetype, event, plugin);
            return;
        }

        if (ClassManager.manager.getSettings().getPurchaseAsync()) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, new ShopItemPurchaseTask(p, this, shop, holder, clicktype, rewardtype, pricetype, event));
        } else {
            purchase(p, shop, holder, clicktype, rewardtype, pricetype, event, plugin, false);
        }
    }


    /**
     * Actions:
     * <br>- take price from player (price might depend on rewardtype)
     * <br>- optionally close the inventory
     * <br>- give reward to player (reward might depend on pricetype; executed asynchronously if async=true and the rewardtype supports it)
     * <br>- optionally logs transaction in transactionslog
     * <br>- take price from player
     * <br>- sends purchase message to player
     * <br>- optionally plays purchase sound
     * <br>- updates shop if needed and inventory still open (done asynchronously if async=true)
     *
     * @param p          Player to purchase the item.
     * @param shop       Shop this shopitem belongs to.
     * @param holder     Holder of the shop.
     * @param clicktype  Clicktype.
     * @param rewardtype Rewardtype.
     * @param pricetype  Pricetype.
     * @param event      Click event which caused purchase. Can be null (for example when click is simulated).
     * @param plugin     Bossshop plugin.
     * @param async      Whether actions which can be executed asynchronously should be.
     */
    @Deprecated
    public void purchase(final Player p, final BSShop shop, final BSShopHolder holder, final ClickType clicktype, final BSRewardType rewardtype, BSPriceType pricetype, final InventoryClickEvent event, final BossShop plugin, boolean async) {
        //Generate message
        String message = getMessage(clicktype);
        if (message != null) {
            message = transformMessage(message, shop, p); //Transform message before taking price because then ItemAll works fine
        }

        String o = null;


        if (!rewardtype.overridesPrice()) {
            o = pricetype.takePrice(p, this, getPrice(clicktype), clicktype); //Take price
        }


        //Close shop if wanted
        if (plugin.getClassManager().getSettings().getPropertyBoolean(Settings.CLOSE_SHOP_AFTER_PURCHASE, this)) {
            p.closeInventory(); //NEW!!! MIGHT CAUSE BUGS!! Before it was executed async and after all other actions are executed.
        }

        if (!pricetype.overridesReward()) {
            //Give Reward
            //Some rewardtypes may not be async!
            if (async && rewardtype.allowAsync()) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        rewardtype.giveReward(p, BSBuy.this, getReward(clicktype), clicktype);
                    }
                }.runTask(plugin);
            } else {
                rewardtype.giveReward(p, this, getReward(clicktype), clicktype);
            }

        }

        if (rewardtype.overridesPrice()) {
            o = rewardtype.getPriceReturnMessage(p, this, pricetype, clicktype);
        }


        //Update message
        if (message != null) {
            if (o != null && o != "" && message.contains("%left%")) {
                message = message.replace("%left%", o);
            }
            message = plugin.getClassManager().getStringManager().transform(message, this, shop, holder, p); //Transform message before taking price because then ItemAll works fine
        }

        boolean need_update = rewardtype.mightNeedShopUpdate() || pricetype.mightNeedShopUpdate();

        //Transactionslog
        if (plugin.getClassManager().getSettings().getTransactionLogEnabled()) {
            plugin.getClassManager().getTransactionLog().addTransaction(p, this, clicktype);
        }

        //Custom "BSPlayerPurchasedEvent" event
        BSPlayerPurchasedEvent e2 = new BSPlayerPurchasedEvent(p, shop, this, clicktype);//Custom Event
        Bukkit.getPluginManager().callEvent(e2);//Custom Event end

        //Send message and play sound
        ClassManager.manager.getMessageHandler().sendMessageDirect(message, p);
        if (pricetype != BSPriceType.Nothing) {
            Misc.playSound(p, ClassManager.manager.getSettings().getPropertyString(Settings.SOUND_SHOPITEM_PURCHASE, this, null));
        } else {
            if (rewardtype.isActualReward()) {
                Misc.playSound(p, ClassManager.manager.getSettings().getPropertyString(Settings.SOUND_SHOPITEM_CLICK, this, null));
            }
        }

        //Update shop if needed
        if (shop.isCustomizable() && need_update && event != null) { //'event' is null in case of a simulated click
            if (p.getOpenInventory() == event.getView()) { //only if inventory is still open

                if (async) {
                    Bukkit.getScheduler().runTask(ClassManager.manager.getPlugin(), new Runnable() {
                        @Override
                        public void run() {
                            shop.updateInventory(event.getInventory(), holder, p, plugin.getClassManager(), holder.getPage(), holder.getHighestPage(), false);
                        }
                    });
                } else {
                    shop.updateInventory(event.getInventory(), holder, p, plugin.getClassManager(), holder.getPage(), holder.getHighestPage(), false);
                }

            }
        }

    }

}
