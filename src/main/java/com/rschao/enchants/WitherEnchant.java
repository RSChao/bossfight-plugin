package com.rschao.enchants;

import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.CustomEnchantment;
import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.definition.EasyEnchant;
import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.util.ColorCodes;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Random;

public class WitherEnchant extends EasyEnchant {
    public WitherEnchant() {
        super("wither");
        CustomEnchantment enchant = makeEnchantment(ChatColor.GRAY + ColorCodes.BOLD.getCode() + "Withering");
        enchant.setSupportedItem("#minecraft:enchantable/sharp_weapon");
        enchant.setMaxLevel(1);
        saveBukkitEnchantment(enchant);
    }
    @Override @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent ev) {
        if(ev.getDamager() instanceof Player player && ev.getEntity() instanceof LivingEntity damaged){
            if(player.getInventory().getItemInMainHand().getType() == Material.AIR) return;
            if(!hasEnchantment(player.getInventory().getItemInMainHand())) return;
            int rng = (new Random()).nextInt(0, 100);
            if(rng > 47 || rng < 37) return;

            damaged.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 5*20, 5));
            damaged.sendMessage(ChatColor.DARK_GRAY + "You have been Withered");
            player.sendMessage(ChatColor.DARK_GRAY + "Your enemy has been Withered");
        }
    }
}
