package com.rschao.techs;

import com.rschao.Plugin;
import com.rschao.boss_battle.bossEvents;
import com.rschao.events.definitions.PlayerPopHeartEvent;
import com.rschao.plugins.techniqueAPI.tech.Technique;
import com.rschao.plugins.techniqueAPI.tech.TechniqueMeta;
import com.rschao.plugins.techniqueAPI.tech.cooldown.cooldownHelper;
import com.rschao.plugins.techniqueAPI.tech.register.TechRegistry;
import com.rschao.plugins.techniqueAPI.tech.selectors.TargetSelectors;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
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
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Set;

public class Magician {
    static final String TECH_ID = "magician";

    // Guardar los clones de cada jugador para poder eliminarlos si recibe daño
    private static final ConcurrentHashMap<UUID, List<Zombie>> illusionClones = new ConcurrentHashMap<>();

    // --- NUEVO: mapa de breaches (pueden existir múltiples simultáneamente) ---
    private static final ConcurrentHashMap<UUID, Breach> breaches = new ConcurrentHashMap<>();

    // Clase interna para representar un breach creado por un usuario
    private static class Breach {
        final UUID owner;
        final Location loc;
        final Set<UUID> trappedPlayers = ConcurrentHashMap.newKeySet();
        final ConcurrentHashMap<UUID, Location> prevLocations = new ConcurrentHashMap<>();

        Breach(UUID owner, Location loc) {
            this.owner = owner;
            this.loc = loc.clone();
        }

        void addTrapped(Player p) {
            UUID id = p.getUniqueId();
            // Guardar ubicación previa
            prevLocations.put(id, p.getLocation().clone());
            trappedPlayers.add(id);
        }

        void releasePlayer(UUID playerId) {
            trappedPlayers.remove(playerId);
            prevLocations.remove(playerId);
        }
    }
    // --- FIN NUEVO ---

    static Technique disappear = new Technique("poof", "Dimensional Breach", new TechniqueMeta(false, cooldownHelper.minutesToMiliseconds(5), List.of("Desvanece al usuario un tiempo", "otorga espectador")), TargetSelectors.self(), (ctx, token) ->{
        Player player = ctx.caster();
        GameMode gameMode = player.getGameMode();
        player.setGameMode(GameMode.SPECTATOR);
        int cooldownSeconds = 0;
        if(bossEvents.bossActive) cooldownSeconds = 3;
        else cooldownSeconds = 10;
        Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), () -> {
            player.setGameMode(gameMode);
        }, cooldownSeconds*20);
    });
    static Technique illusion = new Technique("illusion", "Dimentio's Illusion", new TechniqueMeta(false, cooldownHelper.minutesToMiliseconds(3), List.of("Crea clones zombi del usuario")), TargetSelectors.self(), (ctx, token) -> {
        Player player = ctx.caster();

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
    // --- MODIFICADO: Dimensional Gathering crea un breach por usuario y atrapa jugadores cercanos ---
    static Technique dimensionalGather = new Technique("dimensional_gather", "Dimensional Gathering", new TechniqueMeta(false, cooldownHelper.minutesToMiliseconds(10), List.of("Atrapa a todos en Dimension D")), TargetSelectors.self(), (ctx, token) -> {
        Player player = ctx.caster();

        FileConfiguration config = Plugin.getPlugin(Plugin.class).getConfig();
        Location targetLoc;

        // Reusar loc guardada si existe (comportamiento previo), si no usar la ubicación actual del jugador
        if (config.contains("dimension_d.loc")) {
            targetLoc = (Location) config.get("dimension_d.loc");
        } else {
            targetLoc = player.getLocation().clone();
            config.set("dimension_d.loc", targetLoc);
            Plugin.getPlugin(Plugin.class).saveConfig();
            Plugin.getPlugin(Plugin.class).reloadConfig();
        }

        // Crear breach para este jugador
        Breach breach = new Breach(player.getUniqueId(), targetLoc);
        breaches.put(player.getUniqueId(), breach);

        // Teletransportar a todos los jugadores en un radio de 50 bloques (excepto al usuario) y marcarlos como atrapados
        double radius = 50.0;
        List<Player> nearbyPlayers = player.getWorld().getPlayers().stream()
                .filter(p -> !p.getUniqueId().equals(player.getUniqueId()))
                .filter(p -> p.getLocation().distance(player.getLocation()) <= radius)
                .toList();

        for (Player p : nearbyPlayers) {
            breach.addTrapped(p);
            p.teleport(targetLoc);
        }

        // Partículas / feedback al creador
        Random rand = new Random();
        for(int i = 0; i < 10; i++){
            player.getWorld().spawnParticle(Particle.PORTAL, targetLoc, 4, rand.nextDouble(-0.5,0.5), rand.nextDouble(-0.5,0.5), rand.nextDouble(-0.5,0.5));
        }
    });
    // --- FIN MODIFICADO ---

    // NUEVA TÉCNICA: Dimensional Expulsion - permite al jugador liberarse de cualquier breach que lo contenga
    static Technique dimensionalExpulsion = new Technique("dimensional_expulsion", "Dimensional Expulsion", new TechniqueMeta(false, cooldownHelper.minutesToMiliseconds(2), List.of("Deshace Dimensional Gathering")), TargetSelectors.self(), (ctx, token) -> {
        Player player = ctx.caster();

        UUID pid = player.getUniqueId();
        boolean wasTrapped = false;
        Location targetPrev = null;

        // Buscar todos los breaches que contengan a este jugador
        for (Map.Entry<UUID, Breach> e : breaches.entrySet()) {
            Breach b = e.getValue();
            if (b.trappedPlayers.contains(pid)) {
                wasTrapped = true;
                // Preferir la última ubicación previa encontrada
                Location prev = b.prevLocations.get(pid);
                if (prev != null) targetPrev = prev;
                // Quitar al jugador del breach
                b.releasePlayer(pid);
                for(UUID p: b.trappedPlayers){
                    b.releasePlayer(p);
                }
                // Si el breach quedó vacío, removerlo
                if (b.trappedPlayers.isEmpty()) {
                    breaches.remove(b.owner);
                }
            }
        }

        if (wasTrapped) {
            // Teletransportar al jugador a su ubicación previa si está disponible, si no al spawn del mundo
            if (targetPrev != null) {
                player.teleport(targetPrev);
            } else {
                player.teleport(player.getWorld().getSpawnLocation());
            }
            player.getWorld().spawnParticle(Particle.FIREWORK, player.getLocation(), 20, 0.3,0.3,0.3);
        } else {
            // No estaba atrapado: pequeño feedback opcional
            player.sendMessage("No estás atrapado en ninguna brecha dimensional.");
        }
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

    // --- NUEVO: Listener para manejar las restricciones de los breaches y muerte del dueño ---
    public static class BreachListener implements Listener {
        private final double allowedRadius = 50.0; // radio permitido alrededor de la loc del breach

        @EventHandler
        public void onPlayerMove(PlayerMoveEvent event) {
            Player p = event.getPlayer();
            UUID pid = p.getUniqueId();
            // Si el jugador está atrapado en uno o más breaches, impedir salida del área (teleport de regreso)
            boolean isTrapped = false;
            Location nearestLoc = null;
            double nearestDist = Double.MAX_VALUE;

            for (Breach b : breaches.values()) {
                if (b.trappedPlayers.contains(pid)) {
                    isTrapped = true;
                    double dist = p.getLocation().distance(b.loc);
                    if (dist < nearestDist) {
                        nearestDist = dist;
                        nearestLoc = b.loc;
                    }
                }
            }

            if (isTrapped && nearestLoc != null) {
                if (nearestDist > allowedRadius) {
                    // Teletransportar de vuelta a la loc del breach (mantenerles dentro)
                    Location locasion = nearestLoc.clone();
                    Bukkit.getScheduler().runTask(Plugin.getPlugin(Plugin.class), () -> p.teleport(locasion));
                }
            }
        }

        @EventHandler
        public void onEntityDeath(EntityDeathEvent event) {
            if (!(event.getEntity() instanceof Player)) return;
            Player dead = (Player) event.getEntity();
            UUID ownerId = dead.getUniqueId();

            // Si el muerto era dueño de un breach, liberar a todos los atrapados (mecanismo "matar al usuario")
            Breach b = breaches.remove(ownerId);
            if (b != null) {
                for (UUID trappedId : b.trappedPlayers) {
                    Player trapped = Bukkit.getPlayer(trappedId);
                    Location prev = b.prevLocations.get(trappedId);
                    if (trapped != null) {
                        if (prev != null) trapped.teleport(prev);
                        else trapped.teleport(trapped.getWorld().getSpawnLocation());
                        trapped.getWorld().spawnParticle(Particle.FIREWORK, trapped.getLocation(), 20, 0.3,0.3,0.3);
                    }
                }
                b.trappedPlayers.clear();
                b.prevLocations.clear();
            }
        }
        @EventHandler
        void onPlayerPop(PlayerPopHeartEvent event) {
            Player dead = event.getPlayer();
            UUID ownerId = dead.getUniqueId();

            // Si el muerto era dueño de un breach, liberar a todos los atrapados (mecanismo "matar al usuario")
            Breach b = breaches.remove(ownerId);
            if (b != null) {
                for (UUID trappedId : b.trappedPlayers) {
                    Player trapped = Bukkit.getPlayer(trappedId);
                    Location prev = b.prevLocations.get(trappedId);
                    if (trapped != null) {
                        if (prev != null) trapped.teleport(prev);
                        else trapped.teleport(trapped.getWorld().getSpawnLocation());
                        trapped.getWorld().spawnParticle(Particle.FIREWORK, trapped.getLocation(), 20, 0.3,0.3,0.3);
                    }
                }
                b.trappedPlayers.clear();
                b.prevLocations.clear();
            }
        }

        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {
            // Si un dueño cierra sesión, remover su breach para no dejar jugadores indefinidamente atrapados
            UUID id = event.getPlayer().getUniqueId();
            Breach b = breaches.remove(id);
            if (b != null) {
                for (UUID trappedId : b.trappedPlayers) {
                    Player trapped = Bukkit.getPlayer(trappedId);
                    Location prev = b.prevLocations.get(trappedId);
                    if (trapped != null) {
                        if (prev != null) trapped.teleport(prev);
                        else trapped.teleport(trapped.getWorld().getSpawnLocation());
                        trapped.getWorld().spawnParticle(Particle.FIREWORK, trapped.getLocation(), 20, 0.3,0.3,0.3);
                    }
                }
                b.trappedPlayers.clear();
                b.prevLocations.clear();
            }
        }
    }
    // --- FIN NUEVO ---

    public static void register(){
        TechRegistry.registerTechnique(TECH_ID, disappear);
        TechRegistry.registerTechnique(TECH_ID, illusion);
        TechRegistry.registerTechnique(TECH_ID, spatialDistortion);
        TechRegistry.registerTechnique(TECH_ID, dimensionalGather); // Registrar la nueva técnica
        TechRegistry.registerTechnique(TECH_ID, dimensionalExpulsion); // Registrar expulsion
        // Registrar listener para daño
        Bukkit.getPluginManager().registerEvents(new IllusionListener(), Plugin.getPlugin(Plugin.class));
        // Registrar listener para breaches
        Bukkit.getPluginManager().registerEvents(new BreachListener(), Plugin.getPlugin(Plugin.class));
    }
    static Technique spatialDistortion = new Technique("distortion", "Spatial Distortion", new TechniqueMeta(false, cooldownHelper.minutesToMiliseconds(4), List.of()), TargetSelectors.self(), (ctx, token) ->{
        Player player = ctx.caster();
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
