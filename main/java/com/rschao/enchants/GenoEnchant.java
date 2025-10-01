package com.rschao.enchants;

import com.rschao.events.events;
import com.rschao.smp.enchants.definition.Enchant;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class GenoEnchant extends Enchant {
    public GenoEnchant() {
        super("geno");
    }

    @Override
    public String getDisplayName() {
        return ChatColor.DARK_RED + (ChatColor.BOLD + "Genocidal");
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public List<Material> getApplicableItems() {
        return List.of(Material.NETHERITE_SWORD);
    }

    @Override
    public List<Enchant> getConflictingEnchants() {
        return List.of();
    }

    @Override @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDamage(EntityDamageByEntityEvent ev) {
        if(ev.getDamager() instanceof Player player && ev.getEntity() instanceof Player damaged){
            ItemStack itemUsed = player.getInventory().getItemInMainHand();
            int rng = (new Random()).nextInt(0, 100);
            if(!hasEnchant(itemUsed)) return;
            int level = getEnchantLevel(itemUsed);
            if(rng <= (level)){
                //check if geno is on cooldown
                if(events.genoCooldown.containsKey(player.getUniqueId())){
                    long lastShot = events.genoCooldown.get(player.getUniqueId());
                    long seconds = System.currentTimeMillis()/1000L;
                    if(seconds - lastShot < 300){
                        return;
                    }
                }
                events.genoCooldown.put(player.getUniqueId(), System.currentTimeMillis()/1000L);
                ev.setDamage(1000);
                damaged.sendMessage(net.md_5.bungee.api.ChatColor.DARK_RED + "You feel like you're going to have a bad time");
                player.sendMessage(net.md_5.bungee.api.ChatColor.DARK_RED + "=)");
            }
        }

    }

    @Override
    public boolean isTreasure() {
        return true;
    }
}
