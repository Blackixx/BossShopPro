package org.black_ixx.bossshop.managers;


import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.managers.item.ItemDataPart;
import org.black_ixx.bossshop.misc.Misc;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CommandManager implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {

        if (cmd.getName().equalsIgnoreCase("bossshop") || cmd.getName().equalsIgnoreCase("shop") || cmd.getName().equalsIgnoreCase("bs")) {

            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("reload")) {

                    if (sender.hasPermission("BossShop.reload")) {

                        sender.sendMessage(ChatColor.YELLOW + "Starting BossShop reload...");
                        ClassManager.manager.getPlugin().reloadPlugin(sender);
                        sender.sendMessage(ChatColor.YELLOW + "Done!");

                    } else {
                        ClassManager.manager.getMessageHandler().sendMessage("Main.NoPermission", sender);
                        return false;
                    }
                    return true;
                }

                if (args[0].equalsIgnoreCase("read")) {
                    if (sender instanceof Player) {
                        if (sender.hasPermission("BossShop.read")) {
                            Player p = (Player) sender;
                            ItemStack item = Misc.getItemInMainHand(p);
                            if (item == null || item.getType() == Material.AIR) {
                                ClassManager.manager.getMessageHandler().sendMessage("Main.NeedItemInHand", sender);
                                return false;
                            }
                            List<String> itemdata = ItemDataPart.readItem(item);
                            ClassManager.manager.getItemDataStorage().addItemData(p.getName(), itemdata);
                            ClassManager.manager.getMessageHandler().sendMessage("Main.PrintedItemInfo", sender);
                            for (String line : itemdata) {
                                sender.sendMessage("- " + line);
                            }
                            return true;
                        } else {
                            ClassManager.manager.getMessageHandler().sendMessage("Main.NoPermission", sender);
                            return false;
                        }
                    }
                }

                if (args[0].equalsIgnoreCase("simulate")) {
                    if (sender.hasPermission("BossShop.simulate")) {
                        if (args.length == 4) {
                            Player p = Bukkit.getPlayer(args[1]);
                            if (p == null) {
                                ClassManager.manager.getMessageHandler().sendMessage("Main.PlayerNotFound", sender, args[1]);
                                return false;
                            }

                            BSShop shop = ClassManager.manager.getShops().getShop(args[2]);
                            if (shop == null) {
                                ClassManager.manager.getMessageHandler().sendMessage("Main.ShopNotExisting", sender, null, p, null, null, null);
                                return false;
                            }

                            BSBuy buy = shop.getItem(args[3]);
                            if (buy == null) {
                                ClassManager.manager.getMessageHandler().sendMessage("Main.ShopItemNotExisting", sender, null, p, shop, null, null);
                                return false;
                            }

                            buy.click(p, shop, null, null, null, ClassManager.manager.getPlugin());
                            return true;
                        }
                        sendCommandList(sender);
                        return false;
                    }
                }

                if (args[0].equalsIgnoreCase("close")) {
                    if (sender.hasPermission("BossShop.close")) {
                        Player p = null;
                        String name = sender instanceof Player ? sender.getName() : "CONSOLE";

                        if (sender instanceof Player) {
                            p = (Player) sender;
                        }
                        if (args.length >= 2) {
                            name = args[1];
                            p = Bukkit.getPlayer(name);
                        }

                        if (p == null) {
                            ClassManager.manager.getMessageHandler().sendMessage("Main.PlayerNotFound", sender, name);
                            return false;
                        }

                        p.closeInventory();
                        if (p != sender) {
                            ClassManager.manager.getMessageHandler().sendMessage("Main.CloseShopOtherPlayer", sender, p);
                        }

                    } else {
                        ClassManager.manager.getMessageHandler().sendMessage("Main.NoPermission", sender);
                        return false;
                    }
                    return true;
                }

                if (args.length >= 3 && args[0].equalsIgnoreCase("open")) {

                    if (args.length < 2) {
                        sendCommandList(sender);
                        return false;
                    }

                    String shopname = args[1].toLowerCase();
                    BSShop shop = ClassManager.manager.getShops().getShop(shopname);
                    String name = args[2];
                    Player p = Bukkit.getPlayerExact(name);
                    String argument = args.length > 3 ? args[3] : null;

                    if (p == null) {
                        p = Bukkit.getPlayer(name);
                    }

                    if (p == null) {
                        ClassManager.manager.getMessageHandler().sendMessage("Main.PlayerNotFound", sender, name, null, shop, null, null);
                        return false;
                    }

                    if (shop == null) {
                        ClassManager.manager.getMessageHandler().sendMessage("Main.ShopNotExisting", sender, null, p, shop, null, null);
                        return false;
                    }

                    playerCommandOpenShop(sender, p, shopname, argument);
                    if (p != sender) {
                        ClassManager.manager.getMessageHandler().sendMessage("Main.OpenShopOtherPlayer", sender, null, p, shop, null, null);
                    }

                    return true;
                }

            }

            if (sender instanceof Player) {
                Player p = (Player) sender;

                String shop = ClassManager.manager.getSettings().getMainShop();
                if (args.length != 0) {
                    shop = args[0].toLowerCase();
                }
                String argument = args.length > 1 ? args[1] : null;
                playerCommandOpenShop(sender, p, shop, argument);
                return true;
            }
            sendCommandList(sender);
            return false;
        }

        return false;
    }


    private boolean playerCommandOpenShop(CommandSender sender, Player target, String shop, String argument) {
        if (sender == target) {
            if (!(sender.hasPermission("BossShop.open") || sender.hasPermission("BossShop.open.command") || sender.hasPermission("BossShop.open.command." + shop))) {
                ClassManager.manager.getMessageHandler().sendMessage("Main.NoPermission", sender);
                return false;
            }
        } else {
            if (!sender.hasPermission("BossShop.open.other")) {
                ClassManager.manager.getMessageHandler().sendMessage("Main.NoPermission", sender);
                return false;
            }
        }
        if (argument != null) {
            ClassManager.manager.getPlayerDataHandler().enteredInput(target, argument);
        }
        if (ClassManager.manager == null) {
            return false;
        }
        if (ClassManager.manager.getShops() == null) {
            return false;
        }
        ClassManager.manager.getShops().openShop(target, shop);
        return true;
    }

    private void sendCommandList(CommandSender s) {
        s.sendMessage(ChatColor.RED + "/BossShop - Opens  main shop");
        s.sendMessage(ChatColor.RED + "/BossShop <shop> [input] - Opens named shop");
        s.sendMessage(ChatColor.RED + "/BossShop open <Shop> <Player> [input] - Opens named shop for the named player");
        s.sendMessage(ChatColor.RED + "/BossShop close [Player] - Closes inventory of the named player");
        s.sendMessage(ChatColor.RED + "/BossShop simulate <player> <shop> <shopitem> - Simulates click");
        s.sendMessage(ChatColor.RED + "/BossShop reload - Reloads the Plugin");
        if (s instanceof Player) {
            s.sendMessage(ChatColor.RED + "/BossShop read - Prints out itemdata of item in main hand");
        }
    }

}
