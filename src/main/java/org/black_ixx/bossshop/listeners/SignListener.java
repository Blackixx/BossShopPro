package org.black_ixx.bossshop.listeners;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.BSShop;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;

public class SignListener implements Listener {

    private boolean s;
    private BossShop plugin;

    public SignListener(boolean s, BossShop plugin) {
        this.s = s;
        this.plugin = plugin;
    }

    private BSShop getBossShopSign(String line) {

        if (line == null || line == "") {
            return null;
        }
        line = line.toLowerCase();
        HashMap<Integer, BSShop> set = plugin.getClassManager().getShops().getShops();

        for (Integer s : set.keySet()) {

            BSShop shop = set.get(s);
            String signtext = shop.getSignText();

            if (signtext != null) {
                if (line.endsWith(signtext.toLowerCase())) {
                    return shop;
                }
            }


        }

        return null;
    }

    @EventHandler
    public void createSign(SignChangeEvent e) {

        if (!s) {
            return;
        }

        final BSShop shop = getBossShopSign(e.getLine(0));
        if (shop != null) {

            if (shop.needPermToCreateSign()) {


                if (!e.getPlayer().hasPermission("BossShop.createSign")) {

                    plugin.getClassManager().getMessageHandler().sendMessage("Main.NoPermission", e.getPlayer());
                    e.setCancelled(true);
                    return;
                }
            }

            if (e.getLine(0) != "") {
                e.setLine(0, plugin.getClassManager().getStringManager().transform(e.getLine(0)));
            }
            if (e.getLine(1) != "") {
                e.setLine(1, plugin.getClassManager().getStringManager().transform(e.getLine(1)));
            }
            if (e.getLine(2) != "") {
                e.setLine(2, plugin.getClassManager().getStringManager().transform(e.getLine(2)));
            }
            if (e.getLine(3) != "") {
                e.setLine(3, plugin.getClassManager().getStringManager().transform(e.getLine(3)));
            }


        }
    }


    @EventHandler
    public void interactSign(PlayerInteractEvent e) {
        if (!s) {
            return;
        }

        if (e.getClickedBlock() != null) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

                Block b = e.getClickedBlock();
                if (b.getType().name().contains("SIGN")) {

                    if (b.getState() instanceof Sign) {
                        Sign s = (Sign) b.getState();

                        BSShop shop = getBossShopSign(s.getLine(0));
                        if (shop != null) {

                            if (e.getPlayer().hasPermission("BossShop.open") || e.getPlayer().hasPermission("BossShop.open.sign") || e.getPlayer().hasPermission("BossShop.open.sign." + shop.getShopName())) {
                                plugin.getClassManager().getShops().openShop(e.getPlayer(), shop);
                                return;
                            }

                            plugin.getClassManager().getMessageHandler().sendMessage("Main.NoPermission", e.getPlayer());
                            return;
                        }

                    }
                }
            }
        }
    }


    public void setSignsEnabled(boolean b) {
        s = b;
    }


}
