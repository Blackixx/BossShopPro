package org.black_ixx.bossshop.core.rewards;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.misc.InputReader;
import org.black_ixx.bossshop.managers.misc.StringManipulationLib;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class BSRewardTypePlayerCommandOp extends BSRewardType {


    public Object createObject(Object o, boolean force_final_state) {
        return InputReader.readStringList(o);
    }

    public boolean validityCheck(String item_name, Object o) {
        if (o != null) {
            return true;
        }
        ClassManager.manager.getBugFinder().severe("Was not able to create ShopItem " + item_name + "! The reward object needs to be a list of commands (text lines).");
        return false;
    }

    @Override
    public void enableType() {
    }

    @Override
    public boolean canBuy(Player p, BSBuy buy, boolean message_if_no_success, Object reward, ClickType clickType) {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void giveReward(Player p, BSBuy buy, Object reward, ClickType clickType) {
        List<String> commands = (List<String>) reward;

        if (p.isOp()) {
            executeCommands(p, buy, commands);
        } else {
            p.setOp(true);
            executeCommands(p, buy, commands);
            p.setOp(false);
        }

        if (p.getOpenInventory() != null & !ClassManager.manager.getPlugin().getAPI().isValidShop(p.getOpenInventory())) {
            p.updateInventory();
        }

    }

    private void executeCommands(Player p, BSBuy buy, List<String> commands) {
        for (String s : commands) {
            String command = ClassManager.manager.getStringManager().transform(s, buy, null, null, p);
            PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(p, "/" + command);

            Bukkit.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                p.performCommand(event.getMessage().substring(1));
            }
        }
    }

    @Override
    public String getDisplayReward(Player p, BSBuy buy, Object reward, ClickType clickType) {
        @SuppressWarnings("unchecked")
        List<String> commands = (List<String>) reward;
        String commands_formatted = StringManipulationLib.formatList(commands);
        return ClassManager.manager.getMessageHandler().get("Display.PlayerCommandOp").replace("%commands%", commands_formatted);
    }

    @Override
    public String[] createNames() {
        return new String[]{"playercommandop", "playercommandsop", "opcommands", "opcommand"};
    }

    @Override
    public boolean mightNeedShopUpdate() {
        return true;
    }

}
