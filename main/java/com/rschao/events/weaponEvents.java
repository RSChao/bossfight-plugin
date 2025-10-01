package com.rschao.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.rschao.events.definitions.PlayerPopHeartEvent;
import com.rschao.items.weapons;

public class weaponEvents implements Listener{
    int timer = 0;
    @EventHandler (priority = EventPriority.HIGH)
    public void damage(EntityDamageEvent ev){
        if(!(ev.getEntity() instanceof Player)) return;
        Player p = (Player) ev.getEntity();
        if (((p.getHealth() - ev.getFinalDamage()) <= 0) && p.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(weapons.CHKey, PersistentDataType.INTEGER))
        {
            if(p.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().get(weapons.CHKey, PersistentDataType.INTEGER).intValue() > 3) return;
            ev.setCancelled(true);
            p.sendMessage("But it refused");
            int t = getTest(p.getInventory().getItemInOffHand());
            t += 1;
            setTest(p.getInventory().getItemInOffHand(), t);
            for(PotionEffect effect : p.getActivePotionEffects()){
                p.removePotionEffect(effect.getType());
            }
            p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 1200, 3));
            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 300, 1));
            p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 800, 1));
            p.playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, 25, 1);
            p.setHealth(1);
            PlayerPopHeartEvent event = new PlayerPopHeartEvent(p, t);
            Bukkit.getPluginManager().callEvent(event);
            ItemStack item = p.getInventory().getItemInOffHand();
            if(t >=3){
                item.setAmount(0);
                return;
            }
            ItemMeta meta = item.getItemMeta();
            List<String> list = new ArrayList<String>();
            list.add("Times used:");
            list.add(String.valueOf(t));
            meta.setLore(list);
            item.setItemMeta(meta);
            p.getInventory().setItemInOffHand(item);

        }
    }
    @EventHandler
    void onPlayerLogin(PlayerLoginEvent ev){
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "whitelist off");
    }
    
    public static void setTest(ItemStack item, int progress) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(weapons.CHKey, PersistentDataType.INTEGER, progress);
        item.setItemMeta(meta);
    }
    public static int getTest(ItemStack item)
    {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        return pdc.get(weapons.CHKey, PersistentDataType.INTEGER);
    }
}
