package com.rschao.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import org.bukkit.NamespacedKey;

public class SoulData {
    public static ItemStack soul(NamespacedKey key, int tier, String trait, String model, boolean glint){
        
        ItemStack item = new ItemStack(Material.LEATHER);
        ItemMeta meta = item.getItemMeta();
        meta.setRarity(ItemRarity.EPIC);
        meta.setMaxStackSize(1);
        meta.setFireResistant(true);
        meta.setEnchantmentGlintOverride(glint);
        switch(tier){
            case 1:
            meta.setItemName("Human Soul");
                break;
            case 2:
            meta.setItemName("Empowered Human Soul");
                break;
            case 3:
            meta.setItemName("Corrupted Soul");
                break;
            case 4:
            meta.setItemName("Great Human Soul");
                break;
            case 5:
                meta.setItemName("Superior Human Soul");
                break;
            case 8:
                meta.setItemName("Corazón de la Pureza");
                break;
            case 7:
                meta.setItemName("Corazón del Caos");
                break;
            default:
            meta.setItemName("Human Soul");
                break;
        }
        List<String> list = getStrings(tier, trait);
        meta.setLore(list);
        meta.setItemModel(NamespacedKey.minecraft(model));
        meta.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        return item;
    }

    private static List<String> getStrings(int tier, String trait) {
        List<String> list = new ArrayList<String>();
        list.add(trait);
        if(tier >6){
            if(tier == 7){
                list.add("Este corazón representa");
                list.add("el odio y la malicia del corazón humano.");
                list.add("Equiparlo como un alma potenciará el poder");
                list.add("nacido del abismo");
            }
            else if (tier == 8){
                list.add("Este corazón representa");
                list.add("el amor y la bondad del corazón humano.");
                list.add("Equiparlo como un alma debilitará el poder");
                list.add("que nace del abismo y amenaza tu vida");
                list.add("Tu propio poder se verá inafectado");
            }
        }
        return list;
    }
}
