package cn.chloeprime.gunsmithlib_std_ammo.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class InfectiousLeavesBlock extends LeavesBlock {
    public static final int HIGHEST_STAGE = 7;
    public static final int MAX_DEPTH = 7;
    public static final int ABSOLUTE_MAX_DEPTH = 15;
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, HIGHEST_STAGE);
    public static final IntegerProperty DEPTH = IntegerProperty.create("depth", 0, ABSOLUTE_MAX_DEPTH);

    public InfectiousLeavesBlock(Properties properties) {
        super(properties);
    }

    public int getMaxStage() {
        return HIGHEST_STAGE;
    }

    public int getMaxDepth() {
        return MAX_DEPTH;
    }


    /**
     * Modified from <a href="https://github.com/Ellpeck/NaturesAura/blob/main/src/main/java/de/ellpeck/naturesaura/blocks/BlockGoldenLeaves.java">Natura Aura</a>
     */
    public boolean convert(Level level, BlockPos pos, int depth) {
        var state = level.getBlockState(pos);
        if (state.getBlock() instanceof LeavesBlock && !(state.getBlock() instanceof InfectiousLeavesBlock)) {
            if (!level.isClientSide()) {
                // 防止生长时重复掉落物品
                level.destroyBlock(pos, false);
                level.setBlockAndUpdate(pos, defaultBlockState()
                        .setValue(LeavesBlock.DISTANCE, state.hasProperty(LeavesBlock.DISTANCE) ? state.getValue(LeavesBlock.DISTANCE) : 1)
                        .setValue(LeavesBlock.PERSISTENT, state.hasProperty(LeavesBlock.PERSISTENT) ? state.getValue(LeavesBlock.PERSISTENT) : false)
                        .setValue(InfectiousLeavesBlock.DEPTH, depth));
            }
            return true;
        }
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(STAGE);
        builder.add(DEPTH);
    }

    /**
     * Modified from <a href="https://github.com/Ellpeck/NaturesAura/blob/main/src/main/java/de/ellpeck/naturesaura/blocks/BlockGoldenLeaves.java">Natura Aura</a>
     */
    @Override
    public void randomTick(BlockState state, ServerLevel levelIn, BlockPos pos, RandomSource random) {
        super.randomTick(state, levelIn, pos, random);
        if (!levelIn.isClientSide) {
            int stage = state.getValue(STAGE);
            if (stage < HIGHEST_STAGE) {
                levelIn.setBlockAndUpdate(pos, state.setValue(STAGE, stage + 1));
            }

            if (stage > 1) {
                var offset = pos.relative(Direction.getRandom(random));
                int depth = state.getValue(DEPTH);

                if (levelIn.isLoaded(offset)) {
                    if (depth < getMaxDepth()) {
                        convert(levelIn, offset, Math.min(depth + 1, getMaxDepth()));
                    }
                }
            }
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        // 剪刀剪下来的泰伯利亚树叶不会扩散
        if (state.getValue(PERSISTENT)) {
            return false;
        }
        return state.getValue(STAGE) < getMaxStage() || state.getValue(DEPTH) < getMaxDepth();
    }
}
