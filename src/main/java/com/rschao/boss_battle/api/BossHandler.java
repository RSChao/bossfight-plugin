package com.rschao.boss_battle.api;

import com.rschao.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

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
}
