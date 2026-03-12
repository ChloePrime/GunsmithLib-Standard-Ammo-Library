package cn.chloeprime.gunsmithlib_std_ammo.common.item;

import cn.chloeprime.gunsmithlib_std_ammo.GunsmithLibStdAmmoMod;
import cn.chloeprime.gunsmithlib_std_ammo.common.block.GSABlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class GSAItems {
    private static final DeferredRegister<Item> DFR = DeferredRegister.create(Registries.ITEM, GunsmithLibStdAmmoMod.MOD_ID);

    // 炼钢
    public static final Supplier<Item> STEEL_INGOT =  simpleItem("steel_ingot");

    // 钨处理
    public static final Supplier<Item> NITRATION_MIXTURE = DFR.register("nitration_mixture", () -> new SimpleItem(new Item
            .Properties()
            .craftRemainder(Items.GLASS_BOTTLE)));
    public static final Supplier<BlockItem> PURIFIED_DEBRIS = blockItem("purified_debris", Rarity.UNCOMMON, GSABlocks.PURIFIED_DEBRIS);
    public static final Supplier<Item> RAW_TUNGSTEN = simpleItem("raw_tungsten", Rarity.UNCOMMON);
    public static final Supplier<Item> TUNGSTEN_INGOT = simpleItem("tungsten_ingot", Rarity.UNCOMMON);

    // 绿泰矿
    public static final Supplier<BlockItem> TIBERIUM_ORE = blockItem("tiberium_ore", GSABlocks.TIBERIUM_ORE);
    public static final Supplier<BlockItem> DEEPSLATE_TIBERIUM_ORE = blockItem("deepslate_tiberium_ore", GSABlocks.DEEPSLATE_TIBERIUM_ORE);
    public static final Supplier<BlockItem> NETHER_TIBERIUM_ORE = blockItem("nether_tiberium_ore", GSABlocks.NETHER_TIBERIUM_ORE);
    public static final Supplier<Item> GREEN_TIBERIUM_CRYSTAL = DFR.register("green_tiberium_crystal", () -> new BurnableSimpleItem(1600, new Item.Properties()));
    public static final Supplier<Item> N2_DYNAMITE = simpleItem("n2_dynamite");

    // 蓝泰矿
    public static final Supplier<BlockItem> END_TIBERIUM_SEED_ORE = blockItem("end_tiberium_seed_ore", Rarity.RARE, GSABlocks.END_TIBERIUM_SEED_ORE);
    public static final Supplier<Item> TIBERIUM_SEED = DFR.register("tiberium_seed", () -> new InfectLeavesSeed(
            GSABlocks.TIBERIUM_LEAVES,
            new Item.Properties().rarity(Rarity.RARE)));
    public static final Supplier<BlockItem> TIBERIUM_LEAVES = blockItem("tiberium_leaves", Rarity.RARE, GSABlocks.TIBERIUM_LEAVES);
    public static final Supplier<Item> BLUE_TIBERIUM_CRYSTAL = DFR.register("blue_tiberium_crystal", () -> new BurnableSimpleItem(50, new Item
            .Properties()
            .rarity(Rarity.RARE)));
    public static final Supplier<Item> TIBERIUM_COMPOUND = simpleItem("tiberium_compound", Rarity.RARE);
    public static final Supplier<Item> TIBERIUM_ALLOY_INGOT = simpleItem("tiberium_alloy_ingot", Rarity.RARE);

    private static Supplier<Item> simpleItem(String name) {
        return simpleItem(name, Rarity.COMMON);
    }

    private static Supplier<Item> simpleItem(String name, Rarity rarity) {
        return DFR.register(name, () -> new SimpleItem(new Item.Properties().rarity(rarity)));
    }

    private static Supplier<BlockItem> blockItem(String name, Supplier<? extends Block> block) {
        return blockItem(name, Rarity.COMMON, block);
    }

    private static Supplier<BlockItem> blockItem(String name, Rarity rarity, Supplier<? extends Block> block) {
        return DFR.register(name, () -> new BlockItem(block.get(), new Item.Properties().rarity(rarity)));
    }

    public static void init(IEventBus bus) {
        DFR.register(bus);
    }

    @SubscribeEvent
    public static void onCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(CreativeModeTabs.BUILDING_BLOCKS)) {
            event.accept(PURIFIED_DEBRIS);
        }
        if (event.getTabKey().equals(CreativeModeTabs.NATURAL_BLOCKS)) {
            event.accept(TIBERIUM_ORE);
            event.accept(DEEPSLATE_TIBERIUM_ORE);
            event.accept(NETHER_TIBERIUM_ORE);
            event.accept(END_TIBERIUM_SEED_ORE);
            event.accept(TIBERIUM_LEAVES);
        }
        if (event.getTabKey().equals(CreativeModeTabs.INGREDIENTS)) {
            event.accept(RAW_TUNGSTEN);
            event.accept(TIBERIUM_SEED);
            event.accept(GREEN_TIBERIUM_CRYSTAL);
            event.accept(BLUE_TIBERIUM_CRYSTAL);
            event.accept(STEEL_INGOT);
            event.accept(TUNGSTEN_INGOT);
            event.accept(TIBERIUM_ALLOY_INGOT);
            event.accept(NITRATION_MIXTURE);
            event.accept(N2_DYNAMITE);
            event.accept(TIBERIUM_COMPOUND);
        }
    }

    private GSAItems() {
    }
}
