package com.rschao.events.definitions;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerPopHeartEvent extends Event{
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    Player player;
    int t;
    public PlayerPopHeartEvent(Player player, int t) {
        this.player = player;
        this.t = t;
    }
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
    public Player getPlayer() {
        return player;
    }
    public int getTimesUsed() {
        return t;
    }
}
