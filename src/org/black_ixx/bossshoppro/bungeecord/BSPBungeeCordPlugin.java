package org.black_ixx.bossshoppro.bungeecord;


import net.md_5.bungee.api.plugin.Plugin;

public class BSPBungeeCordPlugin extends Plugin {


    String USER = "%%__USER__%%";

    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerListener(this, new BSPBungeeCordPluginListener(this));
    }
}
