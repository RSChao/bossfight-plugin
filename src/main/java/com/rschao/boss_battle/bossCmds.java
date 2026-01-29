package com.rschao.boss_battle;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import com.rschao.Plugin;
import com.rschao.events.soulEvents;
import com.rschao.events.definitions.BossChangeEvent;
import com.rschao.events.definitions.BossEndEvent;
import com.rschao.events.definitions.BossStartEvent;
import com.rschao.events.definitions.InventoryBackupEvent;
import com.rschao.events.definitions.InventoryRestoreEvent;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.OnePlayer;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.executors.CommandArguments;

public class bossCmds {
    public static CommandAPICommand Load(){
        CommandAPICommand cmd = new CommandAPICommand("boss")
            .withPermission("gaster.admin")
            .withHelp("/boss", "Starts the final battle")
            .withOptionalArguments(new OnePlayer("target"))
            .executesPlayer((Player player, CommandArguments args) -> {
                bossEvents.bossPhase = 0;
                Player target = (Player) args.getOrDefault("target", player);
                //Dispatch a command to the console to give the player the gaster.boss permission
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + target.getName() + " permission set gaster.boss true");
                //save every online player's inventory with no command and a key corresponding to their name, but with "boss." prepended
                Bukkit.getOnlinePlayers().forEach((p) -> {
                    if(!target.hasPermission("gaster.boss")){
                        InvManager.SaveInventory(p, "backup.boss." + p.getName());
                    }
                    
                });
                bossEvents.bossActive = true;
                bossEvents.bossPhase++;
                bossEvents.killCount = 0;
                bossEvents.daboss = target;
                BossStartEvent ev = new BossStartEvent(bossEvents.configName, 1, target, bossEvents.bossPlayers);
                Bukkit.getServer().getPluginManager().callEvent(ev);


                bossEvents.executeBossPhase(bossEvents.bossPhase);
                player.sendMessage("...");
                
                
            });
        return cmd;
    }
    //A new CommandAPICommand that increases the boss's phase
    public static CommandAPICommand NextPhase(){
        CommandAPICommand cmd = new CommandAPICommand("nextphase")
            .withPermission("gaster.boss")
            .withHelp("/nextphase", "Increases the boss's phase")
            .executesPlayer((Player player, CommandArguments args) -> {
                
                bossEvents.bossPhase++;
                bossEvents.executeBossPhase(bossEvents.bossPhase);
                BossChangeEvent event = new BossChangeEvent(bossEvents.configName, bossEvents.bossPhase, player, bossEvents.bossPlayers);
                Bukkit.getServer().getPluginManager().callEvent(event);
                
            });
        return cmd;
    }
    //A new CommandAPICommand command that calls InvManager.SaveInventory
    public static CommandAPICommand SaveInventory(){
        
        @SuppressWarnings("null")
        CommandAPICommand cmd = new CommandAPICommand("saveinv")
            .withPermission("gaster.admin")
            .withHelp("/saveinventory", "Saves the player's inventory")
            .withArguments(new StringArgument("key"))
            .withOptionalArguments(new OnePlayer("target"))
            .executesPlayer((Player player, CommandArguments args) -> {
                Player target = (Player) args.getOrDefault("target", player);
                InvManager.DeleteInventory(args.get("key").toString());
                InvManager.SaveInventory(target, args.get("key").toString());
                player.sendMessage("Saved " + target.getName() + "'s inventory as " + args.get("key").toString());
                @SuppressWarnings("null")
                InventoryBackupEvent event = new InventoryBackupEvent(player, args.get("key").toString());
                Bukkit.getPluginManager().callEvent(event);
            })
            .executesConsole((ConsoleCommandSender exec, CommandArguments args) -> {
                if (args.get("target") == null){
                    exec.sendMessage("Missing player. git gud bozo");
                    return;
                }
                boolean isBossKit = (boolean) args.getOrDefault("isbosskit", args.get("key").toString().startsWith("boss."));
                InvManager.SaveInventory((Player) args.get("target"), args.get("key").toString());
                @SuppressWarnings("null")
                InventoryBackupEvent event = new InventoryBackupEvent((Player) args.get("target"), args.get("key").toString());
                Bukkit.getPluginManager().callEvent(event);
                exec.sendMessage("Saved inventory as " + args.get("key").toString());
            });
        return cmd;
    }
    //A new CommandAPICommand command that calls InvManager.LoadInventory
    public static CommandAPICommand LoadInventory(){
        @SuppressWarnings("null")
        CommandAPICommand cmd = new CommandAPICommand("loadinv")
            .withPermission("gaster.admin")
            .withHelp("/loadinventory", "Loads the player's inventory")
            .withArguments(new StringArgument("key"))
            .withOptionalArguments(new OnePlayer("target"))
            .executesPlayer((Player player, CommandArguments args) -> {
                if(args.get("key") == null){
                    player.sendMessage("Missing key. git gud bozo");
                    return;
                }
                Player target = (Player) args.getOrDefault("target", player);
                InvManager.LoadInventory(target, args.get("key").toString());
                @SuppressWarnings("null")
                String key = args.get("key").toString();
                InventoryRestoreEvent event = new InventoryRestoreEvent(target, key);
                Bukkit.getPluginManager().callEvent(event);
                player.sendMessage("Loaded inventory from " + key);
            })
            .executesConsole((ConsoleCommandSender exec, CommandArguments args) -> {
                if (args.get("target") == null){
                    exec.sendMessage("Missing player. git gud bozo");
                    return;
                }
                if (args.get("key") == null){
                    exec.sendMessage("Missing key. git gud bozo");
                    return;
                    
                }
                InvManager.LoadInventory((Player) args.get("target"), args.get("key").toString());
                @SuppressWarnings("null")
                String key = args.get("key").toString();
                exec.sendMessage("Loaded inventory from " + key);
                InventoryRestoreEvent event = new InventoryRestoreEvent((Player) args.get("target"), key);
                Bukkit.getPluginManager().callEvent(event);
            });
        return cmd;
    }
    //A new CommandAPI command that deletes an inventory stored in config
    public static CommandAPICommand DeleteInventory(){
        @SuppressWarnings("null")
        CommandAPICommand cmd = new CommandAPICommand("delinv")
            .withPermission("gaster.admin")
            .withHelp("/delinv", "Deletes an inventory in ")
            .withArguments(new StringArgument("key"))
            .executesPlayer((Player player, CommandArguments args) -> {
                if(args.get("key") == null){
                    player.sendMessage("Missing key. git gud bozo");
                    return;
                }
                InvManager.DeleteInventory(args.get("key").toString());
                @SuppressWarnings("null")
                String key = args.get("key").toString();
                player.sendMessage("Deleted inventory from " + key);
            })
            .executesConsole((ConsoleCommandSender exec, CommandArguments args) -> {
                if (args.get("key") == null){
                    exec.sendMessage("Missing key. git gud bozo");
                    return;
                }
                InvManager.DeleteInventory(args.get("key").toString());
                @SuppressWarnings("null")
                String key = args.get("key").toString();
                exec.sendMessage("Deleted inventory from " + key);
            });
        return cmd;
    }


    //A new CommandAPI command that resets the boss's phase
    public static CommandAPICommand ResetPhase(){
        CommandAPICommand cmd = new CommandAPICommand("resetphase")
            .withPermission("gaster.admin")
            .withHelp("/resetphase", "Resets the boss's phase")
            .executesPlayer((Player player, CommandArguments args) -> {
                bossEvents.bossPhase = 0;
                bossEvents.bossActive = false;
                BossEndEvent event = new BossEndEvent(bossEvents.configName, bossEvents.bossPhase, player, bossEvents.bossPlayers);
                Bukkit.getServer().getPluginManager().callEvent(event);
                for(Player p : Bukkit.getOnlinePlayers()){
                    //remove the gaster.boss permission from every online player
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " permission unset gaster.boss");
                }
                player.sendMessage("Reset boss phase");
            });
        return cmd;
    }
    public static CommandAPICommand ToggleSoulDrop(){
        @SuppressWarnings("null")
        CommandAPICommand cmd = new CommandAPICommand("setsouldrop")
            .withPermission("gaster.admin")
            .withHelp("/togglesouldrop", "Toggles the event")
            .withArguments(new BooleanArgument("state"))
            .executesPlayer((Player player, CommandArguments args) -> {
                soulEvents.drop = (boolean) args.get("state");
                Plugin.getPlugin(Plugin.class).getConfig().set("soul.drop", soulEvents.drop);
                Plugin.getPlugin(Plugin.class).saveConfig();
                Plugin.getPlugin(Plugin.class).reloadConfig();
                player.sendMessage("Soul dropping is now " + (soulEvents.drop ? "active" : "inactive"));
            })
            .executesConsole((ConsoleCommandSender player, CommandArguments args) -> {
                soulEvents.drop = (boolean) args.get("state");
                Plugin.getPlugin(Plugin.class).getConfig().set("soul.drop", soulEvents.drop);
                Plugin.getPlugin(Plugin.class).saveConfig();
                Plugin.getPlugin(Plugin.class).reloadConfig();
                player.sendMessage("Soul dropping is now " + (soulEvents.drop ? "active" : "inactive"));
            });
        return cmd;
    }
}
