package com.rschao.items;

import java.util.List;
import java.util.ArrayList;

import com.rschao.plugins.showdowncore.showdownCore.api.items.registry.ItemRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.RecipeChoice.ExactChoice;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class Hands {
    public static ItemStack Repeller;
    public static ItemStack TimeHand;
    public static ItemStack GravityHand;
    public static ItemStack HealHand;
    public static ItemStack SpeedHand;
    public static ItemStack BraveryHand;
    public static ItemStack CannonHand;
    public static ItemStack EmptyHand;
    public static ItemStack SoulCleaner;
    public static ItemStack SoulCleanerBuffed;
    public static NamespacedKey RepellerKey;
    public static NamespacedKey THKey;
    public static NamespacedKey GMKey;
    public static NamespacedKey HHKey;
    public static NamespacedKey SHKey;
    public static NamespacedKey BHKey;
    public static NamespacedKey CHKey;
    public static NamespacedKey SoulCleanerKey;
    public static NamespacedKey SoulCleanerBuffedKey;

    public static void Init(){
        Keys();
        Repeller();
        TimeHand();
        GravityHand();
        HealHand();
        SpeedHand();
        BraveryHand();
        CannonHand();
        SoulCleaner();
        SoulCleanerBuffed();
        Empty();
        Recipes();
    }
    static void Keys(){
        RepellerKey = new NamespacedKey("gaster", "repeller");
        THKey = new NamespacedKey("gaster", "timestop");
        GMKey = new NamespacedKey("gaster", "gravitator");
        HHKey = new NamespacedKey("gaster", "healer");
        SHKey = new NamespacedKey("gaster", "speed");
        BHKey = new NamespacedKey("gaster", "bravery");
        CHKey = new NamespacedKey("gaster", "determination");
        SoulCleanerKey = new NamespacedKey("gaster", "cleaner");
        SoulCleanerBuffedKey = new NamespacedKey("gaster", "changer");
    }
    static void Repeller(){
        ItemStack item = new ItemStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setMaxStackSize(1);
        meta.setItemName("Magic Hand");
        List<String> list = new ArrayList<String>();
        list.add("Patience");
        meta.setLore(list);
        meta.getPersistentDataContainer().set(RepellerKey, PersistentDataType.BOOLEAN, true);
        meta.setItemModel(NamespacedKey.minecraft("g_hand"));
        item.setItemMeta(meta);
        Repeller = item;
    }
    static void Empty(){
        ItemStack item = new ItemStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setMaxStackSize(1);
        meta.setItemName("Empty Hand");
        List<String> list = new ArrayList<String>();
        list.add("Can absorb a soul trait");
        meta.setLore(list);
        meta.setItemModel(NamespacedKey.minecraft("g_hand_empty"));
        item.setItemMeta(meta);
        EmptyHand = item;
        
        //an expensive recipe to make the empty hand without using other hands and ignoring other recipes in this class
        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("emptyhand"), EmptyHand);
        sr.shape("ERE", "QSQ", "ERE");
        sr.setIngredient('E', new ExactChoice(weapons.CorrupredEssence));
        sr.setIngredient('R', new ExactChoice(ItemRegistry.getItem("smp:compressed_end_power")));
        sr.setIngredient('Q', Material.QUARTZ);
        sr.setIngredient('S', new ExactChoice(Items.SoulContainer));
        Bukkit.getServer().addRecipe(sr);
    }
    static void TimeHand(){
        ItemStack item = new ItemStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setMaxStackSize(1);
        meta.setItemName("Magic Hand");
        List<String> list = new ArrayList<String>();
        list.add("Persevearence");
        meta.setLore(list);
        meta.getPersistentDataContainer().set(THKey, PersistentDataType.BOOLEAN, true);
        meta.setItemModel(NamespacedKey.minecraft("g_hand_p"));
        item.setItemMeta(meta);
        TimeHand = item;
    }
    static void GravityHand(){
        ItemStack item = new ItemStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setMaxStackSize(1);
        meta.setItemName("Magic Hand");
        List<String> list = new ArrayList<String>();
        list.add("Integrity");
        meta.setLore(list);
        meta.getPersistentDataContainer().set(GMKey, PersistentDataType.BOOLEAN, true);
        meta.setItemModel(NamespacedKey.minecraft("g_hand_i"));
        item.setItemMeta(meta);
        GravityHand = item;
    }
    static void HealHand(){
        ItemStack item = new ItemStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setMaxStackSize(1);
        meta.setItemName("Magic Hand");
        List<String> list = new ArrayList<String>();
        list.add("Kindness");
        meta.setLore(list);
        meta.getPersistentDataContainer().set(HHKey, PersistentDataType.BOOLEAN, true);
        meta.setItemModel(NamespacedKey.minecraft("g_hand_k"));
        item.setItemMeta(meta);
        HealHand = item;
    }
    static void SpeedHand(){
        ItemStack item = new ItemStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setMaxStackSize(1);
        meta.setItemName("Magic Hand");
        List<String> list = new ArrayList<String>();
        list.add("Justice");
        meta.setLore(list);
        meta.getPersistentDataContainer().set(SHKey, PersistentDataType.BOOLEAN, true);
        meta.setItemModel(NamespacedKey.minecraft("g_hand_j"));
        item.setItemMeta(meta);
        SpeedHand = item;
    }
    static void BraveryHand(){
        ItemStack item = new ItemStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setMaxStackSize(1);
        meta.setItemName("Magic Hand");
        List<String> list = new ArrayList<String>();
        list.add("Bravery");
        meta.setLore(list);
        meta.getPersistentDataContainer().set(BHKey, PersistentDataType.BOOLEAN, true);
        meta.setItemModel(NamespacedKey.minecraft("g_hand_b"));
        item.setItemMeta(meta);
        BraveryHand = item;
    }
    static void CannonHand(){
        ItemStack item = new ItemStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setMaxStackSize(1);
        meta.setItemName("Magic Hand");
        List<String> list = new ArrayList<String>();
        list.add("Determination");
        meta.setLore(list);
        meta.getPersistentDataContainer().set(CHKey, PersistentDataType.BOOLEAN, true);
        meta.setItemModel(NamespacedKey.minecraft("g_hand_d"));
        item.setItemMeta(meta);
        CannonHand = item;
    }
    static void Recipes(){
        ShapelessRecipe sr = new ShapelessRecipe(NamespacedKey.minecraft("handlightblue"), Repeller);
        sr.addIngredient(new ExactChoice(EmptyHand));
        sr.addIngredient(new ExactChoice(Items.soul_Patience));
        Bukkit.getServer().addRecipe(sr);

        sr = new ShapelessRecipe(NamespacedKey.minecraft("handpurple"), TimeHand);
        sr.addIngredient(new ExactChoice(EmptyHand));
        sr.addIngredient(new ExactChoice(Items.soul_Persevearence));
        Bukkit.getServer().addRecipe(sr);

        sr = new ShapelessRecipe(NamespacedKey.minecraft("handblue"), GravityHand);
        sr.addIngredient(new ExactChoice(EmptyHand));
        sr.addIngredient(new ExactChoice(Items.soul_Integrity));
        Bukkit.getServer().addRecipe(sr);

        sr = new ShapelessRecipe(NamespacedKey.minecraft("handgreen"), HealHand);
        sr.addIngredient(new ExactChoice(EmptyHand));
        sr.addIngredient(new ExactChoice(Items.soul_Kind));
        Bukkit.getServer().addRecipe(sr);

        sr = new ShapelessRecipe(NamespacedKey.minecraft("handclover"), SpeedHand);
        sr.addIngredient(new ExactChoice(EmptyHand));
        sr.addIngredient(new ExactChoice(Items.soul_Justice));
        Bukkit.getServer().addRecipe(sr);

        sr = new ShapelessRecipe(NamespacedKey.minecraft("handorange"), BraveryHand);
        sr.addIngredient(new ExactChoice(EmptyHand));
        sr.addIngredient(new ExactChoice(Items.soul_Bravery));
        Bukkit.getServer().addRecipe(sr);

        sr = new ShapelessRecipe(NamespacedKey.minecraft("handfrisk"), CannonHand);
        sr.addIngredient(new ExactChoice(EmptyHand));
        sr.addIngredient(new ExactChoice(Items.soul_Determination));
        Bukkit.getServer().addRecipe(sr);
    }
    static void SoulCleaner(){
        ItemStack item = new ItemStack(Material.LEATHER);
        ItemMeta meta = item.getItemMeta();
        meta.setMaxStackSize(1);
        meta.setItemName("Soul Cleaner");
        List<String> list = new ArrayList<String>();
        list.add("Cleans your soul trait");
        list.add("Wont give the soul back");
        meta.setLore(list);
        meta.getPersistentDataContainer().set(SoulCleanerKey, PersistentDataType.BOOLEAN, true);
        meta.setItemModel(NamespacedKey.minecraft("soul_cleaner"));
        item.setItemMeta(meta);
        SoulCleaner = item;
    }
    static void SoulCleanerBuffed(){
        ItemStack item = new ItemStack(Material.LEATHER);
        ItemMeta meta = item.getItemMeta();
        meta.setMaxStackSize(1);
        meta.setItemName("Soul Changer");
        List<String> list = new ArrayList<String>();
        list.add("Cleans your soul trait");
        list.add("Will give the soul back");
        meta.setEnchantmentGlintOverride(true);
        meta.setLore(list);
        meta.getPersistentDataContainer().set(SoulCleanerBuffedKey, PersistentDataType.BOOLEAN, true);
        meta.setItemModel(NamespacedKey.minecraft("soul_cleaner"));
        item.setItemMeta(meta);
        SoulCleanerBuffed = item;
    }
}
