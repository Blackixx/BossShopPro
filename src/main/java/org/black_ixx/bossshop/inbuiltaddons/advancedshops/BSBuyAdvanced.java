package org.black_ixx.bossshop.inbuiltaddons.advancedshops;


import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSInputType;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.conditions.BSCondition;
import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.core.rewards.BSRewardType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Map;

public class BSBuyAdvanced extends BSBuy {

    private Map<ClickType, ActionSet> actions;


    public BSBuyAdvanced(BSRewardType rewardT, BSPriceType priceT, Object reward, Object price, String msg, int location, String permission, String name, BSCondition condition, BSInputType inputtype, String inputmessage, Map<ClickType, ActionSet> actions) {
        super(rewardT, priceT, reward, price, msg, location, permission, name, condition, inputtype, inputmessage);
        this.actions = actions;
    }

    @Override
    public BSRewardType getRewardType(ClickType clicktype) {
        if (actions != null) {
            if (actions.containsKey(clicktype)) {
                return actions.get(clicktype).getRewardType();
            }
        }
        return super.getRewardType(clicktype);
    }

    @Override
    public BSPriceType getPriceType(ClickType clicktype) {
        if (actions != null) {
            if (actions.containsKey(clicktype)) {
                return actions.get(clicktype).getPriceType();
            }
        }
        return super.getPriceType(clicktype);
    }

    @Override
    public Object getReward(ClickType clicktype) {
        if (actions != null) {
            if (actions.containsKey(clicktype)) {
                return actions.get(clicktype).getReward();
            }
        }
        return super.getReward(clicktype);
    }

    @Override
    public Object getPrice(ClickType clicktype) {
        if (actions != null) {
            if (actions.containsKey(clicktype)) {
                return actions.get(clicktype).getPrice();
            }
        }
        return super.getPrice(clicktype);
    }

    @Override
    public String getMessage(ClickType clicktype) {
        if (actions != null) {
            if (actions.containsKey(clicktype)) {
                return actions.get(clicktype).getMessage();
            }
        }
        return super.getMessage(clicktype);
    }

    @Override
    public BSInputType getInputType(ClickType clicktype) {
        if (actions != null) {
            if (actions.containsKey(clicktype)) {
                return actions.get(clicktype).getInputType();
            }
        }
        return super.getInputType(clicktype);
    }

    @Override
    public String getInputText(ClickType clicktype) {
        if (actions != null) {
            if (actions.containsKey(clicktype)) {
                return actions.get(clicktype).getInputText();
            }
        }
        return super.getInputText(clicktype);
    }

    @Override
    public boolean isExtraPermissionGroup(ClickType clicktype) {
        if (actions != null) {
            if (actions.containsKey(clicktype)) {
                return actions.get(clicktype).isExtraPermissionGroup();
            }
        }
        return super.isExtraPermissionGroup(clicktype);
    }

    @Override
    public String getExtraPermission(ClickType clicktype) {
        if (actions != null) {
            if (actions.containsKey(clicktype)) {
                return actions.get(clicktype).getExtraPermission();
            }
        }
        return super.getExtraPermission(clicktype);
    }

    @Override
    public String transformMessage(String msg, BSShop shop, Player p) {
        msg = super.transformMessage(msg, shop, p);
        if (msg == null) {
            return null;
        }
        if (msg.length() == 0) {
            return msg;
        }


        if (actions != null) {
            for (ClickType clicktype : actions.keySet()) {
                ActionSet action = actions.get(clicktype);
                String s = clicktype.name().toLowerCase();

                String tp = "%price_" + s + "%";
                String tr = "%reward_" + s + "%";

                if (msg.contains(tp) || msg.contains(tr)) {
                    String rewardMessage = action.getRewardType().isPlayerDependend(this, clicktype) ? null : action.getRewardType().getDisplayReward(p, this, action.getReward(), clicktype);
                    String priceMessage = action.getPriceType().isPlayerDependend(this, clicktype) ? null : action.getPriceType().getDisplayPrice(p, this, action.getPrice(), clicktype);


                    if (shop != null) { //Does shop need to be customizable and is not already?
                        if (!shop.isCustomizable()) {
                            boolean has_pricevariable = (msg.contains(tp) && (action.getPriceType().isPlayerDependend(this, clicktype)));
                            boolean has_rewardvariable = (msg.contains(tr) && (action.getRewardType().isPlayerDependend(this, clicktype)));
                            if (has_pricevariable || has_rewardvariable) {
                                shop.setCustomizable(true);
                                shop.setDisplaying(true);
                            }
                        }
                    }

                    boolean possibly_customizable = shop == null ? true : shop.isCustomizable();
                    if (possibly_customizable) {
                        if (p != null) { //When shop is customizable, the variables needs to be adapted to the player
                            rewardMessage = action.getRewardType().getDisplayReward(p, this, action.getReward(), clicktype);
                            priceMessage = action.getPriceType().getDisplayPrice(p, this, action.getPrice(), clicktype);
                        }
                    }


                    if (priceMessage != null && priceMessage != "" && priceMessage.length() > 0) {
                        msg = msg.replace(tp, priceMessage);
                    }
                    if (rewardMessage != null && rewardMessage != "" && rewardMessage.length() > 0) {
                        msg = msg.replace(tr, rewardMessage);
                    }
                }

            }
        }

        return msg;
    }

}
