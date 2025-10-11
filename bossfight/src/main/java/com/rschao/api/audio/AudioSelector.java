package com.rschao.api.audio;

import com.rschao.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class AudioSelector {

    public static void PlayBossAudio(String audio, Player[] players){
        stopAudio(players);
        File file = new File(Plugin.getPlugin(Plugin.class).getDataFolder(), "audio.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = config.getConfigurationSection("audio");
        for(String key : section.getKeys(false)){
            if(key.equalsIgnoreCase(audio)){
                String path = section.getString(key + ".path");
                boolean loop = section.getBoolean(key + ".loop");
                if (path.length() < 3) {
                    return;
                }
                boolean local = section.getBoolean(key + ".local");
                if(local){
                    runAudioLocal(path, loop, players);
                } else {
                    runAudio(path, loop, players);
                }
                break;
            }
        }
    }

    public static void PlayUserAudio(String audio, Player player, String user){
        stopAudio(player);
        File file = new File(Plugin.getPlugin(Plugin.class).getDataFolder(), "audio_user.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = config.getConfigurationSection("audio." + user);
        for(String key : section.getKeys(false)){
            if(key.equalsIgnoreCase(audio)){
                String path = section.getString(key + ".path");
                boolean loop = section.getBoolean(key + ".loop");
                if (path.length() < 3) {
                    return;
                }
                boolean local = section.getBoolean(key + ".local");
                if(local){
                    runAudioLocal(path, loop, player);
                } else {
                    runAudio(path, loop, player);
                }
                break;
            }
        }
    }


    static void runAudio(String path, boolean loop, Player[] players){
        for(Player p : players){
            if(p != null && p.isOnline()){
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "oa play "+ p.getName() + " " + path + " {loop:" + loop + "}");
            }
        }
    }
    static void runAudio(String path, boolean loop, Player p){
        if(p != null && p.isOnline()){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "oa play "+ p.getName() + " " + path + " {loop:" + loop + "}");
        }
    }


    static void runAudioLocal(String path, boolean loop, Player[] players){
        for(Player p : players){
            if(p != null && p.isOnline()){
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "oa play "+ p.getName() + " files:" + path + ".mp3 {loop:" + loop + "}");
            }
        }
    }
    static void runAudioLocal(String path, boolean loop, Player p){
        if(p != null && p.isOnline()){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "oa play "+ p.getName() + " files:" + path + ".mp3 {loop:" + loop + "}");
        }
    }
    static void stopAudio(Player[] players){
        for(Player p : players){
            if(p != null && p.isOnline()){
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "oa stop "+ p.getName());
            }
        }
    }
    static void stopAudio(Player p){

        if(p != null && p.isOnline()){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "oa stop "+ p.getName());
        }
    }
}
