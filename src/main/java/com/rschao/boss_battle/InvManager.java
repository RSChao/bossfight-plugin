package com.rschao.boss_battle;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import com.rschao.Plugin;

public class InvManager {

    public static void migrateKits(){
        //get kits in "kits.yml" and move them to individual files
        File oldFile = new File(Plugin.getPlugin(Plugin.class).getDataFolder(), "kits.yml");
        if(!oldFile.exists()) return;
        FileConfiguration oldConfig = new YamlConfiguration();
        try{
            oldConfig.load(oldFile);
        } catch(IOException | InvalidConfigurationException e){
            e.printStackTrace();
        }
        for(String key : oldConfig.getKeys(true)){
            File newFile = new File(Plugin.getPlugin(Plugin.class).getDataFolder(), getFileName(key));
            FileConfiguration newConfig = new YamlConfiguration();
            if(oldConfig.getConfigurationSection(key) == null) continue;
            for(String subKey : oldConfig.getConfigurationSection(key).getKeys(false)){
                newConfig.set(key + "." + subKey, oldConfig.get(key + "." + subKey));
            }
            try {
                newConfig.save(newFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try{
            for(Path p: findFoldersContaining(new File(Plugin.getPlugin(Plugin.class).getDataFolder(), "/kits/").toPath(), "inv.yml")){
                Bukkit.getLogger().info("Deleting extra file in " + p.toString());
                File file = p.resolve("kits.yml").toFile();
                file.delete();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    public static List<Path> findFoldersContaining(Path start, String fileName) throws IOException {
        try (Stream<Path> s = Files.walk(start)) {
            return s.filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().equals(fileName))
                    .map(Path::getParent)
                    .distinct()
                    .collect(Collectors.toList());
        }
    }

    private static String getFileName(String key){
        return "/kits/" + key.replace(".", "/") + ".yml";
    }
    public static void SaveInventory(Player player, String key){
        File file;
        file = new File(Plugin.getPlugin(Plugin.class).getDataFolder(), getFileName(key));
        FileConfiguration config = new YamlConfiguration();
        try{
            config.load(file);
        } catch(IOException | InvalidConfigurationException e){
            e.printStackTrace();
        }
        for(int i = 0; i < player.getInventory().getSize(); i++){
            ItemStack item = player.getInventory().getItem(i);
            if(item != null){
                config.set(key + ".inv." + i, item);
            }
            else{
                player.getInventory().setItem(i, new ItemStack(Material.AIR));
            }
        }
        /*
        config.set(key + ".inv.helmet", player.getInventory().getHelmet());
        config.set(key + ".inv.chestplate", player.getInventory().getChestplate());
        config.set(key + ".inv.leggings", player.getInventory().getLeggings());
        config.set(key + ".inv.boots", player.getInventory().getBoots());
        config.set(key + ".inv.offhand", player.getInventory().getItemInOffHand());
        Plugin.getPlugin(Plugin.class).saveConfig();*/
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void LoadInventory(Player player, String key){
        File file;
        file = new File(Plugin.getPlugin(Plugin.class).getDataFolder(), getFileName(key));
        if(!file.isFile()) {
            player.sendMessage(ChatColor.RED + "File not found.");
            return;
        }
        FileConfiguration config = new YamlConfiguration();
        try{
            config.load(file);
        } catch(IOException | InvalidConfigurationException e){
            e.printStackTrace();
        }
        for(int i = 0; i < player.getInventory().getSize(); i++){
            ItemStack item = config.getItemStack(key + ".inv." + i);
            if(item != null){
                player.getInventory().setItem(i, item);
            }
            else{
                player.getInventory().setItem(i, new ItemStack(Material.AIR));
            }
        }/*
        player.getInventory().setHelmet(config.getItemStack(key + ".inv.helmet"));
        player.getInventory().setChestplate(config.getItemStack(key + ".inv.chestplate"));
        player.getInventory().setLeggings(config.getItemStack(key + ".inv.leggings"));
        player.getInventory().setBoots(config.getItemStack(key + ".inv.boots"));
        player.getInventory().setItemInOffHand(config.getItemStack(key + ".inv.offhand"));*/
    }

    public static void DeleteInventory(String key){
        File file;
        file = new File(Plugin.getPlugin(Plugin.class).getDataFolder(), getFileName(key));
        FileConfiguration config = new YamlConfiguration();
        try{
            config.load(file);
        } catch(IOException | InvalidConfigurationException e){
            e.printStackTrace();
        }
        config.set(key, null);
        Plugin.getPlugin(Plugin.class).saveConfig();
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*public static void SanitizeInventory(Player player) {
        boolean hasSanitized = false;
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item == null || item.getType() == Material.AIR) continue;

            // Skip if max stack size is 1
            if (item.getMaxStackSize() == 1) continue;

            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                // Skip if it has persistent data
                PersistentDataContainer pdc = meta.getPersistentDataContainer();
                if (!pdc.isEmpty()) continue;

                // Skip if it has enchantments
                if (!meta.getEnchants().isEmpty()) continue;

                // Skip if it has custom name
                if (meta.hasDisplayName()) continue;

                // Skip if it has lore
                if (meta.hasLore()) continue;
                if(item.getType() == Material.CHEST) continue; // Skip if it's a chest item

                if(item.getItemMeta() instanceof BannerMeta) continue; // Skip if it's a chest item

                if(item.getType() == Material.TIPPED_ARROW) continue; // Skip if it's a chest item
                if(item.getType() == Material.SPLASH_POTION) continue; // Skip if it's a chest item
                if(item.getType() == Material.LINGERING_POTION) continue; // Skip if it's a chest item
                if(item.getType().toString().contains("SHULKER_BOX")) continue;
                if(item.getType().toString().contains("SIGN")) continue;
                if(item.getType().toString().contains("BOOK")) continue;
                if(item.getType() == Material.BARREL) continue; // Skip if it's a chest item
                if(item.getType().toString().contains("SPAWN")) continue; // Skip if
                if(item.getType().toString().contains("COMMAND")) continue; // Skip if
                if(item.getType() == Material.FIREWORK_ROCKET) continue; // Skip if it's a chest item
                if(item.getType() == Material.FIREWORK_STAR) continue; // Skip if it's a chest item
                if(item.getType() == Material.POTION) continue; // Skip if it's a chest item
                if(item.getType() == Material.OMINOUS_BOTTLE) continue; // Skip if it's a chest item
            }

            // Compare with a new ItemStack of same material and amount
            ItemStack vanilla = new ItemStack(item.getType(), item.getAmount());
            boolean needsSanitize = false;

            // Compare meta (should be null or default)
            ItemMeta vanillaMeta = vanilla.getItemMeta();

            if(!meta.getItemName().equals(vanillaMeta.getItemName())) continue;
            if (meta != null && vanillaMeta != null && !meta.equals(vanillaMeta)) {
                needsSanitize = true;
            }

            // Compare other properties if needed (add more checks if necessary)

            if (!needsSanitize && !item.equals(vanilla)) {
                needsSanitize = true;
            }

            if (needsSanitize) {
                player.getInventory().setItem(i, vanilla);
                hasSanitized = true;
            }
        }
        if (hasSanitized) {
            player.sendMessage(ChatColor.YELLOW + "Your inventory has been sanitized.");
        }
    }*/
}