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

public class Hands_integrity extends BaseAdvancement  {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.tab2_NAMESPACE, "hands_integrity");

  static ItemStack item = new ItemStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE);
  static{
    
  ItemMeta meta = item.getItemMeta();
  meta.setCustomModelData(3);
  item.setItemMeta(meta);
  }

  public Hands_integrity(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(item, "Integrity Hand", AdvancementFrameType.TASK, true, true, x, y , "The strength of mind to control gravity.", "Attract your enemies or", "shift to launching them away"), parent, 1);
    this.registerEvent(PlayerInteractEvent.class, (ev) -> {
      if(ev.getItem() == null) return;
      Player p = ev.getPlayer();
      ItemMeta meta = ev.getItem().getItemMeta();
      if(meta.getPersistentDataContainer().has(Hands.GMKey)){
        incrementProgression(p);
      }
    });
  }
}