package com.rschao.enchants;

import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.CustomEnchantment;
import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.definition.EasyEnchant;
import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.util.ColorCodes;
import org.bukkit.ChatColor;

public class Determined extends EasyEnchant {
    public Determined() {
        super("determined");
        CustomEnchantment enchant = makeEnchantment(ChatColor.RED + ColorCodes.BOLD.getCode() + "Determined");
        enchant.addSupportedItem("minecraft:echo_shard");
        enchant.setMaxLevel(5);
        saveBukkitEnchantment(enchant);
    }
}
