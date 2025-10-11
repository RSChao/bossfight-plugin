package com.rschao.api;

import net.md_5.bungee.api.ChatColor;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public enum SoulType {
    DETERMINATION("determination", ChatColor.RED + "Determination", 0, 1),
    PERSEVERANCE("perseverance", ChatColor.DARK_PURPLE + "Perseverance", 1, 1),
    INTEGRITY("integrity", ChatColor.DARK_BLUE + "Integrity", 2, 1),
    JUSTICE("justice", ChatColor.YELLOW + "Justice", 3, 1),
    BRAVERY("bravery", ChatColor.of(new Color(255, 140, 0)) + "Bravery", 4, 1),
    PATIENCE("patience", ChatColor.AQUA + "Patience", 5, 1),
    KINDNESS("kindness", ChatColor.GREEN + "Kindness", 6, 1),
    HATE("hate", ChatColor.BLACK + "Hate", 7, 3),
    COMPASSION("compassion", ChatColor.of(new Color(168, 230, 207)) + "Compassion", 8, 2),
    AFFECTION("affection", ChatColor.of(new Color(255, 171, 171)) + "Affection", 9, 2),
    EMPATHY("empathy", ChatColor.of(new Color(230, 230, 250)) + "Empathy", 10, 2),
    LOVE("love", ChatColor.of(new Color(220, 20, 60)) + "Love", 11, 4),
    EMPTINESS("emptiness", ChatColor.of(new Color(128, 128, 128)) + "Emptiness", 12, 3),
    HOPE("hope", ChatColor.of(new Color(135, 206, 235)) + "Hope", 13, 4),
    PURITY("purity", ChatColor.of(new Color(255, 255, 240)) + "Purity", 14, 4),
    SINCERITY("sincerity", ChatColor.of(new Color(240, 255, 255)) + "Sincerity", 15, 2),
    CONFLICT("conflict", ChatColor.of(new Color(255, 228, 225)) + "Conflict", 16, 3),
    DARKNESS("darkness", ChatColor.BLACK + "Darkness", 17, 5),
    SEVEN("seven", ChatColor.RED + "S" + ChatColor.DARK_PURPLE + "e" + ChatColor.DARK_BLUE + "v" + ChatColor.YELLOW + "e" + ChatColor.of(new Color(255, 140, 0)) + "n" + ChatColor.AQUA + " sou" + ChatColor.GREEN + "ls", 18, 5),
    AUDACITY("audacity", ChatColor.of(new Color(255, 222, 173)) + "Audacity", 20, 2),
    FORTITUDE("fortitude", ChatColor.of(new Color(173, 216, 230)) + "Fortitude", 21, 2),
    GUIDANCE("guidance", ChatColor.of(new Color(144, 238, 144)) + "Guidance", 22, 2),
    INSANITY("insanity", ChatColor.of(new Color(255, 182, 193)) + "Insanity", 23, 3),
    IRRESOLUTION("irresolution", ChatColor.of(new Color(221, 160, 221)) + "Irresolution", 24, 3),
    MALICE("malice", ChatColor.of(new Color(255, 160, 122)) + "Malice", 25, 3),
    DESPAIR("despair", ChatColor.of(new Color(176, 224, 230)) + "Despair", 26, 3),
    ANIMOSITY("animosity", ChatColor.of(new Color(250, 128, 114)) + "Animosity", 27, 4),
    VOID("void", ChatColor.GRAY + "Void", 19, 5),
    PUREHEART("purity_heart", ChatColor.RED + "P" + ChatColor.DARK_PURPLE + "u" + ChatColor.DARK_BLUE + "r" + ChatColor.YELLOW + "i" + ChatColor.of(new Color(255, 140, 0)) + "t" + ChatColor.AQUA + "y" + ChatColor.GREEN + " Hea" + ChatColor.WHITE + "rt", 30, 8),
    CHAOSHEART("chaos_heart", ChatColor.GRAY + "Chaos Heart", 66, 7),
    NEUTRAL("neutral", ChatColor.BOLD + "Neutral", -5, 5);

    private final String key;
    private final String displayName;
    private final int id;
    private final int tier;

    public static final Map<Integer, Integer[]> opposites = new HashMap<>();

    static {
        opposites.put(0, new Integer[]{7, 12, 25}); // Determination is strong against Hate, Emptiness, and Malice
        opposites.put(1, new Integer[]{5, 24}); // Perseverance is strong against Patience and Irresolution
        opposites.put(2, new Integer[]{6, 10}); // Integrity is strong against Kindness and Empathy
        opposites.put(3, new Integer[]{4, 8}); // Justice is strong against Bravery and Compassion
        opposites.put(4, new Integer[]{3, 27}); // Bravery is strong against Justice and Animosity
        opposites.put(5, new Integer[]{1, 21}); // Patience is strong against Perseverance and Fortitude
        opposites.put(6, new Integer[]{2, 8}); // Kindness is strong against Integrity and Compassion
        opposites.put(7, new Integer[]{11, 13, 9, 0}); // Hate is strong against Love, Hope, and Affection
        opposites.put(8, new Integer[]{4, 6, 23}); // Compassion is strong against Bravery, Kindness, and Insanity
        opposites.put(9, new Integer[]{7, 25}); // Affection is strong against Hate and Malice
        opposites.put(10, new Integer[]{2, 8, 26}); // Empathy is strong against Integrity, Compassion, and Despair
        opposites.put(11, new Integer[]{7, 12, 13}); // Love is strong against Hate, Emptiness, and Hope
        opposites.put(12, new Integer[]{11, 13, 20, 0}); // Emptiness is strong against Love, Hope, and Audacity
        opposites.put(13, new Integer[]{7, 12, 22, 17}); // Hope is strong against Hate, Emptiness, Guidance and Darkness
        opposites.put(14, new Integer[]{23, 24}); // Purity is strong against Insanity and Irresolution
        opposites.put(15, new Integer[]{25, 26}); // Sincerity is strong against Malice and Despair
        opposites.put(16, new Integer[]{20, 21}); // Conflict is strong against Audacity and Fortitude
        opposites.put(17, new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 20, 21, 22, 23, 24, 25, 26, 27}); // Darkness is strong against all but void
        opposites.put(18, new Integer[]{17, 7, 25}); // Seven souls are strong against Darkness, Hate and Malice
        opposites.put(19, new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27}); // Void is strong against all
        opposites.put(20, new Integer[]{12, 16}); // Audacity is strong against Emptiness and Conflict
        opposites.put(21, new Integer[]{5, 16}); // Fortitude is strong against Patience and Conflict
        opposites.put(22, new Integer[]{13, 23}); // Guidance is strong against Hope and Insanity
        opposites.put(23, new Integer[]{8, 14}); // Insanity is strong against Compassion and Purity
        opposites.put(24, new Integer[]{1, 14}); // Irresolution is strong against Perseverance and Purity
        opposites.put(25, new Integer[]{0, 9}); // Malice is strong against Determination and Affection
        opposites.put(26, new Integer[]{10, 15}); // Despair is strong against Empathy and Sincerity
        opposites.put(27, new Integer[]{4, 15, 0}); // Animosity is strong against Bravery and Sincerity
        opposites.put(-5, new Integer[]{}); // Neutral has no strengths or weaknesses
        opposites.put(30, new Integer[]{19}); // Neutral has no strengths or weaknesses
        opposites.put(66, new Integer[]{19}); // Neutral has no strengths or weaknesses
    }

    SoulType(String key, String displayName, int id, int tier) {
        this.key = key;
        this.displayName = displayName;
        this.id = id;
        this.tier = tier;
    }

    public String getKey() {
        return key;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getId() {
        return id;
    }
    public int getTier() {
        return tier;
    }

    public static SoulType getById(int id) {
        for (SoulType soul : values()) {
            if (soul.id == id) {
                return soul;
            }
        }
        return null;
    }

    public static SoulType getByKey(String key) {
        for (SoulType soul : values()) {
            if (soul.key.equals(key)) {
                return soul;
            }
        }
        return null;
    }

    public static boolean areOpposite(int soulD, int soulV) {
        if (soulD == -5 || soulV == -5 || soulV == 19) return false; // Neutral and Void have no weaknesses
        Integer[] oppositeSouls = opposites.get(soulD);
        if (oppositeSouls != null) {
            for (int opposite : oppositeSouls) {
                if (opposite == soulV) {
                    return true;
                }
            }
        }
        return false;
    }
    public static int getSoulAmount(){
        return values().length;
    }
}
