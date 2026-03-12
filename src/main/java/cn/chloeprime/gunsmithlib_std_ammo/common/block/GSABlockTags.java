package cn.chloeprime.gunsmithlib_std_ammo.common.block;

import cn.chloeprime.gunsmithlib_std_ammo.GunsmithLibStdAmmoMod;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class GSABlockTags {
    public static final TagKey<Block> ORES_TUNGSTEN = commonTag("ores/tungsten");
    public static final TagKey<Block> ORES_TIBERIUM = commonTag("ores/tiberium/green");
    public static final TagKey<Block> ORES_TIB_SEED = commonTag("ores/tiberium_seed");
    public static final TagKey<Block> ORE_IN_GROUND_ENDSTONE = commonTag("ores_in_ground/end_stone");

    public static final TagKey<Block> TIB_ORE_PLACEABLE = BlockTags.create(GunsmithLibStdAmmoMod.loc("end_tiberium_seed_ore_placeable"));

    private static TagKey<Block> commonTag(String path) {
        return BlockTags.create(GunsmithLibStdAmmoMod.loc("forge", path));
    }

    private GSABlockTags() {
    }
}
