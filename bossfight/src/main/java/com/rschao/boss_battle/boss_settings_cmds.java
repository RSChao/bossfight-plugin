package com.rschao.boss_battle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import com.rschao.Plugin;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.executors.CommandArguments;

public class boss_settings_cmds {
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
    public static CommandAPICommand bossSettings(){
        List<String> list = reloadList();
        

        CommandAPICommand switchboss = new CommandAPICommand("switchboss")
            .withPermission("bossbattle.switchboss")
            .withArguments(new StringArgument("boss").replaceSuggestions(ArgumentSuggestions.strings(list.toArray(new String[0]))))
            .executesPlayer((Player player, CommandArguments args) -> {
                File file = new File(Plugin.getPlugin(Plugin.class).getDataFolder() + "/bosses/", "general.yml");
                FileConfiguration config = new YamlConfiguration();
                try {
                    if(!file.exists()) {
                        file.createNewFile();
                    }
                    config.load(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(config.getList("bosses.names") != null) {
                    List<String> names = config.getStringList("bosses.names");
                    //check if list has the boss name already
                    if(!names.contains(String.valueOf(args.get("boss")))) {
                        names.add(String.valueOf(args.get("boss")));
                    }
                    //replace any . in the name with /
                    String name = String.valueOf(args.get("boss")).replace(".", "/");

                    config.set("bosses.names", names);
                    Plugin.getPlugin(Plugin.class).getConfig().set("boss.event.name", String.valueOf(name));
                    Plugin.getPlugin(Plugin.class).saveConfig();
                    Plugin.getPlugin(Plugin.class).reloadConfig();
                    Bukkit.getServer().getLogger().info("Boss is currently " + config.getString("boss.event.name"));
                    try {
                        config.save(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    player.sendMessage("§aBoss has been switched to " + name);
                    try {
                        config.load(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    List<String> names = new ArrayList<>();
                    //replace any . in the name with /
                    String name = String.valueOf(args.get("boss")).replace(".", "/");
                    list.add(String.valueOf(args.get("boss")));
                    config.set("bosses.names", names);
                    Plugin.getPlugin(Plugin.class).getConfig().set("boss.event.name", name);
                    Plugin.getPlugin(Plugin.class).saveConfig();
                    Plugin.getPlugin(Plugin.class).reloadConfig();
                    Bukkit.getServer().getLogger().info("Boss is currently " + config.getString("boss.event.name"));
                    try {
                        config.save(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    player.sendMessage("§aBoss has been switched to " + String.valueOf(name));
                    try {
                        config.load(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } 
                reloadList();
            })
            .executesConsole((ConsoleCommandSender player, CommandArguments args) -> {
                File file = new File(Plugin.getPlugin(Plugin.class).getDataFolder() + "/bosses/", "general.yml");
                FileConfiguration config = new YamlConfiguration();
                try {
                    if(!file.exists()) {
                        file.createNewFile();
                    }
                    config.load(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(config.getList("bosses.names") != null) {
                    List<String> names = config.getStringList("bosses.names");
                    //check if list has the boss name already
                    if(!names.contains(String.valueOf(args.get("boss")))) {
                        names.add(String.valueOf(args.get("boss")));
                    }
                    config.set("bosses.names", names);
                    Plugin.getPlugin(Plugin.class).getConfig().set("boss.event.name", String.valueOf(args.get("boss")));
                    Plugin.getPlugin(Plugin.class).saveConfig();
                    Plugin.getPlugin(Plugin.class).reloadConfig();
                    Bukkit.getServer().getLogger().info("Boss is currently " + config.getString("boss.event.name"));
                    try {
                        config.save(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    player.sendMessage("§aBoss has been switched to " + String.valueOf(args.get("boss")));
                    try {
                        config.load(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    List<String> names = new ArrayList<>();
                    list.add(String.valueOf(args.get("boss")));
                    config.set("bosses.names", names);
                    Plugin.getPlugin(Plugin.class).getConfig().set("boss.event.name", String.valueOf(args.get("boss")));
                    Plugin.getPlugin(Plugin.class).saveConfig();
                    Plugin.getPlugin(Plugin.class).reloadConfig();
                    Bukkit.getServer().getLogger().info("Boss is currently " + config.getString("boss.event.name"));
                    try {
                        config.save(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    player.sendMessage("§aBoss has been switched to " + String.valueOf(args.get("boss")));
                    try {
                        config.load(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        return switchboss;
    }
    public static CommandAPICommand SetBossValue(){
        CommandAPICommand cmd = new CommandAPICommand("bossedit")
            .withPermission("gaster.admin")
            .withHelp("/bossedit", "edit boss values for currently selected boss")
            .withSubcommands(BossSetInteger(), BossSetString())
            .executesPlayer((Player player, CommandArguments args) -> {
                player.sendMessage("Choose an option", "/bossedit number", "/bossedit string");
            })
            .executesConsole((ConsoleCommandSender player, CommandArguments args) -> {
                player.sendMessage("Choose an option", "/bossedit number", "/bossedit string");
            });
        return cmd;
    }
    
    public static CommandAPICommand BossSetInteger(){
        CommandAPICommand cmd = new CommandAPICommand("number")
            .withArguments(new StringArgument("key"), new DoubleArgument("value"))
            .executesPlayer((Player player, CommandArguments args) -> {
                bossEvents.getConfig();
                String configName = bossEvents.configName;
                File configFile = new File(Plugin.getPlugin(Plugin.class).getDataFolder() + "/bosses/", configName + ".yml");
                if (!configFile.exists()) {
                    try {
                        configFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
                try {

                    if(!configFile.exists()) {
                        player.sendMessage("No boss file selected.");
                        return;
                    }
                    config.load(configFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                config.set("" + args.get("key"), args.get("value"));
                try {
                    config.save(configFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                player.sendMessage("§a" + args.get("key") + " has been set to " + args.get("value"));
            })
            .executesConsole((ConsoleCommandSender player, CommandArguments args) -> {
                bossEvents.getConfig();
                String configName = bossEvents.configName;
                File configFile = new File(Plugin.getPlugin(Plugin.class).getDataFolder() + "/bosses/", configName + ".yml");
                if (!configFile.exists()) {
                    try {
                        configFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
                try {

                    if(!configFile.exists()) {
                        player.sendMessage("No boss file selected.");
                        return;
                    }
                    config.load(configFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                config.set("" + args.get("key"), args.get("value"));
                try {
                    config.save(configFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                player.sendMessage("§a" + args.get("key") + " has been set to " + args.get("value"));
            });
        return cmd;
    }
    public static CommandAPICommand BossSetString(){
        CommandAPICommand cmd = new CommandAPICommand("string")
            .withArguments(new StringArgument("key"), new StringArgument("value"))
            .executesPlayer((Player player, CommandArguments args) -> {
                bossEvents.getConfig();
                String configName = bossEvents.configName;
                File configFile = new File(Plugin.getPlugin(Plugin.class).getDataFolder() + "/bosses/", configName + ".yml");
                if (!configFile.exists()) {
                    try {
                        configFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
                try {

                    if(!configFile.exists()) {
                        player.sendMessage("No boss file selected.");
                        return;
                    }
                    config.load(configFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                config.set("" + args.get("key"), args.get("value"));
                try {
                    config.save(configFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                player.sendMessage("§a" + args.get("key") + " has been set to " + args.get("value"));
            })
            .executesConsole((ConsoleCommandSender player, CommandArguments args) -> {
                bossEvents.getConfig();
                String configName = bossEvents.configName;
                File configFile = new File(Plugin.getPlugin(Plugin.class).getDataFolder() + "/bosses/", configName + ".yml");
                if (!configFile.exists()) {
                    try {
                        configFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
                try {

                    if(!configFile.exists()) {
                        player.sendMessage("No boss file selected.");
                        return;
                    }
                    config.load(configFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                config.set("" + args.get("key"), args.get("value"));
                try {
                    config.save(configFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                player.sendMessage("§a" + args.get("key") + " has been set to " + args.get("value"));
            });
        return cmd;
    }
}
