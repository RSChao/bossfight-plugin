package com.rschao.items;

import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

import com.rschao.enchants.*;
import com.rschao.plugins.showdowncore.showdownCore.api.items.registry.ItemRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.RecipeChoice.ExactChoice;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.EquippableComponent;
import org.bukkit.persistence.PersistentDataType;

import net.md_5.bungee.api.ChatColor;

public class weapons {
    public static ItemStack CorruptedHeart;
    public static NamespacedKey CHKey;
    public static ItemStack CorruptedHeart2;
    public static ItemStack CorruptedHeart3;
    public static ItemStack CorruptedHeart4;
    public static ItemStack CorruptedHeart5;
    public static NamespacedKey CH2Key;
    public static ItemStack SansEye;
    public static NamespacedKey SEKey;
    public static ItemStack BadEye;
    public static NamespacedKey BEKey;
    public static ItemStack TheToys;
    public static NamespacedKey TTKey;
    public static ItemStack SevenSouls;
    public static NamespacedKey SSKey;
    public static ItemStack SevenHands;
    public static NamespacedKey SHKey;
    public static ItemStack ChaoSword;
    public static NamespacedKey CSKey;
    public static ItemStack DeltaSword;
    public static NamespacedKey DSKey;
    public static ItemStack RustedSword;
    public static NamespacedKey RSKey;
    public static ItemStack AwakenedSword;
    public static NamespacedKey ASKey;
    public static ItemStack Container;
    public static NamespacedKey FCKey;
    public static ItemStack OblivionEssence;
    public static NamespacedKey OSKey;
    public static ItemStack CorrupredEssence;
    public static NamespacedKey CEKey;
    public static ItemStack DimentioMask;
    public static ItemStack Devilsknife;
    public static NamespacedKey DKKey;
    public static ItemStack ChocoBlade;
    public static NamespacedKey CBKey;
    public static void Init(){
        Keys();
        CorruptedHeart();
        CorruptedHeart2();
        CorruptedHeart3();
        CorruptedHeart4();
        CorruptedHeart5();
        SansEye();
        BadEye();
        TheToys();
        SevenSouls();
        ChaoSword();
        RustedSword();
        AwakenedSword();
        Container();
        OblivionEssence();
        OblivionEdge();
        CorruptedEssence();
        DimentioHelmet();
        Devilsknife();
        Chocoblade();
    }
    static void Keys(){
        CHKey = new NamespacedKey("weapon", "corrupted_heart");
        CH2Key = new NamespacedKey("weapon", "corrupted_heart_two");
        SEKey = new NamespacedKey("weapon", "sans_eye");
        BEKey = new NamespacedKey("weapon", "bad_eye");
        TTKey = new NamespacedKey("weapon", "toy");
        SSKey = new NamespacedKey("soul", "seven");
        SHKey = new NamespacedKey("gaster", "sevenhands");
        CSKey = new NamespacedKey("weapon", "chaosword");
        DSKey = new NamespacedKey("weapon", "deltasword");
        RSKey = new NamespacedKey("weapons", "rusted");
        ASKey = new NamespacedKey("weapons", "sevenam");
        FCKey = new NamespacedKey("weapons", "container");
        OSKey = new NamespacedKey("weapons", "coolessence");
        CEKey = new NamespacedKey("weapons", "c_essence");
        DKKey = new NamespacedKey("weapons", "devilsknife");
        CBKey = new NamespacedKey("weapons", "chocoblade");
    }
    static void CorruptedHeart(){
        ItemStack item = new ItemStack(Material.ECHO_SHARD);
        ItemMeta meta = item.getItemMeta();
        meta.setMaxStackSize(1);
        meta.setItemName("Corrupted Heart");
        List<String> list = new ArrayList<String>();
        list.add("Its effects are a mistery");
        meta.setLore(list);
        meta.getPersistentDataContainer().set(CHKey, PersistentDataType.INTEGER, 0);
        meta.setItemModel(NamespacedKey.minecraft("c_heart"));
        meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("determined")), 1, false);
        item.setItemMeta(meta);
        CorruptedHeart = item;

        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("thething"), CorruptedHeart);
        sr.shape("HHH", "HDH", "HHH");
        sr.setIngredient('H', Material.NETHER_STAR);
        sr.setIngredient('D', new ExactChoice(Items.DeterminationEssence));
        Bukkit.addRecipe(sr);
    }
    static void CorruptedHeart2(){
        ItemStack item = new ItemStack(CorruptedHeart);
        ItemMeta meta = item.getItemMeta();
        meta.setItemName("Corrupted Heart");
        List<String> list = new ArrayList<String>();
        list.add("Its effects are a mistery");
        meta.setLore(list);
        meta.getPersistentDataContainer().set(CHKey, PersistentDataType.INTEGER, 0);
        meta.setItemModel(NamespacedKey.minecraft("c_heart"));
        meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("determined")), 2, false);
        meta.setEnchantmentGlintOverride(true);
        item.setItemMeta(meta);
        CorruptedHeart2 = item;

        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("thethingtwo"), CorruptedHeart2);
        sr.shape("HHH", "HDH", "HHH");
        sr.setIngredient('H', new ExactChoice(Items.DeterminationEssence));
        sr.setIngredient('D', new ExactChoice(CorruptedHeart));
        Bukkit.addRecipe(sr);
    }
    static void CorruptedHeart3(){
        ItemStack item = new ItemStack(CorruptedHeart);
        ItemMeta meta = item.getItemMeta();
        meta.setItemName("Corrupted Heart");
        List<String> list = new ArrayList<String>();
        list.add("Its effects are a mistery");
        meta.setLore(list);
        meta.getPersistentDataContainer().set(CHKey, PersistentDataType.INTEGER, 0);
        meta.setItemModel(NamespacedKey.minecraft("c_heart"));
        meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("determined")), 3, false);
        meta.setEnchantmentGlintOverride(true);
        item.setItemMeta(meta);
        CorruptedHeart3 = item;

        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("thethingsri"), CorruptedHeart3);
        sr.shape("HHH", "HDH", "HHH");
        sr.setIngredient('H', new ExactChoice(Items.soul_Determination));
        sr.setIngredient('D', new ExactChoice(CorruptedHeart2));
        Bukkit.addRecipe(sr);
    }
    static void CorruptedHeart4(){
        ItemStack item = new ItemStack(CorruptedHeart);
        ItemMeta meta = item.getItemMeta();
        meta.setItemName("Corrupted Heart");
        List<String> list = new ArrayList<String>();
        list.add("Its effects are a mistery");
        meta.setLore(list);
        meta.getPersistentDataContainer().set(CHKey, PersistentDataType.INTEGER, 0);
        meta.setItemModel(NamespacedKey.minecraft("c_heart"));
        meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("determined")), 4, false);
        meta.setEnchantmentGlintOverride(true);
        item.setItemMeta(meta);
        CorruptedHeart4 = item;

        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("thethingforlup"), CorruptedHeart4);
        sr.shape("HHH", "HDH", "HHH");
        sr.setIngredient('H', new ExactChoice(Items.DeterminationEssence));
        sr.setIngredient('D', new ExactChoice(CorruptedHeart3));
        Bukkit.addRecipe(sr);
    }
    static void CorruptedHeart5(){
        ItemStack item = new ItemStack(CorruptedHeart);
        ItemMeta meta = item.getItemMeta();
        meta.setItemName("Corrupted Heart");
        List<String> list = new ArrayList<String>();
        list.add("Its effects are a mistery");
        meta.setLore(list);
        meta.getPersistentDataContainer().set(CHKey, PersistentDataType.INTEGER, 0);
        meta.setItemModel(NamespacedKey.minecraft("c_heart"));
        meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("determined")), 5, false);
        meta.setEnchantmentGlintOverride(true);
        item.setItemMeta(meta);
        CorruptedHeart5 = item;

        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("thethingfaiv"), CorruptedHeart5);
        sr.shape("HHH", "HDH", "HHH");
        sr.setIngredient('H', new ExactChoice(Items.DeterminationCrystal));
        sr.setIngredient('D', new ExactChoice(CorruptedHeart4));
        Bukkit.addRecipe(sr);
    }
    static void SansEye(){
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        meta.setMaxStackSize(1);
        meta.setItemName("Eye of Judgement");
        meta.getPersistentDataContainer().set(SEKey, PersistentDataType.INTEGER, 0);
        meta.setItemModel(NamespacedKey.minecraft("eye_j"));
        item.setItemMeta(meta);
        SansEye = item;
        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("sans_eye"), SansEye);
        sr.shape("HGH", "GDG", "HGH");
        sr.setIngredient('H', Material.NETHER_STAR);
        sr.setIngredient('G', new ExactChoice(Items.DeterminationCrystal));
        sr.setIngredient('D', new ExactChoice(Items.DeterminationEssence));
        Bukkit.addRecipe(sr);
    }
    static void BadEye(){
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        meta.setMaxStackSize(1);
        meta.setItemName("Eye of Execution");
        meta.getPersistentDataContainer().set(BEKey, PersistentDataType.INTEGER, 0);
        meta.setItemModel(NamespacedKey.minecraft("eye_e"));
        item.setItemMeta(meta);
        BadEye = item;
    }
    static void TheToys(){
        ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setItemName(ChatColor.DARK_GRAY + "Toy knife");
        meta.addEnchant(Enchantment.KNOCKBACK, 10, true);
        meta.setItemModel(NamespacedKey.minecraft("toy_knife"));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.getPersistentDataContainer().set(TTKey, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        TheToys = item;
    }
    static void SevenSouls(){
        ItemStack item = new ItemStack(Material.TURTLE_SCUTE);
        ItemMeta meta = item.getItemMeta();
        meta.setMaxStackSize(1);
        meta.setRarity(ItemRarity.EPIC);
        meta.setItemName("Seven Souls");
        meta.getPersistentDataContainer().set(SSKey, PersistentDataType.BOOLEAN, true);
        meta.setItemModel(NamespacedKey.minecraft("seven_soul"));
        item.setItemMeta(meta);
        SevenSouls = item;
    }
    public static void SevenHands(){
        ItemStack item = new ItemStack(Material.NAUTILUS_SHELL);
        ItemMeta meta = item.getItemMeta();
        meta.setMaxStackSize(1);
        meta.setRarity(ItemRarity.EPIC);
        meta.setItemName("Seven Hands");
        meta.getPersistentDataContainer().set(SHKey, PersistentDataType.BOOLEAN, true);
        meta.setItemModel(NamespacedKey.minecraft("g_seven_hands"));
        item.setItemMeta(meta);
        SevenHands = item;

        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("sevenhands"), SevenHands);
        sr.shape("KDT", "B P", "J I");
        //set the ingredients to the magic hands from class Hands.java
        sr.setIngredient('K', new ExactChoice(Hands.HealHand));
        sr.setIngredient('B', new ExactChoice(Hands.BraveryHand));
        sr.setIngredient('D', new ExactChoice(Hands.CannonHand));
        sr.setIngredient('P', new ExactChoice(Hands.TimeHand));
        sr.setIngredient('T', new ExactChoice(Hands.Repeller));
        sr.setIngredient('J', new ExactChoice(Hands.SpeedHand));
        sr.setIngredient('I', new ExactChoice(Hands.GravityHand));
        Bukkit.addRecipe(sr);
    }
    static void ChaoSword(){
        ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setItemName(ChatColor.DARK_PURPLE + "Ancient " + ChatColor.AQUA + "Chao" + ChatColor.LIGHT_PURPLE + " Sword");
        meta.addEnchant(Enchantment.SHARPNESS, 10, true);
        meta.addEnchant(Enchantment.FIRE_ASPECT, 2, true);
        meta.addEnchant(Enchantment.LOOTING, 3, true);
        meta.addEnchant(Enchantment.UNBREAKING, 10, true);
        meta.setUnbreakable(true);
        meta.setItemModel(NamespacedKey.minecraft("chao_katana"));
        meta.getPersistentDataContainer().set(CSKey, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("geno")), 1, true);
        meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("wither")), 1, true);
        ChaoSword = item;
    }
    static void OblivionEdge(){
        ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setItemName(ChatColor.DARK_PURPLE + "Edge of Oblivion");
        meta.addEnchant(Enchantment.SHARPNESS, 7, true);
        meta.addEnchant(Enchantment.FIRE_ASPECT, 2, true);
        meta.addEnchant(Enchantment.LOOTING, 3, true);
        meta.addEnchant(Enchantment.UNBREAKING, 10, true);
        meta.setUnbreakable(true);
        meta.setItemModel(NamespacedKey.minecraft("oblivion_edge"));
        meta.getPersistentDataContainer().set(DSKey, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("glitch")), 1, true);
        meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("oblivion")), 1, true);
        DeltaSword = item;

        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("deltamamon"), item);
        sr.shape("CGH", "GEG", "GSG");
        sr.setIngredient('G', new ExactChoice(ItemRegistry.getItem("smp:glitch_arrow")));
        sr.setIngredient('E', new ExactChoice(OblivionEssence));
        sr.setIngredient('S', Material.NETHERITE_SWORD);
        sr.setIngredient('H', new ExactChoice(ItemRegistry.getItem("smp:health_gem")));
        sr.setIngredient('C', new ExactChoice(CorruptedHeart));
        Bukkit.addRecipe(sr);
    }
    static void RustedSword(){
        ItemStack item = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setItemName(ChatColor.of(new Color(79, 65, 31)) + "Rusted Edge of Oblivion");
        List<String> list = new ArrayList<>();
        list.add("Used to be very powerful");
        list.add("may be restored with some item...");
        meta.setLore(list);
        meta.getPersistentDataContainer().set(RSKey, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        RustedSword = item;
    }
    static void AwakenedSword(){
        ItemStack item = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setItemName(ChatColor.DARK_PURPLE + "Edge of Oblivion");
        List<String> list = new ArrayList<>();
        list.add("Breaking it shall free its power");
        meta.setLore(list);
        meta.getPersistentDataContainer().set(ASKey, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        AwakenedSword = item;
    }
    static void Container(){
        ItemStack item = new ItemStack(Material.HEART_OF_THE_SEA);
        ItemMeta meta = item.getItemMeta();
        meta.setItemName(ChatColor.LIGHT_PURPLE + "Forgotten Retainer");
        meta.getPersistentDataContainer().set(FCKey, PersistentDataType.BOOLEAN, true);
        List<String> list = new ArrayList<>();
        list.add("Gives power to forgotten objects");
        meta.setLore(list);
        item.setItemMeta(meta);
        Container = item;

        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("contenedor"), item);
        sr.shape("DED", "SFS", "DED");
        sr.setIngredient('D', new ExactChoice(ItemRegistry.getItem("smp:wither_pot")));
        sr.setIngredient('E', Material.ECHO_SHARD);
        sr.setIngredient('S', Material.WITHER_SKELETON_SKULL);
        sr.setIngredient('F', Material.END_PORTAL_FRAME);
        Bukkit.addRecipe(sr);
    }
    static void OblivionEssence(){
        ItemStack item = new ItemStack(Material.HEART_OF_THE_SEA);
        ItemMeta meta = item.getItemMeta();
        meta.setItemName(ChatColor.DARK_PURPLE + "Essence of Oblivion");
        meta.setEnchantmentGlintOverride(true);
        meta.getPersistentDataContainer().set(OSKey, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        OblivionEssence = item;
    }
    static void CorruptedEssence(){
        ItemStack item = new ItemStack(Material.NAUTILUS_SHELL);
        ItemMeta meta = item.getItemMeta();
        meta.setItemName("Corrupted Essence");
        meta.setRarity(ItemRarity.EPIC);
        meta.setItemModel(NamespacedKey.minecraft("c_essence"));
        meta.setEnchantmentGlintOverride(true);
        meta.getPersistentDataContainer().set(CEKey, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        CorrupredEssence = item;

        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("corruptedessence"), item);
        if(Bukkit.getRecipe(NamespacedKey.minecraft("corruptedessence")) != null) {
            Bukkit.getServer().removeRecipe(NamespacedKey.minecraft("corruptedessence"));
        }
        sr.shape("GEG", "EDE", "GEG");
        sr.setIngredient('G', Material.GOLD_INGOT);
        sr.setIngredient('E', Material.ECHO_SHARD);
        sr.setIngredient('D', new ExactChoice(Items.DeterminationEssence));
        Bukkit.addRecipe(sr);
    }
    static void DimentioHelmet(){
        ItemStack item = new ItemStack(Material.DIAMOND_HELMET);
        ItemMeta meta = item.getItemMeta();
        meta.setItemName(ChatColor.DARK_PURPLE + "Máscara de bufón");
        AttributeModifier mod = new AttributeModifier(NamespacedKey.minecraft("dimentio_one"), 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD);
        meta.addAttributeModifier(Attribute.ARMOR, mod);
        AttributeModifier mod2 = new AttributeModifier(NamespacedKey.minecraft("dimentio_two"), 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD);
        meta.addAttributeModifier(Attribute.ARMOR_TOUGHNESS, mod2);
        AttributeModifier mod3 = new AttributeModifier(NamespacedKey.minecraft("dimentio_three"), 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD);
        meta.addAttributeModifier(Attribute.KNOCKBACK_RESISTANCE, mod3);
        meta.setItemModel(NamespacedKey.minecraft("mask"));
        EquippableComponent c = meta.getEquippable();
        c.setSlot(EquipmentSlot.HEAD);
        meta.setEquippable(c);
        meta.addEnchant(Enchantment.PROTECTION, 5, true);
        meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("dimentio")), 1, true);
        meta.setUnbreakable(true);
        meta.setEnchantmentGlintOverride(false);
        item.setItemMeta(meta);
        DimentioMask = item;

    }
    static void Devilsknife(){
        ItemStack item = new ItemStack(Material.NETHERITE_HOE);
        ItemMeta meta = item.getItemMeta();
        meta.setItemName(ChatColor.DARK_BLUE + "Devilsknife");
        AttributeModifier mod = new AttributeModifier(NamespacedKey.minecraft("jevil_one"), 8, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND);
        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, mod);
        AttributeModifier mod2 = new AttributeModifier(NamespacedKey.minecraft("jevil_two"), 1.6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND);
        meta.addAttributeModifier(Attribute.ATTACK_SPEED, mod2);
        meta.getPersistentDataContainer().set(DKKey, PersistentDataType.BOOLEAN, true);
        meta.addEnchant(Enchantment.SHARPNESS, 8, true);
        meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("jevil")), 1, true);
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        Devilsknife = item;

    }
    static void Chocoblade(){
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setItemName(ChatColor.GOLD + "Espada de Chocolate");
        AttributeModifier mod = new AttributeModifier(NamespacedKey.minecraft("choco_one"), 8, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND);
        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, mod);
        AttributeModifier mod2 = new AttributeModifier(NamespacedKey.minecraft("choco_two"), 1.6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND);
        meta.addAttributeModifier(Attribute.ATTACK_SPEED, mod2);
        meta.getPersistentDataContainer().set(CBKey, PersistentDataType.BOOLEAN, true);
        meta.addEnchant(Enchantment.SHARPNESS, 8, true);
        meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("choco")), 1, true);
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        ChocoBlade = item;

    }
}