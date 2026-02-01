package com.rschao.boss_battle;

import com.rschao.api.audio.AudioSelector;
import com.rschao.items.Hands;
import com.rschao.items.Items;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.rschao.Plugin;
import com.rschao.events.soulEvents;
import com.rschao.events.definitions.BossChangeEvent;
import com.rschao.events.definitions.BossEndEvent;
import com.rschao.events.definitions.BossStartEvent;
import com.rschao.items.weapons;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class bossEvents implements Listener {
    public static int bossPhase;
    public static boolean bossActive;
    public static int maxPhase;
    public static String configName;
    static List<Player> bossPlayers = new ArrayList<>();
    static BossPlayerInfo bossinfo;
    public static int killCount = 0;
    public static Player daboss;
//todo: fix boss bugs and Xv1 issues
    public static FileConfiguration getConfig() {
        configName = Plugin.getPlugin(Plugin.class).getConfig().getString("boss.event.name");
        File configFile = new File(Plugin.getPlugin(Plugin.class).getDataFolder() + "/bosses/", configName + ".yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return YamlConfiguration.loadConfiguration(configFile);
    }

    public static Player[] getArrayPlayers(){
        Player[] bossPlayers = new Player[bossEvents.bossPlayers.size()];
        bossEvents.bossPlayers.toArray(bossPlayers);
        return bossPlayers;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    void onPlayerNextPhase(EntityDamageEvent ev){
        if(!(ev.getEntity() instanceof Player)) return;
        Player player = (Player) ev.getEntity();
        FileConfiguration config = getConfig();
        maxPhase = config.getInt("boss.kits");
        //check if the damage the entity took was greater than the entity's health
        if(ev.getFinalDamage() < player.getHealth()){
            return;
        }
        //Detect when a player with specific permission gaster.boss is killed by any other player
        //detect if the player has a totem of undying or weapons.CorruptedHeart in their offhand. Return if they do
        if(ev.getEntity() instanceof Player){
            if(player.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING || player.getInventory().getItemInOffHand().getType() == weapons.CorruptedHeart.getType()){
                return;
            }
        }
        if(player.equals(daboss) && bossActive){
            if(bossPhase > maxPhase){
                //remove the boss's permission and return
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission unset gaster.boss");
                player.sendMessage("You have been defeated");
                return;
            }
            ev.setCancelled(true);
            daboss = player;
            bossPhase++;
            
            Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), new Runnable() {
                @Override
                public void run() {
                    executeBossPhase(bossPhase);
                    BossChangeEvent event = new BossChangeEvent(configName, bossPhase, player, getArrayPlayers());
                    Bukkit.getServer().getPluginManager().callEvent(event);
                }
            }, 2);
        }
    }
    @EventHandler
    void PlayerDed(PlayerDeathEvent ev){
        Player player = ev.getEntity();
        Player killer = player.getKiller();
        FileConfiguration config = getConfig();
        if(!player.equals(daboss)){
            killCount++;
            if(killCount >= bossPlayers.size()){
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission unset gaster.boss");
                killCount = 0;
                killer.sendMessage("You have defeated all enemies");
            }
        }
        if(bossActive && bossPhase >= maxPhase){
            //remove the boss's permission and return
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission unset gaster.boss");
            player.sendMessage("You have been defeated");
            if(config.contains("boss.victory.tp")) {
                //teleport the attacker to the world and coords in the config file in key boss.victory.tp without the TeleportToBoss method
                String world = config.getString("boss.victory.tp.world");
                double x = config.getDouble("boss.victory.tp.x");
                double y = config.getDouble("boss.victory.tp.y");
                double z = config.getDouble("boss.victory.tp.z");
                World tpWorld = Bukkit.getWorld(world);
                bossPhase = 0;
                bossActive = false;
                Location loc = new Location(tpWorld, x, y, z);
                for(Player p: bossPlayers){
                    if(p.getUniqueId().equals(player.getUniqueId())) continue;

                    p.teleport(loc);
                    p.teleport(loc);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tpworld " + world + " " + p.getName());
                    Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), () -> {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tp " + killer.getName() + " " + x + " " + y + " " + z);

                    }, 2);
                }
            }

            //call the BossEndEvent
            BossEndEvent event = new BossEndEvent(configName, bossPhase, player, getArrayPlayers());
            Bukkit.getServer().getPluginManager().callEvent(event);
            String rewardName = configName.replace('/', '.');
            player.sendMessage(rewardName);
            List<ItemStack> drops = ev.getDrops();
            List<ItemStack> books = new ArrayList<>();
            for(ItemStack i : drops){
                if(i.getType().toString().contains("BOOK")) books.add(i);
                if(i.getType().toString().contains("DRAGON")) books.add(i);
                if(i.getType().toString().contains("BOX")) books.add(i);
                if(i.getType().toString().contains("HOOK")) books.add(i);
                if(i.getType().toString().contains("ELYTRA")) books.add(i);
                if(i.getType().toString().contains("PAPER")) books.add(i);
                if(i.getType().toString().contains("WART")) books.add(i);
                if(i.getType().toString().contains("POWDER")) books.add(i);
            }
            drops.clear();
            for(ItemStack i : books) drops.add(i);
            switch(rewardName){
                case "brokensoul.Lore_hands_1":
                    drops.add(Hands.EmptyHand);
                    break;
                case "brokensoul.Lore_delta_1":
                    drops.add(Items.GasterBlaster);
                    drops.add(Items.XKnife);
                    break;
                case "brokensoul.Lore_secret_oblivion":
                    drops.add(weapons.RustedSword);
                    break;
                case "brokensoul.Lore_secret_seven":
                    drops.add(weapons.SevenSouls);
                    break;
                case "bossrush.double_fruit":
                    for(Player p : bossPlayers){
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user %target% permission set fruits.second true".replace("%target%", p.getName()));
                    }
                    break;
                case "s3lore.portal":
                    for(Player p : bossPlayers){
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tag %target% add Showdown".replace("%target%", p.getName()));
                    }
                    break;
                case "s3lore.pale_hunter":
                    drops.add(new ItemStack(Material.PLAYER_HEAD));
                    break;
                case "extra.eval":
                    drops.clear();
                    break;
            }
        }
        else if(bossActive){
            //remove permission gaster.boss from all players
            Bukkit.getOnlinePlayers().forEach((p) -> {
                if(p.hasPermission("gaster.boss")){
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " permission unset gaster.boss");
                }
            });

            bossActive = false;
            bossPhase = 0;
        }
    }

    public static Player getBossPlayer(){
        for(Player p : Bukkit.getOnlinePlayers()){
            if(p.hasPermission("gaster.boss")){
                return p;
            }
        }
        return null;
    }
    public static void executeBossPhase(int phase) {
        daboss = getBossPlayer();
        FileConfiguration config = getConfig();
        String world = config.getString("boss.world." + phase + ".name");

        TeleportToBoss(world, config.getDouble("boss.world." + phase + ".x"), config.getDouble("boss.world." + phase + ".y"), config.getDouble("boss.world." + phase + ".z"), config);
        Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), new Runnable() {
            @Override
            public void run() {
                PhaseDialogue(phase, config);
                Bukkit.getOnlinePlayers().forEach((p) -> {
                    if(!bossActive) return;
                    if(p.hasPermission("gaster.boss")){
                        InvManager.LoadInventory(p, config.getString("boss.world." + phase + ".kit"));
                        //heal the player
                        int i = config.getInt("boss.world." + phase + ".health") *2;
                        if(i <= 0) i = 60;
                        p.getAttribute(Attribute.MAX_HEALTH).setBaseValue(i);
                        p.setHealth(p.getAttribute(Attribute.MAX_HEALTH).getValue());
                        //restore hunger and saturation
                        p.setFoodLevel(20);
                        p.setSaturation(20);
                        String music = config.getString("boss.world." + phase + ".music");
                        if(music != null && !music.isEmpty()) {
                            bossMusic(music);
                        }
                    }
                });
            }
        }, 5);
        BossChangeEvent event = new BossChangeEvent(configName, bossPhase, daboss, getArrayPlayers());
        Bukkit.getServer().getPluginManager().callEvent(event);
    }

    
    static void TeleportToBoss(String worldName, double bossX, double bossY, double bossZ, FileConfiguration config) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return; // World not found
        }
        // Set the boss's coordinates
        Location bossLocation = new Location(world, bossX, bossY, bossZ);
        // Get all online players
        Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
        Location loc = new Location(world, bossX, bossY, bossZ);
        for(Player p:Bukkit.getOnlinePlayers()){
            if(p.hasPermission("gaster.boss")){
                loc = p.getLocation();
                break;
            }
        }
        Player[] temp = new Player[players.length];
            for(int i = 0; i < players.length; i++){
                Player p = players[i];
                if(p.getLocation().toVector().distance(loc.toVector()) <= 50){
                    temp[i] = p;
                }
            }
            if (bossPhase < 2) bossPlayers = new ArrayList<>(List.of(temp));
        if(bossPlayers == null || bossPlayers.size() <= 1){
            bossActive = false;
            bossPhase = 0;
            return;
        }

        // Calculate the coordinates of the players
        int numPlayers = bossPlayers.size();
        double angleIncrement = 2 * Math.PI / numPlayers;
        double radius = 5.0; // Distance from the boss
        for (int i = 0; i < numPlayers; i++) {
            Player player = bossPlayers.get(i);
            if(player == null) continue;
            if (!player.equals(daboss)) {
                double angle = i * angleIncrement;
                double x = bossLocation.getX() + radius * Math.cos(angle);
                double z = bossLocation.getZ() + radius * Math.sin(angle);
                Location playerLocation = new Location(world, x, bossLocation.getY(), z);
                player.teleport(playerLocation);
                player.teleport(playerLocation);
                /*Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), () ->{
                    player.teleport(playerLocation);
                    player.sendMessage("test");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tpworld " + worldName + " " + player.getName());
                    Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), () ->{
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tp " + player.getName() + " " + x + " " + bossY + " " + z);
                    }, 2);
                }, 2); */
            } else {
                player.teleport(bossLocation);
                player.teleport(bossLocation);
                /*Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), () ->{
                    player.sendMessage("test");
                    player.teleport(bossLocation);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tpworld " + worldName + " " + player.getName());
                    Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), () ->{
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tp " + player.getName() + " " + bossX + " " + bossY + " " + bossZ);
                    }, 2);
                }, 2); */
            }
        }
    }
    static void PhaseDialogue(int phase, FileConfiguration config){
        if(!config.contains("boss.world." + phase + ".dialogue")) return;
        if(config.get("boss.world." + phase + ".dialogue") instanceof String){
            AudioSelector.PlayBossAudio(config.getString("boss.world." + phase + ".dialogue"), getArrayPlayers());
        }
        else {
            for(Player p : Bukkit.getOnlinePlayers()){
                p.getName();
                for(String s : config.getStringList("boss.world." + phase + ".dialogue")){
                    //get the index of the string in the list
                    int index = config.getStringList("boss.world." + phase + ".dialogue").indexOf(s);
                    Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), () -> {
                        p.sendMessage(s);
                    }, 20*index);
                }
            }
        }
    }
    static void bossMusic(String music){
        AudioSelector.PlayBossAudio(music, getArrayPlayers());
    }
    @EventHandler
    void onBossStart(BossStartEvent ev){
        int[] souls = new int[2];
        souls[0] = soulEvents.GetSoulN(ev.getBossPlayer());
        souls[1] = soulEvents.GetSecondSoulN(ev.getBossPlayer());
        BossPlayerInfo info = new BossPlayerInfo(ev.getBossPlayer().getName(),((int)ev.getBossPlayer().getAttribute(Attribute.MAX_HEALTH).getValue()), souls, null);
        bossinfo = info;
    }
    @EventHandler
    void onBossEnd(BossEndEvent ev){
        BossPlayerInfo info = bossinfo;
        if(info == null) return;
        if(!ev.getBossPlayer().getName().equals(info.name)) return;
        ev.getBossPlayer().getAttribute(Attribute.MAX_HEALTH).setBaseValue(info.health);
        soulEvents.setSouls(ev.getBossPlayer(), info.souls[0], info.souls[1]);
    }
}
