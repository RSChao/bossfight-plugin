package com.rschao.enchants;

import com.rschao.smp.enchants.definition.Enchant;
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

public class OblivionEnchant extends Enchant {
    public OblivionEnchant() {
        super("oblivion");
    }

    @Override
    public String getDisplayName() {
        return ChatColor.BLACK + (ChatColor.BOLD + "Oblivion");
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
        if(ev.getDamager() instanceof Player player && ev.getEntity() instanceof Player damaged) {
            int rng = (new Random()).nextInt(0, 100);
            if (!hasEnchant(player.getInventory().getItemInMainHand())) return;
            if(rng < 30 && rng > 25){
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
        switch (item.getType()) {
            case NETHERITE_SWORD, NETHERITE_HELMET, NETHERITE_BOOTS, WILD_ARMOR_TRIM_SMITHING_TEMPLATE, NAUTILUS_SHELL,
                 TURTLE_SCUTE, LEATHER, NETHERITE_LEGGINGS, NETHERITE_CHESTPLATE, DIAMOND_SWORD, HEART_OF_THE_SEA,
                 NETHER_STAR, SHIELD, ECHO_SHARD, ELYTRA, BLAZE_POWDER:
                break;
            default:
                bool = true;
                break;
        }
        return bool;
    }

    @Override
    public boolean isTreasure() {
        return true;
    }
}
