package com.rschao.techs;

import com.rschao.Plugin;
import com.rschao.boss_battle.bossEvents;
import com.rschao.plugins.techapi.tech.Technique;
import com.rschao.plugins.techapi.tech.cooldown.cooldownHelper;
import com.rschao.plugins.techapi.tech.register.TechRegistry;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Magician {
    static final String TECH_ID = "magician";

    public static void register(){
        TechRegistry.registerTechnique(TECH_ID, disappear);
        TechRegistry.registerTechnique(TECH_ID, illusion);
        TechRegistry.registerTechnique(TECH_ID, spatialDistortion);
    }

    static Technique disappear = new Technique("poof", "Dimensional Breach", false, cooldownHelper.minutesToMiliseconds(5), (player, itemStack, objects) -> {
        player.setGameMode(GameMode.SPECTATOR);
        Random rand = new Random();
        for(int i = 0; i < 10; i++){
            player.getWorld().spawnParticle(Particle.FLASH, player.getLocation(), 2, rand.nextDouble(-0.2, 0.2), rand.nextDouble(-0.2, 0.2), rand.nextDouble(-0.2, 0.2));
        }
        int cooldownSeconds = 0;
        if(bossEvents.bossActive) cooldownSeconds = 3;
        else cooldownSeconds = 10;

        Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), () -> {
            player.setGameMode(GameMode.SURVIVAL);
            for (int i = 1; i <= 10; i++) {
                player.getWorld().spawnParticle(Particle.FLASH, player.getLocation(), 2, rand.nextDouble(-0.2, 0.2), rand.nextDouble(-0.2, 0.2), rand.nextDouble(-0.2, 0.2));
            }
        }, cooldownSeconds*20);
    });
    static Technique illusion = new Technique("illusion", "Dimentio's Illusion", false, cooldownHelper.minutesToMiliseconds(3), (player, itemStack, objects) -> {
        // Hacer invisible al jugador
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 60, 1, false, false));

        // Crear clones ilusorios
        List<ArmorStand> clones = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ArmorStand clone = (ArmorStand) player.getWorld().spawnEntity(
                    player.getLocation().add(Vector.getRandom().multiply(10)), EntityType.ARMOR_STAND);
            clone.setVisible(false);
            clone.setCustomName(player.getName());
            clone.setCustomNameVisible(true);
            clone.setHelmet(player.getInventory().getHelmet());
            clone.setChestplate(player.getInventory().getChestplate());
            clone.setLeggings(player.getInventory().getLeggings());
            clone.setBoots(player.getInventory().getBoots());
            clone.setItemInHand(player.getInventory().getItemInMainHand());
            clone.setGravity(false);
            clone.setInvulnerable(true);
            clones.add(clone);
        }

        // Eliminar clones después de 3 segundos
        Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), () -> {
            for (ArmorStand clone : clones) {
                clone.remove();
            }
        }, 60L); // 60 ticks = 3 segundos
    });
    static Technique spatialDistortion = new Technique("distortion", "Spatial Distortion", false, cooldownHelper.minutesToMiliseconds(4), (player, itemStack, objects) -> {
        double radius = 6.0;
        List<Entity> targetEs = player.getNearbyEntities(radius, radius, radius).stream().filter(e-> (e instanceof LivingEntity))
                .filter(e -> !e.equals(player))
                .toList();
        List<LivingEntity> targets = new ArrayList<>();
        for(Entity e : targetEs){
            LivingEntity le = (LivingEntity) e;
            targets.add(le);
        }

        Random rand = new Random();
        for (LivingEntity target : targets) {
            // Dirección aleatoria
            Vector randomDir = new Vector(rand.nextInt(-1, 2), 1, rand.nextInt(-1, 2)).normalize().multiply(1.2);
            target.setVelocity(randomDir);

            // Efecto de confusión
            target.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 60, 1));
            // Partículas
            target.getWorld().spawnParticle(Particle.PORTAL, target.getLocation().add(0,1,0), 20, 0.5, 0.5, 0.5, 0.1);
        }
        // Partículas en el jugador
        player.getWorld().spawnParticle(Particle.WITCH, player.getLocation().add(0,1,0), 40, 1, 0.5, 1, 0.2);
    });
}
