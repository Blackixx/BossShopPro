package org.black_ixx.bossshop.misc;

import org.bukkit.enchantments.Enchantment;

public class Enchant {

    private Enchantment type;
    private int lvl;

    public Enchant(Enchantment type, int lvl) {
        this.type = type;
        this.lvl = lvl;
    }

    public Enchantment getType() {
        return type;
    }

    public int getLevel() {
        return lvl;
    }

}
