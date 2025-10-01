package com.rschao.advs.tab0;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.rschao.advs.AdvancementTabNamespaces;
import com.rschao.items.Items;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;

public class Soul_persevearence extends BaseAdvancement  {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.tab0_NAMESPACE, "soul_persevearence");
  static ItemStack item = new ItemStack(Material.LEATHER);
  static{
    
  ItemMeta meta = item.getItemMeta();
  meta.setCustomModelData(19);
  item.setItemMeta(meta);
  }

  public Soul_persevearence(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(item, "New soul: Persevearence", AdvancementFrameType.TASK, true, true, x, y , "Obtain the purple soul, Persevearence"), parent, 1);
    this.registerEvent(PlayerInteractEvent.class, (ev) -> {
      if(ev.getItem() == null) return;
      Player p = ev.getPlayer();
      ItemMeta meta = ev.getItem().getItemMeta();
      if(meta.getPersistentDataContainer().has(Items.PSKey)){
        incrementProgression(p);
      }
    });
  }
}