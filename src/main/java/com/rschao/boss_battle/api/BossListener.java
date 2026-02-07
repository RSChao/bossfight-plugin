package com.rschao.boss_battle.api;

import com.rschao.boss_battle.BossAPI;
import com.rschao.events.definitions.BossChangeEvent;
import com.rschao.items.weapons;
import com.rschao.plugins.showdowncore.showdownCore.api.runnables.ShowdownScript;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Boss;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class BossListener implements Listener {
    static Map<Player, Inventory> rewardsInventories;

    @EventHandler (priority = org.bukkit.event.EventPriority.HIGHEST)
    public void onBossDamage(EntityDamageEvent e){
        if(!(e.getEntity() instanceof Player p)) return;
        if(e.isCancelled()) return;
        BossAPI.findByBoss(p).ifPresent(bi -> {
            if(!bi.isActive()) return;
            ItemStack item = p.getInventory().getItemInOffHand();
            if(!item.getType().isAir() && (item.getType().equals(Material.ECHO_SHARD) || item.getType().equals(Material.TOTEM_OF_UNDYING))) return;
            if(e.getFinalDamage() < p.getHealth()) return;
            if(bi.containsBoss(p) && bi.getCurrentPhase() < BossHandler.getMaxPhase(bi.getBossConfig())){
                e.setCancelled(true);
                bi.advancePhase();
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





}
