package com.rschao.commands;

import com.rschao.Plugin;
import com.rschao.boss_battle.bossEvents;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

public class CheckFlag {
    static List<String> reloadList(){
        File f = new File(Plugin.getPlugin(Plugin.class).getDataFolder() + "/bosses/", "general.yml");
        FileConfiguration conf = new YamlConfiguration();
        try {
            if(!f.exists()) {
                f.createNewFile();
            }
            conf.load(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //get a list of strings from the config file in section "bosses.names"
        return conf.getStringList("bosses.names");
    }
    public static CommandAPICommand load(){
        List<String> list = reloadList();
        return new CommandAPICommand("checkFlags")
                .withArguments(new PlayerArgument("player"))
                .executes((sender, args) -> {
                    Player player = (Player) args.get(0);

                    // Load the flags.yml configuration
                    File file;
                    file = new File(Plugin.getPlugin(Plugin.class).getDataFolder(), "flags.yml");
                    FileConfiguration flagsConfig = new YamlConfiguration();
                    try{
                        flagsConfig.load(file);
                    }
                    catch(Exception e){
                        return;
                    }

                    FileConfiguration boss = bossEvents.getConfig();
                    // Check for the "boss.flags" section
                    ConfigurationSection bossFlagsSection = boss.getConfigurationSection("boss.flags");
                    if(flagsConfig.getConfigurationSection(player.getName()) == null){
                        player.sendMessage("No keys found in target player");
                        return;
                    }
                    // If the section exists, check for keys under it
                    if (bossFlagsSection != null) {
                        boolean keyExists = false;
                        for(String key : flagsConfig.getConfigurationSection(player.getName()).getKeys(true)){
                            if(!key.contains(".")) continue;
                            player.sendMessage(key);
                            for(String flag: bossFlagsSection.getKeys(true)){
                                if(flag.equals(key)) {
                                    keyExists = true;
                                    break;
                                }
                            }
                        }
                        if (keyExists) {
                            player.sendMessage("All flags found. Check valid!");
                        } else {
                            player.sendMessage("Flags have not been found. Check not valid!");
                        }
                    } else {
                        player.sendMessage("No flags needed. Check valid!");
                    }
                });
    }
}
