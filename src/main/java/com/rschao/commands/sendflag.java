package com.rschao.commands;

import java.io.File;
import java.util.List;

import com.rschao.boss_battle.FlagManager;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.OnePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.rschao.Plugin;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.entity.Player;

public class sendflag {
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
        return conf.getStringList("flags.categories");
    }
    public static CommandAPICommand Load() {
        return new CommandAPICommand("sendflag")
                .withArguments(new StringArgument("category").replaceSuggestions(ArgumentSuggestions.strings(reloadList())), new StringArgument("flag"))
                .withOptionalArguments(new OnePlayer("player"))
                .executes((sender, args) -> {
                    String category = (String) args.get("category");
                    String flag = (String) args.get("flag");
                    String finalFlag = category + "." + flag;
                    Player p = (Player) args.get("player");
                    if(p == null){
                        if(sender instanceof Player){
                            p = (Player) sender;
                        }
                        else {
                            sender.sendMessage("No player selected. What are you doing?");
                            return;
                        }
                    }
                    FlagManager.SaveFlag(finalFlag, p);
                    sender.sendMessage("Flag sent successfuly!");
                });
    }
}
