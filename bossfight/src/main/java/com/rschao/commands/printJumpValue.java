package com.rschao.commands;

import org.bukkit.entity.Player;

import com.rschao.Plugin;
import com.rschao.smp.logs.log;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.executors.CommandArguments;

public class printJumpValue {
    public static CommandAPICommand Load() {
        return new CommandAPICommand("sendjumpval")
                .executesPlayer((Player player, CommandArguments args) -> {
                
                double jumpValue = player.getAttribute(org.bukkit.attribute.Attribute.JUMP_STRENGTH).getBaseValue();
                String jumpValueString = String.valueOf(jumpValue);
                log.logToFile(jumpValueString, "jumpval.yml", Plugin.getPlugin(Plugin.class).getDataFolder());
            });
    }
}
