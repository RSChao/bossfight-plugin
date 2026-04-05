package com.rschao.events;


import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.rschao.items.weapons;
import com.rschao.plugins.techniqueAPI.tech.Technique;
import com.rschao.plugins.techniqueAPI.tech.TechniqueMeta;
import com.rschao.plugins.techniqueAPI.tech.context.TechniqueContext;
import com.rschao.plugins.techniqueAPI.tech.feedback.hotbarMessage;
import com.rschao.plugins.techniqueAPI.tech.register.TechRegistry;
import com.rschao.plugins.techniqueAPI.tech.register.TechniqueNameManager;
import com.rschao.plugins.techniqueAPI.tech.selectors.TargetSelectors;
import com.rschao.plugins.techniqueAPI.tech.util.PlayerTechniqueManager;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.rschao.Plugin;
import com.rschao.api.SoulType;
import com.rschao.projectiles.DeterminationProjectile;


public class HandEvents implements Listener{
    private final int gravityRadius = 5; // Radius for gravity effects

    public HandEvents(){
        TechRegistry.registerTechnique("hands", repeller);
        TechRegistry.registerTechnique("hands", timeStop);
        TechRegistry.registerTechnique("hands", gravitator);
        TechRegistry.registerTechnique("hands", healerTech);
        TechRegistry.registerTechnique("hands", determinationTech);
        TechRegistry.registerTechnique("hands", braveryTech);
        TechRegistry.registerTechnique("hands", speedTech);
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player user = event.getPlayer();
            
        if(event.getItem() == null) return;
        PersistentDataContainer pdc = event.getItem().getItemMeta().getPersistentDataContainer();
        
        if (pdc.has(new NamespacedKey("gaster", "repeller"))) {
            handleRepeller(user, false);
            return;
        }
        if(pdc.has(new NamespacedKey("gaster", "timestop"))){
            handleTimeStop(user, false);
        }
        if(pdc.has(new NamespacedKey("gaster", "gravitator"))){
            handleGravitator(user, false);
        }
        if(pdc.has(new NamespacedKey("gaster", "healer"))){
            handleHealer(user, false);
        }
        if(pdc.has(new NamespacedKey("gaster", "determination"))){
            handleDetermination(user, false);
        }
        if(pdc.has(new NamespacedKey("gaster", "bravery"))){
            handleBravery(user, false);
        }
        if(pdc.has(new NamespacedKey("gaster", "speed"))){
            handleSpeed(user, false);
        }
        if(pdc.has(new NamespacedKey("gaster", "sevenhands"))){
            int techIndex = PlayerTechniqueManager.getCurrentTechnique(user.getUniqueId(), "hands");
            if(event.getAction().toString().contains("LEFT_CLICK")){
                Technique t = TechRegistry.getAllTechniques("hands").get(techIndex);
                t.use(new TechniqueContext(user));
            }
            else if(event.getAction().toString().contains("RIGHT_CLICK")){
                Player p = user;
                String groupId = "hands";
                if(p.isSneaking()){
                    if (techIndex == 0) {
                        PlayerTechniqueManager.setCurrentTechnique(p.getUniqueId(), groupId, TechRegistry.getAllTechniques(groupId).size() - 1);
                        techIndex = PlayerTechniqueManager.getCurrentTechnique(p.getUniqueId(), groupId);
                    } else {
                        PlayerTechniqueManager.setCurrentTechnique(p.getUniqueId(), groupId, techIndex - 1);
                        techIndex = PlayerTechniqueManager.getCurrentTechnique(p.getUniqueId(), groupId);
                    }
                }
                else{
                    PlayerTechniqueManager.setCurrentTechnique(p.getUniqueId(), groupId, (techIndex + 1) % TechRegistry.getAllTechniques(groupId).size());
                    techIndex = PlayerTechniqueManager.getCurrentTechnique(p.getUniqueId(), groupId);
                }
                p.sendMessage("You have switched to technique: " + TechniqueNameManager.getDisplayName(p, TechRegistry.getAllTechniques(groupId).get(techIndex)));
            }
        }
    }

    @EventHandler
    void onHotbarSwitch(PlayerItemHeldEvent ev) {

        Player player = ev.getPlayer();
        ItemStack newItem = player.getInventory().getItem(ev.getNewSlot());
        if(newItem == null) return;
        if(newItem.getItemMeta().getPersistentDataContainer().has(weapons.SHKey, PersistentDataType.BOOLEAN)){
            //check the hand selected
            int techIndex =  PlayerTechniqueManager.getCurrentTechnique(player.getUniqueId(), "hands");
            hotbarMessage.sendHotbarMessage(player, "Currently selected: " + TechniqueNameManager.getDisplayName(player, TechRegistry.getAllTechniques("hands").get(techIndex)));
        }
    }

    private void handleRepeller(Player user, boolean notify) {
        repeller.use(new TechniqueContext(user));
        if (notify) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(user.getName() + " has activated the power of patience!");
            }
        }
    }

    Technique repeller = new Technique("patience", "Patience Hand", new TechniqueMeta(false, 10000, List.of("Patience Hand")), TargetSelectors.self(), (ctx, token) -> {
        Player user = ctx.caster();
        int soul1 = soulEvents.GetSoulN(user);
        int soul2 = soulEvents.GetSecondSoulN(user);
        int tier1 = (soul1 == -1) ? 0 : SoulType.getById(soul1).getTier();
        int tier2 = (soul2 == -1) ? 0 : SoulType.getById(soul2).getTier();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!(user.isGliding() && (tier1 >= 2 || tier2 >= 2))) {
                    Location location = user.getLocation();
                    user.getWorld().spawnParticle(Particle.EXPLOSION, location, 30);
                    Vector direction = user.getLocation().getDirection();
                    user.setVelocity(direction.multiply(4));
                } else {
                    net.kryspin.Plugin plugin = Plugin.getPlugin(net.kryspin.Plugin.class);
                    plugin.enableProFlight(user);
                }
            }
        }.runTaskLater(Plugin.getPlugin(Plugin.class), 1L);
    });

    // Technique: Time Stop / Perseverance
    Technique timeStop = new Technique("perseverance", "Perseverance Hand", new TechniqueMeta(false, 30000, List.of("Perseverance Hand")), TargetSelectors.self(), (ctx, token) -> {
        Player user = ctx.caster();
        Player pl = HandPoweredEvents.getClosestPlayer(user.getLocation(), 5);
        if (pl == null) return;
        pl.getActivePotionEffects().forEach((e) -> {
            user.addPotionEffect(e);
            if (pl.getHealth() > user.getHealth()) {
                user.setHealth(pl.getHealth());
            }
        });
    });

    // Technique: Gravitator (push/pull based on sneaking)
    Technique gravitator = new Technique("integrity", "Integrity Hand", new TechniqueMeta(false, 20000, List.of("Gravitator")), TargetSelectors.self(), (ctx, token) -> {
        Player player = ctx.caster();
        if (!player.isSneaking()) {
            gravityPull(player);
        } else {
            gravityPush(player);
        }
    });

    // Technique: Healer
    Technique healerTech = new Technique("kindness", "Kindness Hand", new TechniqueMeta(false, 60000, List.of("Healer")), TargetSelectors.self(), (ctx, token) -> {
        Player user = ctx.caster();
        Location location = user.getLocation();
        user.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, location, 30);
        user.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_HEALTH, 1, 3));
    });

    // Technique: Determination (cannon / blasters)
    Technique determinationTech = new Technique("determination", "Determination Hand", new TechniqueMeta(false, 30000, List.of("Determination")), TargetSelectors.self(), (ctx, token) -> {
        Player user = ctx.caster();
        for (int i = 0; i < 10; i++) {
            final int idx = i;
            Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), () -> {
                shootCannon(user);
            }, 4 + (2 * idx));
        }
    });

    // Technique: Bravery
    Technique braveryTech = new Technique("bravery", "Bravery Hand", new TechniqueMeta(false, 35000, List.of("Bravery")), TargetSelectors.self(), (ctx, token) -> {
        Player user = ctx.caster();
        user.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 10 * 20, 1));
    });

    // Technique: Speed / Justice
    Technique speedTech = new Technique("justice", "Justice Hand", new TechniqueMeta(false, 60000, List.of("Speed")), TargetSelectors.self(), (ctx, token) -> {
        Player user = ctx.caster();
        user.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 50 * 20, 2));
    });

    private void handleTimeStop(Player user, boolean notify) {
        timeStop.use(new TechniqueContext(user));

        if (notify) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(user.getName() + " has activated the power of perseverance!");
            }
        }
    }

    private void handleGravitator(Player user, boolean notify) {
        Player player = user;

        // Delegate the actual push/pull to the technique
        gravitator.use(new TechniqueContext(user));

        if (notify) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(user.getName() + " has activated the power of integrity!");
            }
        }
    }

    private void handleHealer(Player user, boolean notify) {
        healerTech.use(new TechniqueContext(user));

        if (notify) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(user.getName() + " has activated the power of kindness!");
            }
        }
    }

    private void handleDetermination(Player user, boolean notify) {
        determinationTech.use(new TechniqueContext(user));

        if (notify) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(user.getName() + " has activated the power of determination!");
            }
        }
    }

    private void handleBravery(Player user, boolean notify) {
        braveryTech.use(new TechniqueContext(user));

        if (notify) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(user.getName() + " has activated the power of bravery!");
            }
        }
    }
    private void handleSpeed(Player user, boolean notify) {
        speedTech.use(new TechniqueContext(user));

        if (notify) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(user.getName() + " has activated the power of justice!");
            }
        }
    }

    private void gravityPull(Player player) {
        Location location = player.getLocation();
        player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, location, 30);
        player.getWorld().playSound(location, Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);

        for (Entity entity : location.getWorld().getEntities()) {
            if (entity.getLocation().distance(location) <= gravityRadius && entity != player) {
                Vector direction = location.toVector().subtract(entity.getLocation().toVector()).normalize();
                entity.setVelocity(direction.multiply(3)); // Adjust speed as needed
            }
        }
    }

    private void gravityPush(Player player) {
        Location location = player.getLocation();
        player.getWorld().spawnParticle(Particle.EXPLOSION, location, 30);
        player.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);

        for (Entity entity : location.getWorld().getEntities()) {
            if (entity.getLocation().distance(location) <= gravityRadius && entity != player) {
                Vector direction = entity.getLocation().toVector().subtract(location.toVector()).normalize();
                entity.setVelocity(direction.multiply(3)); // Adjust speed as needed
            }
        }
    }
    public void shootCannon(Player player) {
        // Code to shoot Gaster Blaster
        DeterminationProjectile projectile = new DeterminationProjectile(player.getLocation(), player);
        projectile.launch();
    }
}
