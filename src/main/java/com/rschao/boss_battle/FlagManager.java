package com.rschao.boss_battle;

import com.rschao.Plugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class FlagManager {
    public static void SaveFlag(String key, Player p){
        File file;
        file = new File(Plugin.getPlugin(Plugin.class).getDataFolder(), "flags.yml");
        FileConfiguration config = new YamlConfiguration();
        try{
            config.load(file);
        }
        catch(Exception e){
            return;
        }
        config.set(p.getName() + "." + key, true);

        try{
            config.save(file);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public static boolean LoadFlag(String key, Player p){
        File file;
        file = new File(Plugin.getPlugin(Plugin.class).getDataFolder(), "flags.yml");
        FileConfiguration config = new YamlConfiguration();

        try{
            config.load(file);
        }
        catch(Exception e){
            return false;
        }
        return config.getBoolean(p.getName() + "." + key, false);
    }
}
