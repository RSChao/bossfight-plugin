package com.rschao.commands;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.rschao.events.soulEvents;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.executors.CommandArguments;

public class checkSoul {
    public static CommandAPICommand Load(){
        CommandAPICommand cmd = new CommandAPICommand("checksoul")
            .withPermission("gaster.items")
            .withHelp("/clearsoul <player>", "clears a player's soul trait")
            .withOptionalArguments(new PlayerArgument("target"))
            .executesPlayer((Player player, CommandArguments args) -> {
                Player target = (Player) args.getOrDefault("target", () -> player);
                player.sendMessage(soulEvents.GetSoulName(target));
                player.sendMessage(soulEvents.GetSecondSoulName(target));
            })
            .executesConsole((ConsoleCommandSender exec, CommandArguments args) -> {
                Player target = (Player) args.get("target");
                if(target == null){
                    exec.sendMessage("No player selected");
                    return;
                };
                exec.sendMessage(soulEvents.GetSoulName(target));
                exec.sendMessage(soulEvents.GetSecondSoulName(target));
            });
        return cmd;
    }
}
