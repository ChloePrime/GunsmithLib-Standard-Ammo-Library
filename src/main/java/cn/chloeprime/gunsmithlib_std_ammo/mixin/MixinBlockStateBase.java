package cn.chloeprime.gunsmithlib_std_ammo.mixin;

import cn.chloeprime.gunsmithlib_std_ammo.common.block.RedstoneOverride;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockBehaviour.BlockStateBase.class)
public class MixinBlockStateBase {
    @ModifyReturnValue(method = "getSignal", at = @At("RETURN"))
    private int overrideSignalForHackedBlocks(int original, BlockGetter world, BlockPos pos, Direction direction) {
        if (!(world instanceof ServerLevel level)) {
            return original;
        }
        var override = RedstoneOverride.getOverrideSignalFor(level, pos, direction).orElse(original);
        return Math.max(override, original);
    }
}
