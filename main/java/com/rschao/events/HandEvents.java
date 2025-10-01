package com.rschao.events;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.rschao.Plugin;
import com.rschao.api.SoulType;
import com.rschao.projectiles.DeterminationProjectile;


public class HandEvents implements Listener{
    private Map<UUID, Long> repelTimes = new HashMap<>();
    private Map<UUID, Long> stopTimes = new HashMap<>();
    private Map<UUID, Long> pushTimes = new HashMap<>();
    private Map<UUID, Long> pullTimes = new HashMap<>();
    private Map<UUID, Long> healTimes = new HashMap<>();
    private Map<UUID, Long> speedTimes = new HashMap<>();
    private Map<UUID, Long> courageTimes = new HashMap<>();
    private Map<UUID, Long> cannonTimes = new HashMap<>();
    private final int gravityRadius = 5; // Radius for gravity effects
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
            handleSevenSouls(user);
        }
    }

    private void handleRepeller(Player user, boolean notify) {
        Long lastShot = this.repelTimes.get(user.getUniqueId());
        long seconds = System.currentTimeMillis()/1000L;
        if(lastShot != null && seconds-lastShot < 10){
            long time = seconds-lastShot;
            user.sendMessage(String.valueOf(10-time) + "s   ");
            return;
        }
        repelTimes.put(user.getUniqueId(), seconds);
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
                if (notify) {
                    for(Player p: Bukkit.getOnlinePlayers()){
                        p.sendMessage(user.getName() + " has activated the power of patience!");
                    }
                }
            }
        }.runTaskLater(Plugin.getPlugin(Plugin.class), 1L);
    }

    private void handleTimeStop(Player user, boolean notify) {
        Long lastShot = this.stopTimes.get(user.getUniqueId());
        long seconds = System.currentTimeMillis()/1000L;
        if(lastShot != null && seconds-lastShot < 30){
            long time = seconds-lastShot;
            user.sendMessage(String.valueOf(30-time) + "s   ");
            return;
        }
        stopTimes.put(user.getUniqueId(), seconds);
        Player pl = HandPoweredEvents.getClosestPlayer(user.getLocation(), 5);
        if(pl == null) return;
        pl.getActivePotionEffects().forEach((e) -> {
            user.addPotionEffect(e);
            if(pl.getHealth() > user.getHealth()){
                user.setHealth(pl.getHealth());
            }
        });
        
        if (notify) {
            for(Player p: Bukkit.getOnlinePlayers()){
                p.sendMessage(user.getName() + " has activated the power of perseverance!");
            }
        }
    }

    private void handleGravitator(Player user, boolean notify) {
        Player player = user;
        if (!player.isSneaking()) {
            Long lastShot = this.pullTimes.get(user.getUniqueId());
            long seconds = System.currentTimeMillis()/1000L;
            if(lastShot != null && seconds-lastShot < 20){
                long time = seconds-lastShot;
                user.sendMessage(String.valueOf(20-time) + "s   ");
                return;
            }
            pullTimes.put(user.getUniqueId(), seconds);
            gravityPull(player);
        } else{
            Long lastShot = this.pushTimes.get(user.getUniqueId());
            long seconds = System.currentTimeMillis()/1000L;
            if(lastShot != null && seconds-lastShot < 20){
                long time = seconds-lastShot;
                user.sendMessage(String.valueOf(20-time) + "s   ");
                return;
            }
            pushTimes.put(user.getUniqueId(), seconds);
            gravityPush(player);
        }
        
        if (notify) {
            for(Player p: Bukkit.getOnlinePlayers()){
                p.sendMessage(user.getName() + " has activated the power of integrity!");
            }
        }
    }

    private void handleHealer(Player user, boolean notify) {
        Long lastShot = this.healTimes.get(user.getUniqueId());
        long seconds = System.currentTimeMillis()/1000L;
        if(lastShot != null && seconds-lastShot < 60){
            long time = seconds-lastShot;
            user.sendMessage(String.valueOf(60-time) + "s   ");
            return;
        }
        Location location = user.getLocation();
        user.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, location, 30);
        
        healTimes.put(user.getUniqueId(), seconds);
        user.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_HEALTH, 1, 3));
        
        if (notify) {
            for(Player p: Bukkit.getOnlinePlayers()){
                p.sendMessage(user.getName() + " has activated the power of kindness!");
            }
        }
    }

    private void handleDetermination(Player user, boolean notify) {
        Long lastShot = this.cannonTimes.get(user.getUniqueId());
        long seconds = System.currentTimeMillis()/1000L;
        if(lastShot != null && seconds-lastShot < 30){
            long time = seconds-lastShot;
            user.sendMessage(String.valueOf(30-time) + "s   ");
            return;
        }
        cannonTimes.put(user.getUniqueId(), seconds);
        for(int i = 0; i<10; i++){
            Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), () -> {
                shootCannon(user);
            }, 4+(2*i));
        }
        
        if (notify) {
            for(Player p: Bukkit.getOnlinePlayers()){
                p.sendMessage(user.getName() + " has activated the power of determination!");
            }
        }
    }

    private void handleBravery(Player user, boolean notify) {
        Long lastShot = this.courageTimes.get(user.getUniqueId());
        long seconds = System.currentTimeMillis()/1000L;
        if(lastShot != null && seconds-lastShot < 35){
            long time = seconds-lastShot;
            user.sendMessage(String.valueOf(35-time) + "s");
            return;
        }
        courageTimes.put(user.getUniqueId(), seconds);
        user.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 10*20, 1));
        
        if (notify) {
            for(Player p: Bukkit.getOnlinePlayers()){
                p.sendMessage(user.getName() + " has activated the power of bravery!");
            }
        }
    }

    private void handleSevenSouls(Player user) {
        if (user.getHealth() < 10 && user.getFoodLevel() > 10) {
            handleHealer(user, true);
        } else if (user.isSprinting() && !isPlayerInMidAir(user) && user.getFoodLevel() > 15) {
            handleSpeed(user, true);
        } else if (!user.isSprinting() && isPlayerInMidAir(user)&& user.getAbsorptionAmount() == 0) {
            handleGravitator(user, true);
        } else if (((user.isSprinting() && isPlayerInMidAir(user)) || user.isGliding())) {
            handleRepeller(user, true);
        } else if (user.isSprinting()&&user.getSaturation() < 14) {
            handleBravery(user, true);
        } else if (!user.isSprinting() && user.isSneaking() && !isPlayerInMidAir(user) && user.getSaturation() > 10) {
            handleTimeStop(user, true);
        } else if (!user.isSprinting() && user.getInventory().getItemInOffHand().getType() == Material.AIR || user.getInventory().getItemInOffHand() == null) {
            handleDetermination(user, true);
        }
    }
    private void handleSpeed(Player user, boolean notify) {
        Long lastShot = this.speedTimes.get(user.getUniqueId());
        long seconds = System.currentTimeMillis()/1000L;
        if(lastShot != null && seconds-lastShot < 60){
            long time = seconds-lastShot;
            user.sendMessage(String.valueOf(60-time) + "s   ");
            return;
        }
        speedTimes.put(user.getUniqueId(), seconds);
        user.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 50*20, 2));
        
        if (notify) {
            for(Player p: Bukkit.getOnlinePlayers()){
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
    public boolean isPlayerInMidAir(Player player) {
        // Get the player's location
        Location location = player.getLocation();

        // Get the block below the player
        Block blockBelow = location.subtract(0, 1, 0).getBlock();

        // Check if the block below is air or not solid
        return blockBelow.getType() == Material.AIR;
    }
}
