package org.black_ixx.bossshop.managers.external;

import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.request.impl.RedirectRequest;
import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class LilypadManager {

    private Connect connect;

    public LilypadManager(BossShop plugin) {

        if (plugin.getServer().getPluginManager().getPlugin("LilyPad-Connect") == null) {
            ClassManager.manager.getBugFinder().warn("LilyPad-Connect was not found... You need it if you want " + BossShop.NAME + " to work with LilyPad. Get it here: http://ci.lilypadmc.org/job/Bukkit-Connect/");
            return;
        }

        connect = getBukkitConnect(plugin);
    }

    private boolean canConnect() {
        if (connect == null) {
            ClassManager.manager.getBugFinder().warn("LilyPad-Connect was not found... You need it if you want " + BossShop.NAME + " to work with LilyPad. Get it here: http://ci.lilypadmc.org/job/Bukkit-Connect/");
            return false;
        }
        return true;
    }


    private Connect getBukkitConnect(JavaPlugin plugin) {
        if (plugin.getServer().getServicesManager().getRegistration(Connect.class) == null) {
            return null;
        }

        return (Connect) plugin.getServer().getServicesManager().getRegistration(Connect.class).getProvider();
    }

    public void redirectRequest(String server, Player player) {
        if (!canConnect()) {
            return;
        }

        try {

            connect.request(new RedirectRequest(server, player.getName()));
        } catch (Exception exception) {
            player.sendMessage(ChatColor.RED + "The requested Server is offline!");
        }
    }


}
