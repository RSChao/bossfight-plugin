package com.rschao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.rschao.advs.tab0.*;
import com.rschao.commands.*;
import com.rschao.enchants.*;
import com.rschao.smp.enchants.definition.Enchant;
import com.rschao.techs.Choco;
import com.rschao.techs.Jevil;
import com.rschao.techs.Magician;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.UltimateAdvancementAPI;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.events.PlayerLoadingCompletedEvent;
import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.util.CoordAdapter;
import com.rschao.advs.AdvancementTabNamespaces;
import com.rschao.advs.tab2.Hands_bravery;
import com.rschao.advs.tab2.Hands_dt;
import com.rschao.advs.tab2.Hands_integrity;
import com.rschao.advs.tab2.Hands_justice;
import com.rschao.advs.tab2.Hands_kindness;
import com.rschao.advs.tab2.Hands_patience;
import com.rschao.advs.tab2.Hands_perseverance;
import com.rschao.boss_battle.bossCmds;
import com.rschao.boss_battle.bossEvents;
import com.rschao.boss_battle.boss_settings_cmds;
import com.rschao.events.HandEvents;
import com.rschao.events.HandPoweredEvents;
import com.rschao.events.events;
import com.rschao.events.soulEvents;
import com.rschao.events.weaponEvents;
import com.rschao.items.Items;

/*
 * bossfight java plugin
 */
public class Plugin extends JavaPlugin implements Listener
{
  public static final Logger LOGGER=Logger.getLogger("gaster");
  public static UltimateAdvancementAPI api;
  public AdvancementTab tab0;

  public void onEnable()
  {
    LOGGER.info("bossfight enabled");

    Items.Init();
    Bukkit.getPluginManager().registerEvents(new events(), this);
    Bukkit.getPluginManager().registerEvents(new HandEvents(), this);
    Bukkit.getPluginManager().registerEvents(new HandPoweredEvents(), this);
    Bukkit.getPluginManager().registerEvents(new soulEvents(), this);

    List<Enchant> enchants = new ArrayList<>();
    enchants.add(new GenoEnchant());
    enchants.add(new GlitchEnchant());
    enchants.add(new OblivionEnchant());
    enchants.add(new WitherEnchant());
    enchants.add(new DimensionalManipEnchant());
    enchants.add(new Devil());
    enchants.add(new com.rschao.enchants.Choco());
      com.rschao.smp.Plugin.addEnchantsWithoutTable(enchants.toArray(new Enchant[0]));
    for(Enchant e: enchants){
      Bukkit.getPluginManager().registerEvents(e, this);
    }
    Bukkit.getPluginManager().registerEvents(this, this);
    initializeTabs();
    StartCmdsNormal();
    StartBossCmds();
      Magician.register();
      Choco.RegisterTechs();
    Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
        Jevil.RegisterTechs();
        LOGGER.info("Registered Jevil Techs");
    }); // Delay of 5 seconds (20 ticks per second)
  }
  void StartCmdsNormal(){
    
    giveItems.Load().register("gaster");
    giveItems.Souls().register("gaster");
    clearSoul.Load().register("gaster");
    checkSoul.Load().register("gaster");
    sendflag.Load().register("gaster");
    com.rschao.smp.Plugin.LoadAddEnchant();
  }
  void StartBossCmds(){
    bossCmds.Load().register("gaster");
    bossCmds.NextPhase().register("gaster");
    bossCmds.SaveInventory().register("gaster");
    bossCmds.LoadInventory().register("gaster");
    bossCmds.DeleteInventory().register("gaster");
    reload.Load().register("gaster");
    bossCmds.ResetPhase().register("gaster");
    bossCmds.ToggleSoulDrop().register("gaster");
    boss_settings_cmds.bossSettings().register("gaster");
    boss_settings_cmds.SetBossValue().register("gaster");
    CheckFlag.load().register("gaster");
    EvalScore.Load().register("gaster");
    PlayTheme.register();
  }

  public void onDisable()
  {
    LOGGER.info("bossfight disabled");
  }
  public void initializeTabs() {
  ItemStack rootIcon = new ItemStack(Material.LEATHER);
  ItemMeta meta = rootIcon.getItemMeta();
  meta.setItemModel(NamespacedKey.minecraft("soul_e"));
  rootIcon.setItemMeta(meta);
  ItemStack rootIcon2 = new ItemStack(Material.NAUTILUS_SHELL);
  ItemMeta meta2 = rootIcon2.getItemMeta();
  meta2.setItemModel(NamespacedKey.minecraft("g_seven_hands"));
  rootIcon2.setItemMeta(meta2);

  api = UltimateAdvancementAPI.getInstance(this);
  tab0 = api.createAdvancementTab(AdvancementTabNamespaces.tab0_NAMESPACE);
  AdvancementKey oak_sapling0Key = new AdvancementKey(tab0.getNamespace(), "souls");
  CoordAdapter adaptertab0 = CoordAdapter.builder().add(oak_sapling0Key, 0f, 0f).add(Getsoul.KEY, 1f, 0f).add(Cleansoul.KEY, 1f, 1f).add(Soul_determination.KEY, 2f, 0f).add(Soul_compassion.KEY, 3f, 0f).add(Soul_love.KEY, 5f, 0f).add(Soul_hate.KEY, 4f, 0f).add(Soul_master.KEY, 6f, 0f).add(Soul_hostility.KEY, 7f, 0f).add(Soul_seven.KEY, 8f, 0f).add(Boss_final.KEY, 6f, 1f).add(Boss_origin.KEY, 7, 1).build();
  RootAdvancement oak_sapling0 = new RootAdvancement(tab0, oak_sapling0Key.getKey(), new AdvancementDisplay(rootIcon, "The power of souls", AdvancementFrameType.TASK, true, true, adaptertab0.getX(oak_sapling0Key), adaptertab0.getY(oak_sapling0Key), "With it, thou shalt reach new heights"),"textures/block/black_concrete_powder.png",1);
  Getsoul getsoul = new Getsoul(oak_sapling0,adaptertab0.getX(Getsoul.KEY), adaptertab0.getY(Getsoul.KEY));
  Cleansoul cleansoul = new Cleansoul(getsoul,adaptertab0.getX(Cleansoul.KEY), adaptertab0.getY(Cleansoul.KEY));
  Soul_determination soul_determination = new Soul_determination(getsoul,adaptertab0.getX(Soul_determination.KEY), adaptertab0.getY(Soul_determination.KEY));
  Soul_compassion soul_compassion = new Soul_compassion(soul_determination,adaptertab0.getX(Soul_compassion.KEY), adaptertab0.getY(Soul_compassion.KEY));
  Soul_love soul_love = new Soul_love(soul_compassion,adaptertab0.getX(Soul_love.KEY), adaptertab0.getY(Soul_love.KEY));
  Soul_hate soul_hate = new Soul_hate(soul_determination,adaptertab0.getX(Soul_hate.KEY), adaptertab0.getY(Soul_hate.KEY));
  Soul_master soul_master = new Soul_master( adaptertab0.getX(Soul_master.KEY), adaptertab0.getY(Soul_master.KEY), soul_love, soul_hate);
  Soul_hostility soul_hostility = new Soul_hostility(soul_master,adaptertab0.getX(Soul_hostility.KEY), adaptertab0.getY(Soul_hostility.KEY));
  Soul_seven oak_sapling20 = new Soul_seven(soul_hostility,adaptertab0.getX(Soul_seven.KEY), adaptertab0.getY(Soul_seven.KEY));
  Boss_final boss_final = new Boss_final(soul_love,adaptertab0.getX(Boss_final.KEY), adaptertab0.getY(Boss_final.KEY));
  Boss_origin boss_origin = new Boss_origin(boss_final,adaptertab0.getX(Boss_origin.KEY), adaptertab0.getY(Boss_origin.KEY));
  tab0.registerAdvancements(oak_sapling0 ,getsoul ,cleansoul ,soul_determination ,soul_compassion ,soul_love  ,soul_hate ,soul_master ,soul_hostility ,oak_sapling20 ,boss_final, boss_origin);
}
  @EventHandler
  public void onJoin(PlayerLoadingCompletedEvent e) {
    Player p = e.getPlayer();
    tab0.grantRootAdvancement(p);
    tab0.showTab(p);
  }
  public static void EnableCHeart(){
    Bukkit.getPluginManager().registerEvents(new weaponEvents(), Plugin.getPlugin(Plugin.class));

    Bukkit.getPluginManager().registerEvents(new bossEvents(), Plugin.getPlugin(Plugin.class));
  }
}
