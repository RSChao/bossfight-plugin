package com.rschao.techs;

import com.rschao.Plugin;
import com.rschao.api.SoulType;
import com.rschao.events.soulEvents;
import com.rschao.plugins.techniqueAPI.tech.Technique;
import com.rschao.plugins.techniqueAPI.tech.TechniqueMeta;
import com.rschao.plugins.techniqueAPI.tech.cooldown.cooldownHelper;
import com.rschao.plugins.techniqueAPI.tech.feedback.hotbarMessage;
import com.rschao.plugins.techniqueAPI.tech.register.TechRegistry;
import com.rschao.plugins.techniqueAPI.tech.selectors.TargetSelectors;
import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

public class Choco {

    static final String TECH_ID = "nocilla";
    static final Plugin plugin = Plugin.getPlugin(Plugin.class);
    public static void RegisterTechs() {
        TechRegistry.registerTechnique(TECH_ID, acrobatics);
        TechRegistry.registerTechnique(TECH_ID, cards);
        TechRegistry.registerTechnique(TECH_ID, starstorm);
    }

    static Technique acrobatics = new Technique("acrobacias", "Acrobatics", new TechniqueMeta(false, cooldownHelper.minutesToMiliseconds(1), List.of("Impulsa al usuario y le da jump boost y velocidad")), TargetSelectors.self(), (ctx, token) ->{
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
                user.addPotionEffect(PotionEffectType.SPEED.createEffect(2000, 1));
                user.addPotionEffect(PotionEffectType.JUMP_BOOST.createEffect(2000, 1));
            }
        }.runTaskLater(Plugin.getPlugin(Plugin.class), 1L);
        hotbarMessage.sendHotbarMessage(user, "You have used the Acrobatics technique!");
    });

    static Technique cards = new Technique("cartas", "Card Madness", new TechniqueMeta(false, cooldownHelper.minutesToMiliseconds(5), List.of("Crea una esfera de flechas")), TargetSelectors.self(), (ctx, token) ->{
        Player player = ctx.caster();
        // Lanzar flechas en una esfera alrededor del jugador
        double radius = 5;
        int thetaStep = 15; // ángulo polar
        int phiStep = 15;   // ángulo azimutal

        Location playerLoc = player.getLocation().add(0, 1, 0); // un poco sobre el suelo

        for (int theta = 0; theta <= 180; theta += thetaStep) {
            double thetaRad = Math.toRadians(theta);
            for (int phi = 0; phi < 360; phi += phiStep) {
                double phiRad = Math.toRadians(phi);

                // Coordenadas esféricas a cartesianas
                double x = radius * Math.sin(thetaRad) * Math.cos(phiRad);
                double y = radius * Math.cos(thetaRad);
                double z = radius * Math.sin(thetaRad) * Math.sin(phiRad);

                Vector direction = new Vector(x, y, z).normalize();

                Arrow arrow = player.getWorld().spawnArrow(
                        playerLoc,
                        direction,
                        1.0f,
                        12.0f
                );
                arrow.setDamage(20);
                arrow.setVelocity(direction.multiply(5));
            }
        }
    });

    static Technique starstorm = new Technique("lluviaestelar", "Deck StarStorm", new TechniqueMeta(false, cooldownHelper.minutesToMiliseconds(10), List.of("Invoca meteoritos del cielo")), TargetSelectors.self(), (ctx, token) ->{
        Player player = ctx.caster();
        Location center = player.getLocation().clone();
        World world = center.getWorld();
        int radius = 30; // Set radius to 10 blocks
        Random rand = new Random();
        int stars = rand.nextInt(11);
        int speedX = rand.nextInt(-1, 1);
        int speedZ = rand.nextInt(-1, 1);
        int speedY = rand.nextInt(-1, 7);
        new BukkitRunnable() {
            int count = 0;
            @Override
            public void run() {
                if (count >= stars) {
                    this.cancel();
                    return;
                }
                double angle = rand.nextDouble() * 2 * Math.PI;
                double dist = 5 + rand.nextDouble() * (radius - 5); // Adjust distance based on radius
                double x = center.getX() + Math.cos(angle) * dist;
                double z = center.getZ() + Math.sin(angle) * dist;
                double y = center.getY() + 30 + rand.nextDouble() * 10;
                Location spawnLoc = new Location(world, x, y, z);

                // Visual: falling ender crystal (use falling block for effect)
                FallingBlock fb = world.spawnFallingBlock(spawnLoc, Material.OBSIDIAN.createBlockData());
                Vector v = fb.getVelocity();
                v.setX(speedX);
                v.setZ(speedZ);
                v.setY(-speedY);
                fb.setVelocity(v);
                spawnLoc.setX(spawnLoc.getX() + speedX);
                spawnLoc.setZ(spawnLoc.getZ() + speedZ);
                // Damage on landing
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Location landLoc = spawnLoc.clone();
                        landLoc.setY(center.getY());
                        boolean pulled = false;
                        for (Player p : world.getPlayers()) {
                            if (!p.equals(player)
                                    && p.getLocation().distance(center) <= radius
                                    && p.getLocation().distance(landLoc) < 7) {
                                // Atraer al jugador hacia el centro de la explosión
                                Vector pull = landLoc.toVector().subtract(p.getLocation().toVector()).normalize().multiply(2);
                                p.setVelocity(pull);
                                pulled = true;
                            }
                        }
                        Runnable explosion = () -> {
                            for (Player p : world.getPlayers()) {
                                if (!p.equals(player)
                                        && p.getLocation().distance(center) <= radius
                                        && p.getLocation().distance(landLoc) < 3) {
                                    p.damage(7, player);
                                }
                            }
                            landLoc.getBlock().setType(Material.AIR);
                            player.getWorld().createExplosion(landLoc, 7.0f, true, true);
                        };
                        if (pulled) {
                            // Retrasar explosión 3 ticks si alguien fue atraído
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    explosion.run();
                                }
                            }.runTaskLater(plugin, 3L);
                        } else {
                            explosion.run();
                        }
                    }
                }.runTaskLater(plugin, 25L); // ~1.25s fall time

                count++;
            }
        }.runTaskTimer(plugin, 0L, 6L);
        hotbarMessage.sendHotbarMessage(player, "You have used the Star Storm technique!");
    });
}
