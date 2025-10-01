package com.rschao.enchants;

import com.rschao.items.weapons;
import com.rschao.smp.enchants.definition.Enchant;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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

public class GlitchEnchant extends Enchant {
    public GlitchEnchant() {
        super("glitch");
    }

    @Override
    public String getDisplayName() {
        return ChatColor.DARK_GRAY + (ChatColor.BOLD + "Glitch");
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
            ItemStack itemUsed = player.getInventory().getItemInMainHand();
            PersistentDataContainer pdc = Objects.requireNonNull(itemUsed.getItemMeta()).getPersistentDataContainer();
            int rng = (new Random()).nextInt(0, 100);
            if(!hasEnchant(itemUsed)) return;
            //noinspection UnstableApiUsage
            if(pdc.has(new NamespacedKey("gaster", "x_knife"))){

                //xknife
                if(rng > 39 || rng < 28) return;

                damaged.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5*20, 255));
                damaged.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 5*20, 255));
                damaged.sendMessage(net.md_5.bungee.api.ChatColor.DARK_GRAY + "You have been Glitched");
                player.sendMessage(net.md_5.bungee.api.ChatColor.DARK_GRAY + "Your enemy has been Glitched");
            }
            if(pdc.has(weapons.DSKey)){
                if(rng < 33 && rng > 28){
                    damaged.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3*20, 255));
                    damaged.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 3*20, 255));
                    damaged.sendMessage(net.md_5.bungee.api.ChatColor.DARK_GRAY + "You have been Glitched");
                    player.sendMessage(net.md_5.bungee.api.ChatColor.DARK_GRAY + "Your enemy has been Glitched");
                }
            }
        }
    }

    @Override
    public boolean isTreasure() {
        return true;
    }
}
