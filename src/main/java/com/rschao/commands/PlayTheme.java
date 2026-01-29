package com.rschao.commands;

import com.rschao.api.audio.AudioSelector;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import com.rschao.Plugin;

public class PlayTheme {
    public static void register() {
        new CommandAPICommand("playtheme")
            .withArguments(new StringArgument("type").replaceSuggestions(ArgumentSuggestions.strings("main", "alt")), new StringArgument("user"))
            .executesPlayer((player, args) -> {
                String type = (String) args.get("type");
                String user = (String) args.get("user");
                if (type == null || user == null) {
                    player.sendMessage(ChatColor.RED + "Uso incorrecto. Usa: /playtheme <main|alt> <user>");
                    return;
                }
                String theme = type.equalsIgnoreCase("main") ? "main" : "alt";

                // Verificar si el tema existe en audio_user.yml
                File file = new File(Plugin.getPlugin(Plugin.class).getDataFolder(), "audio_user.yml");
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                if (!config.contains("audio." + user + "." + theme)) {
                    player.sendMessage(ChatColor.RED + "No se encontró el tema '" + theme + "' para el usuario.");
                    return;
                }

                AudioSelector.PlayUserAudio(theme, player, user);
            })
            .register();
    }
}
