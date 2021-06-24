package org.black_ixx.bossshop.inbuiltaddons.logictypes;

import org.black_ixx.bossshop.core.rewards.BSRewardType;

public class BSRewardPart {

    private BSRewardType rewardtype;
    private Object reward;

    public BSRewardPart(BSRewardType rewardtype, Object reward) {
        this.reward = reward;
        this.rewardtype = rewardtype;
    }

    public BSRewardType getRewardType() {
        return rewardtype;
    }

    public Object getReward() {
        return reward;
    }
}
