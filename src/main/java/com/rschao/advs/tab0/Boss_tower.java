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

public class Boss_tower extends BaseAdvancement implements HiddenVisibility {

    public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.tab0_NAMESPACE, "boss_guaka");
    static ItemStack icon = new ItemStack(Material.LEATHER);
    static {

        ItemMeta meta = icon.getItemMeta();
        meta.setEnchantmentGlintOverride(true);
        meta.setItemModel(NamespacedKey.minecraft("purity_heart"));
        icon.setItemMeta(meta);
    }

    public Boss_tower(Advancement parent, float x, float y) {
        super(KEY.getKey(), new AdvancementDisplay(icon, "§lClenser of Light", AdvancementFrameType.CHALLENGE, true, true, x, y , "Defeat the God of Light", "and reach the Golden Whacka"), parent, 1);
        registerEvent(BossEndEvent.class, (e) ->{
            if(e.getBossName().equals("tower/necrozma")){
                for(Player p : e.getBossPlayers()){
                    incrementProgression(p);
                }
            }
        });
    }


    @Override
    public void giveReward(Player player) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tp "+player.getName()+" -3539 -1 -867");
        player.sendMessage(ChatColor.GOLD + "[" + ChatColor.YELLOW + "Tower of Suicide" + ChatColor.GOLD + "]" + ChatColor.YELLOW + "You have been granted passage to the Golden Whacka.");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user "+player.getName()+" parent add tower-victor");
    }
}
