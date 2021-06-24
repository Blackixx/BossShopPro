package org.black_ixx.bossshop.core.rewards;


import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.misc.InputReader;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;


public class BSRewardTypeBungeeCordServer extends BSRewardType {


    public Object createObject(Object o, boolean force_final_state) {
        return InputReader.readString(o, true);
    }

    public boolean validityCheck(String item_name, Object o) {
        if (o != null) {
            return true;
        }
        ClassManager.manager.getBugFinder().severe("Was not able to create ShopItem " + item_name + "! The reward object needs to be the name of connected BungeeCordServer (a single text line).");
        return false;
    }

    public void enableType() {
        ClassManager.manager.getSettings().setBungeeCordServerEnabled(true);
    }

    @Override
    public boolean canBuy(Player p, BSBuy buy, boolean message_if_no_success, Object reward, ClickType clickType) {
        return true;
    }

    @Override
    public void giveReward(Player p, BSBuy buy, Object reward, ClickType clickType) {
        String server = (String) reward;
        ClassManager.manager.getBungeeCordManager().sendToServer(server, p, ClassManager.manager.getPlugin());
    }

    @Override
    public String getDisplayReward(Player p, BSBuy buy, Object reward, ClickType clickType) {
        String server = (String) reward;
        return ClassManager.manager.getMessageHandler().get("Display.BungeeCordServer").replace("%server%", server);
    }

    @Override
    public String[] createNames() {
        return new String[]{"bungeecordserver", "bungee"};
    }

    public boolean logTransaction() {
        return false;
    }

    @Override
    public boolean mightNeedShopUpdate() {
        return false;
    }

}
