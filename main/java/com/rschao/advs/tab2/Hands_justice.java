package com.rschao.advs.tab2;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.rschao.advs.AdvancementTabNamespaces;
import com.rschao.items.Hands;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;

public class Hands_justice extends BaseAdvancement  {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.tab2_NAMESPACE, "hands_justice");

  static ItemStack item = new ItemStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE);
  static{
    
  ItemMeta meta = item.getItemMeta();
  meta.setCustomModelData(5);
  item.setItemMeta(meta);
  }

  public Hands_justice(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(item, "Justice Hand", AdvancementFrameType.TASK, true, true, x, y , "The willingness to act fast when", "danger approaches"), parent, 1);
    this.registerEvent(PlayerInteractEvent.class, (ev) -> {
      if(ev.getItem() == null) return;
      Player p = ev.getPlayer();
      ItemMeta meta = ev.getItem().getItemMeta();
      if(meta.getPersistentDataContainer().has(Hands.SHKey)){
        incrementProgression(p);
      }
    });
  }
}