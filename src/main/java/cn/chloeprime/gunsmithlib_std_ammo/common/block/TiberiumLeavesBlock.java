package cn.chloeprime.gunsmithlib_std_ammo.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;

public class TiberiumLeavesBlock extends InfectiousLeavesBlock {
    public static final Vector4f GREEN_POLE = new Vector4f(0, 1, 0x38 / 255F, 1);
    public static final Vector4f BLUE_POLE = new Vector4f(0x38 / 255F, 0xC7 / 255F, 1, 1);
    public static final Vector4f STABLE = new Vector4f(0x20 / 255F, 1, 0x20 / 255F, 1);

    public TiberiumLeavesBlock(Properties properties) {
        super(properties);
    }

    public static int lightFunc(BlockState state) {
        if (!(state.getBlock() instanceof InfectiousLeavesBlock block)) {
            return 0;
        }
        if (state.getValue(LeavesBlock.PERSISTENT)) {
            return 14;
        }
        var stage = state.getValue(InfectiousLeavesBlock.STAGE);
        var maxStage = block.getMaxStage();
        return Mth.lerpInt((float) stage / maxStage, 0, 12);
    }

    public static Vector4f colorFunc(BlockState state, Vector4f baseColor) {
        if (!(state.getBlock() instanceof InfectiousLeavesBlock block)) {
            return baseColor;
        }
        if (state.getValue(LeavesBlock.PERSISTENT)) {
            return STABLE;
        }
        int stage = state.getValue(STAGE);
        int maxStage = block.getMaxStage();
        if (stage >= maxStage) {
            return BLUE_POLE;
        }
        var greened = new Vector4f(baseColor).lerp(GREEN_POLE, (float) stage / maxStage);
        var halfStage = (maxStage + 1) / 2;
        if (stage < halfStage) {
            return greened;
        }
        return greened.lerp(BLUE_POLE, (float) (stage + 1 - halfStage) / halfStage);
    }

    @Override
    public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity) {
        // 生长阶段满时敲上去会播放紫水晶音效。
        return state.getValue(PERSISTENT) || state.getValue(STAGE) >= getMaxStage()
                ? SoundType.AMETHYST
                : super.getSoundType(state, level, pos, entity);
    }
}
