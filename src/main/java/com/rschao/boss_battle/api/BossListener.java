package com.rschao.boss_battle.api;

import com.rschao.boss_battle.BossAPI;
import com.rschao.enchants.Determined;
import com.rschao.events.definitions.BossChangeEvent;
import com.rschao.items.weapons;
import com.rschao.plugins.showdowncore.showdownCore.api.runnables.ShowdownScript;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Boss;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

import com.rschao.boss_battle.DropsManager;

public class BossListener implements Listener {
    public static String bossName;

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onBossDamage(EntityDamageEvent e){
        if(!(e.getEntity() instanceof Player p)) return;
        if(e.isCancelled()) return;
        BossAPI.findByBoss(p).ifPresent(bi -> {
            if(!bi.isActive()) return;

            if (bi.containsBoss(p) && BossAPI.getBossHealth(bi.getBossConfig(), bi.getCurrentPhase()) != null) {
                Enchantment ench = new Determined().getCustomEnchantment().toBukkitEnchantment();
                if(ench != null) {
                    ItemStack item = p.getInventory().getItemInOffHand();
                    if(item.containsEnchantment(ench)){
                        if(e.getDamage() > 500){
                            bi.applyBossDamage(100);
                        }
                    }
                }
                bi.applyBossDamage(e.getFinalDamage());
                return;
            }
        });
    }

    @EventHandler
    public void onPlayerDeath(org.bukkit.event.entity.PlayerDeathEvent e){
        Player p = e.getEntity();
        for(BossInstance bi : BossAPI.getInstancesWithFighter(p)){
            if(bi.containsBoss(p)){
                bi.removeBoss(p);
                if(bi.getBosses().isEmpty()){
                    bi.getBosses().add(p);
                    bi.end();
                    e.getDrops().clear();
                }
            } else if(bi.containsFighter(p)){
                bi.removeFighter(p);
                if(bi.getFighters().isEmpty()){
                    bi.end();
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player player)) return;

        String title = e.getView().getTitle();
        if (!title.equals("§6Boss Drops")) return;

        String bossName = BossListener.bossName;
        if (bossName != null && !bossName.equals("unknown")) {
            DropsManager.saveDropsToConfig(bossName, e.getInventory());
            player.sendMessage("§aBoss drops saved!");
        }
    }
}
