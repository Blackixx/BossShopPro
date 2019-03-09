package org.black_ixx.bossshop.inbuiltaddons.logictypes;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.rewards.BSRewardType;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

public class BSRewardTypeAnd extends BSRewardType {

    @Override
    public Object createObject(Object o, boolean force_final_state) {
        List<BSRewardPart> rewardparts = new ArrayList<BSRewardPart>();

        ConfigurationSection rewards = (ConfigurationSection) o;
        for (int i = 1; rewards.contains("RewardType" + i); i++) {
            String rewardType = rewards.getString("RewardType" + i);
            Object rewardObject = rewards.get("Reward" + i);

            BSRewardType rewardT = BSRewardType.detectType(rewardType);

            if (rewardT == null) {
                ClassManager.manager.getBugFinder().severe("Invalid RewardType '" + rewardType + "' inside reward list of shopitem with rewardtype AND.");
                ClassManager.manager.getBugFinder().severe("Valid RewardTypes:");
                for (BSRewardType type : BSRewardType.values()) {
                    ClassManager.manager.getBugFinder().severe("-" + type.name());
                }
                continue;
            }

            Object rewardO = rewardT.createObject(rewardObject, true);

            if (!rewardT.validityCheck("?", rewardO)) {
                ClassManager.manager.getBugFinder().severe("Invalid Reward '" + rewardO + "' (RewardType= " + rewardType + ") inside reward list of shopitem with rewardtype AND.");
                continue;
            }
            rewardT.enableType();

            BSRewardPart part = new BSRewardPart(rewardT, rewardO);
            rewardparts.add(part);

        }
        return rewardparts;
    }

    @Override
    public boolean validityCheck(String item_name, Object o) {
        if (o != null) {
            return true;
        }
        ClassManager.manager.getBugFinder().severe("Was not able to create ShopItem " + item_name + "! The reward object needs to be a list of reward-blocks. Every rewardblock needs to contain reward and rewardtype.");
        return false;
    }

    @Override
    public void enableType() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean canBuy(Player p, BSBuy buy, boolean message_if_no_success, Object reward, ClickType clickType) {
        List<BSRewardPart> rewardparts = (List<BSRewardPart>) reward;
        for (BSRewardPart part : rewardparts) {
            if (!part.getRewardType().canBuy(p, buy, message_if_no_success, part.getReward(), clickType)) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void giveReward(Player p, BSBuy buy, Object reward, ClickType clickType) {
        List<BSRewardPart> rewardparts = (List<BSRewardPart>) reward;
        for (BSRewardPart part : rewardparts) {
            part.getRewardType().giveReward(p, buy, part.getReward(), clickType);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getDisplayReward(Player p, BSBuy buy, Object reward, ClickType clickType) {
        String sep = ClassManager.manager.getMessageHandler().get("Main.ListAndSeparator");
        String s = "";

        List<BSRewardPart> rewardparts = (List<BSRewardPart>) reward;
        for (int i = 0; i < rewardparts.size(); i++) {
            BSRewardPart part = rewardparts.get(i);
            s += part.getRewardType().getDisplayReward(p, buy, part.getReward(), clickType) + (i < rewardparts.size() - 1 ? sep : "");
        }
        return s;
    }

    @Override
    public String[] createNames() {
        return new String[]{"and"};
    }

    @Override
    public boolean mightNeedShopUpdate() {
        return true;
    }

}
