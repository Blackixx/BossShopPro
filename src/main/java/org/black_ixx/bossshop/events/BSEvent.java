package org.black_ixx.bossshop.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

public abstract class BSEvent extends Event {

    public BSEvent(){
        super(!Bukkit.getServer().isPrimaryThread());
    }
}
