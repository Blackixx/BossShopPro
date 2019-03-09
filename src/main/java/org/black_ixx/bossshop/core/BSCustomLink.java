package org.black_ixx.bossshop.core;

import org.bukkit.entity.Player;


public class BSCustomLink {

    private BSCustomActions actions;
    private int id;

    public BSCustomLink(int id, BSCustomActions actions) {
        this.actions = actions;
        this.id = id;
    }

    public void doAction(Player p) {
        actions.customAction(p, id);
    }


}
