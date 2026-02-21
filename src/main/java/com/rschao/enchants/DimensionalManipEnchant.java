package com.rschao.enchants;


import com.rschao.Plugin;
import com.rschao.api.SoulType;
import com.rschao.effect.SphereFlashEffect;
import com.rschao.events.soulEvents;
import com.rschao.items.weapons;
import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.CustomEnchantment;
import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.util.ColorCodes;
import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.definition.EasyEnchant;
import com.rschao.plugins.techniqueAPI.tech.Technique;
import com.rschao.plugins.techniqueAPI.tech.context.TechniqueContext;
import com.rschao.plugins.techniqueAPI.tech.feedback.hotbarMessage;
import com.rschao.plugins.techniqueAPI.tech.register.TechRegistry;
import com.rschao.plugins.techniqueAPI.tech.register.TechniqueNameManager;
import com.rschao.plugins.techniqueAPI.tech.util.PlayerTechniqueManager;
import de.slikey.effectlib.Effect;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class DimensionalManipEnchant extends EasyEnchant {
    public DimensionalManipEnchant() {
        super("dimentio");
        CustomEnchantment enchant = makeEnchantment(ChatColor.DARK_PURPLE + ColorCodes.BOLD.getCode() + "Dimensional Manipulator");
        enchant.setSupportedItem("#minecraft:enchantable/head_armor");
        enchant.setMaxLevel(1);
        saveBukkitEnchantment(enchant);
    }
    String groupId = "magician";
    int techIndex = 0;
    @EventHandler
    void onMagic(PlayerInteractEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItem(EquipmentSlot.HEAD);
        if(item == null) return;

        if(!item.hasItemMeta()) return;
        if(!hasEnchantment(item)) return;
        if(event.getItem() == null) return;
        if(!event.getItem().hasItemMeta()) return;
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
                    technique.use(new TechniqueContext(p, p.getInventory().getItemInMainHand()));
                }
                else if(event.getAction().toString().contains("RIGHT")){

                    PlayerTechniqueManager.setCurrentTechnique(p.getUniqueId(), groupId, (techIndex + 1) % TechRegistry.getAllTechniques(groupId).size());
                    techIndex = PlayerTechniqueManager.getCurrentTechnique(p.getUniqueId(), groupId);
                    p.sendMessage("You have switched to technique: " + TechniqueNameManager.getDisplayName(p, TechRegistry.getAllTechniques(groupId).get(techIndex)));
                }
            }
        }
    }
    @EventHandler
    void onSwitchToChaos(PlayerItemHeldEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItem(EquipmentSlot.HEAD);
        if(item == null) return;
        if(!item.hasItemMeta()) return;
        if(!hasEnchantment(item)) return;
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
            String techName = techs.get(techIndex).getDisplayName();
            hotbarMessage.sendHotbarMessage(player, "Technique: " + techName + " (Enchant " + (ChatColor.DARK_PURPLE + (ChatColor.BOLD + "Dimensional Manipulator")) +  ChatColor.RESET + ")" );
        }
    }

    @EventHandler
    void onChangeGamemode(PlayerGameModeChangeEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItem(EquipmentSlot.HEAD);
        if(item == null) return;
        if(!item.hasItemMeta()) return;
        if(!hasEnchantment(item)) return;

        Player player = event.getPlayer();
        GameMode oldMode = player.getGameMode();
        GameMode newMode = event.getNewGameMode();
        SphereFlashEffect effect = new SphereFlashEffect(Plugin.getEffectManager());
        effect.setLocation(player.getLocation().add(0, 1, 0));
        effect.particle = Particle.DUST;
        effect.color = Color.PURPLE;
        // Si entra o sale de modo espectador
        if (oldMode != GameMode.SPECTATOR && newMode == GameMode.SPECTATOR) {
            // Entrando a espectador
            player.playSound(player.getLocation(), "dimentio_tp_2", 1.0F, 1.0F);
            effect.start();
            stopEffects(effect);
        } else if (oldMode == GameMode.SPECTATOR && newMode != GameMode.SPECTATOR) {
            // Saliendo de espectador
            player.playSound(player.getLocation(), "dimentio_tp_1", 1.0F, 1.0F);
            effect.start();
            stopEffects(effect);

        }

    }
    void stopEffects(Effect effect) {
        Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), () -> {
            if (effect != null) {
                effect.cancel();
            }
        }, 40L); // 40 ticks = 2 seconds
    }
}
