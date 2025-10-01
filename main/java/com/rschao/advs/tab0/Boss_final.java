package com.rschao.advs.tab0;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.rschao.advs.AdvancementTabNamespaces;
import com.rschao.events.definitions.BossEndEvent;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.visibilities.HiddenVisibility;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;

public class Boss_final extends BaseAdvancement implements HiddenVisibility {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.tab0_NAMESPACE, "boss_final");
  static ItemStack item = new ItemStack(Material.TURTLE_SCUTE);
  static{
    
  ItemMeta meta = item.getItemMeta();
  meta.setItemModel(NamespacedKey.minecraft("seven_soul"));
  item.setItemMeta(meta);
  }

  public Boss_final(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(item, "§lDestroyer of Darkness", AdvancementFrameType.CHALLENGE, true, true, x, y , "Defeal the holder of Darkness,", "the Broken Soul of Showdown"), parent, 1);
    registerEvent(BossEndEvent.class, (e) ->{
      if(e.getBossName().equals("brokensoul/Lore_final")){
        for(Player p : e.getBossPlayers()){
          incrementProgression(p);
        }
      }
    });
  }
}