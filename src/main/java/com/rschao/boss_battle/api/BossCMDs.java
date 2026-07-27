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
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Stream;

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

    public static CommandAPICommand createSuperBoss(){
        CommandAPICommand cmd = new CommandAPICommand("createsuperboss")
                .withPermission("gaster.admin")
                .withArguments(new StringArgument("key"), new StringArgument("exclude"))
                .executes(((sender, args) -> {
                    String key = (String) args.get("key");
                    String rawExclude = (String) args.get("exclude");
                    String[] excluded = rawExclude.split("-");
                    List<String> bossesToExclude = Arrays.stream(excluded).toList();
                    List<String> bosses = getAllBosses();
                    List<String> bossesInSuperboss = new ArrayList<>();

                    for(String boss : bosses){
                        if(boss.equals(key)) continue;
                        boolean excludeBoss = false;
                        for(String exclude: bossesToExclude){
                            if(boss.contains(exclude)) {
                                excludeBoss = true;
                                break;
                            }
                        }
                        if(excludeBoss) continue;
                        bossesInSuperboss.add(boss);
                    }
                    File f = BossHandler.getBossFile(key);
                    FileConfiguration config = BossHandler.createNewBoss(key);

                    List<Map<String, Object>> phases = getBossPhases(bossesInSuperboss.toArray(new String[0]));

                    for(int i = 0; i < phases.size(); i++){
                        config.set("boss.world." + (i+1), phases.get(i));
                    }

                    config.set("boss.kits", phases.size());
                    try{
                        config.save(f);
                    } catch (IOException e){
                        e.printStackTrace();
                        sender.sendMessage(ChatColor.RED + "Failed to save superboss");
                    }

                }));
        return cmd;
    }



    static List<String> getAllBosses(){
        File bossesDir = new File(Plugin.getPlugin(Plugin.class).getDataFolder(), "bosses");
        if(!bossesDir.exists()) bossesDir.mkdirs();

        File file = new File(bossesDir, "general.yml");
        FileConfiguration config = new YamlConfiguration();
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            config.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Reconstruir la lista desde cero para reflejar exactamente el contenido del disco
        List<String> names = new ArrayList<>();

        Path base = bossesDir.toPath();
        String folderToExclude = "template";
        String fileToExclude = "general.yml";

        // Recorrer recursivamente con Files.walk
        try (Stream<Path> stream = Files.walk(base)) {
            stream.filter(Files::isRegularFile).forEach(path -> {
                String fname = path.getFileName().toString();
                // Excluir el archivo general.yml
                if (fname.equalsIgnoreCase(fileToExclude)) return;
                // Excluir cualquier ruta que contenga la carpeta 'template'
                String pathStr = path.toString();
                if (pathStr.contains(File.separator + folderToExclude + File.separator) || pathStr.endsWith(File.separator + folderToExclude) || pathStr.contains(folderToExclude + File.separator)) return;

                // Guardar la ruta relativa desde /bosses/ usando '/' como separador
                String relative = base.relativize(path).toString().replace('\\', '/');
                relative = relative.replace('/', '.');
                relative = relative.replace(".yml", "");
                if (!names.contains(relative)) names.add(relative);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return names;
    }

    /**
     * Carga uno o varios bosses y recopila la configuración de cada fase.
     * Las fases se esperan en las rutas "boss.world.1", "boss.world.2", ...
     * Devuelve una lista de mapas (cada mapa representa la sección de la fase) para poder
     * pegarlos directamente en otra FileConfiguration usando target.set("boss.world.X", map).
     */
    public static List<Map<String, Object>> getBossPhases(String[] configNames) {
        List<Map<String, Object>> phases = new ArrayList<>();

        for (String configName : configNames) {
            FileConfiguration bossConfig = BossHandler.loadBoss(configName);
            if (bossConfig == null) continue;

            int idx = 1;
            while (true) {
                String basePath = "boss.world." + idx;
                if (!bossConfig.contains(basePath)) break;

                ConfigurationSection sec = bossConfig.getConfigurationSection(basePath);
                Map<String, Object> values = new HashMap<>();
                if (sec != null) {
                    values.putAll(sec.getValues(true));
                }

                phases.add(values);
                idx++;
            }
        }

        return phases;
    }

}
