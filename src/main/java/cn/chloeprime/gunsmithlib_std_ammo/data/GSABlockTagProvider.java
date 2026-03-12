package cn.chloeprime.gunsmithlib_std_ammo.data;

import cn.chloeprime.gunsmithlib_std_ammo.common.block.GSABlockTags;
import cn.chloeprime.gunsmithlib_std_ammo.common.block.GSABlocks;
import com.google.common.base.Suppliers;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class GSABlockTagProvider extends BlockTagsProvider {
    public static final Supplier<Block[]> TIB_ORES = Suppliers.memoize(() -> new Block[]{
            GSABlocks.TIBERIUM_ORE.get(),
            GSABlocks.DEEPSLATE_TIBERIUM_ORE.get(),
            GSABlocks.NETHER_TIBERIUM_ORE.get(),
    });

    public GSABlockTagProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            String modId,
            @Nullable ExistingFileHelper existingFileHelper
    ) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addTags(@Nonnull HolderLookup.Provider registry) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                GSABlocks.PURIFIED_DEBRIS.get(),
                GSABlocks.END_TIBERIUM_SEED_ORE.get(),
                GSABlocks.TIBERIUM_LEAVES.get())
                .add(TIB_ORES.get());
        tag(BlockTags.NEEDS_STONE_TOOL).add(
                TIB_ORES.get());
        tag(BlockTags.NEEDS_DIAMOND_TOOL).add(
                GSABlocks.PURIFIED_DEBRIS.get(),
                GSABlocks.END_TIBERIUM_SEED_ORE.get());
        tag(BlockTags.DRAGON_IMMUNE).add(
                GSABlocks.END_TIBERIUM_SEED_ORE.get());

        tag(Tags.Blocks.ORES).addTags(
                GSABlockTags.ORES_TUNGSTEN,
                GSABlockTags.ORES_TIBERIUM,
                GSABlockTags.ORES_TIB_SEED);

        tag(GSABlockTags.ORES_TUNGSTEN).add(GSABlocks.PURIFIED_DEBRIS.get());
        tag(Tags.Blocks.ORE_RATES_SINGULAR).add(GSABlocks.PURIFIED_DEBRIS.get());

        tag(GSABlockTags.ORES_TIBERIUM).add(TIB_ORES.get());
        tag(Tags.Blocks.ORES_IN_GROUND_STONE).add(GSABlocks.TIBERIUM_ORE.get());
        tag(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE).add(GSABlocks.DEEPSLATE_TIBERIUM_ORE.get());
        tag(Tags.Blocks.ORES_IN_GROUND_NETHERRACK).add(GSABlocks.NETHER_TIBERIUM_ORE.get());
        tag(GSABlockTags.ORE_IN_GROUND_ENDSTONE).add(GSABlocks.END_TIBERIUM_SEED_ORE.get());
        tag(Tags.Blocks.ORE_RATES_SINGULAR).add(GSABlocks.TIBERIUM_ORE.get());
        tag(Tags.Blocks.ORE_RATES_SINGULAR).add(GSABlocks.DEEPSLATE_TIBERIUM_ORE.get());
        tag(Tags.Blocks.ORE_RATES_DENSE).add(GSABlocks.NETHER_TIBERIUM_ORE.get());

        tag(GSABlockTags.TIB_ORE_PLACEABLE).add(Blocks.END_STONE);
        tag(GSABlockTags.ORES_TIB_SEED).add(GSABlocks.END_TIBERIUM_SEED_ORE.get());
        tag(Tags.Blocks.ORE_RATES_SINGULAR).add(GSABlocks.END_TIBERIUM_SEED_ORE.get());

        tag(BlockTags.LEAVES).add(GSABlocks.TIBERIUM_LEAVES.get());
    }

}
