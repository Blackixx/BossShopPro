package org.black_ixx.bossshop.listeners;


import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.misc.MathTools;
import org.black_ixx.bossshop.misc.Misc;
import org.black_ixx.bossshop.misc.userinput.BSAnvilHolder;
import org.black_ixx.bossshop.settings.Settings;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.WeakHashMap;

public class InventoryListener implements Listener {


    private WeakHashMap<Player, Long> lastClicks;
    private WeakHashMap<Player, Integer> clickspamCounts;
    private int clickDelay, clickspamDelay, clickspamWarnings, clickSpamForgetTime;
    private BossShop plugin;

    public InventoryListener(BossShop plugin) {
        this.plugin = plugin;
        lastClicks = new WeakHashMap<>();
        clickspamCounts = new WeakHashMap<>();
    }

    public void init(int clickDelay, int clickspamDelay, int clickspamWarnings, int clickSpamForgetTime) {
        this.clickDelay = clickDelay;
        this.clickspamDelay = clickspamDelay;
        this.clickspamWarnings = clickspamWarnings;
        this.clickSpamForgetTime = clickSpamForgetTime;
    }

    @EventHandler
    public void closeShop(InventoryCloseEvent e) {
        if (!(e.getInventory().getHolder() instanceof BSShopHolder)) {
            return;
        }
        BSShopHolder holder = (BSShopHolder) e.getInventory().getHolder();

        if (e.getPlayer() instanceof Player) {
            final Player p = (Player) e.getPlayer();
            plugin.getClassManager().getMessageHandler().sendMessage("Main.CloseShop", p, null, (Player) e.getPlayer(), holder.getShop(), holder, null);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!ClassManager.manager.getPlugin().getAPI().isValidShop(p.getOpenInventory())) {
                        Misc.playSound(p, ClassManager.manager.getSettings().getPropertyString(Settings.SOUND_SHOP_CLOSE, this, null));
                    }
                }
            }.runTask(plugin);
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void purchase(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof BSShopHolder)) {
            return;
        }

        boolean cancel = true;
        try {
            if (!(ClassManager.manager.getPlugin().getAPI().isValidShop(event.getClickedInventory()))) {
                switch (event.getAction()) {
                    case DROP_ALL_SLOT:
                    case DROP_ONE_SLOT:
                    case HOTBAR_MOVE_AND_READD:
                    case HOTBAR_SWAP:
                    case PICKUP_ALL:
                    case PICKUP_HALF:
                    case PICKUP_ONE:
                    case PICKUP_SOME:
                    case PLACE_ALL:
                    case PLACE_ONE:
                    case PLACE_SOME:
                    case SWAP_WITH_CURSOR:
                        cancel = false;
                        break;
                    default:
                        break;
                }
            }
        } catch (NoSuchMethodError e) {
            //error when not using spigot api
        }
        if (cancel) {
            event.setCancelled(true);
            event.setResult(Result.DENY);
        }


        BSShopHolder holder = (BSShopHolder) event.getInventory().getHolder();


        if (event.getWhoClicked() instanceof Player) {

            if (event.getCurrentItem() == null) {
                return;
            }

            if (event.getClick() == ClickType.DOUBLE_CLICK) {
                return;
            }

            if (event.getCursor() == null) {
                return;
            }

            if (event.getSlotType() == SlotType.QUICKBAR) {
                return;
            }

            BSBuy buy = holder.getShopItem(event.getRawSlot());
            if (buy == null) {
                return;
            }
            event.setCancelled(true);
            event.setResult(Result.DENY);
            //event.setCurrentItem(null);

            final Player p = (Player) event.getWhoClicked();
            ClickType clicktype = event.getClick();


            BSShop shop = ((BSShopHolder) event.getInventory().getHolder()).getShop();

            //Anti spam delay
            if (!p.hasPermission("BossShop.bypasss")) {
                if (lastClicks.containsKey(p)) {
                    long lastClick = lastClicks.get(p);


                    //Offensive clickspam
                    if (System.currentTimeMillis() < lastClick + clickspamDelay) {
                        int clickspamCount = 0;
                        if (clickspamCounts.containsKey(p)) {
                            clickspamCount = clickspamCounts.get(p);
                        }
                        clickspamCount++;

                        if (clickspamCount > clickspamWarnings) {
                            p.kickPlayer(ClassManager.manager.getMessageHandler().get("Main.OffensiveClickSpamKick"));
                        } else {
                            double timeLeft = lastClicks.get(p) + clickspamDelay - System.currentTimeMillis();
                            timeLeft = Math.max(0.1f, timeLeft / 1000);
                            ClassManager.manager.getMessageHandler().sendMessageDirect(ClassManager.manager.getStringManager().transform(ClassManager.manager.getMessageHandler().get("Main.OffensiveClickSpamWarning").replace("%time_left%", MathTools.displayNumber(timeLeft, 1)), buy, shop, holder, p), p);
                        }
                        clickspamCounts.put(p, clickspamCount);
                        return;
                    }

                    //Clickspam
                    if (System.currentTimeMillis() < lastClick + clickDelay) {
                        double timeLeft = lastClicks.get(p) + clickDelay - System.currentTimeMillis();
                        timeLeft = Math.max(0.1f, timeLeft / 1000);
                        ClassManager.manager.getMessageHandler().sendMessageDirect(ClassManager.manager.getStringManager().transform(ClassManager.manager.getMessageHandler().get("Main.ClickSpamWarning").replace("%time_left%", MathTools.displayNumber(timeLeft, 1)), buy, shop, holder, p), p);
                        return;
                    }

                    //Forget old clickspam
                    if (clickspamCounts.containsKey(p)) {
                        if (lastClick + clickSpamForgetTime < System.currentTimeMillis()) {
                            clickspamCounts.remove(p);
                        }
                    }

                }
                lastClicks.put(p, System.currentTimeMillis());
            }

            buy.click(p, shop, holder, clicktype, event, plugin);
        }
    }

    @EventHandler
    public void drag(InventoryDragEvent event) {
        if (!(event.getInventory().getHolder() instanceof BSShopHolder)) {
            return;
        }
        //When there is a good way to detect whether the upper inventory has been affected by the drag event or not then please inbuilt it and only cancel the event in that case
        event.setCancelled(true);
        event.setResult(Result.DENY);
    }


    @EventHandler
    public void quit(PlayerQuitEvent event) {
        playerLeave(event);
    }

    @EventHandler
    public void kick(PlayerKickEvent event) {
        playerLeave(event);
    }

    public void playerLeave(PlayerEvent event) {
        if (lastClicks != null) {
            if (lastClicks.containsKey(event.getPlayer())) {
                lastClicks.remove(event.getPlayer());
            }
        }
        if (clickspamCounts != null) {
            if (clickspamCounts.containsKey(event.getPlayer())) {
                clickspamCounts.remove(event.getPlayer());
            }
        }
    }


    @EventHandler
    public void onAnvilEvent(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();

            if (e.getInventory().getHolder() instanceof BSAnvilHolder) {
                e.setResult(Result.DENY);
                e.setCancelled(true);
                BSAnvilHolder holder = (BSAnvilHolder) e.getInventory().getHolder();
                String text = holder.getOutputText();
                if (text != null) {
                    holder.userClickedResult(p);
                }
            }
        }
    }


}
