package com.rschao.items;


import java.util.List;
import java.util.ArrayList;

import com.rschao.enchants.GenoEnchant;
import com.rschao.enchants.GlitchEnchant;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice.ExactChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.rschao.api.SoulData;
import net.md_5.bungee.api.ChatColor;

public class Items {
    public static ItemStack GKnife;
    public static NamespacedKey SwordKey;
    public static ItemStack XKnife;
    public static NamespacedKey XKKey;
    public static ItemStack GasterBlaster;
    public static NamespacedKey GBKey;
    public static ItemStack omegaBlaster;
    public static NamespacedKey OBKey;
    public static ItemStack PurityHeart;
    public static NamespacedKey PHKey;
    public static ItemStack ChaosHeart;
    public static NamespacedKey ChHKey;
    public static ItemStack soul_Determination;
    public static NamespacedKey DSKey;
    public static ItemStack soul_Persevearence;
    public static NamespacedKey PSKey;
    public static ItemStack soul_Hate;
    public static NamespacedKey HSKey;
    public static ItemStack soul_Justice;
    public static NamespacedKey JSKey;
    public static ItemStack soul_Bravery;
    public static NamespacedKey BSKey;
    public static ItemStack soul_Kind;
    public static NamespacedKey KSKey;
    public static ItemStack soul_Integrity;
    public static NamespacedKey ISKey;
    public static ItemStack soul_Patience;
    public static NamespacedKey PtSKey;
    public static ItemStack DeterminationCrystal;
    public static ItemStack DeterminationEssence;
    public static ItemStack soul_Compassion;
    public static NamespacedKey CSKey;
    public static ItemStack soul_Affection;
    public static NamespacedKey ASKey;
    public static ItemStack soul_Empathy;
    public static NamespacedKey ESKey;
    public static ItemStack soul_Love;
    public static NamespacedKey LSKey;
    public static ItemStack soul_Emptiness;
    public static NamespacedKey eSKey;
    public static ItemStack soul_Hope;
    public static NamespacedKey HpSKey;
    public static ItemStack soul_Hostility;
    public static NamespacedKey HoSKey;
    public static ItemStack soul_Neutral;
    public static NamespacedKey NSKey;
    public static ItemStack soul_Void;
    public static NamespacedKey VSKey;
    public static ItemStack SoulContainer;
    public static NamespacedKey SCKey;
    public static ItemStack soul_Purity;
    public static NamespacedKey PuSKey;
    public static ItemStack soul_Sincerity;
    public static NamespacedKey SiSKey;
    public static ItemStack soul_Conflict;
    public static NamespacedKey CoSKey;
    public static ItemStack soul_Audacity;
    public static NamespacedKey AuSKey;
    public static ItemStack soul_Fortitude;
    public static NamespacedKey FoSKey;
    public static ItemStack soul_Guidance;
    public static NamespacedKey GuSKey;
    public static ItemStack soul_Insanity;
    public static NamespacedKey InSKey;
    public static ItemStack soul_Irresolution;
    public static NamespacedKey IrSKey;
    public static ItemStack soul_Malice;
    public static NamespacedKey MaSKey;
    public static ItemStack soul_Despair;
    public static NamespacedKey DeSKey;
    public static ItemStack soul_Animosity;
    public static NamespacedKey AnSKey;
    static void Keys(){
        SwordKey = new NamespacedKey("gaster", "geno_knife");
        XKKey = new NamespacedKey("gaster", "x_knife");
        GBKey = new NamespacedKey("gaster", "blaster");
        OBKey = new NamespacedKey("gaster", "omega");
        PHKey = new NamespacedKey("soul", "purity_heart");
        ChHKey = new NamespacedKey("soul", "chaos_heart");
        DSKey = new NamespacedKey("soul", "determination");
        PSKey = new NamespacedKey("soul", "perseverance");
        HSKey = new NamespacedKey("soul", "hate");
        JSKey = new NamespacedKey("soul", "justice");
        BSKey = new NamespacedKey("soul", "bravery");
        KSKey = new NamespacedKey("soul", "kindness");
        ISKey = new NamespacedKey("soul", "integrity");
        PtSKey = new NamespacedKey("soul", "patience");
        CSKey = new NamespacedKey("soul", "compassion");
        ASKey = new NamespacedKey("soul", "affection");
        ESKey = new NamespacedKey("soul", "empathy");
        LSKey = new NamespacedKey("soul", "love");
        eSKey = new NamespacedKey("soul", "emptiness");
        HpSKey = new NamespacedKey("soul", "hope");
        HoSKey = new NamespacedKey("soul", "darkness");
        NSKey = new NamespacedKey("soul", "neutral");
        VSKey = new NamespacedKey("soul", "void");
        SCKey = new NamespacedKey("soul", "container");
        PuSKey = new NamespacedKey("soul", "purity");
        SiSKey = new NamespacedKey("soul", "sincerity");
        CoSKey = new NamespacedKey("soul", "conflict");
        AuSKey = new NamespacedKey("soul", "audacity");
        FoSKey = new NamespacedKey("soul", "fortitude");
        GuSKey = new NamespacedKey("soul", "guidance");
        InSKey = new NamespacedKey("soul", "insanity");
        IrSKey = new NamespacedKey("soul", "irresolution");
        MaSKey = new NamespacedKey("soul", "malice");
        DeSKey = new NamespacedKey("soul", "despair");
        AnSKey = new NamespacedKey("soul", "animosity");
    }
    public static void Init(){
        DeterminationCrystal();
        DeterminationEssence();
        Keys();
        Souls();
        Gaster();
        Knives();
        weapons.Init();
        Hands.Init();
        weapons.SevenHands();
        HandsNew.init();
        soulRecipes();
    }
    static void Gaster(){
        GasterBlaster();
        omegaBlaster();
    }
    static void Knives(){
        Geno();
        Xknife();
    }
    static void Souls(){
        SoulContainer();
        ChaosHeart();
        PurityHeart();
        soul_Determination();
        soul_Persevearence();
        soul_Kind();
        soul_Bravery();
        soul_Justice();
        soul_Integrity();
        soul_Patience();
        soul_Hate();
        soul_Compassion();
        soul_Empathy();
        soul_Affection();
        soul_Love();
        soul_Emptiness();
        soul_Hope();
        soul_Hostility();
        soul_Neutral();
        soul_Void();
        soul_Purity();
        soul_Sincerity();
        soul_Conflict();
        soul_Audacity();
        soul_Fortitude();
        soul_Guidance();
        soul_Insanity();
        soul_Irresolution();
        soul_Malice();
        soul_Despair();
        soul_Animosity();
    }
    static void Geno(){
        ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setItemName(ChatColor.DARK_RED + "Genocidal knife");
        meta.addEnchant(Enchantment.SHARPNESS, 3, true);
        meta.setItemModel(NamespacedKey.minecraft("geno_knife"));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.getPersistentDataContainer().set(SwordKey, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        item = (new GenoEnchant()).addEnchant(item, 2);
        GKnife = item;
    }
    static void Xknife(){
        ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setItemName(ChatColor.BLACK + "" + ChatColor.MAGIC + "The X" + ChatColor.RESET + "" + ChatColor.BLACK + " knife");
        meta.addEnchant(Enchantment.SHARPNESS, 6, true);
        meta.setItemModel(NamespacedKey.minecraft("x_knife"));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.getPersistentDataContainer().set(XKKey, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        item = (new GlitchEnchant()).addEnchant(item, 1);
        XKnife = item;
    }
    static void GasterBlaster(){
        ItemStack item = new ItemStack(Material.LEATHER);
        ItemMeta meta = item.getItemMeta();
        meta.setItemName("Gaster Blaster");
        meta.setItemModel(NamespacedKey.minecraft("gaster_blaster"));
        meta.setMaxStackSize(1);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.getPersistentDataContainer().set(GBKey, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        GasterBlaster = item;
    }
    static void omegaBlaster(){
        ItemStack item = new ItemStack(Material.LEATHER);
        ItemMeta meta = item.getItemMeta();
        meta.setRarity(ItemRarity.EPIC);
        meta.setMaxStackSize(1);
        meta.setItemName("Omega Gaster Blaster");
        List<String> list = new ArrayList<String>();
        list.add("A gaster blaster,");
        list.add("powered with human souls");
        meta.setLore(list);
        meta.setItemModel(NamespacedKey.minecraft("omega_blaster"));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.getPersistentDataContainer().set(OBKey, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        omegaBlaster = item;

        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("omegablaster"), item);
        sr.shape("GDG", "LBP", "GGG");
        sr.setIngredient('G', Material.GHAST_TEAR);
        sr.setIngredient('B', new ExactChoice(GasterBlaster));
        sr.setIngredient('L', new ExactChoice(soul_Bravery));
        sr.setIngredient('P', new ExactChoice(soul_Patience));
        sr.setIngredient('D', new ExactChoice(soul_Determination));
        Bukkit.getServer().addRecipe(sr);
    }
    static void PurityHeart(){
        PurityHeart = SoulData.soul(PHKey, 8, "Corazón de la Pureza", "purity_heart", true);
    }
    static void ChaosHeart(){
        ChaosHeart = SoulData.soul(ChHKey, 7, "Corazón del Caos", "chaos_heart", true);
    }
    static void soul_Determination(){
        soul_Determination = SoulData.soul(DSKey, 1, "Determination", "soul", false);
        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("chara"), soul_Determination);
        sr.shape("EC");
        sr.setIngredient('E', new ExactChoice(DeterminationEssence));
        sr.setIngredient('C', new ExactChoice(SoulContainer));
        Bukkit.getServer().addRecipe(sr);
    }
    static void soul_Persevearence(){
        soul_Persevearence = SoulData.soul(PSKey, 1, "Perseverance", "soul_p", false);
        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("purple"), soul_Persevearence);
        sr.shape("AAA", "ACA", "AAA");
        sr.setIngredient('A', Material.AMETHYST_BLOCK);
        sr.setIngredient('C', new ExactChoice(SoulContainer));
        Bukkit.getServer().addRecipe(sr);
    }
    static void soul_Hate(){
        soul_Hate = SoulData.soul(HSKey, 3, "Hate", "soul_h", true);
        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("chao"), soul_Hate);
        sr.shape("BBB", "PCP", "BBB");
        sr.setIngredient('C', new ExactChoice(SoulContainer));
        sr.setIngredient('P', new ExactChoice(DeterminationEssence));
        sr.setIngredient('B', Material.INK_SAC);
        Bukkit.getServer().addRecipe(sr);
    }
    static void soul_Justice(){
        soul_Justice = SoulData.soul(JSKey, 1, "Justice", "soul_j", false);
        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("clover"), soul_Justice);
        sr.shape("EEE", "ECE", "EEE");
        sr.setIngredient('E', Material.END_ROD);
        sr.setIngredient('C', new ExactChoice(SoulContainer));
        Bukkit.getServer().addRecipe(sr);
    }
    static void soul_Bravery(){
        soul_Bravery = SoulData.soul(BSKey, 1, "Bravery", "soul_b", false);
        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("bravery"), soul_Bravery);
        sr.shape("BBB", "BCB", "BBB");
        sr.setIngredient('B', Material.BLAZE_ROD);
        sr.setIngredient('C', new ExactChoice(SoulContainer));
        Bukkit.getServer().addRecipe(sr);
    }
    static void soul_Kind(){
        soul_Kind = SoulData.soul(KSKey, 1, "Kindness", "soul_k", false);
        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("greenie"), soul_Kind);
        sr.shape("EEE", "ECE", "EEE");
        sr.setIngredient('E', Material.EMERALD);
        sr.setIngredient('C', new ExactChoice(SoulContainer));
        Bukkit.getServer().addRecipe(sr);
    }
    static void soul_Integrity(){
        soul_Integrity = SoulData.soul(ISKey, 1, "Integrity", "soul_i", false);
        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("bluey"), soul_Integrity);
        sr.shape("LLL", "LCL", "LLL");
        sr.setIngredient('L', Material.LAPIS_BLOCK);
        sr.setIngredient('C', new ExactChoice(SoulContainer));
        Bukkit.getServer().addRecipe(sr);
    }
    static void soul_Patience(){
        soul_Patience = SoulData.soul(PtSKey, 1, "Patience", "soul_pt", false);
        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("nicecolor"), soul_Patience);
        sr.shape("SSS", "SCS", "SSS");
        sr.setIngredient('S', Material.DIAMOND);
        sr.setIngredient('C', new ExactChoice(SoulContainer));
        Bukkit.getServer().addRecipe(sr);
    }
    static void soul_Compassion(){
        soul_Compassion = SoulData.soul(CSKey, 2, "Compassion", "soul_c", false);
        ShapelessRecipe shapelessRecipe = new ShapelessRecipe(NamespacedKey.minecraft("compasion"), soul_Compassion);
        shapelessRecipe.addIngredient(new ExactChoice(DeterminationCrystal));
        shapelessRecipe.addIngredient(new ExactChoice(soul_Kind));
        shapelessRecipe.addIngredient(new ExactChoice(soul_Patience));
        shapelessRecipe.addIngredient(new ExactChoice(SoulContainer));
        Bukkit.getServer().addRecipe(shapelessRecipe);
    }
    static void soul_Empathy(){
        soul_Empathy = SoulData.soul(ESKey, 2, "Empathy", "soul_e", false);
        ShapelessRecipe shapelessRecipe = new ShapelessRecipe(NamespacedKey.minecraft("empatia"), soul_Empathy);
        shapelessRecipe.addIngredient(new ExactChoice(soul_Compassion));
        shapelessRecipe.addIngredient(new ExactChoice(soul_Kind));
        shapelessRecipe.addIngredient(new ExactChoice(SoulContainer));
        Bukkit.getServer().addRecipe(shapelessRecipe);
    }
    static void soul_Affection(){
        soul_Affection = SoulData.soul(ASKey, 2, "Affection", "soul_a", false);
        ShapelessRecipe shapelessRecipe = new ShapelessRecipe(NamespacedKey.minecraft("afecto"), soul_Affection);
        shapelessRecipe.addIngredient(new ExactChoice(soul_Compassion));
        shapelessRecipe.addIngredient(new ExactChoice(soul_Empathy));
        shapelessRecipe.addIngredient(new ExactChoice(SoulContainer));
        Bukkit.getServer().addRecipe(shapelessRecipe);
    }
    static void soul_Love(){
        soul_Love = SoulData.soul(LSKey, 4, "Love", "soul_love", true);
        ShapelessRecipe shapelessRecipe = new ShapelessRecipe(NamespacedKey.minecraft("amor"), soul_Love);
        shapelessRecipe.addIngredient(new ExactChoice(DeterminationCrystal));
        shapelessRecipe.addIngredient(new ExactChoice(soul_Affection));
        shapelessRecipe.addIngredient(new ExactChoice(soul_Empathy));
        shapelessRecipe.addIngredient(new ExactChoice(soul_Compassion));
        shapelessRecipe.addIngredient(new ExactChoice(SoulContainer));
        Bukkit.getServer().addRecipe(shapelessRecipe);
    }
    static void soul_Emptiness(){
        soul_Emptiness = SoulData.soul(eSKey, 3, "Emptiness", "soul_em", true);
        ShapelessRecipe shapelessRecipe = new ShapelessRecipe(NamespacedKey.minecraft("vacio"), soul_Emptiness);
        shapelessRecipe.addIngredient(new ExactChoice(soul_Determination));
        shapelessRecipe.addIngredient(new ExactChoice(soul_Hate));
        shapelessRecipe.addIngredient(new ExactChoice(SoulContainer));
        Bukkit.getServer().addRecipe(shapelessRecipe);
    }
    static void soul_Hope(){
        soul_Hope = SoulData.soul(HpSKey, 4, "Hope", "soul_hp", true);
        ShapelessRecipe shapelessRecipe = new ShapelessRecipe(NamespacedKey.minecraft("esperanza"), soul_Hope);
        shapelessRecipe.addIngredient(new ExactChoice(SoulContainer));
        shapelessRecipe.addIngredient(new ExactChoice(soul_Love));
        shapelessRecipe.addIngredient(new ExactChoice(soul_Persevearence));
        Bukkit.getServer().addRecipe(shapelessRecipe);
    }
    static void soul_Hostility(){
        
        soul_Hostility = SoulData.soul(HoSKey, 5, "Darkness", "soul_h", false);
    }
    static void soul_Void(){
        soul_Void = SoulData.soul(VSKey, 5, "Void", "soul_em", false);
    }
    static void soul_Neutral(){
        ItemStack item = new ItemStack(Material.LEATHER);
        ItemMeta meta = item.getItemMeta();
        meta.setRarity(ItemRarity.EPIC);
        meta.setMaxStackSize(1);
        meta.setFireResistant(true);
        meta.setItemName("Superior Human soul");
        List<String> list = new ArrayList<String>();
        list.add("Neutral");
        list.add("A soul with no power");
        list.add("or will to fight.");
        list.add("It doesnt allow another soul to coexist");
        list.add("yet it has no weaknesses nor strengths");
        meta.setLore(list);
        meta.setItemModel(NamespacedKey.minecraft("soul_cleaner"));
        meta.getPersistentDataContainer().set(NSKey, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        soul_Neutral = item;

        ShapedRecipe shapelessRecipe = new ShapedRecipe(NamespacedKey.minecraft("marica"), soul_Neutral);
        shapelessRecipe.shape("QDQ", "NCN", "QDQ");
        shapelessRecipe.setIngredient('C', new ExactChoice(SoulContainer));
        shapelessRecipe.setIngredient('N', Material.NETHER_STAR);
        shapelessRecipe.setIngredient('Q', Material.QUARTZ_BLOCK);
        shapelessRecipe.setIngredient('D', new ExactChoice(DeterminationCrystal));
        Bukkit.getServer().addRecipe(shapelessRecipe);
    }
    static void SoulContainer() {
        ItemStack item = new ItemStack(Material.LEATHER);
        ItemMeta meta = item.getItemMeta();
        meta.setRarity(ItemRarity.RARE);
        meta.setFireResistant(true);
        meta.setItemName("Soul Container");
        meta.setItemModel(NamespacedKey.minecraft("soul_container"));
        meta.getPersistentDataContainer().set(SCKey, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        SoulContainer = item;

        ItemStack resultItem = new ItemStack(SoulContainer);
        resultItem.setAmount(3);

        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("soulcontainer"), resultItem);
        sr.shape("ODO", "DGD", "ODO");
        sr.setIngredient('G', Material.GLASS_BOTTLE);
        sr.setIngredient('O', Material.OBSIDIAN);
        sr.setIngredient('D', new ExactChoice(DeterminationCrystal));
        Bukkit.getServer().addRecipe(sr);
    }
    static void DeterminationCrystal(){
        ItemStack item = new ItemStack(Material.HEART_OF_THE_SEA);
        ItemMeta meta = item.getItemMeta();
        meta.setItemName("Determination Crystal");
        meta.setRarity(ItemRarity.RARE);
        meta.setEnchantmentGlintOverride(true);
        item.setItemMeta(meta);
        DeterminationCrystal = item;
    }
    static void DeterminationEssence(){
        ItemStack item = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta meta = item.getItemMeta();
        meta.setRarity(ItemRarity.UNCOMMON);
        meta.setEnchantmentGlintOverride(true);
        meta.setItemName("Determination Essence");
        item.setItemMeta(meta);
        DeterminationEssence = item;
        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("detessence"), item);
        sr.shape("QQQ", "QQQ", "QQQ");
        sr.setIngredient('Q', new ExactChoice(DeterminationCrystal));
        Bukkit.getServer().addRecipe(sr);
        sr = null;
        ItemStack resultItem = new ItemStack(DeterminationCrystal);
        resultItem.setAmount(9);
        sr = new ShapedRecipe(NamespacedKey.minecraft("detcrystal"), resultItem);
        sr.shape("E");
        sr.setIngredient('E', new ExactChoice(DeterminationEssence));
        Bukkit.getServer().addRecipe(sr);
    }
    static void soulRecipes(){
        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("cleaner"), Hands.SoulCleaner);
        sr.shape(" N ", "NCN", " N ");
        sr.setIngredient('N', Material.NETHERRACK);
        sr.setIngredient('C', new ExactChoice(DeterminationCrystal));
        Bukkit.getServer().addRecipe(sr);
        sr = null;
        sr = new ShapedRecipe(NamespacedKey.minecraft("cleaner_buffed"), Hands.SoulCleanerBuffed);
        sr.shape(" C ", "CSC", " C ");
        sr.setIngredient('C', new ExactChoice(DeterminationCrystal));
        sr.setIngredient('S', new ExactChoice(Hands.SoulCleaner));
        Bukkit.getServer().addRecipe(sr);
    }
    public static ItemStack getSoulItem(int soulN) {
        switch (soulN) {
            case 0: return soul_Determination;
            case 1: return soul_Persevearence;
            case 2: return soul_Integrity;
            case 3: return soul_Justice;
            case 4: return soul_Bravery;
            case 5: return soul_Patience;
            case 6: return soul_Kind;
            case 7: return soul_Hate;
            case 8: return soul_Compassion;
            case 9: return soul_Affection;
            case 10: return soul_Empathy;
            case 11: return soul_Love;
            case 12: return soul_Emptiness;
            case 13: return soul_Hope;
            case 14: return soul_Purity;
            case 15: return soul_Sincerity;
            case 16: return soul_Conflict;
            case 17: return soul_Hostility;
            case 18: return weapons.SevenSouls;
            case 19: return soul_Void;
            case 20: return soul_Audacity;
            case 21: return soul_Fortitude;
            case 22: return soul_Guidance;
            case 23: return soul_Insanity;
            case 24: return soul_Irresolution;
            case 25: return soul_Malice;
            case 26: return soul_Despair;
            case 27: return soul_Animosity;
            case -5: return soul_Neutral;
            case 30: return PurityHeart;
            case 66: return ChaosHeart;
            default: return null;
        }
    }
    static void soul_Purity() {
        soul_Purity = SoulData.soul(PuSKey, 2, "Purity", "soul_purity", true);
        ShapelessRecipe recipe = new ShapelessRecipe(NamespacedKey.minecraft("purity"), soul_Purity);
        recipe.addIngredient(new ExactChoice(soul_Compassion));
        recipe.addIngredient(new ExactChoice(soul_Integrity));
        recipe.addIngredient(new ExactChoice(SoulContainer));
        Bukkit.getServer().addRecipe(recipe);
    }

    static void soul_Sincerity() {
        soul_Sincerity = SoulData.soul(SiSKey, 2, "Sincerity", "soul_sincerity", false);
        ShapelessRecipe recipe = new ShapelessRecipe(NamespacedKey.minecraft("sincerity"), soul_Sincerity);
        recipe.addIngredient(new ExactChoice(soul_Kind));
        recipe.addIngredient(new ExactChoice(soul_Justice));
        recipe.addIngredient(new ExactChoice(SoulContainer));
        Bukkit.getServer().addRecipe(recipe);
    }

    static void soul_Conflict() {
        soul_Conflict = SoulData.soul(CoSKey, 3, "Conflict", "soul_conflict", true);
        ShapelessRecipe recipe = new ShapelessRecipe(NamespacedKey.minecraft("conflict"), soul_Conflict);
        recipe.addIngredient(new ExactChoice(soul_Hate));
        recipe.addIngredient(new ExactChoice(soul_Bravery));
        recipe.addIngredient(new ExactChoice(soul_Persevearence));
        recipe.addIngredient(new ExactChoice(SoulContainer));
        Bukkit.getServer().addRecipe(recipe);
    }

    static void soul_Audacity() {
        soul_Audacity = SoulData.soul(AuSKey, 2, "Audacity", "soul_audacity", true);
        ShapelessRecipe recipe = new ShapelessRecipe(NamespacedKey.minecraft("audacity"), soul_Audacity);
        recipe.addIngredient(new ExactChoice(soul_Determination));
        recipe.addIngredient(new ExactChoice(soul_Bravery));
        recipe.addIngredient(new ExactChoice(soul_Compassion));
        recipe.addIngredient(new ExactChoice(SoulContainer));
        Bukkit.getServer().addRecipe(recipe);
    }

    static void soul_Fortitude() {
        soul_Fortitude = SoulData.soul(FoSKey, 2, "Fortitude", "soul_fortitude", true);
        ShapelessRecipe recipe = new ShapelessRecipe(NamespacedKey.minecraft("fortitude"), soul_Fortitude);
        recipe.addIngredient(new ExactChoice(soul_Persevearence));
        recipe.addIngredient(new ExactChoice(soul_Integrity));
        recipe.addIngredient(new ExactChoice(soul_Patience));
        recipe.addIngredient(new ExactChoice(SoulContainer));
        Bukkit.getServer().addRecipe(recipe);
    }

    static void soul_Guidance() {
        soul_Guidance = SoulData.soul(GuSKey, 2, "Guidance", "soul_guidance", true);
        ShapelessRecipe recipe = new ShapelessRecipe(NamespacedKey.minecraft("guidance"), soul_Guidance);
        recipe.addIngredient(new ExactChoice(soul_Affection));
        recipe.addIngredient(new ExactChoice(soul_Kind));
        recipe.addIngredient(new ExactChoice(soul_Justice));
        recipe.addIngredient(new ExactChoice(SoulContainer));
        Bukkit.getServer().addRecipe(recipe);
    }

    static void soul_Insanity() {
        soul_Insanity = SoulData.soul(InSKey, 3, "Insanity", "soul_insanity", true);
        ShapelessRecipe recipe = new ShapelessRecipe(NamespacedKey.minecraft("insanity"), soul_Insanity);
        recipe.addIngredient(new ExactChoice(soul_Hate));
        recipe.addIngredient(new ExactChoice(soul_Emptiness));
        recipe.addIngredient(new ExactChoice(soul_Compassion));
        recipe.addIngredient(new ExactChoice(soul_Love));
        recipe.addIngredient(new ExactChoice(SoulContainer));
        Bukkit.getServer().addRecipe(recipe);
    }

    static void soul_Irresolution() {
        soul_Irresolution = SoulData.soul(IrSKey, 3, "Irresolution", "soul_irresolution", true);
        ShapelessRecipe recipe = new ShapelessRecipe(NamespacedKey.minecraft("irresolution"), soul_Irresolution);
        recipe.addIngredient(new ExactChoice(soul_Patience));
        recipe.addIngredient(new ExactChoice(soul_Integrity));
        recipe.addIngredient(new ExactChoice(soul_Compassion));
        recipe.addIngredient(new ExactChoice(soul_Kind));
        recipe.addIngredient(new ExactChoice(SoulContainer));
        Bukkit.getServer().addRecipe(recipe);
    }

    static void soul_Malice() {
        soul_Malice = SoulData.soul(MaSKey, 3, "Malice", "soul_malice", true);
        ShapelessRecipe recipe = new ShapelessRecipe(NamespacedKey.minecraft("malice"), soul_Malice);
        recipe.addIngredient(new ExactChoice(soul_Hate));
        recipe.addIngredient(new ExactChoice(soul_Emptiness));
        recipe.addIngredient(new ExactChoice(soul_Affection));
        recipe.addIngredient(new ExactChoice(soul_Justice));
        Bukkit.getServer().addRecipe(recipe);
    }

    static void soul_Despair() {
        soul_Despair = SoulData.soul(DeSKey, 3, "Despair", "soul_despair", true);
        ShapelessRecipe recipe = new ShapelessRecipe(NamespacedKey.minecraft("despair"), soul_Despair);
        recipe.addIngredient(new ExactChoice(SoulContainer));
        recipe.addIngredient(new ExactChoice(soul_Compassion));
        recipe.addIngredient(new ExactChoice(soul_Empathy));
        recipe.addIngredient(new ExactChoice(soul_Integrity));
        Bukkit.getServer().addRecipe(recipe);
    }

    static void soul_Animosity() {
        soul_Animosity = SoulData.soul(AnSKey, 4, "Animosity", "soul_animosity", true);
        ShapelessRecipe recipe = new ShapelessRecipe(NamespacedKey.minecraft("animosity"), soul_Animosity);
        recipe.addIngredient(new ExactChoice(soul_Hate));
        recipe.addIngredient(new ExactChoice(soul_Bravery));
        recipe.addIngredient(new ExactChoice(soul_Compassion));
        recipe.addIngredient(new ExactChoice(SoulContainer));
        recipe.addIngredient(new ExactChoice(soul_Love));
        Bukkit.getServer().addRecipe(recipe);
    }
}
