package com.rschao.enchants;

import com.rschao.items.weapons;
import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.CustomEnchantment;
import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.definition.EasyEnchant;
import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.util.ColorCodes;
import com.rschao.plugins.techniqueAPI.tech.Technique;
import com.rschao.plugins.techniqueAPI.tech.context.TechniqueContext;
import com.rschao.plugins.techniqueAPI.tech.feedback.hotbarMessage;
import com.rschao.plugins.techniqueAPI.tech.register.TechRegistry;
import com.rschao.plugins.techniqueAPI.tech.util.PlayerTechniqueManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public class Choco extends EasyEnchant {
    public Choco() {
        super("choco");
        CustomEnchantment enchant = makeEnchantment(ChatColor.GOLD + ColorCodes.BOLD.getCode() + "Chocolate Blade");
        enchant.setSupportedItem("#minecraft:enchantable/sharp_weapon");
        enchant.setMaxLevel(1);
        saveBukkitEnchantment(enchant);
    }
    String groupId = "nocilla";
    int techIndex = 0;
    @EventHandler
    void onMagic(PlayerInteractEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if(item == null) return;

        if(!item.hasItemMeta()) return;
        if(!hasEnchantment(item)) return;


        if(event.getItem().getItemMeta().getPersistentDataContainer().has(weapons.CBKey)){
            if(!event.getPlayer().isSneaking()) return;
            Player p = event.getPlayer();
            techIndex = PlayerTechniqueManager.getCurrentTechnique(event.getPlayer().getUniqueId(), groupId);
            if(event.getAction().toString().contains("LEFT")){
                Technique technique = TechRegistry.getAllTechniques(groupId).get(techIndex);
                if(technique == null) return;
                technique.use(new TechniqueContext(p, p.getInventory().getItemInMainHand()));
            }
            else if(event.getAction().toString().contains("RIGHT")){
                PlayerTechniqueManager.setCurrentTechnique(p.getUniqueId(), groupId, (techIndex + 1) % TechRegistry.getAllTechniques(groupId).size());
                techIndex = PlayerTechniqueManager.getCurrentTechnique(p.getUniqueId(), groupId);
                p.sendMessage("You have switched to technique: " + TechRegistry.getAllTechniques(groupId).get(techIndex).getDisplayName());
            }

        }
    }
    @EventHandler
    void onSwitchToChaos(PlayerItemHeldEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItem(event.getNewSlot());
        if(item == null) return;
        if(!item.hasItemMeta()) return;
        ItemStack sword = event.getPlayer().getInventory().getItem(event.getNewSlot());
        Player player = event.getPlayer();
        if(sword == null) return;
        if(sword.getItemMeta().getPersistentDataContainer().isEmpty()) return;
        if(sword.getItemMeta().getPersistentDataContainer().has(weapons.CBKey)){
            java.util.List<Technique> techs = TechRegistry.getAllTechniques(groupId);
            if (techs == null || techs.isEmpty() || techIndex < 0 || techIndex >= techs.size()) {
                hotbarMessage.sendHotbarMessage(player, "No technique selected.");
                return;
            }
            String techName = techs.get(techIndex).getDisplayName();
            hotbarMessage.sendHotbarMessage(player, "Technique: " + techName + " (Enchant " + (ChatColor.GOLD + (ChatColor.BOLD + "Chocolate Blade")) +  ChatColor.RESET + ")" );
        }
    }
}
