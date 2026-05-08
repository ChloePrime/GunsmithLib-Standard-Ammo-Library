package cn.chloeprime.gunsmithlib_std_ammo.common.gunpack;

import cn.chloeprime.gunsmithlib_std_ammo.common.block.RedstoneOverride;
import com.tacz.guns.api.event.server.AmmoHitBlockEvent;
import mod.chloeprime.gunsmithlib.api.util.Gunsmith;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Mod.EventBusSubscriber
public final class RedstoneAmmoSystem {
    @SubscribeEvent
    public static void onHitBlock(AmmoHitBlockEvent event) {
        var level = Objects.requireNonNull(event.getLevel());
        if (level.isClientSide()) {
            return;
        }
        var hit = event.getHitResult();
        if (hit == null || hit.getType() == HitResult.Type.MISS) {
            return;
        }
        var data = Optional.ofNullable(Gunsmith.createGunItemFromId(event.getAmmo().getGunId()))
                .flatMap(Gunsmith::getGunInfo)
                .flatMap(gunInfo -> GSAGunpackExtension.forGunOrAmmo(gunInfo, GSAGunpackExtension::getRedstoneData))
                .flatMap(Function.identity())
                .orElse(null);
        if (data == null) {
            return;
        }
        powerBlocks(event.getLevel(), data, hit.getBlockPos(), hit.getDirection());
    }

    private static void powerBlocks(Level level, RedstoneData data, BlockPos pos, Direction hitDirection) {
        // Override
        RedstoneOverride.override(level, pos, hitDirection, data);
        // Update
        var hitFromPos = pos.relative(hitDirection);
        level.neighborChanged(pos, level.getBlockState(hitFromPos).getBlock(), hitFromPos);
        level.updateNeighborsAt(pos, level.getBlockState(pos).getBlock());
    }

    private RedstoneAmmoSystem() {
    }
}
