package com.rschao.events.definitions;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.rschao.Plugin;

public class BossChangeEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    public String bossName;
    public int phase;
    public boolean isCancelled;
    public Player boss;
    public Player[] bossPlayers;
    public BossChangeEvent(String bossName, int phase, Player boss, Player[] bossPlayers) {
        this.bossName = bossName;
        this.phase = phase;
        this.boss = boss;
        this.isCancelled = false;
        this.bossPlayers = bossPlayers;
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
    
    public String getWorldName(){
        //access the boss config and get the world name
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(Plugin.getPlugin(Plugin.class).getDataFolder(), bossName + ".yml"));
        return config.getString("boss.worlds.1.name");
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
