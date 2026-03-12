package cn.chloeprime.gunsmithlib_std_ammo.data;

import cn.chloeprime.gunsmithlib_std_ammo.common.block.GSABlockTags;
import cn.chloeprime.gunsmithlib_std_ammo.common.item.GSAItems;
import cn.chloeprime.gunsmithlib_std_ammo.common.item.GSAItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class GSAItemTagProvider extends ItemTagsProvider {

    public GSAItemTagProvider(
            PackOutput packOutput,
            CompletableFuture<HolderLookup.Provider> registry,
            CompletableFuture<TagLookup<Block>> blockTagProvider,
            String modId,
            @Nullable ExistingFileHelper existingFileHelper
    ) {
        super(packOutput, registry, blockTagProvider, modId, existingFileHelper);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addTags(@Nonnull HolderLookup.Provider registry) {
        copy(BlockTags.LEAVES, ItemTags.LEAVES);
        copy(Tags.Blocks.ORES, Tags.Items.ORES);
        copy(Tags.Blocks.ORE_RATES_SINGULAR, Tags.Items.ORE_RATES_SINGULAR);
        copy(Tags.Blocks.ORE_RATES_DENSE, Tags.Items.ORE_RATES_DENSE);
        copy(Tags.Blocks.ORES_IN_GROUND_STONE, Tags.Items.ORES_IN_GROUND_STONE);
        copy(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE, Tags.Items.ORES_IN_GROUND_DEEPSLATE);
        copy(Tags.Blocks.ORES_IN_GROUND_NETHERRACK, Tags.Items.ORES_IN_GROUND_NETHERRACK);
        copy(GSABlockTags.ORE_IN_GROUND_ENDSTONE, GSAItemTags.ORE_IN_GROUND_ENDSTONE);
        copy(GSABlockTags.ORES_TUNGSTEN, GSAItemTags.ORES_TUNGSTEN);
        copy(GSABlockTags.ORES_TIBERIUM, GSAItemTags.ORES_TIBERIUM);
        copy(GSABlockTags.ORES_TIB_SEED, GSAItemTags.ORES_TIB_SEED);

        tag(GSAItemTags.INGOTS).addTag(GSAItemTags.INGOTS_STEEL);
        tag(GSAItemTags.INGOTS_STEEL).add(GSAItems.STEEL_INGOT.get());

        tag(GSAItemTags.RAW_MATERIALS).addTag(GSAItemTags.RAW_MATERIALS_TUNGSTEN);
        tag(GSAItemTags.RAW_MATERIALS_TUNGSTEN).add(GSAItems.RAW_TUNGSTEN.get());

        tag(GSAItemTags.INGOTS).addTag(GSAItemTags.INGOTS_TUNGSTEN);
        tag(GSAItemTags.INGOTS_TUNGSTEN).add(GSAItems.TUNGSTEN_INGOT.get());

        tag(Tags.Items.SEEDS).add(GSAItems.TIBERIUM_SEED.get());
        tag(GSAItemTags.GEMS).addTags(
                GSAItemTags.GEMS_TIB_SEED,
                GSAItemTags.GEMS_TIB_GREEN,
                GSAItemTags.GEMS_TIB_BLUE);
        tag(GSAItemTags.GEMS_TIB_SEED).add(GSAItems.TIBERIUM_SEED.get());
        tag(GSAItemTags.GEMS_TIB_GREEN).add(GSAItems.GREEN_TIBERIUM_CRYSTAL.get());
        tag(GSAItemTags.GEMS_TIB_BLUE).add(GSAItems.BLUE_TIBERIUM_CRYSTAL.get());

        tag(GSAItemTags.INGOTS).addTag(GSAItemTags.INGOTS_TIB_ALLOY);
        tag(GSAItemTags.INGOTS_TIB_ALLOY).add(GSAItems.TIBERIUM_ALLOY_INGOT.get());
    }
}
