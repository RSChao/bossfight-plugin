package com.rschao.advs.tab0;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.rschao.advs.AdvancementTabNamespaces;
import com.rschao.events.definitions.BossEndEvent;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.visibilities.HiddenVisibility;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;

public class Boss_arlek extends BaseAdvancement implements HiddenVisibility {

    public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.tab0_NAMESPACE, "boss_arlek");
    static ItemStack icon = new ItemStack(Material.LEATHER);
    static {

        ItemMeta meta = icon.getItemMeta();
        meta.setEnchantmentGlintOverride(true);
        meta.setItemModel(NamespacedKey.minecraft("purity_heart"));
        icon.setItemMeta(meta);
    }

    public Boss_arlek(Advancement parent, float x, float y) {
        super(KEY.getKey(), new AdvancementDisplay(icon, "§lBlesser of Dreams", AdvancementFrameType.CHALLENGE, true, true, x, y , "End the reign of the Nightmare", "Heart and save it's owner"), parent, 1);
        registerEvent(BossEndEvent.class, (e) ->{
            if(e.getBossName().equals("s3lore/chains/chains")){
                for(Player p : e.getBossPlayers()){
                    incrementProgression(p);
                }
            }
        });
    }


    @Override
    public void giveReward(Player player) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user "+player.getName()+" parent add nightmare-deleter");
        player.sendMessage(ChatColor.BLACK + "[" + ChatColor.DARK_GRAY + "Arlek" + ChatColor.BLACK + "]" + ChatColor.GRAY + "Well done, child of purity.");
    }
}
