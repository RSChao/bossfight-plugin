package com.rschao.enchants;

import com.rschao.events.events;
import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.CustomEnchantment;
import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.definition.EasyEnchant;
import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.util.ColorCodes;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class GenoEnchant extends EasyEnchant {
    public GenoEnchant() {
        super("geno");
        CustomEnchantment enchant = makeEnchantment(ChatColor.DARK_RED + ColorCodes.BOLD.getCode() + "Genocidal");
        enchant.setSupportedItem("#minecraft:enchantable/sharp_weapon");
        enchant.setMaxLevel(2);
        saveBukkitEnchantment(enchant);
    }
    @Override @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDamage(EntityDamageByEntityEvent ev) {
        if(ev.getDamager() instanceof Player player && ev.getEntity() instanceof Player damaged){
            ItemStack itemUsed = player.getInventory().getItemInMainHand();
            if(itemUsed.getType() == Material.AIR) return;
            int rng = (new Random()).nextInt(0, 100);

            if(!hasEnchantment(itemUsed)) return;
            int level = itemUsed.getEnchantmentLevel(Enchantment.getByKey(getKey()));
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
}
