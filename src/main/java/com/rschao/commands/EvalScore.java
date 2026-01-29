package com.rschao.commands;

import com.rschao.boss_battle.InvManager;
import com.rschao.events.soulEvents;
import com.rschao.Plugin;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.OnePlayer;
import dev.jorel.commandapi.executors.CommandArguments;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class EvalScore {

    public static CommandAPICommand Load(){
        CommandAPICommand cmd = new CommandAPICommand("evalscore")
                .withPermission("gaster.items")
                .withArguments(new OnePlayer("target"), new IntegerArgument("score", 0, 100))
                .executes((CommandSender sender, CommandArguments args) -> {
                    File file = new File(Plugin.getPlugin(Plugin.class).getDataFolder(), "eval_scores.yml");
                    if (!file.exists()) {
                        try {
                            file.createNewFile();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Player target = (Player) args.get("target");
                    FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
                    String name = target.getName();
                    if(name.startsWith(".")){
                        name = name.substring(1);
                    }
                    fileConfiguration.set("eval." + name, (Integer) args.get("score"));
                    try {
                        fileConfiguration.save(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sender.sendMessage("Set " + target.getName() + "'s score to " + args.get("score"));
                });
        return cmd;
    }
}
