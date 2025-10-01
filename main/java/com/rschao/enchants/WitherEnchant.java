package com.rschao.enchants;

import com.rschao.smp.enchants.definition.Enchant;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Random;

public class WitherEnchant extends Enchant {
    public WitherEnchant() {
        super("wither");
    }

    @Override
    public String getDisplayName() {
        return org.bukkit.ChatColor.GRAY + (org.bukkit.ChatColor.BOLD + "Withering");
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public List<Material> getApplicableItems() {
        return List.of(Material.NETHERITE_SWORD);
    }

    @Override
    public List<Enchant> getConflictingEnchants() {
        return List.of();
    }

    @Override @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent ev) {
        if(ev.getDamager() instanceof Player player && ev.getEntity() instanceof Player damaged){
            if(!hasEnchant(player.getInventory().getItemInMainHand())) return;
            int rng = (new Random()).nextInt(0, 100);
            if(rng > 47 || rng < 37) return;

            damaged.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 5*20, 5));
            damaged.sendMessage(ChatColor.DARK_GRAY + "You have been Withered");
            player.sendMessage(ChatColor.DARK_GRAY + "Your enemy has been Withered");
        }
    }

    @Override
    public boolean isTreasure() {
        return true;
    }
}
