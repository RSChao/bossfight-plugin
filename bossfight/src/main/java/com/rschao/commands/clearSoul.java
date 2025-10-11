package com.rschao.commands;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.rschao.events.soulEvents;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.executors.CommandArguments;

public class clearSoul {
    public static CommandAPICommand Load(){
        CommandAPICommand cmd = new CommandAPICommand("clearsoul")
            .withPermission("gaster.items")
            .withHelp("/clearsoul <player>", "clears a player's soul trait")
            .withArguments(new PlayerArgument("target"))
            .executesPlayer((Player player, CommandArguments args) -> {
                Player target = (Player) args.getOrDefault("target", () -> player);
                soulEvents.clearSouls(target);
                player.sendMessage("Soul trait taken");
            })
            .executesConsole((ConsoleCommandSender exec, CommandArguments args) -> {
                Player target = (Player) args.get("target");
                if(target == null){
                    exec.sendMessage("No player selected");
                    return;
                }
                soulEvents.clearSouls(target);
                exec.sendMessage("Soul trait taken");
            });
        return cmd;
    }
}
