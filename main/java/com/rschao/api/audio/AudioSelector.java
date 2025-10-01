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
        /*switch (audio){
            case "bs_final_1":
                runAudio(AudioType.FALLEN_ANGEL, players);
                break;
            case "bs_final_2":
                runAudio(AudioType.CLOSING_BATTLE, players);
                break;
            case "bs_final_3":
                runAudio(AudioType.ULTIMATE_SHOW, players);
                break;
            case "dimentio":
                runAudio(AudioType.MERRY_GO_ROUND, players);
                break;
            case "origin_chao":
                runAudio(AudioType.RULER_CORONATION, players);
                break;
            case "knight":
                runAudio(AudioType.BLACK_KNIFE, players);
                break;
            case "visho":
                runAudio(AudioType.GUARDIAN, players);
                break;
            default:
                break;
        }*/
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


    static void runAudio(String path, boolean loop, Player[] players){
        for(Player p : players){
            if(p != null && p.isOnline()){
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "oa play "+ p.getName() + " " + path + " {loop:" + loop + "}");
            }
        }
    }


    static void runAudioLocal(String path, boolean loop, Player[] players){
        for(Player p : players){
            if(p != null && p.isOnline()){
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "oa play "+ p.getName() + " files:" + path + ".mp3 {loop:" + loop + "}");
            }
        }
    }
    static void stopAudio(Player[] players){
        for(Player p : players){
            if(p != null && p.isOnline()){
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "oa stop "+ p.getName());
            }
        }
    }
}
