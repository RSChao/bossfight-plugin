package com.rschao.techs;

import com.rschao.Plugin;
import com.rschao.plugins.techapi.tech.Technique;
import com.rschao.plugins.techapi.tech.feedback.hotbarMessage;
import com.rschao.plugins.techapi.tech.register.TechRegistry;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Jevil {
    static final String TECH_ID = "jevilknife";

    public static void RegisterTechs() {
        TechRegistry.registerTechnique(TECH_ID, spade);
        TechRegistry.registerTechnique(TECH_ID, heartbarrier);
        TechRegistry.registerTechnique(TECH_ID, diamondchain);
        TechRegistry.registerTechnique(TECH_ID, shuffle);
        TechRegistry.registerTechnique(TECH_ID, laugh);
    }

    static Technique spade = new Technique("jevil_spade", "Spade Bomb", false, TechRegistry.getById("spade").getCooldownMillis(), TechRegistry.getById("spade").getAction());
    static Technique heartbarrier = new Technique("jevil_heartbarrier", "Heart Barrier", false, TechRegistry.getById("heartbarrier").getCooldownMillis(), TechRegistry.getById("heartbarrier").getAction());
    static Technique diamondchain = new Technique("jevil_diamondchain", "Diamond Chain", false, TechRegistry.getById("diamondchain").getCooldownMillis(), TechRegistry.getById("diamondchain").getAction());
    static Technique shuffle = new Technique("jevil_shuffle", "Card Shuffle", false, TechRegistry.getById("shuffle").getCooldownMillis(), TechRegistry.getById("shuffle").getAction());
    static Technique laugh = new Technique("jevil_laugh", "Jevil's Laugh", true, TechRegistry.getById("laugh").getCooldownMillis(), (player, item, args) -> {
        for (org.bukkit.entity.Entity entity : player.getNearbyEntities(20, 20, 20)) {
            if (entity instanceof Player) {
                Player target = (Player) entity;
                target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5 * 20, 255));
                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 5 * 20, 255));
                target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 5 * 20, 255));
                target.getAttribute(org.bukkit.attribute.Attribute.JUMP_STRENGTH).setBaseValue(0);
                double jumpstrength = 0.41999998697815;
                Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), () -> {
                    target.getAttribute(org.bukkit.attribute.Attribute.JUMP_STRENGTH).setBaseValue(jumpstrength);
                    target.damage(300);
                }, 5 * 20);
            }
        }
        hotbarMessage.sendHotbarMessage(player, ChatColor.DARK_GRAY + "You have used the Jevil's Laugh technique");
    });
}
