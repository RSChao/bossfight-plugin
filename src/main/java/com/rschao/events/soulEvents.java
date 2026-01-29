package com.rschao.events;

import java.io.File;
import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.rschao.Plugin;
import com.rschao.api.SoulType;
import com.rschao.items.Hands;
import com.rschao.items.Items;

import net.md_5.bungee.api.ChatColor;

public class soulEvents implements Listener {
    public static final File confFile = new File(Plugin.getPlugin(Plugin.class).getDataFolder(), "souls.yml");
    boolean showMessage = true;
    public static boolean opposite;
    public static String soulName;
    public static boolean allowSecondSoul = false; // New variable to enable/disable second soul
    static Map<UUID, Boolean> soullessMap = new HashMap<>();

    public static HashMap<Integer, String> soulItems = new HashMap<>();
    public static HashMap<UUID, Set<Integer>> playerSouls = new HashMap<>();
    public static boolean drop = false;
    static {
        soulItems.put(18, "Hate");
        soulItems.put(29, "Love");
        soulItems.put(30, "Emptiness");
        soulItems.put(31, "Hope");
    }

    public static void setSoulless(Player p, boolean value) {
        soullessMap.put(p.getUniqueId(), value);
    }
    public static void setSoullessTimed(Player p, boolean value, int ticks) {

        soullessMap.put(p.getUniqueId(), value);
        Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), () -> {
            soullessMap.put(p.getUniqueId(), !value);
                }, ticks);
    }
    public static boolean isSoulless(Player p) {
        return soullessMap.getOrDefault(p.getUniqueId(), false);
    }

    @EventHandler
    void OnPlayerUseSoul(PlayerInteractEvent ev) {
        if(!ev.getPlayer().hasPermission("gaster.soul")) return;
        if(ev.getPlayer().hasPermission("gaster.soul.second")) allowSecondSoul = true;
        else allowSecondSoul = false;
        if(ev.getItem() == null) return;
        Player playerPdc = ev.getPlayer();
        int soul = 0;
        soul = GetSoulN(ev.getItem());
        if(ev.getItem().getItemMeta().getPersistentDataContainer().has(Hands.SoulCleanerBuffedKey, PersistentDataType.BOOLEAN)) return;
        if(ev.getItem().getItemMeta().getPersistentDataContainer().has(Hands.SoulCleanerKey, PersistentDataType.BOOLEAN)) return;
        //check if the item is a soul item
        if(!isSoulItem(ev.getItem())) return;
        // Check if the player already has a soul
        if (GetSoulN(playerPdc) == SoulType.NEUTRAL.getId()) {
            ev.getPlayer().sendMessage("You already own the power of Neutral, no other soul can be added.");
            return;
        }
        if((soul == SoulType.PUREHEART.getId() && GetSoulN(playerPdc) == SoulType.CHAOSHEART.getId()) || soul == SoulType.CHAOSHEART.getId() && GetSoulN(playerPdc) == SoulType.PUREHEART.getId()){
            ev.getPlayer().sendMessage("These two souls cannot be together");
            return;
        }
        if(GetSoulN(playerPdc) == soul){
            ev.getPlayer().sendMessage("You may not own two of the same soul");
            return;
        }
        if (soul == SoulType.NEUTRAL.getId()) {
            if (GetSoulN(playerPdc) != -1 || GetSecondSoulN(playerPdc) != -1) {
                ev.getPlayer().sendMessage("You cannot add the Neutral soul while owning another soul.");
                return;
            }
            setSouls(playerPdc, -5, -1);
            ev.getPlayer().sendMessage("You now own the power of Neutral.");
            reduceItemAmount(ev);
            return;
        }
        if (GetSoulN(playerPdc) != -1 && GetSecondSoulN(playerPdc) != -1 && allowSecondSoul) {
            ev.getPlayer().sendMessage("You already own the power of " + GetSoulName(playerPdc) + " and " + GetSecondSoulName(playerPdc));
            return;
        } else if (GetSoulN(playerPdc) != -1 && !allowSecondSoul) {
            ev.getPlayer().sendMessage("You already own the power of " + GetSoulName(playerPdc));
            return;
        }

        // Assign the soul to the player
        if (GetSoulN(playerPdc) > -1 && GetSecondSoulN(playerPdc) < 0) {
            setSouls(playerPdc, GetSoulN(playerPdc), soul);
        } else if (GetSoulN(playerPdc) == -1) {
            setSouls(playerPdc, soul, -1);
        }
        ev.getPlayer().sendMessage("You now own the power of " + GetSoulName(ev.getItem()));
        reduceItemAmount(ev);
    }

    private void reduceItemAmount(PlayerInteractEvent ev) {
        Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), () -> {
            int newAmount = ev.getItem().getAmount() - 1;
            ev.getItem().setAmount(newAmount);
        }, 2);
    }

    @EventHandler
    void OnPlayerHit(EntityDamageByEntityEvent ev) {
        if(ev.getEntity() instanceof Player && ev.getDamager() instanceof Player){
            Player v = (Player) ev.getEntity();
            Player d = (Player) ev.getDamager();
            if(isSoulless(d)) return;
            int VsoulN = GetSoulN(v);
            int DsoulN = GetSoulN(d);
            int VSecondSoulN = v.hasPermission("gaster.soul.second") ? GetSecondSoulN(v) : -1;
            int DSecondSoulN = d.hasPermission("gaster.soul.second") ? GetSecondSoulN(d) : -1;
            if(DsoulN == -1) return;
            soulName = GetSoulName(d);
            opposite = GetSoulDifferences(DsoulN, VsoulN) || GetSoulDifferences(DsoulN, VSecondSoulN) || GetSoulDifferences(DSecondSoulN, VsoulN) || GetSoulDifferences(DSecondSoulN, VSecondSoulN);
            if(isSoulless(v)) opposite = true;
            if(opposite || (GetSoulN(v) == -1)) {
                ev.setDamage(ev.getDamage() + 5);
                if (allowSecondSoul && DSecondSoulN != -1) {
                    ev.setDamage(ev.getDamage() + 5);
                }
                //show message to players, ensuring that it wont repeat to the same player for 3 minutes
                if(showMessage){
                    String victimSouls = GetSoulName(v);
                    if (VSecondSoulN != -1) {
                        victimSouls += " and " + GetSecondSoulName(v);
                    }
                    String damagerSouls = GetSoulName(d);
                    if (DSecondSoulN != -1) {
                        damagerSouls += " and " + GetSecondSoulName(d);
                    }
                    d.sendMessage("You inflicted extra damage to your opponent's soul (" + victimSouls + ChatColor.RESET + ")");
                    v.sendMessage("You received extra damage by your opponent's soul (" + damagerSouls + ChatColor.RESET + ")");
                    showMessage = false;
                    Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), new Runnable() {
                        @Override
                        public void run(){
                            showMessage = true;
                        }
                    }, 1800);
                }
            }
            else {
                if ((SoulType.getById(DsoulN).getTier() == 3 || SoulType.getById(DSecondSoulN).getTier() == 3) && (SoulType.getById(VsoulN).getTier() == 1 || SoulType.getById(VSecondSoulN).getTier() == 1)) {
                    ev.setDamage(ev.getDamage() + 2);
                    if(showMessage){
                    String victimSouls = GetSoulName(v);
                    if (VSecondSoulN != -1) {
                        victimSouls += " and " + GetSecondSoulName(v);
                    }
                    String damagerSouls = GetSoulName(d);
                    if (DSecondSoulN != -1) {
                        damagerSouls += " and " + GetSecondSoulName(d);
                    }
                    d.sendMessage("You inflicted extra damage to your opponent's soul (" + victimSouls + ChatColor.RESET + ")");
                    v.sendMessage("You received extra damage by your opponent's soul (" + damagerSouls + ChatColor.RESET + ")");
                    showMessage = false;
                    Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), new Runnable() {
                        @Override
                        public void run(){
                            showMessage = true;
                        }
                    }, 1800);
                }
                }
                if ((SoulType.getById(DsoulN).getTier() == 4 || SoulType.getById(DSecondSoulN).getTier() == 4) && (SoulType.getById(VsoulN).getTier() <= 3 || SoulType.getById(VSecondSoulN).getTier() <=3)) {
                    ev.setDamage(ev.getDamage() + 2);
                    if(showMessage){
                    String victimSouls = GetSoulName(v);
                    if (VSecondSoulN != -1) {
                        victimSouls += " and " + GetSecondSoulName(v);
                    }
                    String damagerSouls = GetSoulName(d);
                    if (DSecondSoulN != -1) {
                        damagerSouls += " and " + GetSecondSoulName(d);
                    }
                    d.sendMessage("You inflicted extra damage to your opponent's soul (" + victimSouls + ChatColor.RESET + ")");
                    v.sendMessage("You received extra damage by your opponent's soul (" + damagerSouls + ChatColor.RESET + ")");
                    showMessage = false;
                    Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), new Runnable() {
                        @Override
                        public void run(){
                            showMessage = true;
                        }
                    }, 1800);
                }
                }
            }
            
        } 
    }

    public static int GetSoulN(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return -1;
        if (item.getItemMeta() == null) return -1;
        if (item.getItemMeta().getPersistentDataContainer() == null) return -1;
        if (item.getItemMeta().getPersistentDataContainer().isEmpty()) return -1;
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        for (NamespacedKey key : pdc.getKeys()) {
            SoulType soul = SoulType.getByKey(key.getKey());
            if (soul != null) {
                return soul.getId();
            }
        }
        return -1; // Default to no soul
    }
    public static int GetSoulN(Player p) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(confFile);
        String playerName = p.getName();
        if (playerName.startsWith(".")) {
            playerName = playerName.substring(1);
        }
        if(config.get("souls." + playerName) == null) return -1;
        int soulN = config.getIntegerList("souls." + playerName).get(0);
        return soulN;
    }

    public static int GetSecondSoulN(Player p) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(confFile);
        String playerName = p.getName();
        if (playerName.startsWith(".")) {
            playerName = playerName.substring(1);
        }
        if(config.get("souls." + playerName) == null) return -1;
        int soulN = config.getIntegerList("souls." + playerName).get(1);
        return soulN;
    }
    public static void setSouls(Player p, int id, int id2) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(confFile);
        String playerName = p.getName();
        if (playerName.startsWith(".")) {
            playerName = playerName.substring(1);
        }
        List<Integer> souls = List.of(id, id2);
        config.set("souls." + playerName, souls);
        try {
            config.save(confFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String GetSoulName(Player p) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(confFile);
        String playerName = p.getName();
        if (playerName.startsWith(".")) {
            playerName = playerName.substring(1);
        }
        if(config.get("souls." + playerName) == null) return "None";
        int soulN = config.getIntegerList("souls." + playerName).get(0);
        if (soulN == -1) return "None";
        return SoulType.getById(soulN).getDisplayName();
    }
    public static String GetSoulName(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return "None";
        if (item.getItemMeta() == null) return "None";
        if (item.getItemMeta().getPersistentDataContainer() == null) return "None";
        if (item.getItemMeta().getPersistentDataContainer().isEmpty()) return "None";
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        for (NamespacedKey key : pdc.getKeys()) {
            SoulType soul = SoulType.getByKey(key.getKey());
            if (soul != null) {
                return soul.getDisplayName();
            }
        }
        return "None"; // Default to no soul
    }

    public static String GetSecondSoulName(Player p) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(confFile);
        String playerName = p.getName();
        if (playerName.startsWith(".")) {
            playerName = playerName.substring(1);
        }
        if(config.get("souls." + playerName) == null) return "None";
        int soulN = config.getIntegerList("souls." + playerName).get(1);
        if (soulN == -1) return "None";
        return SoulType.getById(soulN).getDisplayName();
    }

    boolean GetSoulDifferences(int soulD, int soulV) {
        return SoulType.areOpposite(soulD, soulV);
    }
    public static void clearSouls(Player p) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(confFile);
        String playerName = p.getName();
        if (playerName.startsWith(".")) {
            playerName = playerName.substring(1);
        }
        if(config.get("souls." + playerName) == null) return;
        config.set("souls." + playerName, null);
        try {
            config.save(confFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    void onPlayerDed(PlayerDeathEvent ev){
        //check the player's soul and, if a config value soul.drop is true, spawn one or both souls at the player's death location
        if(ev.getEntity().getKiller() instanceof Player){
            Player v = ev.getEntity();
            Player d = ev.getEntity().getKiller();
            int VsoulN = GetSoulN(v);
            int DsoulN = GetSoulN(d);
            int VSecondSoulN = v.hasPermission("gaster.soul.second") ? GetSecondSoulN(v) : -1;
            if(DsoulN == -1) return;
            soulName = GetSoulName(d);
            FileConfiguration config = Plugin.getPlugin(Plugin.class).getConfig();
            drop = config.getBoolean("soul.drop");
            if(drop) {
                if (VSecondSoulN != -1) {
                    ItemStack i = Items.getSoulItem(VSecondSoulN);
                    if(i != null) v.getWorld().dropItemNaturally(v.getLocation(), i);
                    
                }
                ItemStack i = Items.getSoulItem(VsoulN);
                if(i != null) v.getWorld().dropItemNaturally(v.getLocation(), i);
                clearSouls(v);
            }
        }
    }
    public static boolean isSoulItem(ItemStack i){
        if(i == null || i.getType() == Material.AIR) return false;
        if (i.getType() != Material.LEATHER && i.getType() != Material.TURTLE_SCUTE && i.getType() != Material.NETHER_STAR) return false;
        if (i.getItemMeta() == null) return false;
        if (i.getItemMeta().getPersistentDataContainer() == null) return false;
        if (i.getItemMeta().getPersistentDataContainer().isEmpty()) return false;
        PersistentDataContainer pdc = i.getItemMeta().getPersistentDataContainer();
        for (NamespacedKey key : pdc.getKeys()) {
            SoulType soul = SoulType.getByKey(key.getKey());
            if (soul != null) {
                return true;
            }
        }
        return false; // Default to no soul
    }

    public static boolean hasSoul(Player p, int soulId){
        FileConfiguration config = YamlConfiguration.loadConfiguration(confFile);
        String playerName = p.getName();
        if (playerName.startsWith(".")) {
            playerName = playerName.substring(1);
        }
        if(config.get("souls." + playerName) == null) return false;
        List<Integer> souls = config.getIntegerList("souls." + playerName);
        return souls.contains(soulId);
    }
}
