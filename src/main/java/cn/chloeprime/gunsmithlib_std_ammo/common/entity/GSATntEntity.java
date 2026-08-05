package cn.chloeprime.gunsmithlib_std_ammo.common.entity;

import cn.chloeprime.gunsmithlib_std_ammo.mixin.PrimedTntAccessor;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GSATntEntity extends PrimedTnt {
    private float power = 4;

    public static final EntityType<GSATntEntity> TYPE_N2_BOMB = EntityType.Builder
            .<GSATntEntity>of((type, level) -> Util.make(new GSATntEntity(type, level), et -> et.setPower(6)), MobCategory.MISC)
            .fireImmune()
            .sized(0.98F, 0.98F)
            .clientTrackingRange(10)
            .updateInterval(10)
            .build("n2_bomb");

    public GSATntEntity(EntityType<? extends PrimedTnt> type, Level level) {
        super(type, level);
    }

    public GSATntEntity(EntityType<? extends PrimedTnt> type, Level level, double x, double y, double z, @Nullable LivingEntity owner) {
        this(type, level);
        this.setPos(x, y, z);
        double d0 = level.random.nextDouble() * Math.PI * 2;
        this.setDeltaMovement(-Math.sin(d0) * 0.02, 0.2, -Math.cos(d0) * 0.02);
        this.setFuse(80);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        ((PrimedTntAccessor) this).gunsmithlib_std_ammo$setOwner(owner);
    }

    public void setPower(float power) {
        this.power = power;
    }

    @Override
    protected void readAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Power", Tag.TAG_ANY_NUMERIC)) {
            this.setPower(compound.getFloat("Power"));
        }
    }

    @Override
    protected void addAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("Power", this.power);
    }

    @Override
    protected void explode() {
        this.level().explode(this, this.getX(), this.getY(0.0625), this.getZ(), this.power, Level.ExplosionInteraction.TNT);
    }
}
