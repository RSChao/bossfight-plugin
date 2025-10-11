package com.rschao.advs.tab0;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.rschao.advs.AdvancementTabNamespaces;
import com.rschao.items.Hands;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;

public class Cleansoul extends BaseAdvancement  {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.tab0_NAMESPACE, "cleansoul");

  static ItemStack item = new ItemStack(Material.LEATHER);
  static{
    
  ItemMeta meta = item.getItemMeta();
  meta.setItemModel(NamespacedKey.minecraft("soul_cleaner"));
  item.setItemMeta(meta);
  }
  public Cleansoul(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(item, "Rest in peace, partner", AdvancementFrameType.TASK, true, true, x, y , "Wash your absorbed soul away"), parent, 1);
    this.registerEvent(PlayerInteractEvent.class, (ev) -> {
      if(ev.getItem() == null) return;
      Player p = ev.getPlayer();
      ItemMeta meta = ev.getItem().getItemMeta();
      if(meta.getPersistentDataContainer().has(Hands.SoulCleanerKey) || meta.getPersistentDataContainer().has(Hands.SoulCleanerBuffedKey)){
        incrementProgression(p);
      }
    });
  }
}