package com.rschao.boss_battle;

import com.rschao.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DropsManager {
    private static final String DROPS_INV_TITLE = "§6Boss Drops";

    public static void openDropsInventory(Player player, String bossName) {
        Inventory inv = Bukkit.createInventory(null, 54, DROPS_INV_TITLE);

        // Cargar drops existentes desde el config
        List<ItemStack> existingDrops = loadDropsFromConfig(bossName);
        for (int i = 0; i < existingDrops.size() && i < inv.getSize(); i++) {
            inv.setItem(i, existingDrops.get(i));
        }

        player.openInventory(inv);
    }

    public static void saveDropsToConfig(String bossName, Inventory inv) {
        FileConfiguration config = getBossConfig(bossName);
        if (config == null) return;

        List<ItemStack> drops = new ArrayList<>();
        for (ItemStack item : inv.getContents()) {
            if (item != null && !item.getType().equals(Material.AIR)) {
                drops.add(item.clone());
            }
        }

        config.set("boss.drops", drops);
        saveBossConfig(bossName, config);
    }

    public static List<ItemStack> loadDropsFromConfig(String bossName) {
        FileConfiguration config = getBossConfig(bossName);
        if (config == null || !config.contains("boss.drops")) {
            return new ArrayList<>();
        }

        List<?> dropsList = config.getList("boss.drops");
        List<ItemStack> result = new ArrayList<>();
        if (dropsList != null) {
            for (Object obj : dropsList) {
                if (obj instanceof ItemStack) {
                    result.add((ItemStack) obj);
                }
            }
        }
        return result;
    }

    public static FileConfiguration getBossConfig(String bossName) {
        String configName = bossName.replace(".", "/");
        File configFile = new File(Plugin.getPlugin(Plugin.class).getDataFolder() + "/bosses/", configName + ".yml");
        if (!configFile.exists()) return null;

        try {
            return YamlConfiguration.loadConfiguration(configFile);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveBossConfig(String bossName, FileConfiguration config) {
        String configName = bossName.replace(".", "/");
        File configFile = new File(Plugin.getPlugin(Plugin.class).getDataFolder() + "/bosses/", configName + ".yml");

        try {
            config.save(configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ItemStack createShulkerBoxWithDrops(List<ItemStack> drops) {
        ItemStack shulkerBox = new ItemStack(Material.SHULKER_BOX);
        org.bukkit.inventory.meta.BlockStateMeta meta = (org.bukkit.inventory.meta.BlockStateMeta) shulkerBox.getItemMeta();

        if (meta != null) {
            org.bukkit.block.ShulkerBox box = (org.bukkit.block.ShulkerBox) meta.getBlockState();
            Inventory boxInv = box.getInventory();

            for (ItemStack item : drops) {
                if (item != null && !item.getType().equals(Material.AIR)) {
                    boxInv.addItem(item.clone());
                }
            }

            meta.setBlockState(box);
            shulkerBox.setItemMeta(meta);
        }

        return shulkerBox;
    }

    public static String getCurrentBossName() {
        return Plugin.getPlugin(Plugin.class).getConfig().getString("boss.event.name", "unknown");
    }
}

