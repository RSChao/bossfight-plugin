package com.rschao.advs.tab0;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.visibilities.HiddenVisibility;
import com.rschao.advs.AdvancementTabNamespaces;
import com.rschao.events.definitions.BossEndEvent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Boss_aion extends BaseAdvancement implements HiddenVisibility {

    public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.tab0_NAMESPACE, "boss_aion");
    static ItemStack icon = new ItemStack(Material.LEATHER);
    static {
        ItemMeta meta = icon.getItemMeta();
        meta.setEnchantmentGlintOverride(true);
        meta.setItemModel(NamespacedKey.minecraft("aegis_sword"));
        icon.setItemMeta(meta);
    }

    public Boss_aion(Advancement parent, float x, float y) {
        super(KEY.getKey(), new AdvancementDisplay(icon, "§dWarrior of the Future", AdvancementFrameType.CHALLENGE, true, true, x, y , "Defeat the Construct of Atemporality, Aion", "and give Showdown an ending"), parent, 1);
        registerEvent(BossEndEvent.class, (e) ->{
            if(e.getBossName().equals("season4/main_lore_chao/aion")){
                for(Player p : e.getBossPlayers()){
                    incrementProgression(p);
                }
            }
        });
    }


    @Override
    public void giveReward(Player player) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user "+player.getName()+" parent add lore-s4");
    }
}

