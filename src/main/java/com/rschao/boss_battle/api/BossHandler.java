package com.rschao.boss_battle.api;

import com.rschao.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class BossHandler {

    static FileConfiguration getTemplateFile(){
        File file = new File(Plugin.getPlugin(Plugin.class).getDataFolder(), "/bosses/template/template.yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration createNewBoss(String name){
        File file = new File(Plugin.getPlugin(Plugin.class).getDataFolder(), "/bosses/"+name+".yml");
        if(file.exists()) return null;
        try {
            file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        FileConfiguration template = getTemplateFile();
        for(String key: template.getKeys(true)){
            config.set(key, template.get(key));
        }
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return config;
    }

    public static ConfigurationSection getPhaseData(FileConfiguration config, int phase){
        if(!config.contains("boss.world."+phase)) return null;
        return config.getConfigurationSection("boss.world."+phase);
    }

    public static int getMaxPhase(FileConfiguration config){
        return config.getInt("boss.kits");
    }

    public static FileConfiguration loadBoss(String name) {
        File file = new File(Plugin.getPlugin(Plugin.class).getDataFolder(), "/bosses/" + name.replace(".", "/") + ".yml");
        if (!file.exists()) return null;
        return YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Obtiene la "clave" del boss (nombre del archivo sin extensión) correspondiente
     * al FileConfiguration proporcionado.
     *
     * La carpeta raíz donde se buscan los ficheros es `dataFolder + "/bosses/"`.
     * Devuelve el nombre del archivo (sin .yml/.yaml) o null si no se encuentra coincidencia.
     */
    public static String getBossKey(FileConfiguration config) {
        Bukkit.getLogger().warning("Buscando clave para config");
        if (config == null) return null;

        File bossesDir = new File(Plugin.getPlugin(Plugin.class).getDataFolder(), "bosses");

        Bukkit.getLogger().warning("Directorio bosses: " + bossesDir.getAbsolutePath());
        Bukkit.getLogger().warning("" + (!bossesDir.exists() || !bossesDir.isDirectory()));
        if (!bossesDir.exists() || !bossesDir.isDirectory()) return null;

        String targetYaml = config.saveToString();

        File[] files = bossesDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".yml") || name.toLowerCase().endsWith(".yaml"));
        Bukkit.getLogger().warning("" + (files == null));
        if (files == null) return null;

        for (File f : files) {
            try {
                FileConfiguration other = YamlConfiguration.loadConfiguration(f);
                if (targetYaml.equals(other.saveToString())) {
                    String name = f.getName();
                    int idx = name.lastIndexOf('.');
                    return (idx > 0) ? name.substring(0, idx) : name;
                }
            } catch (Exception ignored) {
            }
        }

        String filePath = config.getCurrentPath();
        String baseDir = bossesDir.getAbsolutePath();
        if (filePath != null && filePath.startsWith(baseDir)) {
            String relativePath = filePath.substring(baseDir.length()).replace(File.separatorChar, '/');
            if (relativePath.startsWith("/")) relativePath = relativePath.substring(1);
            if (relativePath.endsWith(".yml")) relativePath = relativePath.substring(0, relativePath.length() - 4);
            else if (relativePath.endsWith(".yaml")) relativePath = relativePath.substring(0, relativePath.length() - 5);
            return relativePath;
        }
        return filePath;
    }


    /**
     * Resolve include/exclude and return list of boss keys (filename without extension).
     */
    public static List<String> resolveBossListFromRush(FileConfiguration superbossConfig) {
        if (superbossConfig == null || !superbossConfig.contains("bossrush")) return Collections.emptyList();
        ConfigurationSection br = superbossConfig.getConfigurationSection("bossrush");
        if (br == null) return Collections.emptyList();

        List<String> include = br.getStringList("include").stream().filter(s -> s != null && !s.isEmpty()).collect(Collectors.toList());
        List<String> exclude = br.getStringList("exclude").stream().filter(s -> s != null && !s.isEmpty()).collect(Collectors.toList());

        File bossesDir = new File(Plugin.getPlugin(Plugin.class).getDataFolder(), "bosses");
        if (!bossesDir.exists() || !bossesDir.isDirectory()) return Collections.emptyList();

        File[] files = bossesDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".yml") || name.toLowerCase().endsWith(".yaml"));
        if (files == null) return Collections.emptyList();

        List<String> allKeys = new ArrayList<>();
        for (File f : files) {
            String n = f.getName();
            int idx = n.lastIndexOf('.');
            if (idx > 0) n = n.substring(0, idx);
            allKeys.add(n);
        }

        if (!include.isEmpty()) {
            // keep only those included that exist
            return include.stream().filter(allKeys::contains).collect(Collectors.toList());
        } else if (!exclude.isEmpty()) {
            return allKeys.stream().filter(k -> !exclude.contains(k)).collect(Collectors.toList());
        } else {
            return allKeys;
        }
    }

    public static boolean isBossRush(FileConfiguration config) {
        return config != null && config.contains("bossrush");
    }

    /**
     * Prepares superboss: builds per-phase order lists, sets "boss.kits" in the provided config,
     * and saves the randomized order to bosses/boss_order.yml under order.<superKey>.<phase>.
     * Call this every time you start a new fight for that superboss so the order is randomized each run.
     */
    public static void prepareSuperboss(String superKey, FileConfiguration superbossConfig) {
        if (superbossConfig == null || superKey == null) return;
        if (!isBossRush(superbossConfig)) return;

        ConfigurationSection br = superbossConfig.getConfigurationSection("bossrush");
        String phaseVal = null;
        if (br != null) {
            Object raw = br.get("phase");
            if (raw != null) phaseVal = raw.toString();
        }
        // default behavior
        if (phaseVal == null) phaseVal = "1";

        List<String> included = resolveBossListFromRush(superbossConfig);
        if (included.isEmpty()) {
            superbossConfig.set("boss.kits", 0);
            return;
        }

        // load each boss to know its max phases
        Map<String, Integer> bossMax = new LinkedHashMap<>();
        for (String bkey : included) {
            FileConfiguration bc = BossHandler.loadBoss(bkey);
            int m = (bc == null) ? 0 : BossHandler.getMaxPhase(bc);
            if (m <= 0) m = 1; // fallback to 1
            bossMax.put(bkey, m);
        }

        Map<Integer, List<String>> phaseToBosses = new LinkedHashMap<>();
        LinkedHashMap<Integer, Map<String, Object>> phaseMapping = new LinkedHashMap<>();
        List<String> bossList = new ArrayList<>(bossMax.keySet());
        Collections.shuffle(bossList);

        if ("all".equalsIgnoreCase(phaseVal)) {
            // Para cada boss en el orden aleatorio, añadir sus fases 1..max consecutivas
            int idx = 1;
            for (String bossKey : bossList) {
                int max = bossMax.getOrDefault(bossKey, 1);
                for (int p = 1; p <= max; p++) {
                    Map<String, Object> entry = new HashMap<>();
                    entry.put("boss", bossKey);
                    entry.put("phase", p);
                    phaseMapping.put(idx++, entry);
                }
            }
        } else if ("last".equalsIgnoreCase(phaseVal)) {
            int idx = 1;
            for (String bossKey : bossList) {
                int max = bossMax.getOrDefault(bossKey, 1);
                Map<String, Object> entry = new HashMap<>();
                entry.put("boss", bossKey);
                entry.put("phase", max);
                phaseMapping.put(idx++, entry);
            }
        } else {
            // try integer
            int requested;
            try {
                requested = Integer.parseInt(phaseVal);
                if (requested <= 0) requested = 1;
            } catch (Exception ex) {
                requested = 1;
            }
            // each boss contributes its requested-th phase (or last if not enough)
            List<String> list = new ArrayList<>(bossMax.keySet());
            Collections.shuffle(list);
            for (int i = 0; i < list.size(); i++) {
                phaseToBosses.put(i + 1, Collections.singletonList(list.get(i)));
            }
            // NOTE: when you later use the stored order, for each boss you must map the phase to:
            // int bossPhase = Math.min(requested, bossMax.get(bossKey))
            // we only store the bossKey order here; consumer must decide which internal phase to use.
            superbossConfig.set("bossrush._requested_phase", requested);
        }

        // write boss_order file
        try {
            File orderFile = new File(Plugin.getPlugin(Plugin.class).getDataFolder(), "bosses/boss_order.yml");
            FileConfiguration orderCfg = YamlConfiguration.loadConfiguration(orderFile);
            ConfigurationSection root = orderCfg.getConfigurationSection("order");
            if (root == null) root = orderCfg.createSection("order");

            ConfigurationSection thisSection = root.getConfigurationSection(superKey);
            if (thisSection == null) thisSection = root.createSection(superKey);

            for (Map.Entry<Integer, List<String>> e : phaseToBosses.entrySet()) {
                thisSection.set(String.valueOf(e.getKey()), e.getValue());
            }

            orderCfg.save(orderFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // update boss.kits to number of superboss phases
        superbossConfig.set("boss.kits", phaseToBosses.size());
        try{
            superbossConfig.save(new File(Plugin.getPlugin(Plugin.class).getDataFolder(), "bosses/" + superKey + ".yml"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
