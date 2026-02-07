package com.rschao.events.definitions;

import com.rschao.boss_battle.api.BossHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BossEndEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    public String bossName;
    public int phase;
    public boolean isCancelled;
    public Player boss;
    public Player[] bossPlayers;
    public FileConfiguration config;
    public BossEndEvent(String bossName, int phase, Player boss, Player[] bossPlayers) {
        this.bossName = bossName;
        this.phase = phase;
        this.boss = boss;
        this.isCancelled = false;
        this.bossPlayers = bossPlayers;
        config = BossHandler.loadBoss(bossName);
    }
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
    public int getPhase() {
        return phase;
    }
    public String getBossName() {
        return bossName;
    }
    @Override
    public boolean isCancelled() {
        return isCancelled;
    }
    @Override
    public void setCancelled(boolean arg0) {
        isCancelled = arg0;
    }
    public Player getBossPlayer(){
        return boss;
    }
    public String getEventAsString(boolean json) {
        if(json){
            return "BossChangeEvent{" +
                "bossName='" + bossName + "\', phase=" + phase +", isCancelled=" + isCancelled + ", boss=" + boss + '}';
        }
        else{
            String text = "";
            text += "BossName: " + bossName + "\n";
            text += "Phase: " + phase + "\n";
            text += "Boss: " + boss.getName() + "\n";
            text += "BossPlayers: ";
            for(Player player : bossPlayers){
                text += player.getName() + "\n";
            }
            return text;
        }
    }
    public Player[] getBossPlayers() {
        return bossPlayers;
    }
}
