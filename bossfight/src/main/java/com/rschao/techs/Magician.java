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
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.components.EquippableComponent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Magician {
    static final String TECH_ID = "magician";

    // Guardar los clones de cada jugador para poder eliminarlos si recibe daño
    private static final ConcurrentHashMap<UUID, List<Zombie>> illusionClones = new ConcurrentHashMap<>();

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
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 1, false, false));

        // Crear clones ilusorios (zombis)
        List<Zombie> clones = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Zombie clone = (Zombie) player.getWorld().spawnEntity(
                    player.getLocation().add(Vector.getRandom().multiply(10)), EntityType.ZOMBIE);
            clone.setCustomName(player.getName());
            clone.setCustomNameVisible(true);
            clone.setBaby(false);
            clone.setAI(true);
            clone.setSilent(true);
            clone.setInvulnerable(false);
            clone.setRemoveWhenFarAway(false);
            clone.setCanPickupItems(false);

            // Copiar el equipo del jugador
            EntityEquipment eq = clone.getEquipment();
            eq.setHelmet(cloneItem(player.getInventory().getHelmet()));
            eq.setChestplate(cloneItem(player.getInventory().getChestplate()));
            eq.setLeggings(cloneItem(player.getInventory().getLeggings()));
            eq.setBoots(cloneItem(player.getInventory().getBoots()));
            eq.setItemInMainHand(cloneItem(player.getInventory().getItemInMainHand()));
            eq.setItemInOffHand(cloneItem(player.getInventory().getItemInOffHand()));

            // Prevenir drops
            eq.setHelmetDropChance(0f);
            eq.setChestplateDropChance(0f);
            eq.setLeggingsDropChance(0f);
            eq.setBootsDropChance(0f);
            eq.setItemInMainHandDropChance(0f);
            eq.setItemInOffHandDropChance(0f);

            // Invisibilidad al zombi
            clone.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 1, false, false));

            clones.add(clone);
        }

        illusionClones.put(player.getUniqueId(), clones);

        // Eliminar clones después de 5 segundos
        Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), () -> {
            removeIllusionClones(player.getUniqueId());
        }, 20*20); // 100 ticks = 5 segundos
    });

    // Clonar item para evitar referencias compartidas
    private static ItemStack cloneItem(ItemStack item) {
        return (item == null) ? null : item.clone();
    }

    // Eliminar clones y quitar invisibilidad al jugador
    public static void removeIllusionClones(UUID playerId) {
        List<Zombie> clones = illusionClones.remove(playerId);
        if (clones != null) {
            for (Zombie z : clones) {
                z.remove();
            }
        }
        Player p = Bukkit.getPlayer(playerId);
        if (p != null) {
            p.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
    }

    // Listener para daño al jugador
    public static class IllusionListener implements Listener {
        @EventHandler
        public void onPlayerHit(EntityDamageEvent event) {
            if (!(event.getEntity() instanceof Player)) return;
            Player player = (Player) event.getEntity();
            if (illusionClones.containsKey(player.getUniqueId())) {
                removeIllusionClones(player.getUniqueId());
                event.getHandlers().unregister(this);
            }
            if(illusionClones.isEmpty()) {
                event.getHandlers().unregister(this);
            }
        }
        @EventHandler
        void onMobTarget(EntityTargetLivingEntityEvent event) {
            if(event.getTarget() instanceof Player player){
                if(illusionClones.containsKey(player.getUniqueId())){
                    event.setCancelled(true);
                }
            }
            if(illusionClones.isEmpty()) {
                event.getHandlers().unregister(this);
            }
        }

        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {
            UUID id = event.getPlayer().getUniqueId();
            removeIllusionClones(id);
            event.getHandlers().unregister(this);
            if(illusionClones.isEmpty()) {
                event.getHandlers().unregister(this);
            }
        }
    }

    public static void register(){
        TechRegistry.registerTechnique(TECH_ID, disappear);
        TechRegistry.registerTechnique(TECH_ID, illusion);
        TechRegistry.registerTechnique(TECH_ID, spatialDistortion);
        // Registrar listener para daño
        Bukkit.getPluginManager().registerEvents(new IllusionListener(), Plugin.getPlugin(Plugin.class));
    }
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
