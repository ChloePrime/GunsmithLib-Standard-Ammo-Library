package cn.chloeprime.gunsmithlib_std_ammo.common.block;

import cn.chloeprime.gunsmithlib_std_ammo.GunsmithLibStdAmmoMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

public final class GSABlocks {
    private static final DeferredRegister<Block> DFR = DeferredRegister.create(Registries.BLOCK, GunsmithLibStdAmmoMod.MOD_ID);
    public static final IntProvider TIB_ORE_EXP = UniformInt.of(3, 7);
    public static final IntProvider TIB_SEED_ORE_EXP = UniformInt.of(16, 40);

    public static final Supplier<RotatedPillarBlock> PURIFIED_DEBRIS = DFR.register("purified_debris", () -> new RotatedPillarBlock(BlockBehaviour
            .Properties.of()
            .mapColor(MapColor.COLOR_BLACK)
            .requiresCorrectToolForDrops()
            .strength(30, 1200)
            .sound(SoundType.ANCIENT_DEBRIS)));

    public static final Supplier<Block> TIBERIUM_ORE = DFR.register("tiberium_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .strength(3.0F, 3.0F), TIB_ORE_EXP));

    public static final Supplier<Block> DEEPSLATE_TIBERIUM_ORE = DFR.register("deepslate_tiberium_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .strength(4.5F, 3.0F)
            .sound(SoundType.DEEPSLATE), TIB_ORE_EXP));

    public static final Supplier<Block> NETHER_TIBERIUM_ORE = DFR.register("nether_tiberium_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .strength(3.0F, 3.0F)
            .sound(SoundType.NETHER_GOLD_ORE), TIB_ORE_EXP));

    public static final Supplier<Block> END_TIBERIUM_SEED_ORE = DFR.register("end_tiberium_seed_ore", () -> new RedStoneOreBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .randomTicks()
            .lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 15 : 0)
            .strength(30, 1200)
            .sound(SoundType.ANCIENT_DEBRIS)
    ) {
        @ParametersAreNonnullByDefault
        public void spawnAfterBreak(
                BlockState state, ServerLevel level, BlockPos pos,
                ItemStack stack, boolean dropExperience
        ) {
            super.spawnAfterBreak(state, level, pos, stack, dropExperience);
        }

        @Override
        @ParametersAreNonnullByDefault
        public int getExpDrop(
                BlockState state, LevelReader level, RandomSource randomSource,
                BlockPos pos, int fortuneLevel, int silkTouchLevel
        ) {
            return silkTouchLevel == 0 ? TIB_SEED_ORE_EXP.sample(randomSource) : 0;
        }
    });

    public static final Supplier<TiberiumLeavesBlock> TIBERIUM_LEAVES = DFR.register("tiberium_leaves", () -> new TiberiumLeavesBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_LIGHT_GREEN)
            .requiresCorrectToolForDrops()
            .strength(1.5F)
            .lightLevel(TiberiumLeavesBlock::lightFunc)
            .randomTicks()
            .noOcclusion()
            .sound(SoundType.GRASS)
    ));

    public static void init(IEventBus bus) {
        DFR.register(bus);
    }

    private GSABlocks() {
    }
}
