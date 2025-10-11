package com.rschao.enchants;

import com.rschao.api.SoulType;
import com.rschao.events.soulEvents;
import com.rschao.items.weapons;
import com.rschao.plugins.techapi.tech.PlayerTechniqueManager;
import com.rschao.plugins.techapi.tech.Technique;
import com.rschao.plugins.techapi.tech.feedback.hotbarMessage;
import com.rschao.plugins.techapi.tech.register.TechRegistry;
import com.rschao.smp.enchants.definition.Enchant;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Devil extends Enchant {
    public Devil() {
        super("jevilknife");
    }

    @Override
    public String getDisplayName() {
        return ChatColor.DARK_BLUE + (ChatColor.BOLD + "Devil's Knife");
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public List<Material> getApplicableItems() {
        return List.of();
    }

    @Override
    public List<Enchant> getConflictingEnchants() {
        return List.of();
    }

    @Override
    public boolean isTreasure() {
        return false;
    }
    String groupId = "jevilknife";
    int techIndex = 0;
    @EventHandler
    void onMagic(PlayerInteractEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if(item == null) return;
        Devil mani = new Devil();

        if(!item.hasItemMeta()) return;
        if(!mani.hasEnchant(item)) return;

        if(event.getItem().getItemMeta().getPersistentDataContainer().has(weapons.DKKey)){
            if(!event.getPlayer().isSneaking()) return;
            Player p = event.getPlayer();
            techIndex = PlayerTechniqueManager.getCurrentTechnique(event.getPlayer().getUniqueId(), groupId);
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
    @EventHandler
    void onSwitchToChaos(PlayerItemHeldEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItem(event.getNewSlot());
        if(item == null) return;
        if(!item.hasItemMeta()) return;
        Devil mani = new Devil();
        if(!mani.hasEnchant(item)) return;
        ItemStack sword = event.getPlayer().getInventory().getItem(event.getNewSlot());
        Player player = event.getPlayer();
        if(sword == null) return;
        if(sword.getItemMeta().getPersistentDataContainer().isEmpty()) return;
        if(sword.getItemMeta().getPersistentDataContainer().has(weapons.DKKey)){
            java.util.List<Technique> techs = TechRegistry.getAllTechniques(groupId);
            if (techs == null || techs.isEmpty() || techIndex < 0 || techIndex >= techs.size()) {
                hotbarMessage.sendHotbarMessage(player, "No technique selected.");
                return;
            }
            String techName = techs.get(techIndex).getName();
            hotbarMessage.sendHotbarMessage(player, "Technique: " + techName + " (Enchant " + getDisplayName() +  ChatColor.RESET + ")" );
        }
    }
}
