package org.black_ixx.bossshop.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BSRegisterTypesEvent extends BSEvent {

    private static final HandlerList handlers = new HandlerList();


    public BSRegisterTypesEvent() {
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}