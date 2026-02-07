// language: java
package com.rschao.boss_battle.api;

import com.rschao.Plugin;
import com.rschao.api.audio.AudioSelector;
import com.rschao.boss_battle.BossAPI;
import com.rschao.boss_battle.InvManager;
import com.rschao.events.definitions.BossChangeEvent;
import com.rschao.events.definitions.BossEndEvent;
import com.rschao.events.definitions.BossStartEvent;
import com.rschao.events.soulEvents;
import com.rschao.plugins.showdowncore.showdownCore.api.runnables.ShowdownScript;
import com.rschao.plugins.showdowncore.showdownCore.api.runnables.registry.ScriptRegistry;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.IntStream;

public class BossInstance {
    private final FileConfiguration config;
    private final String key;
    private final List<Player> bosses; // players that are bosses (first = big boss)
    private final List<Player> fighters; // players fighting them
    private int currentPhase = 1;
    private boolean active = false;
    private List<ItemStack> rewards = new ArrayList<>();

    public BossInstance(String key, FileConfiguration config, List<Player> bosses, List<Player> fighters){
        this.key = key;
        this.config = config;
        this.bosses = bosses;
        this.fighters = fighters;
    }


    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public int getCurrentPhase() {
        return currentPhase;
    }

    public String getKey() {
        return key;
    }

    public boolean containsBoss(Player p) { return bosses.contains(p); }
    public boolean containsFighter(Player p) { return fighters.contains(p); }

    public FileConfiguration getBossConfig() {
        return config;
    }

    public List<Player> getBosses() { return bosses; }
    public List<Player> getFighters() { return fighters; }

    public void removeFighter(Player p) { fighters.remove(p); }
    public void removeBoss(Player p) { bosses.remove(p); }

    public void end() {
        active = false;
        Location victoryLoc = BossAPI.getVictoryLocation(config);
        if(victoryLoc != null) {
            for (Player p : fighters) {
                p.teleport(victoryLoc);
            }
        }
        BossEndEvent ev = new BossEndEvent(key, getCurrentPhase(), bosses.getFirst(), getFighters().toArray(new Player[0]));
        Bukkit.getServer().getPluginManager().callEvent(ev);
        if(ev.isCancelled()) return;
        rewards.addAll(Arrays.stream(bosses.getFirst().getInventory().getContents()).toList());
        handleDrops(bosses.getFirst(), rewards);
    }

    public void start() {
        active = true;
        Location loc = BossAPI.getLocation(config, currentPhase);
        if(loc == null) return;
        BossStartEvent ev = new BossStartEvent(key, 1, bosses.getFirst(), fighters.toArray(new Player[0]));
        if(getCurrentPhase() == 1){
            Bukkit.getServer().getPluginManager().callEvent(ev);
        }
        BossChangeEvent event = new BossChangeEvent(key, getCurrentPhase(), bosses.getFirst(), getFighters().toArray(new Player[0]));
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (ev.isCancelled() || event.isCancelled()) return;
        teleportToLocation(loc);
        String kitName = BossAPI.getKit(config, currentPhase);
        if(kitName != null){
            for(Player p: bosses){
                loadBossKit(kitName, p);
            }
        }
        String music = BossAPI.getMusic(config, currentPhase);
        if(music != null) AudioSelector.PlayBossAudio(music, bosses.toArray(new Player[0]));
        if(music != null) AudioSelector.PlayBossAudio(music, fighters.toArray(new Player[0]));
        List<String> soulStrings = BossAPI.getAddon(config, currentPhase, "souls");
        if(soulStrings != null && soulStrings.size() >= 2){
            int soul1 = Integer.parseInt(soulStrings.get(0));
            int soul2 = Integer.parseInt(soulStrings.get(1));
            for(Player p: bosses){
                soulEvents.setSouls(p, soul1, soul2);
            }
        }
        List<String> dialogue = BossAPI.getDialogue(config, currentPhase);
        if(dialogue != null && !dialogue.isEmpty()){
            new BukkitRunnable() {
                int index = 0;
                @Override
                public void run() {
                    if(index >= dialogue.size()) {
                        cancel();
                        return;
                    }
                    String line = dialogue.get(index);
                    for(Player p: bosses){
                        p.sendMessage(line);
                    }
                    for(Player p: fighters){
                        p.sendMessage(line);
                    }
                    index++;
                }
            }.runTaskTimer(Plugin.getPlugin(Plugin.class), 0L, 50L);
        }

    }

    public void advancePhase() {
        currentPhase++;
        start();
    }



    void teleportToLocation(Location loc){
        World w = loc.getWorld();
        if(w == null) return;
        for(Player p: bosses){
            p.teleport(loc);
        }
        for(Player p: fighters){
            p.teleport(loc);
        }
    }

    void loadBossKit(String kitName, Player p){
        InvManager.LoadInventory(p, kitName);
    }
    void handleDrops(Player p, List<ItemStack> drops){
        ShowdownScript<List<ItemStack>> script = getRewardScript(key);
        List<ItemStack> finalDrops = drops;

        if (script != null) {
            List<ItemStack> res = executeScript(script, drops);
            if (res != null) finalDrops = res;
        } else {
            ShowdownScript<List<ItemStack>> base = BossAPI.baseRewards(drops);
            List<ItemStack> res = executeScript(base, drops);
            if (res != null) finalDrops = res;
        }

        if (finalDrops == null) finalDrops = drops;

        // Dar ítems al jugador y soltar sobrantes en el suelo
        for (ItemStack item : new ArrayList<>(finalDrops)) {
            if (item == null) continue;
            Map<Integer, ItemStack> leftover = p.getInventory().addItem(item);
            if (!leftover.isEmpty()) {
                for (ItemStack l : leftover.values()) {
                    p.getWorld().dropItemNaturally(p.getLocation(), l);
                }
            }
        }
    }

    private static <T> T executeScript(ShowdownScript<T> script, Object... args) {
        script.setArgs(args);
        return script.run();
    }

    static ShowdownScript<List<ItemStack>> getRewardScript(String key){
        ShowdownScript<?> script = ScriptRegistry.getScript("boss_reward", key);
        if(script == null) return null;
        return (ShowdownScript<List<ItemStack>>) script;
    }
}
