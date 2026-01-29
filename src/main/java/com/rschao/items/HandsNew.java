package com.rschao.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice.ExactChoice;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class HandsNew {
    public static ItemStack SincerityHand;
    public static ItemStack EmpathyHand;
    public static ItemStack AudacityHand;
    public static ItemStack GuidanceHand;
    public static ItemStack PurityHand;
    public static ItemStack LoveHand;
    public static ItemStack FortitudeHand;
    public static ItemStack SevenHand;
    public static NamespacedKey ZHKey;
    public static NamespacedKey EHKey;
    public static NamespacedKey AHKey;
    public static NamespacedKey GHKey;
    public static NamespacedKey PHKey;
    public static NamespacedKey LHKey;
    public static NamespacedKey FHKey;
    public static NamespacedKey SHKey;

    public static void keys(){
        ZHKey = new NamespacedKey("gaster", "sincerity");
        EHKey = new NamespacedKey("gaster", "empathy");
        AHKey = new NamespacedKey("gaster", "audacity");
        GHKey = new NamespacedKey("gaster", "guidance");
        PHKey = new NamespacedKey("gaster", "purity");
        LHKey = new NamespacedKey("gaster", "love");
        FHKey = new NamespacedKey("gaster", "fortitude");
        SHKey = new NamespacedKey("gaster", "seven_powered");
    }

    public static void init() {
        keys();
        ShieldHand();
        ZoomHand();
        ArrowHand();
        PurityHand();
        EmpathyHand();
        LoveHand();
        FortitudeHand();
        SevenHand();
    }
    public static void ShieldHand() {
        ItemStack item = new ItemStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setItemModel(NamespacedKey.minecraft("g_hand_guidance"));
        meta.setItemName("Empowered Magic Hand");
        List<String> lore = new ArrayList<>();
        lore.add("Guidance");
        meta.setLore(lore);
        meta.getPersistentDataContainer().set(GHKey, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        GuidanceHand = item;

        ShapelessRecipe sr = new ShapelessRecipe(NamespacedKey.minecraft("handguidance"), GuidanceHand);
        sr.addIngredient(new ExactChoice(Hands.EmptyHand));
        sr.addIngredient(new ExactChoice(Items.soul_Guidance));
        Bukkit.getServer().addRecipe(sr);
    }
    public static void ZoomHand() {
        ItemStack item = new ItemStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setItemModel(NamespacedKey.minecraft("g_hand_sincerity"));
        meta.setItemName("Empowered Magic Hand");
        List<String> lore = new ArrayList<>();
        lore.add("Sincerity");
        meta.setLore(lore);
        meta.getPersistentDataContainer().set(ZHKey, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        SincerityHand = item;

        ShapelessRecipe sr = new ShapelessRecipe(NamespacedKey.minecraft("handsincerity"), SincerityHand);
        sr.addIngredient(new ExactChoice(Hands.EmptyHand));
        sr.addIngredient(new ExactChoice(Items.soul_Sincerity));
        Bukkit.getServer().addRecipe(sr);
    }
    public static void ArrowHand() {
        ItemStack item = new ItemStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setItemModel(NamespacedKey.minecraft("g_hand_audacity"));
        meta.setItemName("Empowered Magic Hand");
        List<String> lore = new ArrayList<>();
        lore.add("Audacity");
        meta.setLore(lore);
        meta.getPersistentDataContainer().set(AHKey, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        AudacityHand = item;

        ShapelessRecipe sr = new ShapelessRecipe(NamespacedKey.minecraft("handaudacity"), AudacityHand);
        sr.addIngredient(new ExactChoice(Hands.EmptyHand));
        sr.addIngredient(new ExactChoice(Items.soul_Audacity));
        Bukkit.getServer().addRecipe(sr);
    }
    public static void PurityHand() {
        ItemStack item = new ItemStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setItemModel(NamespacedKey.minecraft("g_hand_purity"));
        meta.setItemName("Empowered Magic Hand");
        List<String> lore = new ArrayList<>();
        lore.add("Purity");
        meta.setLore(lore);
        meta.getPersistentDataContainer().set(PHKey, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        PurityHand = item;

        ShapelessRecipe sr = new ShapelessRecipe(NamespacedKey.minecraft("handpurity"), PurityHand);
        sr.addIngredient(new ExactChoice(Hands.EmptyHand));
        sr.addIngredient(new ExactChoice(Items.soul_Purity));
        Bukkit.getServer().addRecipe(sr);
    }
    public static void EmpathyHand() {
        ItemStack item = new ItemStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setItemModel(NamespacedKey.minecraft("g_hand_empathy"));
        meta.setItemName("Empowered Magic Hand");
        List<String> lore = new ArrayList<>();
        lore.add("Empathy");
        meta.setLore(lore);
        meta.getPersistentDataContainer().set(EHKey, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        EmpathyHand = item;

        ShapelessRecipe sr = new ShapelessRecipe(NamespacedKey.minecraft("handempathy"), EmpathyHand);
        sr.addIngredient(new ExactChoice(Hands.EmptyHand));
        sr.addIngredient(new ExactChoice(Items.soul_Empathy));
        Bukkit.getServer().addRecipe(sr);
    }
    public static void LoveHand() {
        ItemStack item = new ItemStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setItemModel(NamespacedKey.minecraft("g_hand_love"));
        meta.setItemName("Empowered Magic Hand");
        List<String> lore = new ArrayList<>();
        lore.add("Love");
        meta.setLore(lore);
        meta.getPersistentDataContainer().set(LHKey, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        LoveHand = item;

        ShapelessRecipe sr = new ShapelessRecipe(NamespacedKey.minecraft("handlove"), LoveHand);
        sr.addIngredient(new ExactChoice(Hands.EmptyHand));
        sr.addIngredient(new ExactChoice(Items.soul_Love));
        Bukkit.getServer().addRecipe(sr);
    }

    public static void FortitudeHand() {
        ItemStack item = new ItemStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setItemModel(NamespacedKey.minecraft("g_hand_fortitude"));
        meta.setItemName("Empowered Magic Hand");
        List<String> lore = new ArrayList<>();
        lore.add("Fortitude");
        meta.setLore(lore);
        meta.getPersistentDataContainer().set(FHKey, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        FortitudeHand = item;

        ShapelessRecipe sr = new ShapelessRecipe(NamespacedKey.minecraft("handfortitude"), FortitudeHand);
        sr.addIngredient(new ExactChoice(Hands.EmptyHand));
        sr.addIngredient(new ExactChoice(Items.soul_Fortitude));
        Bukkit.getServer().addRecipe(sr);
    }
    public static void SevenHand() {
        ItemStack item = new ItemStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setItemModel(NamespacedKey.minecraft("g_seven_hands_powered"));
        meta.setRarity(ItemRarity.EPIC);
        meta.setItemName("Empowered Seven Hands");
        meta.getPersistentDataContainer().set(SHKey, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        SevenHand = item;

        ShapelessRecipe sr = new ShapelessRecipe(NamespacedKey.minecraft("handsevenbuffed"), SevenHand);
        sr.addIngredient(new ExactChoice(GuidanceHand));
        sr.addIngredient(new ExactChoice(SincerityHand));
        sr.addIngredient(new ExactChoice(AudacityHand));
        sr.addIngredient(new ExactChoice(PurityHand));
        sr.addIngredient(new ExactChoice(EmpathyHand));
        sr.addIngredient(new ExactChoice(LoveHand));
        sr.addIngredient(new ExactChoice(FortitudeHand));
        Bukkit.getServer().addRecipe(sr);
    }
}
