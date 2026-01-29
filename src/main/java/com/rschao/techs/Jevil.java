package com.rschao.techs;

import com.rschao.Plugin;
import com.rschao.plugins.techniqueAPI.tech.Technique;
import com.rschao.plugins.techniqueAPI.tech.TechniqueMeta;
import com.rschao.plugins.techniqueAPI.tech.feedback.hotbarMessage;
import com.rschao.plugins.techniqueAPI.tech.register.TechRegistry;
import com.rschao.plugins.techniqueAPI.tech.selectors.TargetSelectors;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class Jevil {
    static final String TECH_ID = "jevilknife";

    public static void RegisterTechs() {
        try{
            TechRegistry.registerTechnique(TECH_ID, spade);
            TechRegistry.registerTechnique(TECH_ID, heartbarrier);
            TechRegistry.registerTechnique(TECH_ID, diamondchain);
            TechRegistry.registerTechnique(TECH_ID, shuffle);
            TechRegistry.registerTechnique(TECH_ID, laugh);
        } catch(Exception e){
            e.printStackTrace();
            return;
        }
    }

    static Technique spade = new Technique("jevil_spade", "Spade Bomb", new TechniqueMeta(false, TechRegistry.getById("spade").getMeta().getCooldownMillis(), TechRegistry.getById("spade").getMeta().getDescription()), TechRegistry.getById("spade").getTargetSelector(), TechRegistry.getById("spade").getAction());
    static Technique heartbarrier = new Technique("jevil_heartbarrier", "Heart Barrier", new TechniqueMeta(false, TechRegistry.getById("heartbarrier").getMeta().getCooldownMillis(), TechRegistry.getById("heartbarrier").getMeta().getDescription()), TechRegistry.getById("heartbarrier").getTargetSelector(), TechRegistry.getById("heartbarrier").getAction());
    static Technique diamondchain = new Technique("jevil_diamondchain", "Diamond Chain", new TechniqueMeta(false, TechRegistry.getById("diamondchain").getMeta().getCooldownMillis(), TechRegistry.getById("diamondchain").getMeta().getDescription()), TechRegistry.getById("diamondchain").getTargetSelector(), TechRegistry.getById("diamondchain").getAction());
    static Technique shuffle = new Technique("jevil_shuffle", "Card Shuffle", new TechniqueMeta(false, TechRegistry.getById("shuffle").getMeta().getCooldownMillis(), TechRegistry.getById("shuffle").getMeta().getDescription()), TechRegistry.getById("shuffle").getTargetSelector(), TechRegistry.getById("shuffle").getAction());
    static Technique laugh = new Technique("jevil_laugh", "Jevil's Laugh", new TechniqueMeta(true, TechRegistry.getById("laugh").getMeta().getCooldownMillis(), List.of("")), TargetSelectors.self(), (ctx, token) ->{
        Player player = ctx.caster();
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
