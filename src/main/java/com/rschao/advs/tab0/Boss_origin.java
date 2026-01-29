package com.rschao.advs.tab0;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.visibilities.HiddenVisibility;
import com.rschao.advs.AdvancementTabNamespaces;
import com.rschao.events.definitions.BossEndEvent;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;

public class Boss_origin extends BaseAdvancement implements HiddenVisibility {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.tab0_NAMESPACE, "boss_origin");
  static ItemStack item = new ItemStack(Material.LEATHER);
  static{
    
  ItemMeta meta = item.getItemMeta();
  meta.setItemModel(NamespacedKey.minecraft("soul_em"));
  item.setItemMeta(meta);
  }

  public Boss_origin(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(item, "Hero of Showdown", AdvancementFrameType.CHALLENGE, true, true, x, y , "Defeat the God of Oblivion", "and save all worlds", "", "§6Rewards:", "§6-New rank Showdown Hero", "§6-????"), parent, 1);
    this.registerEvent(BossEndEvent.class, (ev) -> {
      if(ev.getBossName().equals("s2finale.origin_chao")){
        for(Player p : ev.getBossPlayers()){
          incrementProgression(p);
        }
      }
    });
  }

  @Override
  public void giveReward(Player player) {
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user "+player.getName()+" parent add showdown-hero");
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user "+player.getName()+" parent add origin-chao-ded");
    Location loc = new Location(Bukkit.getWorld("Showdown"), 50005, 77, 177);
    player.teleport(loc);
    player.sendMessage("You have been teleported to ???");
  }
}