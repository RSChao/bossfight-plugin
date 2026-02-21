package com.rschao.events;

import java.util.ArrayList;
import java.util.List;

import com.rschao.plugins.showdowncore.showdownCore.api.runnables.ShowdownScript;
import com.rschao.plugins.showdowncore.showdownCore.api.runnables.registry.ScriptRegistry;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
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

    public static void ImplementCore(){
        ShowdownScript popPlayer = new ShowdownScript<Void>((Object[] args) -> {
            Player p = (Player) args[0];
            for(PotionEffect effect : p.getActivePotionEffects()){
                p.removePotionEffect(effect.getType());
            }
            p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 1200, 3));
            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 300, 1));
            p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 800, 1));
            p.playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, 25, 1);
            p.setHealth(1);
            PlayerPopHeartEvent event = new PlayerPopHeartEvent(p, 1, 1);
            Bukkit.getPluginManager().callEvent(event);
            return null;
        });
        ScriptRegistry.registerScript("bossfight:pop_player", popPlayer);
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void damage(EntityDamageEvent ev){
        if(!(ev.getEntity() instanceof Player)) return;
        Player p = (Player) ev.getEntity();
        if(!p.getInventory().getItemInOffHand().hasItemMeta()) return;
        if (((p.getHealth() - ev.getFinalDamage()) <= 0) && p.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(weapons.CHKey, PersistentDataType.INTEGER))
        {
            ItemStack item = p.getInventory().getItemInOffHand();

            int level =item.getEnchantmentLevel(Enchantment.getByKey(NamespacedKey.minecraft("determined")));
            if(level<1) level = 1;
            if(p.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().get(weapons.CHKey, PersistentDataType.INTEGER).intValue() > 3*level) return;
            ev.setCancelled(true);
            p.sendMessage("But it refused");
            int t = getTest(p.getInventory().getItemInOffHand(), weapons.CHKey);
            t += 1;
            setTest(p.getInventory().getItemInOffHand(), t, weapons.CHKey);
            popPlayerLimit(p, t, 3*level);
            if(t >=3*level){
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
    
    public static void setTest(ItemStack item, int progress, NamespacedKey key) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(key, PersistentDataType.INTEGER, progress);
        item.setItemMeta(meta);
    }
    public static int getTest(ItemStack item, NamespacedKey key)
    {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        return pdc.get(key, PersistentDataType.INTEGER);
    }
    public static void popPlayerLimit(Player p, int timesUsed, int maxUses){
        for(PotionEffect effect : p.getActivePotionEffects()){
            p.removePotionEffect(effect.getType());
        }
        p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 1200, 3));
        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 300, 1));
        p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 800, 1));
        p.playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, 25, 1);
        p.setHealth(1);
        PlayerPopHeartEvent event = new PlayerPopHeartEvent(p, timesUsed, maxUses);
        Bukkit.getPluginManager().callEvent(event);
    }
}
