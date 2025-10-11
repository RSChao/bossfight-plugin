package com.rschao.advs.tab0;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.rschao.advs.AdvancementTabNamespaces;
import com.rschao.events.soulEvents;
import com.rschao.items.Items;
import com.rschao.items.weapons;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.visibilities.ParentGrantedVisibility;
import com.fren_gor.ultimateAdvancementAPI.advancement.multiParents.MultiParentsAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;

@SuppressWarnings("unused")
public class Soul_master extends MultiParentsAdvancement implements ParentGrantedVisibility {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.tab0_NAMESPACE, "soul_master");
  static ItemStack item = new ItemStack(Material.LEATHER);
  static{
    
  ItemMeta meta = item.getItemMeta();
  meta.setItemModel(NamespacedKey.minecraft("soul_love"));
  meta.setEnchantmentGlintOverride(true);
  item.setItemMeta(meta);
  }

  public Soul_master(float x, float y, BaseAdvancement... parents) {
    super(KEY.getKey(), new AdvancementDisplay(item, "§8§lSoul §4§lMaster", AdvancementFrameType.CHALLENGE, true, true, x,y , "Obtain all great souls:", "Determination, Love, Emptiness", "and Hope", "§6Reward:", "§6-Second soul slot", "§6-1x Corrupted Heart", "§6-1x Eye of Execution"),1, parents );
    this.registerEvent(PlayerInteractEvent.class, (ev) -> {
      if(ev.getItem() == null) return;
      Player p = ev.getPlayer();
      ItemMeta meta = ev.getItem().getItemMeta();
      // Use soulEvents's soulItems hashmap to check if the player has souls with numbers 7, 11, 12 and 13. If they interact with one of those, save it to the hashmap. If they have all, increment progression.
      if (meta == null) return;
      if(!meta.hasCustomModelData()) return;
      if(soulEvents.soulItems.containsKey(meta.getCustomModelData())){
        int modelData = meta.getCustomModelData();
        if (modelData == 18 || modelData == 29 || modelData == 30 || modelData == 31) {
          soulEvents.playerSouls.putIfAbsent(p.getUniqueId(), new HashSet<>());
          Set<Integer> playerSouls = soulEvents.playerSouls.get(p.getUniqueId());
          playerSouls.add(modelData);
          if (playerSouls.containsAll(Arrays.asList(18, 29, 30, 31))) {
            this.incrementProgression(p);
          }
        }
      }
    });
  }

  @Override
  public void giveReward(Player player) {
    //set player permission "gaster.soul.second"
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set gaster.soul.second true");
    player.sendMessage("§6You have unlocked the second soul slot!");
    player.getInventory().addItem(weapons.CorruptedHeart);
    player.getInventory().addItem(weapons.BadEye);
  }
}