package com.rschao.enchants;

import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.CustomEnchantment;
import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.definition.EasyEnchant;
import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.util.ColorCodes;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ItemTag;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.md_5.bungee.api.chat.hover.content.Item;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Random;

public class OblivionEnchant extends EasyEnchant {
    public OblivionEnchant() {
        super("oblivion");
        CustomEnchantment enchant = makeEnchantment(ChatColor.BLACK + ColorCodes.BOLD.getCode() + "Oblivion");
        enchant.setSupportedItem("#minecraft:enchantable/sharp_weapon");
        enchant.setMaxLevel(1);
        saveBukkitEnchantment(enchant);
    }

    @Override @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent ev) {
        if(ev.getDamager() instanceof Player player && ev.getEntity() instanceof Player damaged) {
            int rng = (new Random()).nextInt(0, 100);
            if(player.getInventory().getItemInMainHand().getType() == Material.AIR) return;
            if(!hasEnchantment(player.getInventory().getItemInMainHand())) return;
            if(rng < 3){
                ItemStack[] array = damaged.getInventory().getContents();
                int random = (new Random()).nextInt(0, array.length-1);
                if(GetBlackListItem(array[random])){
                    if(array[random].getType().equals(Material.AIR)) random = (new Random()).nextInt(0, array.length-1);
                    if(array[random].getType().equals(Material.AIR)){
                        player.sendMessage("But no memories were forgotten");
                    }
                    TextComponent txt = getTextComponent(array, random);
                    player.sendMessage("Your enemy's items fell into oblivion:");
                    player.spigot().sendMessage(txt);
                    damaged.sendMessage("Your items fell into oblivion");
                    damaged.spigot().sendMessage(txt);


                    array[random].setAmount(0);
                    damaged.getInventory().setContents(array);
                }
                else{
                    player.sendMessage("But no memories were forgotten");
                }
            }
        }
    }

    private static TextComponent getTextComponent(ItemStack[] array, int random) {
        ItemMeta meta = array[random].getItemMeta();
        TextComponent txt = new TextComponent(new TranslatableComponent(array[random].getTranslationKey()));
        if (meta!=null){
            String nbt = meta.getAsString();
            ItemTag tag = ItemTag.ofNbt(nbt);
            Item hover = new Item(array[random].getType().getKey().toString(), array[random].getAmount(), tag);
            txt.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, hover));

        }
        return txt;
    }

    boolean GetBlackListItem(ItemStack item){
        boolean bool = false;
        if(item == null) return bool;
        if(item.getItemMeta() == null) return bool;
        if(!item.getItemMeta().getPersistentDataContainer().isEmpty()) return bool;
        if(!item.getEnchantments().isEmpty()) return bool;
        switch (item.getType()) {
            case NETHERITE_SWORD, NETHERITE_HELMET, NETHERITE_BOOTS, WILD_ARMOR_TRIM_SMITHING_TEMPLATE, NAUTILUS_SHELL,
                 TURTLE_SCUTE, LEATHER, NETHERITE_LEGGINGS, NETHERITE_CHESTPLATE, DIAMOND_SWORD, HEART_OF_THE_SEA,
                 NETHER_STAR, SHIELD, ECHO_SHARD, ELYTRA, BLAZE_POWDER, COOKIE:
                break;
            default:
                bool = true;
                break;
        }
        return bool;
    }
}
