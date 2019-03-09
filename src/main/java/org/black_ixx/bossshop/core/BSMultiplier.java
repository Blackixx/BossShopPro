package org.black_ixx.bossshop.core;

import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.entity.Player;

public class BSMultiplier {

    public final static int RANGE_ALL = 0;
    public final static int RANGE_PRICE_ONLY = 1;
    public final static int RANGE_REWARD_ONLY = 2;

    private String permission = "Permission.Node";
    private BSPriceType type = BSPriceType.Nothing;
    private double multiplier = 1.0;
    private int range = RANGE_ALL;


    public BSMultiplier(String config_line) {
        String[] parts = config_line.split(":", 4);

        if (parts.length != 3 && parts.length != 4) {
            ClassManager.manager.getBugFinder().warn("Invalid Multiplier Group Line... \"" + config_line + "\"! It should look like this: \"Permission.Node:<type>:<multiplier>:<price/reward/both>\"");
            return;
        }

        String permission = parts[0].trim();

        if (parts[1].trim().equalsIgnoreCase("<type>")) {
            return;
        }


        BSPriceType type = BSPriceType.detectType(parts[1].trim());
        double multiplier = 1.0;
        int range = RANGE_ALL;

        if (type == null || !type.supportsMultipliers()) {
            ClassManager.manager.getBugFinder().warn("Invalid Multiplier Group Line... \"" + config_line + "\"! It should look like this: \"Permission.Node:<type>:<multiplier>:<price/reward/both>\". '" + parts[1].trim() + "' does not support multipliers!");
            return;
        }
        try {
            multiplier = Double.parseDouble(parts[2].trim());
        } catch (Exception e) {
            ClassManager.manager.getBugFinder().warn("Invalid Multiplier Group Line... \"" + config_line + "\"! It should look like this: \"Permission.Node:<type>:<multiplier>:<price/reward/both>\". '" + parts[2].trim() + "' is no valid multiplier... What you can use instead (examples): 0.25, 0.3, 0.75, 1.0, 1.5, 2.0 etc.!");
            return;
        }


        if (parts.length >= 4) {
            String rs = parts[3].trim();
            if (rs.equalsIgnoreCase("price")) {
                range = RANGE_PRICE_ONLY;
            } else if (rs.equalsIgnoreCase("reward")) {
                range = RANGE_REWARD_ONLY;
            }
        }

        setup(permission, type, multiplier, range);

    }

    public BSMultiplier(String permission, BSPriceType type, double multiplier, int range) {
        setup(permission, type, multiplier, range);
    }

    public void setup(String permission, BSPriceType type, double multiplier, int range) {
        this.permission = permission;
        this.type = type;
        this.multiplier = multiplier;
        this.range = range;
    }

    public boolean isValid() {
        return type.supportsMultipliers();
    }

    public BSPriceType getType() {
        return type;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public String getPermission() {
        return permission;
    }

    public boolean hasPermission(Player p) {
        return p.hasPermission(permission);
    }

    public double calculateValue(Player p, BSPriceType type, double d, int range) {
        if (isMultiplierActive(p, type, range)) {
            switch (this.range) {
                case RANGE_ALL: //Multiplier supports both types -> buy price is multiplied and sell reward is divided
                    switch (range) {
                        case RANGE_ALL:
                            return d * this.multiplier;
                        case RANGE_PRICE_ONLY:
                            return d * this.multiplier;
                        case RANGE_REWARD_ONLY:
                            return d / this.multiplier;
                    }

                    //If Multiplier supports one of both types in both cases the value is multiplied
                case RANGE_REWARD_ONLY:
                    if (range == RANGE_ALL || range == RANGE_REWARD_ONLY) {
                        d *= this.multiplier;
                    }
                    break;

                case RANGE_PRICE_ONLY:
                    if (range == RANGE_ALL || range == RANGE_PRICE_ONLY) {
                        d *= this.multiplier;
                    }
                    break;

            }
        }

        return d;
    }


    public boolean isMultiplierActive(Player p, BSPriceType type, int range) {
        if (this.type == type) {
            if (hasPermission(p)) {
                if (isInRange(range)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isInRange(int range) {
        switch (range) {
            case RANGE_ALL:
                return true;
            case RANGE_REWARD_ONLY:
                return this.range == RANGE_REWARD_ONLY || this.range == RANGE_ALL;
            case RANGE_PRICE_ONLY:
                return this.range == RANGE_PRICE_ONLY || this.range == RANGE_ALL;
        }
        return false;
    }

    public double calculateWithMultiplier(double d) {
        return d * multiplier;
    }

    public int calculateWithMultiplier(int d) {
        return (int) (d * multiplier);
    }


}
