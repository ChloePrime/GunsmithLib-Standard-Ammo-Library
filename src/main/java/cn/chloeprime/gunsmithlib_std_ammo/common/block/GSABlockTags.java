package cn.chloeprime.gunsmithlib_std_ammo.common.block;

import cn.chloeprime.gunsmithlib_std_ammo.GunsmithLibStdAmmoMod;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;

public final class GSABlockTags {
    public static final TagKey<Block> STORAGE_BLOCKS = Tags.Blocks.STORAGE_BLOCKS;
    public static final TagKey<Block> STORAGE_BLOCKS_ENGINEERING_PLASTIC = commonTag("storage_blocks/engineering_plastic");
    public static final TagKey<Block> ORES_TUNGSTEN = commonTag("ores/tungsten");
    public static final TagKey<Block> ORES_TIBERIUM = commonTag("ores/tiberium/green");
    public static final TagKey<Block> ORES_TIB_SEED = commonTag("ores/tiberium_seed");
    public static final TagKey<Block> ORE_IN_GROUND_ENDSTONE = commonTag("ores_in_ground/end_stone");
    public static final TagKey<Block> OBSTRUCTS_LARGE_BULLET = BlockTags.create(GunsmithLibStdAmmoMod.loc("obstructs_large_bullets"));

    public static final TagKey<Block> TIB_ORE_PLACEABLE = BlockTags.create(GunsmithLibStdAmmoMod.loc("end_tiberium_seed_ore_placeable"));

    private static TagKey<Block> commonTag(String path) {
        return BlockTags.create(GunsmithLibStdAmmoMod.loc("forge", path));
    }

    private GSABlockTags() {
    }
}
