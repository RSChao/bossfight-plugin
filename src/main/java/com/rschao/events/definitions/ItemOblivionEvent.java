package com.rschao.events.definitions;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class ItemOblivionEvent extends Event {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final Player player;
    private final Player cause;
    private final ItemStack item;
    public ItemOblivionEvent(Player player, Player oblivioner, ItemStack oblivionItem) {
        this.player = player;
        this.cause = oblivioner;
        this.item = oblivionItem;
    }
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public ItemStack getItem() {
        return item;
    }

    public Player getCausePlayer() {
        return cause;
    }

    public Player getPlayer() {
        return player;
    }
}
