package com.rschao.commands;

import com.rschao.api.audio.AudioSelector;
import com.rschao.Plugin;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class BossMusic {

    public static String[] reloadSuggestions(){
        {
            try {
                File file = new File(Plugin.getPlugin(Plugin.class).getDataFolder(), "audio.yml");
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                ConfigurationSection section = config.getConfigurationSection("audio");
                if (section == null) return new String[0];
                return section.getKeys(false).toArray(new String[0]);
            } catch (Exception e) {
                return new String[0];
            }
        }
    }
    public static void register() {
        new CommandAPICommand("bossmusic")
            .withArguments(new StringArgument("theme").replaceSuggestions(ArgumentSuggestions.strings(info -> reloadSuggestions())))
            .executesPlayer((player, args) -> {
                String theme = (String) args.get("theme");
                if (theme == null || theme.isEmpty()) {
                    player.sendMessage(ChatColor.RED + "Uso incorrecto. Usa: /bossmusic <tema>");
                    return;
                }
                File file = new File(Plugin.getPlugin(Plugin.class).getDataFolder(), "audio.yml");
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                if (!config.contains("audio." + theme)) {
                    player.sendMessage(ChatColor.RED + "No se encontró el tema '" + theme + "'.");
                    return;
                }
                Player[] targets = new Player[1];
                targets[0] = player;
                AudioSelector.PlayBossAudio(theme, targets);
                player.sendMessage(ChatColor.GREEN + "Reproduciendo tema: " + theme);
            }).register("gaster");
    }
}

