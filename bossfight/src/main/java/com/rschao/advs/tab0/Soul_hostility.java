package com.rschao.advs.tab0;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.rschao.advs.AdvancementTabNamespaces;
import com.rschao.items.Items;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.visibilities.HiddenVisibility;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;

public class Soul_hostility extends BaseAdvancement implements HiddenVisibility {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.tab0_NAMESPACE, "soul_hostility");
  static ItemStack item = new ItemStack(Material.LEATHER);
  static{
    
  ItemMeta meta = item.getItemMeta();
  meta.setItemModel(NamespacedKey.minecraft("soul_h"));
  item.setItemMeta(meta);
  }

  public Soul_hostility(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(item, "§0§lSoul of Darkness", AdvancementFrameType.CHALLENGE, true, true, x, y , "Obtain the soul of pure darkness,", "stronger than anything imaginable."), parent, 1);
    this.registerEvent(PlayerInteractEvent.class, (ev) -> {
      if(ev.getItem() == null) return;
      Player p = ev.getPlayer();
      ItemMeta meta = ev.getItem().getItemMeta();
      if(meta.getPersistentDataContainer().has(Items.HoSKey)){
        incrementProgression(p);
      }
    });
  }
}