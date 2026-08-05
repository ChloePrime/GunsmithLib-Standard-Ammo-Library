package cn.chloeprime.gunsmithlib_std_ammo.common.block;

import cn.chloeprime.gunsmithlib_std_ammo.common.entity.GSATntEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableInt;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

public class GSATntBlock extends TntBlock {
    public record Properties(
            BlockBehaviour.Properties blockProperties,
            MutableInt fuse,
            MutableFloat power
    ) {
        public static Properties of(BlockBehaviour.Properties blockProperties) {
            return new Properties(blockProperties, new MutableInt(80), new MutableFloat(4));
        }

        public Properties fuse(int value) {
            this.fuse.setValue(value);
            return this;
        }

        public Properties power(float value) {
            this.power.setValue(value);
            return this;
        }
    }

    public GSATntBlock(Supplier<? extends EntityType<? extends PrimedTnt>> tntType, Properties properties) {
        super(properties.blockProperties());
        this.tntType = tntType;
        this.fuse = properties.fuse().intValue();
        this.power = properties.power().floatValue();
    }

    private final Supplier<? extends EntityType<? extends PrimedTnt>> tntType;
    private final int fuse;
    private final float power;

    @Override
    @ParametersAreNonnullByDefault
    public void onCaughtFire(BlockState state, Level level, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
        if (!level.isClientSide) {
            var primed = newPrimedTnt(level, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, igniter);
            primed.setFuse(this.fuse);
            primed.setPower(this.power);
            level.addFreshEntity(primed);
            level.playSound(null, primed.getX(), primed.getY(), primed.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(igniter, GameEvent.PRIME_FUSE, pos);
        }
    }

    protected GSATntEntity newPrimedTnt(Level level, double x, double y, double z, @Nullable LivingEntity igniter) {
        return new GSATntEntity(tntType.get(), level, x, y, z, igniter);
    }
}
