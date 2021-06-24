package org.black_ixx.bossshop.inbuiltaddons.advancedshops;

import org.black_ixx.bossshop.core.BSInputType;
import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.core.rewards.BSRewardType;
import org.black_ixx.bossshop.managers.ClassManager;

public class ActionSet {


    private BSRewardType rewardT;
    private BSPriceType priceT;

    private Object reward;
    private Object price;

    private String msg;
    private String permission;

    private BSInputType inputtype;
    private String inputtext;

    private boolean perm_is_group;


    public ActionSet(BSRewardType rewardType, BSPriceType priceType, Object reward, Object price, String msg, String extrapermission, BSInputType inputtype, String inputtext) {
        this.rewardT = rewardType;
        this.priceT = priceType;
        this.reward = reward;
        this.price = price;
        this.msg = msg;
        this.inputtype = inputtype;
        this.inputtext = inputtext;

        if (extrapermission != null && extrapermission != "") {
            this.permission = extrapermission;
            if (permission.startsWith("[") && permission.endsWith("]")) {
                if (permission.length() > 2) {
                    String group = permission.substring(1, permission.length() - 1);
                    if (group != null) {
                        ClassManager.manager.getSettings().setVaultEnabled(true);
                        this.permission = group;
                        perm_is_group = true;
                    }
                }
            }
        }
    }


    public BSRewardType getRewardType() {
        return rewardT;
    }

    public BSPriceType getPriceType() {
        return priceT;
    }

    public Object getReward() {
        return reward;
    }

    public Object getPrice() {
        return price;
    }

    public String getMessage() {
        return msg;
    }

    public String getExtraPermission() {
        return permission;
    }

    public boolean isExtraPermissionGroup() {
        return perm_is_group;
    }

    public BSInputType getInputType() {
        return inputtype;
    }

    public String getInputText() {
        return inputtext;
    }

}
