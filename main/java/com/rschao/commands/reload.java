package com.rschao.commands;

import org.bukkit.entity.Player;

import com.rschao.Plugin;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.executors.CommandArguments;

public class reload {
    public static CommandAPICommand Load(){
        CommandAPICommand cmd = new CommandAPICommand("bossreload")
            .withPermission("gaster.admin")
            .executesPlayer((Player player, CommandArguments args) -> {
                Plugin.getPlugin(Plugin.class).reloadConfig();
                player.sendMessage("Config reloaded");
            });
        return cmd;
    }
}
