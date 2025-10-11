package com.rschao.boss_battle;

import java.io.File;
import java.io.IOException;

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
    public static void SaveInventory(Player player, String key) {
        SaveInventory(player, key, key.startsWith("boss."));
    }

    public static void SaveInventory(Player player, String key, boolean isBossKit){
        File file;
        file = new File(Plugin.getPlugin(Plugin.class).getDataFolder(), "kits.yml");
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
        config.set(key + ".inv.helmet", player.getInventory().getHelmet());
        config.set(key + ".inv.chestplate", player.getInventory().getChestplate());
        config.set(key + ".inv.leggings", player.getInventory().getLeggings());
        config.set(key + ".inv.boots", player.getInventory().getBoots());
        config.set(key + ".inv.offhand", player.getInventory().getItemInOffHand());
        Plugin.getPlugin(Plugin.class).saveConfig();
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void LoadInventory(Player player, String key, boolean runCmd){
        File file;
        file = new File(Plugin.getPlugin(Plugin.class).getDataFolder(), "kits.yml");

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
        }
        player.getInventory().setHelmet(config.getItemStack(key + ".inv.helmet"));
        player.getInventory().setChestplate(config.getItemStack(key + ".inv.chestplate"));
        player.getInventory().setLeggings(config.getItemStack(key + ".inv.leggings"));
        player.getInventory().setBoots(config.getItemStack(key + ".inv.boots"));
        player.getInventory().setItemInOffHand(config.getItemStack(key + ".inv.offhand"));
        if (runCmd) {
            if(config.contains(key + ".cmd")){
                String cmd = config.getString(key + ".cmd");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName()));
            }
        }
    }

    public static void DeleteInventory(String key){
        boolean isBossKit = key.startsWith("boss.");
        File file;
        file = new File(Plugin.getPlugin(Plugin.class).getDataFolder(), "kits.yml");
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
    @SuppressWarnings("null")
    public static void SanitizeInventory(Player player) {
        boolean hasSanitized = false;
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item == null || item.getType() == Material.AIR) continue;

            // Skip if max stack size is 1
            if (item.getMaxStackSize() == 1) continue;

            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                // Skip if has persistent data
                PersistentDataContainer pdc = meta.getPersistentDataContainer();
                if (!pdc.isEmpty()) continue;

                // Skip if has enchantments
                if (!meta.getEnchants().isEmpty()) continue;

                // Skip if has custom name
                if (meta.hasDisplayName()) continue;

                // Skip if has lore
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
    }
}