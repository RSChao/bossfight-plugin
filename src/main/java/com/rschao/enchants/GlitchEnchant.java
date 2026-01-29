package com.rschao.enchants;

import com.rschao.items.weapons;
import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.CustomEnchantment;
import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.definition.EasyEnchant;
import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.util.ColorCodes;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GlitchEnchant extends EasyEnchant {
    public GlitchEnchant() {
        super("glitch");
        CustomEnchantment enchant = makeEnchantment(ChatColor.DARK_GRAY + ColorCodes.BOLD.getCode() + "Glitch");
        enchant.setSupportedItem("#minecraft:enchantable/sharp_weapon");
        enchant.setMaxLevel(2);
        saveBukkitEnchantment(enchant);
    }

    @Override @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent ev) {
        if(ev.getDamager() instanceof Player player && ev.getEntity() instanceof LivingEntity damaged){
            ItemStack itemUsed = player.getInventory().getItemInMainHand();
            if(itemUsed.getType() == Material.AIR) return;
            PersistentDataContainer pdc = Objects.requireNonNull(itemUsed.getItemMeta()).getPersistentDataContainer();
            int rng = (new Random()).nextInt(0, 100);
            if(!hasEnchantment(itemUsed)) return;
            int level = itemUsed.getEnchantmentLevel(Enchantment.getByKey(getKey()));
            if(rng < 28+(5*level) && rng > 28){
                damaged.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3*20, 255));
                damaged.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 3*20, 255));
                damaged.sendMessage(net.md_5.bungee.api.ChatColor.DARK_GRAY + "You have been Glitched");
                player.sendMessage(net.md_5.bungee.api.ChatColor.DARK_GRAY + "Your enemy has been Glitched");
            }
        }
    }

}
