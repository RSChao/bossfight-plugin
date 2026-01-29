package com.rschao.commands;

import com.rschao.Plugin;
import com.rschao.boss_battle.bossEvents;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OpenKitViewer implements Listener {
    // Mapea inventario abierto -> clave de kit
    private final Map<Inventory, String> openKits = new HashMap<>();
    private final Map<Inventory, Player> openEditors = new HashMap<>();

    public static CommandAPICommand Load(){
        // Registrar listener y devolver comando
        OpenKitViewer listener = new OpenKitViewer();
        Bukkit.getServer().getPluginManager().registerEvents(listener, Plugin.getPlugin(Plugin.class));

        CommandAPICommand cmd = new CommandAPICommand("viewkit")
                .withPermission("gaster.items")
                // Sobrecarga por fase (entero)
                .withArguments(new IntegerArgument("phase")).executes((CommandSender sender, CommandArguments args) -> {
                    if(!(sender instanceof Player)){
                        sender.sendMessage("Este comando solo puede ser ejecutado por un jugador.");
                        return;
                    }
                    Player p = (Player) sender;
                    int phase = (Integer) args.get("phase");
                    // Obtener clave de kit desde el archivo del boss
                    FileConfiguration bossCfg = bossEvents.getConfig();
                    String kitKey = bossCfg.getString("boss.world." + phase + ".kit");
                    if(kitKey == null || kitKey.isEmpty()){
                        p.sendMessage("No se encontró un kit para la fase " + phase);
                        return;
                    }
                    listener.openEditor(p, kitKey);
                });
        return cmd;
    }

    private void openEditor(Player opener, String key){
        // Cargar kits.yml
        File file = new File(Plugin.getPlugin(Plugin.class).getDataFolder(), "kits.yml");
        FileConfiguration cfg = new YamlConfiguration();
        try {
            if(!file.exists()) file.createNewFile();
            cfg.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            opener.sendMessage("Error al cargar kits.yml");
            return;
        }

        // Crear inventario editor: 54 ranuras (36 inventario + espacios para armadura/offhand)
        Inventory inv = Bukkit.createInventory(null, 54, "Kit Editor: " + key);

        // Rellenar slots principales (0..35)
        int mainSize = 36; // tamaños estándar del inventario del jugador
        for(int i = 0; i < mainSize; i++){
            ItemStack item = cfg.getItemStack(key + ".inv." + i);
            if(item != null){
                inv.setItem(i, item);
            } else {
                inv.setItem(i, null);
            }
        }

        // Asignar armadura/offhand a ranuras designadas dentro del editor
        // Mapeo interno: helmet->36, chest->37, legs->38, boots->39, offhand->40
        inv.setItem(36, cfg.getItemStack(key + ".inv.helmet"));
        inv.setItem(37, cfg.getItemStack(key + ".inv.chestplate"));
        inv.setItem(38, cfg.getItemStack(key + ".inv.leggings"));
        inv.setItem(39, cfg.getItemStack(key + ".inv.boots"));
        inv.setItem(40, cfg.getItemStack(key + ".inv.offhand"));

        // Abrir inventario para el jugador y guardar mapeos
        opener.openInventory(inv);
        openKits.put(inv, key);
        openEditors.put(inv, opener);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent ev){
        Inventory inv = ev.getInventory();
        if(!openKits.containsKey(inv)) return;

        String key = openKits.remove(inv);
        Player editor = openEditors.remove(inv);

        // Cargar kits.yml para sobrescribir valores
        File file = new File(Plugin.getPlugin(Plugin.class).getDataFolder(), "kits.yml");
        FileConfiguration cfg = new YamlConfiguration();
        try {
            if(!file.exists()) file.createNewFile();
            cfg.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            if(editor != null) editor.sendMessage("Error al guardar el kit: no se pudo cargar kits.yml");
            return;
        }

        // Guardar slots principales 0..35
        int mainSize = 36;
        for(int i = 0; i < mainSize; i++){
            ItemStack item = inv.getItem(i);
            if(item != null){
                cfg.set(key + ".inv." + i, item);
            } else {
                cfg.set(key + ".inv." + i, null);
            }
        }

        // Guardar armaduras/offhand desde las ranuras utilizadas en el editor
        cfg.set(key + ".inv.helmet", inv.getItem(36));
        cfg.set(key + ".inv.chestplate", inv.getItem(37));
        cfg.set(key + ".inv.leggings", inv.getItem(38));
        cfg.set(key + ".inv.boots", inv.getItem(39));
        cfg.set(key + ".inv.offhand", inv.getItem(40));

        // Persistir archivo
        try {
            cfg.save(file);
            if(editor != null) editor.sendMessage("Kit \"" + key + "\" guardado correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
            if(editor != null) editor.sendMessage("Error al guardar el kit en disco.");
        }
    }
}
