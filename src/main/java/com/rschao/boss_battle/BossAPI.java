package com.rschao.boss_battle;

import com.rschao.boss_battle.api.BossHandler;
import com.rschao.boss_battle.api.BossInstance;
import com.rschao.plugins.showdowncore.showdownCore.api.runnables.ShowdownScript;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BossAPI {
    private static final List<BossInstance> running = Collections.synchronizedList(new ArrayList<>());


    public static List<String> getDialogue(FileConfiguration config, int phase){
        ConfigurationSection s = BossHandler.getPhaseData(config, phase);
        if(s == null || !s.contains("dialogue")) return null;
        return s.getStringList("dialogue");
    }

    public static String getKit(FileConfiguration config, int phase){
        ConfigurationSection s = BossHandler.getPhaseData(config, phase);
        if(s == null || !s.contains("kit")) return null;
        return s.getString("kit");
    }

    public static Location getLocation(FileConfiguration config, int phase){
        ConfigurationSection s = BossHandler.getPhaseData(config, phase);
        if(s == null || !s.contains("x") || !s.contains("y") || !s.contains("z") || !s.contains("name")) return null;
        String world = s.getString("name");
        double x = s.getDouble("x");
        double y = s.getDouble("y");
        double z = s.getDouble("z");
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    public static Location getVictoryLocation(FileConfiguration config){
        ConfigurationSection s = config.getConfigurationSection("boss.victory.tp");
        if(s == null || !s.contains("x") || !s.contains("y") || !s.contains("z") || !s.contains("world")) return null;
        String world = s.getString("world");
        double x = s.getDouble("x");
        double y = s.getDouble("y");
        double z = s.getDouble("z");
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    public static List<String> getAddon(FileConfiguration config, int phase, String addon){
        ConfigurationSection s = BossHandler.getPhaseData(config, phase);
        if(s == null || !s.contains(addon)) return new ArrayList<>();
        return s.getStringList(addon);
    }

    public static String getMusic(FileConfiguration config, int phase){
        ConfigurationSection s = BossHandler.getPhaseData(config, phase);
        if(s == null || !s.contains("music")) return null;
        return s.getString("music");
    }

    public static BossInstance startFight(String key, FileConfiguration configuration, List<Player> bosses, List<Player> fighters) {
        BossInstance instance = new BossInstance(key, configuration, bosses, fighters);
        running.add(instance);
        instance.start();
        return instance;
    }

    public static Optional<BossInstance> findByBoss(Player p) {
        return running.stream().filter(bi -> bi.containsBoss(p)).findFirst();
    }

    public static void stopInstance(BossInstance bi) {
        bi.end();
        running.remove(bi);
    }
    public static List<BossInstance> getInstancesWithFighter(Player p) {
        List<BossInstance> result = new ArrayList<>();
        for (BossInstance bi : running) {
            if (bi.containsBoss(p) || bi.containsFighter(p)) {
                result.add(bi);
            }
        }
        return result;
    }
    public static List<BossInstance> getInstancesWithPlayer(Player p) {
        List<BossInstance> result = new ArrayList<>();
        for (BossInstance bi : running) {
            if (bi.containsBoss(p)) {
                result.add(bi);
            }
        }
        return result;
    }


    public static ShowdownScript<List<ItemStack>> baseRewards(List<ItemStack> drop){
        ShowdownScript<List<ItemStack>> script = new ShowdownScript<>((args) ->{
            List<ItemStack> drops = (List<ItemStack>) args[0];
            List<String> keep = List.of("BOOK", "DRAGON", "BOX", "HOOK", "ELYTRA", "PAPER", "WART", "POWDER");
            for(ItemStack dropped : drops){
                if(keep.stream().anyMatch(s -> dropped.getType().name().contains(s))){
                    continue;
                }
                drops.remove(drop);
            }
            return drops;
        });
        script.setArgs(drop);
        return script;
    }
}
