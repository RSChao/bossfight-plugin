package com.rschao.events;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.rschao.enchants.GenoEnchant;
import com.rschao.enchants.GlitchEnchant;
import com.rschao.enchants.OblivionEnchant;
import com.rschao.enchants.WitherEnchant;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Warden;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.joml.Random;

import com.rschao.Plugin;
import com.rschao.boss_battle.InvManager;
import com.rschao.events.definitions.BossChangeEvent;
import com.rschao.events.definitions.BossEndEvent;
import com.rschao.events.definitions.BossStartEvent;
import com.rschao.items.Items;
import com.rschao.items.weapons;
import com.rschao.projectiles.DeterminationProjectile;
import com.rschao.projectiles.GasterBlasterProjectile;
import com.rschao.smp.events.CustomRecipeChecker;

import net.md_5.bungee.api.ChatColor;

public class events implements Listener {
    private final Map<UUID, Long> shootTimes = new HashMap<>();
    public static Map<UUID, Long> genoCooldown = new HashMap<>();
    public static final String path = Plugin.getPlugin(Plugin.class).getDataFolder().getAbsolutePath();
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player user = event.getPlayer();
            
        if(event.getItem() == null) return;
        // Check if the item used is the special item
        PersistentDataContainer pdc = event.getItem().getItemMeta().getPersistentDataContainer();
        if(pdc.has(new NamespacedKey("gaster", "blaster"))){
            Player player = event.getPlayer();
            if (event.getItem() != null && pdc.has(new NamespacedKey("gaster", "blaster"))) {
                Long lastShot = this.shootTimes.get(user.getUniqueId());
                long seconds = System.currentTimeMillis()/1000L;
                if(lastShot != null && seconds-lastShot < 5){
                    long time = seconds-lastShot;
                    user.sendMessage(5 - time + "s   ");
                    return;
                }
                shootTimes.put(user.getUniqueId(), seconds);
                shootGasterBlaster(player);
            }
        }
        if(pdc.has(new NamespacedKey("gaster", "omega"))){
            Player player = event.getPlayer();
            Long lastShot = this.shootTimes.get(user.getUniqueId());
            long seconds = System.currentTimeMillis()/1000L;
                if(lastShot != null && seconds-lastShot < 5){
                    long time = seconds-lastShot;
                    user.sendMessage(5 - time + "s   ");
                    return;
                }
            shootTimes.put(user.getUniqueId(), seconds);
            DeterminationProjectile proj = new DeterminationProjectile(player.getLocation(), player);
            proj.launch();
            Vector direction = player.getEyeLocation().getDirection();
            if(player.hasPermission("omega.kinektos")){
                
            player.setVelocity(direction.multiply(-4));
            }
            else{
                player.setVelocity(direction.multiply(-2));
            }
        }
        if(pdc.has(new NamespacedKey("gaster", "cleaner"))){
            Player player = event.getPlayer();
            //int maxSoulKeys = SoulType.getSoulAmount(); // Dynamically fetch the max number of soul keys
            soulEvents.clearSouls(player);
            player.sendMessage("The Soul you wore now rests in peace");
        }
        if(pdc.has(new NamespacedKey("gaster", "changer"))){
            Player player = event.getPlayer();
            pdc = player.getPersistentDataContainer();
            if(soulEvents.GetSecondSoulN(player) != -1 && soulEvents.allowSecondSoul){
                player.getInventory().addItem(Items.getSoulItem(soulEvents.GetSecondSoulN(player)));
                soulEvents.setSouls(player, soulEvents.GetSoulN(player), -1);
                player.sendMessage("The second Soul you wore now rests in your hands"); 
             
            } else if(soulEvents.GetSoulN(player) != -1){
                player.getInventory().addItem(Items.getSoulItem(soulEvents.GetSoulN(player)));
                soulEvents.setSouls(player, -1, -1);
                player.sendMessage("The Soul you wore now rests in your hands");
            } else {
                player.sendMessage("You do not have any souls to change.");
            }
        }
        Player p = event.getPlayer();
        if(event.getItem().getItemMeta().getPersistentDataContainer().has(weapons.SEKey, PersistentDataType.INTEGER)){
            Location loc = p.getLocation().clone();
            loc.add(events.getRNG(-2, 2), 0, events.getRNG(-2, 2));
            int t = getTest(event.getItem(), weapons.SEKey);
            double health = p.getHealth();
            p.setHealth(1);
            Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), () -> {
                p.teleport(loc);
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5*20, 3));
                p.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 5*20, 255));
            }, 2);
            Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), () -> p.setHealth(health), 30);
            t += 1;
            setTest(weapons.SEKey, event.getItem(), t);
            if(t > 10) event.getItem().setAmount(event.getItem().getAmount() - 1);
        }
        if(event.getItem().getItemMeta().getPersistentDataContainer().has(weapons.BEKey, PersistentDataType.INTEGER)){
            Location loc = p.getLocation().clone();
            loc.add(events.getRNG(-2, 2), 0, events.getRNG(-2, 2));
            int t = getTest(event.getItem(), weapons.BEKey);
            double health = p.getHealth();
            p.setHealth(1);
            Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), () -> {
                p.teleport(loc);
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10*20, 3));
            }, 2);
            Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), () -> p.setHealth(health), 20);
            t += 1;
            setTest(weapons.BEKey, event.getItem(), t);
            if(t > 20) event.getItem().setAmount(event.getItem().getAmount() - 1);
        }
        if(event.getItem().getItemMeta().getPersistentDataContainer().has(Items.SCKey, PersistentDataType.BOOLEAN)){
            if(event.getPlayer().hasPermission("gaster.soul")) return;
            event.getItem().setAmount(event.getItem().getAmount() - 1);
            //give the player the gaster.soul permission
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + event.getPlayer().getName() + " permission set gaster.soul true");
            event.getPlayer().sendMessage(ChatColor.DARK_RED + "You have been granted the power to take a soul");
        }
    }
    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity victim = event.getEntity();
        if(damager instanceof Fireball && damager.getCustomName().equalsIgnoreCase("blasterBall")){
            event.setCancelled(true);
            Player player = (Player) victim;
            player.damage(20 / (1 - 0.56));
        }
        if(damager instanceof Fireball && damager.getCustomName().equalsIgnoreCase("determinationBall")){
            event.setCancelled(true);
            Player player = (Player) victim;
            player.damage(22 / (1 - 0.64));
        }
    }

    @EventHandler
    void OnPlayerDeath(PlayerDeathEvent ev){
        if(ev.getEntity().getKiller() == null) return;
        Player killer = ev.getEntity().getKiller();
        
        Player victim = ev.getEntity();
        //ev.getDrops().add(Items.DeterminationCrystal);
        if(killer == null) return;
        if(killer.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(new NamespacedKey("gaster", "geno_knife"))){
            ev.setDeathMessage(ChatColor.RED + victim.getName() + ChatColor.DARK_RED + " tasted real pain from " + ChatColor.RED + killer.getName() + ChatColor.DARK_RED + " :)");
        }
        else if(soulEvents.opposite){
            if(soulEvents.GetSecondSoulN(killer) == -1 && soulEvents.GetSoulN(killer) != -1){
                ev.setDeathMessage(victim.getName() + " was killed by " + killer.getName() + " with the power of " + soulEvents.GetSoulName(killer));
            }
            else if(soulEvents.GetSecondSoulN(killer) != -1 && soulEvents.GetSoulN(killer) == -1){
                ev.setDeathMessage(victim.getName() + " was killed by " + killer.getName() + " with the power of " + soulEvents.GetSoulName(killer) + " and " + soulEvents.GetSecondSoulName(killer));
            }
        }
        //check if the player was killed by an endermite
        if(victim.getKiller() != null){
            if(victim.getKiller().getType() == EntityType.ENDERMITE){
                //check if the endermite's name is "Tickles"
                if(victim.getKiller().getCustomName() == null) return;
                if(victim.getKiller().getCustomName().equalsIgnoreCase("Tickles")){
                    ev.setDeathMessage(victim.getName() +" was tickled to death by a Tickles");
                }
            }
            if(victim.getKiller().getType() == EntityType.VEX){
                //check if the vex's name is "Flying Tickles"
                if(victim.getKiller().getCustomName() == null) return;
                if(victim.getKiller().getCustomName().equalsIgnoreCase("Flying Tickles")){
                    ev.setDeathMessage(victim.getName() +" was tickled to death by a Flying Tickles");
                }
            }
        }
        
    }

    @EventHandler
    void MobGoDed(EntityDeathEvent ev){
        if(ev.getEntity() instanceof Player){
            ev.getDrops().add(Items.DeterminationCrystal);
            return;
        }
        if(!(ev.getEntity() instanceof Warden)){ return; }
        int rng = getRNG(0, 100);
        if(rng > 50){
            ev.getDrops().add(Items.DeterminationCrystal);
            if(rng > 75){
                ev.getDrops().add(Items.DeterminationCrystal);
                if(rng > 95){
                    ev.getDrops().add(Items.DeterminationCrystal);
                }
            }
        }
        else{
            int random = getRNG(0, 500);
            if(random == 0){
                ev.getDrops().add(Items.DeterminationEssence);
                for (Player p : Bukkit.getOnlinePlayers()){
                    p.sendMessage("A Determined Warden has given up its remnants");
                }
            }
        }

    }
    public static ItemStack buffsword;
    static{
        ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setItemName(ChatColor.DARK_RED + "Sword of Chaos");
        meta.addEnchant(Enchantment.SHARPNESS, 10, true);
        meta.addEnchant(Enchantment.FIRE_ASPECT, 2, true);
        meta.addEnchant(Enchantment.LOOTING, 3, true);
        meta.addEnchant(Enchantment.UNBREAKING, 10, true);
        meta.setUnbreakable(true);
        meta.setItemModel(NamespacedKey.minecraft("domain_sword"));
        item.setItemMeta(meta);
        item = (new GenoEnchant()).addEnchant(item, 1);
        item = (new WitherEnchant()).addEnchant(item, 1);
        item = (new GlitchEnchant()).addEnchant(item, 1);
        item = (new OblivionEnchant()).addEnchant(item, 1);
        buffsword = item;

        // Registrar receta anvil para buffsword
        CustomRecipeChecker.addRecipe((left, right) -> {
            try {
                if (left != null && right != null
                    && left.hasItemMeta() && right.hasItemMeta()
                    && left.getItemMeta().getPersistentDataContainer().has(weapons.CSKey)
                    && right.getItemMeta().getPersistentDataContainer().has(weapons.DSKey)) {
                    return new ItemStack(buffsword);
                }
            } catch (Exception ignored) {}
            return null;
        });

        // Registrar receta de reparación para chearts
        CustomRecipeChecker.addRecipe((left, right) -> {
            try {
                if (left != null && right != null
                    && left.hasItemMeta() && right.hasItemMeta()
                    && left.getItemMeta().getPersistentDataContainer().has(weapons.CHKey)
                    && right.getItemMeta().getPersistentDataContainer().has(weapons.CEKey)) {
                    ItemStack x = weapons.CorruptedHeart.clone();
                    ItemMeta metax = x.getItemMeta();
                    int t = weaponEvents.getTest(x);
                    if(t < 3 && t>0){
                        metax.getPersistentDataContainer().set(weapons.CHKey, PersistentDataType.INTEGER, t-1);
                        metax.setItemName(ChatColor.DARK_RED + "Corrupted Heart");
                        if(t-1 > 0){
                            List<String> lore = meta.getLore();
                            lore.add(ChatColor.RED + "Times used: " + (t-1));
                            metax.setLore(lore);
                        }
                        x.setItemMeta(metax);
                        return x;
                    }
                }
            } catch (Exception ignored) {}
            return null;
        });
    }
    ItemStack cheart_revitalized;
    ItemStack chaos_katana;
    @EventHandler
    void AnvilRecipes(PrepareAnvilEvent ev){
        ItemStack[] items = ev.getInventory().getContents();
        if(items[0] == null) return;
        if(items[0].getItemMeta().getPersistentDataContainer().has(weapons.CSKey) && (items[1] == null || items[1].getType() == Material.AIR)){
            ItemStack item = new ItemStack(items[0]);
            ItemMeta meta = item.getItemMeta();
            if(meta.getItemModel().equals(NamespacedKey.minecraft("chaos_katana"))){
                meta.setItemModel(NamespacedKey.minecraft("domain_sword"));
            }
            else {
                meta.setItemModel(NamespacedKey.minecraft("chao_katana"));
            }
            item.setItemMeta(meta);
            ev.setResult(item);
            chaos_katana = item;
        }
        for(ItemStack i : items){
            if(i == null) return;
        }
        if(items[0].getItemMeta().getPersistentDataContainer().has(weapons.RSKey) && items[1].getItemMeta().getPersistentDataContainer().has(weapons.FCKey)){
            ev.setResult(weapons.AwakenedSword);
        }
        else if(items[0].getItemMeta().getPersistentDataContainer().has(weapons.CSKey) && items[1].getItemMeta().getPersistentDataContainer().has(weapons.DSKey)){
            ItemStack item = new ItemStack(buffsword);
            ev.setResult(item);

        }
        else if(items[0].getItemMeta().getPersistentDataContainer().has(weapons.CHKey) && items[1].getItemMeta().getPersistentDataContainer().has(weapons.CEKey)){
            ItemStack item = new ItemStack(weapons.CorruptedHeart);
            ItemMeta meta = item.getItemMeta();
            int t = getTest(items[0], weapons.CHKey);
            if(t < 3 && t>0){
                meta.getPersistentDataContainer().set(weapons.CHKey, PersistentDataType.INTEGER, t-1);
                meta.setItemName(ChatColor.DARK_RED + "Corrupted Heart");
                if(t-1 > 0){
                    List<String> lore = meta.getLore();
                    lore.add(ChatColor.RED + "Times used: " + (t-1));
                    meta.setLore(lore);
                }
                item.setItemMeta(meta);
                ev.setResult(item);
                cheart_revitalized = item;
            }
            else{
                ev.setResult(null);
            }
        }
    }
    @EventHandler
    void onInventoryClick(InventoryClickEvent ev) {
        if (ev.getInventory() instanceof AnvilInventory anvilInventory) {
            if (ev.getSlotType() == InventoryType.SlotType.RESULT) {
                ItemStack result = anvilInventory.getItem(2);
                if (result != null && result.isSimilar(weapons.AwakenedSword)) {
                    Player player = (Player) ev.getWhoClicked();
                    player.getInventory().addItem(result);
                    anvilInventory.clear();
                    ev.setCancelled(true);
                }
                else if (result != null && result.isSimilar(buffsword)) {
                    Player player = (Player) ev.getWhoClicked();
                    player.getInventory().addItem(result);
                    anvilInventory.clear();
                    ev.setCancelled(true);
                }
                else if (result != null && result.isSimilar(cheart_revitalized)) {
                    Player player = (Player) ev.getWhoClicked();
                    player.getInventory().addItem(result);
                    anvilInventory.clear();
                    ev.setCancelled(true);
                }
                else if (result != null && result.isSimilar(chaos_katana)) {
                    Player player = (Player) ev.getWhoClicked();
                    player.getInventory().addItem(result);
                    anvilInventory.clear();
                    ev.setCancelled(true);
                }
            }
        }
    }
    @EventHandler
    void onItemBreak(PlayerItemBreakEvent ev){
        PersistentDataContainer pdc = ev.getBrokenItem().getItemMeta().getPersistentDataContainer();
        if(pdc.has(weapons.ASKey)){
            Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), () -> ev.getPlayer().getInventory().addItem(weapons.OblivionEssence), 3);
        }
    }

    public static int getRNG(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    public void shootGasterBlaster(Player player) {
        // Code to shoot Gaster Blaster
        GasterBlasterProjectile projectile = new GasterBlasterProjectile(player.getLocation(), player);
        projectile.launch();
    }
    public void setTest(NamespacedKey key, ItemStack item, int progress) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(key, PersistentDataType.INTEGER, progress);
        item.setItemMeta(meta);
    }
    public int getTest(ItemStack item, NamespacedKey key)
    {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        return pdc.get(key, PersistentDataType.INTEGER);
    }

    /*@EventHandler
    void onBossStart(BossStartEvent ev){
        soulEvents.drop = false;
        Plugin.getPlugin(Plugin.class).getConfig().set("soul.drop", soulEvents.drop);
        Plugin.getPlugin(Plugin.class).saveConfig();
        Plugin.getPlugin(Plugin.class).reloadConfig();
        //get world name
        String worldName = ev.getWorldName();
        if(worldName.equals("Realm")){
            ev.getBossPlayer().setGameMode(GameMode.ADVENTURE);
            for(Player p : ev.getBossPlayers()){
                p.setGameMode(GameMode.ADVENTURE);
            }
        }
        else{
            ev.getBossPlayer().setGameMode(GameMode.SURVIVAL);
            for(Player p : ev.getBossPlayers()){
                p.setGameMode(GameMode.SURVIVAL);
            }
        }
    }*/
    @EventHandler
    void onBossChange(BossChangeEvent ev){
        //get the boss name as the name of the config file, then load it and look for the value of key boss.world.x.soul (x being the phase number)
        String bossName = ev.getBossName();
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(events.path + "/bosses", bossName + ".yml"));
        List<Integer> souls = config.getIntegerList("boss.world." + ev.getPhase() + ".souls");
        Player player = ev.getBossPlayer();
        //int maxSoulKeys = SoulType.getSoulAmount(); // Dynamically fetch the max number of soul keys
        soulEvents.clearSouls(player);
        if(!souls.isEmpty()) {
            if(souls.size() < 2){
                soulEvents.setSouls(player, souls.get(0), -1);
            }
            else{
                soulEvents.setSouls(player, souls.get(0), souls.get(1));
            }
        }
    }
    @EventHandler
    void onBossEnd(BossEndEvent ev){
        FileConfiguration config = Plugin.getPlugin(Plugin.class).getConfig();
        boolean drop = config.getBoolean("soul.permadrop");
        if(drop){
            soulEvents.drop = true;
            Plugin.getPlugin(Plugin.class).getConfig().set("soul.drop", soulEvents.drop);
            Plugin.getPlugin(Plugin.class).saveConfig();
            Plugin.getPlugin(Plugin.class).reloadConfig();
        }
    }
    @EventHandler
    void onNotchEat(PlayerItemConsumeEvent ev){
        if (ev.getItem().getType() != Material.ENCHANTED_GOLDEN_APPLE) return;
        Player player = ev.getPlayer();

        // Save any existing fire resistance effect
        PotionEffect oldFireRes = player.getPotionEffect(PotionEffectType.FIRE_RESISTANCE);

        Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), () -> {
            PotionEffect newFireRes = player.getPotionEffect(PotionEffectType.FIRE_RESISTANCE);
            // If the player now has fire resistance, and it wasn't there before, remove it
            if (newFireRes != null) {
                // If there was no fire resistance before, remove it
                if (oldFireRes == null) {
                    player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
                } else if (newFireRes.getDuration() > oldFireRes.getDuration() || newFireRes.getAmplifier() != oldFireRes.getAmplifier()) {
                    // If the new effect is different (longer duration or different amplifier), restore the old one
                    player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
                    player.addPotionEffect(oldFireRes);
                }
            }
        }, 2L);
    }
    @EventHandler
    void onPlayerClick(InventoryClickEvent ev){
        InvManager.SanitizeInventory((Player) ev.getWhoClicked());
    }

    void onPlayerInteractCheart(PlayerInteractEvent ev){
        if(ev.getItem() == null) return;
        if(ev.getItem().isSimilar(weapons.CorruptedHeart)){
            if(ev.getAction() == Action.RIGHT_CLICK_AIR){
                //check that the corrupted heart is on the main hand
                if(!ev.getPlayer().getInventory().getItemInMainHand().isSimilar(weapons.CorruptedHeart)) return;
                //swap the item in the main hand with the item in the offhand
                Player p = ev.getPlayer();
                ItemStack mainHandItem = p.getInventory().getItemInMainHand();
                ItemStack offHandItem = p.getInventory().getItemInOffHand();
                p.getInventory().setItemInMainHand(offHandItem);
                p.getInventory().setItemInOffHand(mainHandItem);
            }
        }
    }
}

