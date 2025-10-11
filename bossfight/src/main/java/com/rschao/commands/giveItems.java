package com.rschao.commands;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.rschao.items.Items;
import com.rschao.items.weapons;
import com.rschao.items.Hands;
import com.rschao.items.HandsNew;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.executors.CommandArguments;

public class giveItems {
    public static CommandAPICommand Load(){
        CommandAPICommand cmd = new CommandAPICommand("gaster")
            .withPermission("gaster.items")
            .withSubcommands(Weapons(), Hands(), Cleaner(), CleanerTo(), Crystal(), Swords(), Souls(), container())
            .executesPlayer((Player player, CommandArguments args) -> {
                player.sendMessage("/gaster <type>");
            });
        return cmd;
    }
    public static CommandAPICommand Souls(){
        CommandAPICommand cmd = new CommandAPICommand("souls")
        .withSubcommands(MainSouls(), New(), Special())
        .executesPlayer((Player player, CommandArguments args) -> {
            player.sendMessage("select a type");
        });
        return cmd;
    }
    public static CommandAPICommand Special(){
        CommandAPICommand cmd = new CommandAPICommand("special")
        .withSubcommands(Darkness(), SevenSouls(), Void(), Neutral(), Chaos(), PurityH())
        .executesPlayer((Player player, CommandArguments args) -> {
            player.sendMessage("select a type");
        });
        return cmd;
    }
    public static CommandAPICommand Swords(){
        CommandAPICommand cmd = new CommandAPICommand("swords")
        .withSubcommands(ChaoSword(), DeltaSword(), RustSword(), Retainer(), AwakenedSword(), DeltaEssence())
        .executesPlayer((Player player, CommandArguments args) -> {
            player.sendMessage("select a type");
        });
        return cmd;
    }
    
    public static CommandAPICommand ChaoSword(){
        CommandAPICommand cmd = new CommandAPICommand("chao_sword")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, weapons.ChaoSword, amount);
        });
        return cmd;
    }
    public static CommandAPICommand RustSword(){
        CommandAPICommand cmd = new CommandAPICommand("rusted_sword")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, weapons.RustedSword, amount);
        });
        return cmd;
    }
    public static CommandAPICommand c_essence(){
        CommandAPICommand cmd = new CommandAPICommand("corrupted_essence")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, weapons.CorrupredEssence, amount);
        });
        return cmd;
    }

    public static CommandAPICommand DimentioHelmet(){
        CommandAPICommand cmd = new CommandAPICommand("dimentio")
                .withOptionalArguments(new IntegerArgument("amount"))
                .executesPlayer((Player player, CommandArguments args) -> {
                    Integer amountObj = (Integer) args.getOrDefault("amount", 1);
                    int amount = (amountObj != null) ? amountObj : 1;
                    addItem(player, weapons.DimentioMask, amount);
                });
        return cmd;
    }

    public static CommandAPICommand Jevil(){
        CommandAPICommand cmd = new CommandAPICommand("jevil")
                .withOptionalArguments(new IntegerArgument("amount"))
                .executesPlayer((Player player, CommandArguments args) -> {
                    Integer amountObj = (Integer) args.getOrDefault("amount", 1);
                    int amount = (amountObj != null) ? amountObj : 1;
                    addItem(player, weapons.Devilsknife, amount);
                });
        return cmd;
    }

    public static CommandAPICommand Choco(){
        CommandAPICommand cmd = new CommandAPICommand("choco")
                .withOptionalArguments(new IntegerArgument("amount"))
                .executesPlayer((Player player, CommandArguments args) -> {
                    Integer amountObj = (Integer) args.getOrDefault("amount", 1);
                    int amount = (amountObj != null) ? amountObj : 1;
                    addItem(player, weapons.ChocoBlade, amount);
                });
        return cmd;
    }

    public static CommandAPICommand AwakenedSword(){
        CommandAPICommand cmd = new CommandAPICommand("awakened_sword")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, weapons.AwakenedSword, amount);
        });
        return cmd;
    }
    public static CommandAPICommand DeltaEssence(){
        CommandAPICommand cmd = new CommandAPICommand("oblivion_essence")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, weapons.OblivionEssence, amount);
        });
        return cmd;
    }
    public static CommandAPICommand Retainer(){
        CommandAPICommand cmd = new CommandAPICommand("retainer")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, weapons.Container, amount);
        });
        return cmd;
    }

    public static CommandAPICommand DeltaSword(){
        CommandAPICommand cmd = new CommandAPICommand("oblivion_edge")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, weapons.DeltaSword, amount);
        });
        return cmd;
    }
    public static CommandAPICommand Weapons(){
        CommandAPICommand cmd = new CommandAPICommand("weapons")
        .withSubcommands(GKnife(), XKnife(), GasterBlaster(), OmegaBlaster(), CorruptedHeart(), SansEye(), BadEye(), TheToys(), c_essence(), DimentioHelmet(), Choco(), Jevil())
        .executesPlayer((Player player, CommandArguments args) -> {
            player.sendMessage("select a weapon");
        });
        return cmd;
    }
    public static CommandAPICommand GKnife(){
        CommandAPICommand cmd = new CommandAPICommand("gknife")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Items.GKnife, amount);
        });
        return cmd;
    }
    public static CommandAPICommand XKnife(){
        CommandAPICommand cmd = new CommandAPICommand("xknife")
        .withOptionalArguments(new IntegerArgument("amount"))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Items.XKnife, amount);
        });
        return cmd;
    }
    public static CommandAPICommand GasterBlaster(){
        CommandAPICommand cmd = new CommandAPICommand("gaster_blaster")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Items.GasterBlaster, amount);
        });
        return cmd;
    }
    public static CommandAPICommand OmegaBlaster(){
        CommandAPICommand cmd = new CommandAPICommand("omega_blaster")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Items.omegaBlaster, amount);
        });
        return cmd;
    }
    public static CommandAPICommand CorruptedHeart(){
        CommandAPICommand cmd = new CommandAPICommand("c_heart")
        .withOptionalArguments(new IntegerArgument("amount"))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, weapons.CorruptedHeart, amount);
        });
        return cmd;
    }
    public static CommandAPICommand SansEye(){
        CommandAPICommand cmd = new CommandAPICommand("sanseye")
        .withOptionalArguments(new IntegerArgument("amount"))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, weapons.SansEye, amount);
        });
        return cmd;
    }
    public static CommandAPICommand BadEye(){
        CommandAPICommand cmd = new CommandAPICommand("badeye")
        .withOptionalArguments(new IntegerArgument("amount"))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, weapons.BadEye, amount);
        });
        return cmd;
    }
    public static CommandAPICommand TheToys(){
        CommandAPICommand cmd = new CommandAPICommand("thetoys")
        .withOptionalArguments(new IntegerArgument("amount"))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, weapons.TheToys, amount);
        });
        return cmd;
    }
    public static CommandAPICommand Cleaner(){
        CommandAPICommand cmd = new CommandAPICommand("cleaner")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Hands.SoulCleaner, amount);
        });
        return cmd;
    }
    public static CommandAPICommand CleanerTo(){
        CommandAPICommand cmd = new CommandAPICommand("changer")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Hands.SoulCleanerBuffed, amount);
        });
        return cmd;
    }
    public static CommandAPICommand EmptyHand(){
        CommandAPICommand cmd = new CommandAPICommand("emptyhand")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Hands.EmptyHand, amount);
        });
        return cmd;
    }

    public static CommandAPICommand Hands(){
        CommandAPICommand cmd = new CommandAPICommand("hands")
        .withSubcommands(Base(), empowered(), EmptyHand())
        .executesPlayer((Player player, CommandArguments args) -> {
            player.sendMessage("select a hand");
        });
        return cmd;
    }
    public static CommandAPICommand Base(){
        CommandAPICommand cmd = new CommandAPICommand("base")
        .withSubcommands(Repeller(), TimeHand(), GravityHand(), HealHand(), SpeedHand(), BraveryHand(), CannonHand(), SevenHands())
        .executesPlayer((Player player, CommandArguments args) -> {
            addItem(player, Hands.Repeller, 1);
            addItem(player, Hands.CannonHand, 1);
            addItem(player, Hands.TimeHand, 1);
            addItem(player, Hands.SpeedHand, 1);
            addItem(player, Hands.HealHand, 1);
            addItem(player, Hands.GravityHand, 1);
            addItem(player, Hands.BraveryHand, 1);
        });
        return cmd;
    }
    public static CommandAPICommand Repeller(){
        CommandAPICommand cmd = new CommandAPICommand("patience")
        .withOptionalArguments(new IntegerArgument("amount"))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Hands.Repeller, amount);
        });
        return cmd;
    }
    public static CommandAPICommand TimeHand(){
        CommandAPICommand cmd = new CommandAPICommand("perseverance")
        .withOptionalArguments(new IntegerArgument("amount"))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Hands.TimeHand, amount);
        });
        return cmd;
    }
    public static CommandAPICommand GravityHand(){
        CommandAPICommand cmd = new CommandAPICommand("integrity")
                .withOptionalArguments(new IntegerArgument("amount"))
                .executesPlayer((Player player, CommandArguments args) -> {
                    Integer amountObj = (Integer) args.getOrDefault("amount", 1);
                    int amount = (amountObj != null) ? amountObj : 1;
                    addItem(player, Hands.GravityHand, amount);
                });
        return cmd;
    }
    public static CommandAPICommand HealHand(){
        CommandAPICommand cmd = new CommandAPICommand("kindness")
        .withOptionalArguments(new IntegerArgument("amount"))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Hands.HealHand, amount);
        });
        return cmd;
    }
    public static CommandAPICommand SpeedHand(){
        CommandAPICommand cmd = new CommandAPICommand("justice")
        .withOptionalArguments(new IntegerArgument("amount"))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Hands.SpeedHand, amount);
        });
        return cmd;
    }
    public static CommandAPICommand BraveryHand(){
        CommandAPICommand cmd = new CommandAPICommand("bravery")
        .withOptionalArguments(new IntegerArgument("amount"))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Hands.BraveryHand, amount);
        });
        return cmd;
    }
    public static CommandAPICommand CannonHand(){
        CommandAPICommand cmd = new CommandAPICommand("determination")
        .withOptionalArguments(new IntegerArgument("amount"))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Hands.CannonHand, amount);
        });
        return cmd;
    }
    public static CommandAPICommand empowered(){
        CommandAPICommand cmd = new CommandAPICommand("powered")
        .withSubcommands(SincerityHand(), EmpathyHand(), AudacityHand(), GuidanceHand(), PurityHand(), LoveHand(), FortitudeHand(), SevenBuffs())
        .executesPlayer((Player player, CommandArguments args) -> {
            player.sendMessage("select a hand");
        });
        return cmd;
    }

    public static CommandAPICommand SincerityHand(){
        CommandAPICommand cmd = new CommandAPICommand("sincerityhand")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, HandsNew.SincerityHand, amount);
        });
        return cmd;
    }

    public static CommandAPICommand EmpathyHand(){
        CommandAPICommand cmd = new CommandAPICommand("empathyhand")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, HandsNew.EmpathyHand, amount);
        });
        return cmd;
    }

    public static CommandAPICommand AudacityHand(){
        CommandAPICommand cmd = new CommandAPICommand("audacityhand")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, HandsNew.AudacityHand, amount);
        });
        return cmd;
    }

    public static CommandAPICommand GuidanceHand(){
        CommandAPICommand cmd = new CommandAPICommand("guidancehand")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, HandsNew.GuidanceHand, amount);
        });
        return cmd;
    }

    public static CommandAPICommand PurityHand(){
        CommandAPICommand cmd = new CommandAPICommand("purityhand")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, HandsNew.PurityHand, amount);
        });
        return cmd;
    }

    public static CommandAPICommand LoveHand(){
        CommandAPICommand cmd = new CommandAPICommand("lovehand")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, HandsNew.LoveHand, amount);
        });
        return cmd;
    }

    public static CommandAPICommand FortitudeHand(){
        CommandAPICommand cmd = new CommandAPICommand("fortitudehand")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, HandsNew.FortitudeHand, amount);
        });
        return cmd;
    }

    public static CommandAPICommand MainSouls(){
        CommandAPICommand cmd = new CommandAPICommand("base")
        .withSubcommands(DT(), Persevearence(), Kind(), Justice(), Bravery(), Integrity(), Patience())
        .executesPlayer((Player player, CommandArguments args) -> {
            player.sendMessage("Select soul");
        });
        return cmd;
    }
    public static CommandAPICommand SevenSouls(){
        CommandAPICommand cmd = new CommandAPICommand("seven")
        .executesPlayer((Player player, CommandArguments args) -> {
            player.getInventory().addItem(weapons.SevenSouls);
        });
        return cmd;
    }
    public static CommandAPICommand Neutral(){
        CommandAPICommand cmd = new CommandAPICommand("neutral")
        .executesPlayer((Player player, CommandArguments args) -> {
            player.getInventory().addItem(Items.soul_Neutral);
        });
        return cmd;
    }
    public static CommandAPICommand Void(){
        CommandAPICommand cmd = new CommandAPICommand("void")
                .executesPlayer((Player player, CommandArguments args) -> {
                    player.getInventory().addItem(Items.soul_Void);
                });
        return cmd;
    }
    public static CommandAPICommand Chaos(){
        CommandAPICommand cmd = new CommandAPICommand("chaos")
                .executesPlayer((Player player, CommandArguments args) -> {
                    player.getInventory().addItem(Items.ChaosHeart);
                });
        return cmd;
    }
    public static CommandAPICommand PurityH(){
        CommandAPICommand cmd = new CommandAPICommand("purity_heart")
                .executesPlayer((Player player, CommandArguments args) -> {
                    player.getInventory().addItem(Items.PurityHeart);
                });
        return cmd;
    }
    public static CommandAPICommand SevenHands(){
        CommandAPICommand cmd = new CommandAPICommand("sevenhands")
        .executesPlayer((Player player, CommandArguments args) -> {
            player.getInventory().addItem(weapons.SevenHands);
        });
        return cmd;
    }
    public static CommandAPICommand SevenBuffs(){
        CommandAPICommand cmd = new CommandAPICommand("sevenhands")
        .executesPlayer((Player player, CommandArguments args) -> {
            player.getInventory().addItem(HandsNew.SevenHand);
        });
        return cmd;
    }
    public static CommandAPICommand container(){
        CommandAPICommand cmd = new CommandAPICommand("soulcontainer")
        .executesPlayer((Player player, CommandArguments args) -> {
            player.getInventory().addItem(Items.SoulContainer);
        });
        return cmd;
    }
    public static CommandAPICommand New(){
        CommandAPICommand cmd = new CommandAPICommand("empowered")
        .withSubcommands(Compassion(), Empathy(), Affection(), Love(), Hope(), Empty(), Hate(), Purity(), Sincerity(), Conflict(), Audacity(), Fortitude(), Guidance(), Insanity(), Irresolution(), Malice(), Despair(), Animosity())
        .executesPlayer((Player player, CommandArguments args) -> {
            player.sendMessage("Select soul");
        });
        return cmd;
    }
    public static CommandAPICommand DT(){
        CommandAPICommand cmd = new CommandAPICommand("determination")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Items.soul_Determination, amount);
        });
        return cmd;
    }
    public static CommandAPICommand Persevearence(){
        CommandAPICommand cmd = new CommandAPICommand("persevearence")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Items.soul_Persevearence, amount);
        });
        return cmd;
    }
    public static CommandAPICommand Kind(){
        CommandAPICommand cmd = new CommandAPICommand("kindness")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Items.soul_Kind, amount);
        });
        return cmd;
    }
    public static CommandAPICommand Bravery(){
        CommandAPICommand cmd = new CommandAPICommand("bravery")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Items.soul_Bravery, amount);
        });
        return cmd;
    }
    public static CommandAPICommand Justice(){
        CommandAPICommand cmd = new CommandAPICommand("justice")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Items.soul_Justice, amount);
        });
        return cmd;
    }
    public static CommandAPICommand Integrity(){
        CommandAPICommand cmd = new CommandAPICommand("integrity")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Items.soul_Integrity, amount);
        });
        return cmd;
    }
    public static CommandAPICommand Patience(){
        CommandAPICommand cmd = new CommandAPICommand("patience")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Items.soul_Patience, amount);
        });
        return cmd;
    }
    public static CommandAPICommand Hate(){
        CommandAPICommand cmd = new CommandAPICommand("hate")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Items.soul_Hate, amount);
        });
        return cmd;
    }
    public static CommandAPICommand Compassion(){
        CommandAPICommand cmd = new CommandAPICommand("compassion")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Items.soul_Compassion, amount);
        });
        return cmd;
    }
    public static CommandAPICommand Affection(){
        CommandAPICommand cmd = new CommandAPICommand("affection")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Items.soul_Affection, amount);
        });
        return cmd;
    }
    public static CommandAPICommand Empathy(){
        CommandAPICommand cmd = new CommandAPICommand("empathy")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Items.soul_Empathy, amount);
        });
        return cmd;
    }
    public static CommandAPICommand Love(){
        CommandAPICommand cmd = new CommandAPICommand("love")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Items.soul_Love, amount);
        });
        return cmd;
    }
    public static CommandAPICommand Empty(){
        CommandAPICommand cmd = new CommandAPICommand("emptiness")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Items.soul_Emptiness, amount);
        });
        return cmd;
    }
    public static CommandAPICommand Hope(){
        CommandAPICommand cmd = new CommandAPICommand("hope")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Items.soul_Hope, amount);
        });
        return cmd;
    }
    public static CommandAPICommand Crystal(){
        CommandAPICommand cmd = new CommandAPICommand("crystal")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Items.DeterminationCrystal, amount);
        });
        return cmd;
    }
    public static CommandAPICommand Darkness(){
        CommandAPICommand cmd = new CommandAPICommand("darkness")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Items.soul_Hostility, amount);
        });
        return cmd;
    }
    public static CommandAPICommand Purity(){
        CommandAPICommand cmd = new CommandAPICommand("purity")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Items.soul_Purity, amount);
        });
        return cmd;
    }
    public static CommandAPICommand Sincerity(){
        CommandAPICommand cmd = new CommandAPICommand("sincerity")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Items.soul_Sincerity, amount);
        });
        return cmd;
    }
    public static CommandAPICommand Conflict(){
        CommandAPICommand cmd = new CommandAPICommand("conflict")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Items.soul_Conflict, amount);
        });
        return cmd;
    }
    public static CommandAPICommand Audacity(){
        CommandAPICommand cmd = new CommandAPICommand("audacity")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Items.soul_Audacity, amount);
        });
        return cmd;
    }
    public static CommandAPICommand Fortitude(){
        CommandAPICommand cmd = new CommandAPICommand("fortitude")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Items.soul_Fortitude, amount);
        });
        return cmd;
    }
    public static CommandAPICommand Guidance(){
        CommandAPICommand cmd = new CommandAPICommand("guidance")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Items.soul_Guidance, amount);
        });
        return cmd;
    }
    public static CommandAPICommand Insanity(){
        CommandAPICommand cmd = new CommandAPICommand("insanity")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Items.soul_Insanity, amount);
        });
        return cmd;
    }
    public static CommandAPICommand Irresolution(){
        CommandAPICommand cmd = new CommandAPICommand("irresolution")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Items.soul_Irresolution, amount);
        });
        return cmd;
    }
    public static CommandAPICommand Malice(){
        CommandAPICommand cmd = new CommandAPICommand("malice")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Items.soul_Malice, amount);
        });
        return cmd;
    }
    public static CommandAPICommand Despair(){
        CommandAPICommand cmd = new CommandAPICommand("despair")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Items.soul_Despair, amount);
        });
        return cmd;
    }
    public static CommandAPICommand Animosity(){
        CommandAPICommand cmd = new CommandAPICommand("animosity")
        .withOptionalArguments(new IntegerArgument("amount", 1, 64))
        .executesPlayer((Player player, CommandArguments args) -> {
            Integer amountObj = (Integer) args.getOrDefault("amount", 1);
            int amount = (amountObj != null) ? amountObj : 1;
            addItem(player, Items.soul_Animosity, amount);
        });
        return cmd;
    }
    static void addItem(Player p, ItemStack item, int amount){
        for (int i = 0; i < amount; i++) {
            p.getInventory().addItem(item);
        }
    }
    static void addItem(Player p, int amount, ItemStack ... items){
        for (ItemStack item : items) {
            for (int i = 0; i < amount; i++) {
                p.getInventory().addItem(item);
            }
        }
    }
}