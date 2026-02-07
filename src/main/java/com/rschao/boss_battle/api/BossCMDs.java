package com.rschao.boss_battle.api;

import com.rschao.Plugin;
import com.rschao.boss_battle.BossAPI;
import com.rschao.boss_battle.InvManager;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BossCMDs {

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

    public static CommandAPICommand Load(){
        CommandAPICommand cmd = new CommandAPICommand("boss")
                .withPermission("gaster.admin")
                .withHelp("/boss <config> [player]", "Inicia un boss usando la configuración indicada")
                .withArguments(new StringArgument("config").replaceSuggestions(ArgumentSuggestions.strings(info -> reloadList().toArray(new String[0]))))
                .withOptionalArguments(new EntitySelectorArgument.OnePlayer("target"))
                .executesPlayer((Player player, CommandArguments args) -> {
                    String configName = (String) args.get("config");
                    Player target = (Player) args.getOrDefault("target", player);

                    FileConfiguration config = BossHandler.loadBoss(configName);
                    if (config == null) {
                        player.sendMessage("Configuración de boss no encontrada: " + configName);
                        return;
                    }

                    // Guardar inventarios (backup)
                    Bukkit.getOnlinePlayers().forEach((p) -> {
                        InvManager.SaveInventory(p, "backup.boss." + p.getName());
                    });

                    // Construir listas de bosses y fighters
                    List<Player> bosses = new ArrayList<>();
                    bosses.add(target);

                    List<Player> fighters = new ArrayList<>();
                    Location dabossLoc = target.getLocation();
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p == null) continue;
                        if (!p.isOnline()) continue;
                        if (p.getUniqueId().equals(target.getUniqueId())) continue;
                        if (!p.getWorld().equals(dabossLoc.getWorld())) continue;
                        if (p.getLocation().distanceSquared(dabossLoc) <= 50.0 * 50.0) {
                            fighters.add(p);
                        }
                    }
                    BossAPI.findByBoss(target).ifPresent(bi -> {
                        BossAPI.getInstancesWithPlayer(target).forEach(b -> BossAPI.stopInstance(b));
                    });
                    // Iniciar la instancia con la API
                    BossInstance instance = BossAPI.startFight(configName, config, bosses, fighters);
                    player.sendMessage("Boss iniciado: " + configName);
                });
        return cmd;
    }
    // Comando: /nextphase para avanzar fase del boss si el jugador es boss
    public static CommandAPICommand NextPhase(){
        CommandAPICommand cmd = new CommandAPICommand("nextphase")
                .withPermission("gaster.boss")
                .withHelp("/nextphase", "Aumenta la fase del boss (si eres boss)")
                .executesPlayer((Player player, CommandArguments args) -> {
                    BossAPI.findByBoss(player).ifPresent(bi -> {
                        bi.advancePhase();
                        player.sendMessage("Fase incrementada a " + bi.getCurrentPhase());
                    });
                });
        return cmd;
    }

    public static CommandAPICommand Reset(){
        CommandAPICommand cmd = new CommandAPICommand("bossreset")
                .withPermission("gaster.admin")
                .withHelp("/bossreset", "Reinicia el estado de todos los jugadores (teleport a spawn, clear inventory)")
                .executesPlayer((Player player, CommandArguments args) -> {
                    Bukkit.getOnlinePlayers().forEach(p -> {
                        for(Player pl : Bukkit.getOnlinePlayers()){
                            for(BossInstance instance : BossAPI.getInstancesWithPlayer(pl)){
                                BossAPI.stopInstance(instance);
                            }
                        }
                        InvManager.LoadInventory(p, "backup.boss." + p.getName());
                    });
                    player.sendMessage("Estado de jugadores reiniciado.");
                });
        return cmd;
    }
}
