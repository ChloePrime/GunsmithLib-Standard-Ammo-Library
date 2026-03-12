package cn.chloeprime.gunsmithlib_std_ammo.common.item;

import cn.chloeprime.gunsmithlib_std_ammo.GunsmithLibStdAmmoMod;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;

public class GSAItemTags {
    public static final TagKey<Item> ORES_TUNGSTEN = commonTag("ores/tungsten");
    public static final TagKey<Item> ORES_TIBERIUM = commonTag("ores/tiberium/green");
    public static final TagKey<Item> ORES_TIB_SEED = commonTag("ores/tiberium_seed");
    public static final TagKey<Item> ORE_IN_GROUND_ENDSTONE = commonTag("ores_in_ground/end_stone");

    public static final TagKey<Item> RAW_MATERIALS = Tags.Items.RAW_MATERIALS;
    public static final TagKey<Item> RAW_MATERIALS_TUNGSTEN = commonTag("raw_materials/tungsten");

    public static final TagKey<Item> INGOTS = Tags.Items.INGOTS;
    public static final TagKey<Item> INGOTS_STEEL = commonTag("ingots/steel");
    public static final TagKey<Item> INGOTS_TUNGSTEN = commonTag("ingots/tungsten");
    public static final TagKey<Item> INGOTS_TIB_ALLOY = commonTag("ingots/tiberium_alloy");

    public static final TagKey<Item> GEMS = Tags.Items.GEMS;
    public static final TagKey<Item> GEMS_TIB_SEED = commonTag("gems/tiberium_seed");
    public static final TagKey<Item> GEMS_TIB_GREEN = commonTag("gems/tiberium/green");
    public static final TagKey<Item> GEMS_TIB_BLUE = commonTag("gems/tiberium/blue");

    private static TagKey<Item> commonTag(String path) {
        return ItemTags.create(GunsmithLibStdAmmoMod.loc("forge", path));
    }
}
