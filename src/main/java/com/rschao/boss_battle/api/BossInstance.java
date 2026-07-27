// language: java
package com.rschao.boss_battle.api;

import com.rschao.Plugin;
import com.rschao.api.audio.AudioSelector;
import com.rschao.boss_battle.BossAPI;
import com.rschao.boss_battle.DropsManager;
import com.rschao.boss_battle.InvManager;
import com.rschao.events.definitions.BossChangeEvent;
import com.rschao.events.definitions.BossEndEvent;
import com.rschao.events.definitions.BossStartEvent;
import com.rschao.events.soulEvents;
import com.rschao.plugins.showdowncore.showdownCore.api.runnables.ShowdownScript;
import com.rschao.plugins.showdowncore.showdownCore.api.runnables.registry.ScriptRegistry;
import com.rschao.plugins.techniqueAPI.tech.cooldown.CooldownManager;
import com.rschao.plugins.techniqueAPI.tech.util.PlayerTechniqueManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Barrel;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
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
        if (victoryLoc != null) {
            for (Player p : fighters) {
                p.teleport(victoryLoc);
                p.teleport(victoryLoc);
            }
        }

        BossEndEvent ev = new BossEndEvent(key, getCurrentPhase(), bosses.getFirst(), getFighters().toArray(new Player[0]));
        Bukkit.getServer().getPluginManager().callEvent(ev);
        if (ev.isCancelled()) return;

        // Recompensas básicas tomadas del inventario del boss principal (igual que antes)
        rewards.addAll(Arrays.stream(new ItemStack[0]).toList());

        String rewardCMD = BossAPI.getRewardCommand(config);
        if (rewardCMD != null) {
            for (Player p : fighters) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), rewardCMD.replace("%player%", p.getName()));
            }
        }

        // Si hay drops configurados globalmente para este boss (caso no-superboss)
        List<ItemStack> configuredDrops = DropsManager.loadDropsFromConfig(key);


        // No es superboss: comportamiento previo
        if (configuredDrops != null && !configuredDrops.isEmpty()) {
            for (Player fighter : fighters) {
                ItemStack shulkerBox = DropsManager.createShulkerBoxWithDrops(configuredDrops);
                Map<Integer, ItemStack> leftover = fighter.getInventory().addItem(shulkerBox);
                for (ItemStack l : leftover.values()) {
                    fighter.getWorld().dropItemNaturally(fighter.getLocation(), l);
                }
            }
        } else {
            handleDrops(bosses.getFirst(), rewards);
        }
    }

    public void start() {
        active = true;

        // Determinar configuración y fase efectivas.
        FileConfiguration effectiveConfig = this.config;
        int effectivePhase = this.currentPhase;


        Location loc = BossAPI.getLocation(effectiveConfig, effectivePhase);
        if(loc == null) return;

        BossStartEvent ev = new BossStartEvent(key, 1, bosses.getFirst(), fighters.toArray(new Player[0]));
        if(getCurrentPhase() == 1){
            Bukkit.getServer().getPluginManager().callEvent(ev);
        }
        BossChangeEvent event = new BossChangeEvent(key, getCurrentPhase(), bosses.getFirst(), getFighters().toArray(new Player[0]));
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (ev.isCancelled() || event.isCancelled()) return;

        teleportToLocation(loc);

        String kitName = BossAPI.getKit(effectiveConfig, effectivePhase);
        if(kitName != null){
            for(Player p: bosses){
                loadBossKit(kitName, p);
                //set health and reset techniques :)
                p.setHealth(p.getAttribute(Attribute.MAX_HEALTH).getBaseValue());
                CooldownManager.removeAllCooldowns(p);
            }
        }

        String music = BossAPI.getMusic(effectiveConfig, effectivePhase);
        if(music != null) AudioSelector.PlayBossAudio(music, bosses.toArray(new Player[0]));
        if(music != null) AudioSelector.PlayBossAudio(music, fighters.toArray(new Player[0]));

        List<String> soulStrings = BossAPI.getAddon(effectiveConfig, effectivePhase, "souls");
        if(soulStrings.isEmpty()) soulStrings = List.of("-1", "-1");
        int soul1 = Integer.parseInt(soulStrings.get(0));
        int soul2 = (soulStrings.size() > 1) ? Integer.parseInt(soulStrings.get(1)) : -1;

        for(Player p: bosses){
            soulEvents.setSouls(p, soul1, soul2);
        }

        List<String> dialogue = BossAPI.getDialogue(effectiveConfig, effectivePhase);
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
        ShowdownScript<?> script = ScriptRegistry.getScript("boss_reward", key.toLowerCase());
        if(script == null) return null;
        return (ShowdownScript<List<ItemStack>>) script;
    }

    /**
     * Empaqueta `items` en barriles (27 slots cada uno) y, si hay varios barriles,
     * los agrupa en barriles exteriores (anidamiento) hasta reducir el número de contenedores.
     * Devuelve la lista final de ItemStack que representan barriles listos para dar a jugadores.
     */
    private List<ItemStack> packIntoBarrels(List<ItemStack> items) {
        final int CAP = 27; // slots por barrel
        List<ItemStack> currentLevel = new ArrayList<>();

        // Crear barriles "interiores" llenos con los items
        int idx = 0;
        while (idx < items.size()) {
            ItemStack barrel = new ItemStack(Material.BARREL);
            BlockStateMeta meta = (BlockStateMeta) barrel.getItemMeta();
            Barrel bs = (Barrel) meta.getBlockState();
            Inventory inv = bs.getInventory();
            int slot = 0;
            while (slot < CAP && idx < items.size()) {
                ItemStack it = items.get(idx++);
                if (it != null) inv.setItem(slot++, it.clone());
            }
            bs.update();
            meta.setBlockState(bs);
            barrel.setItemMeta(meta);
            currentLevel.add(barrel);
        }

        // Si solo hay 0 o 1 barriles interiores, devolvemos tal cual
        if (currentLevel.size() <= 1) return currentLevel;

        // Anidar: empaquetar barriles actuales dentro de barriles "exteriores" (CAP por barrel)
        // Repetir hasta que no se pueda reducir más (o quede 1 contenedor)
        while (currentLevel.size() > 1) {
            List<ItemStack> nextLevel = new ArrayList<>();
            int i = 0;
            while (i < currentLevel.size()) {
                ItemStack outer = new ItemStack(Material.BARREL);
                BlockStateMeta meta = (BlockStateMeta) outer.getItemMeta();
                Barrel bs = (Barrel) meta.getBlockState();
                Inventory inv = bs.getInventory();
                int slot = 0;
                while (slot < CAP && i < currentLevel.size()) {
                    ItemStack inner = currentLevel.get(i++);
                    inv.setItem(slot++, inner.clone()); // colocar el item "barril" dentro
                }
                bs.update();
                meta.setBlockState(bs);
                outer.setItemMeta(meta);
                nextLevel.add(outer);
            }
            // Si nextLevel no reduce la cantidad, salimos para evitar loop infinito
            if (nextLevel.size() >= currentLevel.size()) {
                return currentLevel;
            }
            currentLevel = nextLevel;
        }

        return currentLevel;
    }
}
