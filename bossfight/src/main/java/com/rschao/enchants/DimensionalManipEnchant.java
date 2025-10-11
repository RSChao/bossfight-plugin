package com.rschao.enchants;


import com.rschao.api.SoulType;
import com.rschao.events.soulEvents;
import com.rschao.items.weapons;
import com.rschao.plugins.techapi.tech.PlayerTechniqueManager;
import com.rschao.plugins.techapi.tech.Technique;
import com.rschao.plugins.techapi.tech.feedback.hotbarMessage;
import com.rschao.plugins.techapi.tech.register.TechRegistry;
import com.rschao.smp.enchants.definition.Enchant;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class DimensionalManipEnchant extends Enchant {
    public DimensionalManipEnchant() {
        super("dimentio");
    }

    @Override
    public String getDisplayName() {
        return ChatColor.DARK_PURPLE + (ChatColor.BOLD + "Dimensional Manipulator");
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public List<Material> getApplicableItems() {
        return List.of(Material.DIAMOND_HELMET);
    }

    @Override
    public List<Enchant> getConflictingEnchants() {
        return List.of();
    }

    @Override
    public boolean isTreasure() {
        return true;
    }


    String groupId = "magician";
    int techIndex = 0;
    @EventHandler
    void onMagic(PlayerInteractEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItem(EquipmentSlot.HEAD);
        if(item == null) return;
        DimensionalManipEnchant mani = new DimensionalManipEnchant();

        if(!item.hasItemMeta()) return;
        if(!mani.hasEnchant(item)) return;

        if(event.getItem().getItemMeta().getPersistentDataContainer().has(weapons.CSKey) && event.getItem().getItemMeta().getPersistentDataContainer().has(weapons.DSKey)){
            int soul1 = soulEvents.GetSoulN(event.getPlayer());
            int soul2 = soulEvents.GetSecondSoulN(event.getPlayer());
            if(soul1 == SoulType.CHAOSHEART.getId() || soul2 == SoulType.CHAOSHEART.getId()){
                if(!event.getPlayer().isSneaking()) return;
                Player p = event.getPlayer();
                techIndex = PlayerTechniqueManager.getCurrentTechnique(event.getPlayer().getUniqueId(), "magician");
                if(event.getAction().toString().contains("LEFT")){
                    Technique technique = TechRegistry.getAllTechniques(groupId).get(techIndex);
                    if(technique == null) return;
                    technique.use(p, event.getItem(), Technique.nullValue());
                }
                else if(event.getAction().toString().contains("RIGHT")){
                    PlayerTechniqueManager.setCurrentTechnique(p.getUniqueId(), groupId, (techIndex + 1) % TechRegistry.getAllTechniques(groupId).size());
                    techIndex = PlayerTechniqueManager.getCurrentTechnique(p.getUniqueId(), groupId);
                    p.sendMessage("You have switched to technique: " + TechRegistry.getAllTechniques(groupId).get(techIndex).getName());
                }
            }
        }
    }
    @EventHandler
    void onSwitchToChaos(PlayerItemHeldEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItem(EquipmentSlot.HEAD);
        if(item == null) return;
        if(!item.hasItemMeta()) return;
        DimensionalManipEnchant mani = new DimensionalManipEnchant();
        if(!mani.hasEnchant(item)) return;
        ItemStack sword = event.getPlayer().getInventory().getItem(event.getNewSlot());
        Player player = event.getPlayer();
        if(sword == null) return;
        if(sword.getItemMeta().getPersistentDataContainer().isEmpty()) return;
        if(sword.getItemMeta().getPersistentDataContainer().has(weapons.CSKey) && sword.getItemMeta().getPersistentDataContainer().has(weapons.DSKey)){
            java.util.List<Technique> techs = TechRegistry.getAllTechniques(groupId);
            if (techs == null || techs.isEmpty() || techIndex < 0 || techIndex >= techs.size()) {
                hotbarMessage.sendHotbarMessage(player, "No technique selected.");
                return;
            }
            String techName = techs.get(techIndex).getName();
            hotbarMessage.sendHotbarMessage(player, "Technique: " + techName + " (Enchant " + getDisplayName() +  ChatColor.RESET + ")" );
        }
    }
    @EventHandler
    void onPlayerTP(PlayerTeleportEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItem(EquipmentSlot.HEAD);
        if(item == null) return;
        DimensionalManipEnchant mani = new DimensionalManipEnchant();
        if(!item.hasItemMeta()) return;
        if(!mani.hasEnchant(item)) return;
        Player player = event.getPlayer();
        // Play sound
        player.playSound(event.getTo(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);

        // Spawn particles
        for (int i = 0; i < 10; i++) {
            player.getWorld().spawnParticle(Particle.PORTAL, event.getTo(), 100);
        }

        // Play sound
        player.playSound(event.getFrom(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);

        // Spawn particles
        for (int i = 0; i < 10; i++) {
            player.getWorld().spawnParticle(Particle.PORTAL, event.getFrom(), 100);
        }
    }

    @EventHandler
    void onChangeGamemode(PlayerGameModeChangeEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItem(EquipmentSlot.HEAD);
        if(item == null) return;
        DimensionalManipEnchant mani = new DimensionalManipEnchant();
        if(!item.hasItemMeta()) return;
        if(!mani.hasEnchant(item)) return;

        Player player = event.getPlayer();
        GameMode oldMode = player.getGameMode();
        GameMode newMode = event.getNewGameMode();

        // Si entra o sale de modo espectador
        if (oldMode != GameMode.SPECTATOR && newMode == GameMode.SPECTATOR) {
            // Entrando a espectador
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
            for (int i = 0; i < 10; i++) {
                player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 100);
            }
        } else if (oldMode == GameMode.SPECTATOR && newMode != GameMode.SPECTATOR) {
            // Saliendo de espectador
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
            for (int i = 0; i < 10; i++) {
                player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 100);
            }
        }
    }
}
