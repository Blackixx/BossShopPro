package org.black_ixx.bossshop.core;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.core.rewards.BSRewardType;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.misc.userinput.BSUserInput;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.concurrent.Callable;

public enum BSInputType {

    PLAYER {
        @Override
        @SuppressWarnings("deprecation")
        public void forceInput(final Player p, final BSShop shop, final BSBuy buy, final BSShopHolder holder, final ClickType clicktype,
                               final BSRewardType rewardtype, final BSPriceType pricetype, final InventoryClickEvent event, final BossShop plugin) {

            new BSUserInput() {
                @Override
                public void receivedInput(final Player p, String text) {
                    if (Bukkit.getServer().getPlayer(text) == null) {
                        ClassManager.manager.getMessageHandler().sendMessage("Main.PlayerNotFound", p, text, null, shop, holder, buy);
                        shop.openInventory(p, holder.getPage(), true);
                        return;
                    }
                    ClassManager.manager.getPlayerDataHandler().enteredInput(p, text);
                    Bukkit.getScheduler().callSyncMethod(plugin, new Callable<Boolean>() {
                        @Override
                        public Boolean call() {
                            buy.purchase(p, shop, holder, clicktype, rewardtype, pricetype, event, plugin, false);
                            return true;
                        }
                    });
                }
            }.getUserInput(p, null, null, buy.getInputText(clicktype));


        }
    },
    TEXT {
        @Override
        @SuppressWarnings("deprecation")
        public void forceInput(final Player p, final BSShop shop, final BSBuy buy, final BSShopHolder holder, final ClickType clicktype,
                               final BSRewardType rewardtype, final BSPriceType pricetype, final InventoryClickEvent event, final BossShop plugin) {

            new BSUserInput() {
                @Override
                public void receivedInput(final Player p, String text) {
                    ClassManager.manager.getPlayerDataHandler().enteredInput(p, text);
                    Bukkit.getScheduler().callSyncMethod(plugin, new Callable<Boolean>() {
                        @Override
                        public Boolean call() {
                            buy.purchase(p, shop, holder, clicktype, rewardtype, pricetype, event, plugin, false);
                            return true;
                        }
                    });
                }
            }.getUserInput(p, null, null, buy.getInputText(clicktype));

        }
    };


    public abstract void forceInput(final Player p, final BSShop shop, final BSBuy buy, final BSShopHolder holder, final ClickType clicktype, final BSRewardType rewardtype, final BSPriceType pricetype, final InventoryClickEvent event, final BossShop plugin);


}
