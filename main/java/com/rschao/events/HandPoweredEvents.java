package com.rschao.events;
import com.rschao.items.HandsNew;

import net.md_5.bungee.api.ChatMessageType;

import com.rschao.Plugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
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
import org.bukkit.potion.PotionEffectTypeCategory;
import org.bukkit.util.Vector;

public class HandPoweredEvents implements Listener {
    Map<Player, Map<String, Long>> handCooldowns = new HashMap<>();
    Map<Player, String> selectedHand = new HashMap<>();

    String getHandNames(String key){
        switch (key) {
            case "GHKey":
                return "Guidance Hand";
            case "ZHKey":
                return "Sincerity Hand";
            case "AHKey":
                return "Audacity Hand";
            case "PHKey":
                return "Purity Hand";
            case "EHKey":
                return "Empathy Hand";
            case "LHKey":
                return "Love Hand";
            case "FHKey":
                return "Fortitude Hand";
            default:
                return null;
        }
    }

    @EventHandler
    void onHandUseEvent(PlayerInteractEvent ev) {
        Player player = ev.getPlayer();
        ItemStack item = ev.getItem();
        if (item == null) return;
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();

        // Handle SevenHand functionality
        if (pdc.has(HandsNew.SHKey, PersistentDataType.BOOLEAN)) {
            if (ev.getAction().toString().contains("RIGHT_CLICK")) {
                // Cycle through hands
                String[] hands = {"GHKey", "ZHKey", "AHKey", "PHKey", "EHKey", "LHKey", "FHKey"};
                String currentHand = selectedHand.getOrDefault(player, "GHKey");
                int index = (java.util.Arrays.asList(hands).indexOf(currentHand)+1) % hands.length;
                selectedHand.put(player, hands[index]);
                player.sendMessage("Selected hand: " + getHandNames(hands[index]));
                return;
            } else if (ev.getAction().toString().contains("LEFT_CLICK")) {
                // Activate selected hand
                String currentHand = selectedHand.getOrDefault(player, "GHKey");
                if (isOnCooldown(player, currentHand)) {
                    player.sendMessage("Your selected hand is on cooldown for " + getCooldown(player, currentHand) + " seconds.");
                    return;
                }
                activateHand(player, currentHand);
            }
        }

        // Individual hand activations
        if (pdc.has(HandsNew.GHKey, PersistentDataType.BOOLEAN)) {
            if (isOnCooldown(player, "GHKey")) {
                player.sendMessage("Your hand is on cooldown for " + getCooldown(player, "GHKey") + " seconds.");
                return;
            }
            activateHand(player, "GHKey");
        }

        if (pdc.has(HandsNew.ZHKey, PersistentDataType.BOOLEAN)) {
            if (isOnCooldown(player, "ZHKey")) {
                player.sendMessage("Your hand is on cooldown for " + getCooldown(player, "ZHKey") + " seconds.");
                return;
            }
            activateHand(player, "ZHKey");
        }

        if (pdc.has(HandsNew.AHKey, PersistentDataType.BOOLEAN)) {
            if (isOnCooldown(player, "AHKey")) {
                player.sendMessage("Your hand is on cooldown for " + getCooldown(player, "AHKey") + " seconds.");
                return;
            }
            activateHand(player, "AHKey");
        }

        if (pdc.has(HandsNew.PHKey, PersistentDataType.BOOLEAN)) {
            if (isOnCooldown(player, "PHKey")) {
                player.sendMessage("Your hand is on cooldown for " + getCooldown(player, "PHKey") + " seconds.");
                return;
            }
            activateHand(player, "PHKey");
        }

        if (pdc.has(HandsNew.EHKey, PersistentDataType.BOOLEAN)) {
            if (isOnCooldown(player, "EHKey")) {
                player.sendMessage("Your hand is on cooldown for " + getCooldown(player, "EHKey") + " seconds.");
                return;
            }
            activateHand(player, "EHKey");
        }

        if (pdc.has(HandsNew.LHKey, PersistentDataType.BOOLEAN)) {
            if (isOnCooldown(player, "LHKey")) {
                player.sendMessage("Your hand is on cooldown for " + getCooldown(player, "LHKey") + " seconds.");
                return;
            }
            activateHand(player, "LHKey");
        }

        if (pdc.has(HandsNew.FHKey, PersistentDataType.BOOLEAN)) {
            if (isOnCooldown(player, "FHKey")) {
                player.sendMessage("Your hand is on cooldown for " + getCooldown(player, "FHKey") + " seconds.");
                return;
            }
            activateHand(player, "FHKey");
        }
    }

    private void activateHand(Player player, String handKey) {
        switch (handKey) {
            case "GHKey":
                Set<Block> sphere = sphereAround(player.getLocation(), 5);
                for (Block b : sphere) {
                    if (b.getType().equals(Material.AIR)) {
                        b.setType(Material.GREEN_STAINED_GLASS);
                        Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), () -> {
                            b.setType(Material.AIR);
                        }, 60L);
                    }
                }
                setCooldown(player, "GHKey", 30);
                break;
            case "ZHKey":
                Player closestPlayer = getClosestPlayer(player.getLocation(), 20);
                if (closestPlayer != null) {
                    for (int i = 0; i < 40; i++) {
                        Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), () -> {
                            Vector direction = player.getEyeLocation().getDirection().normalize().multiply(20);
                            Location targetLocation = player.getEyeLocation().add(direction);
                            closestPlayer.teleport(targetLocation);
                        }, i);
                    }
                } else {
                    player.sendMessage("No players nearby to launch.");
                }
                setCooldown(player, "ZHKey", 40);
                break;
            case "AHKey":
                // Summon arrows and set them to fly in the direction of the player with slight randomness
                for (int i = 0; i < 15; i++) {
                    
                    Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), () -> {
                        Vector direction = player.getEyeLocation().getDirection().normalize();
                        Vector randomOffset = new Vector(
                            (Math.random() - 0.5) * 0.2, 
                            (Math.random() - 0.5) * 0.2, 
                            (Math.random() - 0.5) * 0.2
                        );
                    Vector finalDirection = direction.add(randomOffset).multiply(5); // Adjust speed multiplier as needed
                        org.bukkit.entity.Arrow arrow = player.launchProjectile(org.bukkit.entity.Arrow.class, finalDirection);
                        arrow.setDamage(10.0); // Set base damage to 50
                        arrow.setGravity(false);
                        arrow.setVelocity(finalDirection);
                        arrow.setPickupStatus(org.bukkit.entity.AbstractArrow.PickupStatus.DISALLOWED); // Prevent pickup
                    }, i * 2L); // Slight delay between each arrow
                }
                setCooldown(player, "AHKey", 120);
                break;
            case "PHKey":
                for(Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getLocation().distance(player.getLocation()) < 3) {
                        for(int i = 0; i < 10; i++) {
                            Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), () -> {
                                //clear all negative effects
                                p.getActivePotionEffects().forEach(effect -> {
                                    if (effect.getType().getCategory().equals(PotionEffectTypeCategory.HARMFUL)) {
                                        p.removePotionEffect(effect.getType());
                                    }
                                });
                            }, i * 2L); // Slight delay between each effect
                        }
                    }
                }
                setCooldown(player, "PHKey", 60);
                break;
            case "EHKey":
                Player p = getClosestPlayer(player.getLocation(), 15);
                if(p.getHealth() >= p.getAttribute(Attribute.MAX_HEALTH).getValue()) {
                    return;
                }
                if(player.getHealth() > p.getHealth()){
                    p.setHealth(player.getHealth());
                }
                setCooldown(player, "EHKey", 180);
                break;
            case "LHKey":
                for(Player pl : Bukkit.getOnlinePlayers()) {
                    if (pl.getLocation().distance(player.getLocation()) <= 5) {
                        if(pl.getHealth() + 10 >= pl.getAttribute(Attribute.MAX_HEALTH).getValue()) {
                            if(pl.getHealth() >= pl.getAttribute(Attribute.MAX_HEALTH).getValue()) {
                            } else {
                                pl.setHealth(pl.getAttribute(Attribute.MAX_HEALTH).getValue());
                            }
                        }
                        else {
                            pl.setHealth(pl.getHealth() + 10);
                        }
                        pl.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1, false, false));
                        pl.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 120*20, 2, false, false));
                    }
                }
                
                setCooldown(player, "LHKey", 120);
                break;
            case "FHKey":
                for(Player pl : Bukkit.getOnlinePlayers()) {
                    if (pl.getLocation().distance(player.getLocation()) <= 5) {
                        pl.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 120*20, 4, false, false));
                    }
                }
                setCooldown(player, "FHKey", 90);
                break;
        }
    }

    public static Player getClosestPlayer(Location location, int radius) {
        Player closestPlayer = null;
        double closestDistance = Double.MAX_VALUE;

        for (Player player : Bukkit.getOnlinePlayers()) {
            if(player.getWorld() != location.getWorld()) continue; // Skip players in different worlds
            double distance = player.getLocation().distance(location);
            if (distance > radius) continue; // Skip players outside the radius
            if (distance > 1 && distance < closestDistance) {
                closestDistance = distance;
                closestPlayer = player;
            }
        }

        return closestPlayer;
    }


    public static Set<Block> sphereAround(Location location, int radius) {
        Set<Block> sphere = new HashSet<Block>();
        Block center = location.getBlock();
        for(int x = -radius; x <= radius; x++) {
            for(int y = -radius; y <= radius; y++) {
                for(int z = -radius; z <= radius; z++) {
                    Block b = center.getRelative(x, y, z);
                    if(center.getLocation().distance(b.getLocation()) <= radius && center.getLocation().distance(b.getLocation()) > (radius - 2)) {
                        sphere.add(b);
                    }
                }
             
            }         
        }     
        return sphere;
    }

    private boolean isOnCooldown(Player player, String handKey) {
        if (!handCooldowns.containsKey(player)) return false;
        Map<String, Long> playerCooldowns = handCooldowns.get(player);
        if (!playerCooldowns.containsKey(handKey)) return false;
        long timeLeft = (playerCooldowns.get(handKey) - System.currentTimeMillis()) / 1000;
        return timeLeft > 0;
    }

    private void setCooldown(Player player, String handKey, int seconds) {
        handCooldowns.putIfAbsent(player, new HashMap<>());
        handCooldowns.get(player).put(handKey, System.currentTimeMillis() + (seconds * 1000));
    }

    private int getCooldown(Player player, String handKey) {
        if (!handCooldowns.containsKey(player)) return 0;
        Map<String, Long> playerCooldowns = handCooldowns.get(player);
        if (!playerCooldowns.containsKey(handKey)) return 0;
        long timeLeft = (playerCooldowns.get(handKey) - System.currentTimeMillis()) / 1000;
        return (int) Math.max(timeLeft, 0);
    }
    @EventHandler
    void onHotbarSwitch(PlayerItemHeldEvent ev) {
        
        Player player = ev.getPlayer();
        ItemStack newItem = player.getInventory().getItem(ev.getNewSlot());
        if(newItem == null) return;
        if(newItem.getItemMeta().getPersistentDataContainer().has(HandsNew.SHKey, PersistentDataType.BOOLEAN)){
            //check the hand selected
            String handKey = selectedHand.getOrDefault(player, "GHKey");
            if (handKey != null) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new net.md_5.bungee.api.chat.TextComponent("Current hand: " + getHandNames(handKey)));
            }
        }
    }
}
