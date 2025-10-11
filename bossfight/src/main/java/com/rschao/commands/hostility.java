package com.rschao.commands;

import org.bukkit.entity.Player;
import com.rschao.items.Items;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.executors.CommandArguments;

public class hostility {
    public static CommandAPICommand Load(){
        CommandAPICommand cmd = new CommandAPICommand("hostility")
            .withArguments(new PlayerArgument("target"))
            .withPermission("gaster.items")
            .executesPlayer((Player player, CommandArguments args) -> {
                Player target = (Player) args.getOrDefault("target", () -> player);
                target.getInventory().addItem(Items.soul_Hostility);
            });
        return cmd;
    }
}
