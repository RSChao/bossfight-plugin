package com.rschao.events.definitions;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class InventoryRestoreEvent extends Event {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    String key;
    Player player;
    public InventoryRestoreEvent(Player player, String key) {
        this.key = key;
        this.player = player;
    }
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
    public String getKey() {
        return key;
    }
    public Player getPlayer() {
        return player;
    }
    public String getEventAsString() {
        return "InventoryBackupEvent{" + "key='" + key + '\'' + ", player=" + player.getName() + '}';
    }
}
