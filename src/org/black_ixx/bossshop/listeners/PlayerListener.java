package org.black_ixx.bossshop.listeners;


import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.features.PlayerDataHandler;
import org.black_ixx.bossshop.misc.userinput.BSChatUserInput;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {


    private BossShop plugin;

    public PlayerListener(BossShop plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void shopCommand(PlayerCommandPreprocessEvent e) {
        if (!e.isCancelled()) {
            if (plugin.getClassManager().getSettings().getShopCommandsEnabled()) {
                Player p = e.getPlayer();
                String cmd = e.getMessage().substring(1);

                if (plugin.getClassManager().getShops() != null) {
                    BSShop shop = plugin.getClassManager().getShops().getShopByCommand(cmd);
                    if (shop != null) {

                        if (p.hasPermission("BossShop.open") || p.hasPermission("BossShop.open.command") || p.hasPermission("BossShop.open.command." + shop.getShopName())) {
                            ClassManager.manager.getShops().openShop(p, shop);
                        } else {
                            ClassManager.manager.getMessageHandler().sendMessage("Main.NoPermission", p);
                        }
                        //p.performCommand("bossshop "+shop.getShopName());

                        e.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }


    @EventHandler(priority = EventPriority.LOW)
    public void chat(AsyncPlayerChatEvent e) {
        if (ClassManager.manager.getPlayerDataHandler() != null) {
            PlayerDataHandler h = ClassManager.manager.getPlayerDataHandler();

            BSChatUserInput i = h.getInputRequest(e.getPlayer());
            if (i != null) {
                if (i.isUpToDate()) {
                    i.input(e.getPlayer(), e.getMessage());
                    e.setCancelled(true);
                }
                h.removeInputRequest(e.getPlayer());
            }
        }
    }


    @EventHandler
    public void quitServer(PlayerQuitEvent event) {
        leave(event.getPlayer());
    }

    @EventHandler
    public void kickedOffServer(PlayerKickEvent event) {
        leave(event.getPlayer());
    }

    public void leave(Player p) {
        if (ClassManager.manager.getPlayerDataHandler() != null) {
            ClassManager.manager.getPlayerDataHandler().leftServer(p);
        }
    }

}
