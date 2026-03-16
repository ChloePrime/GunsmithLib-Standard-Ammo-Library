package cn.chloeprime.gunsmithlib_std_ammo.mixin;

import cn.chloeprime.gunsmithlib_std_ammo.common.effect.BurningEffect;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
    @Inject(method = "handleDamageEvent", at = @At("HEAD"), cancellable = true)
    private void cancelSuddenBurningFireDamageEventForLocalPlayer(DamageSource source, CallbackInfo ci) {
        if (BurningEffect.isInHurtSafeInsurance(this)) {
            if (source.is(DamageTypes.ON_FIRE)) {
                ci.cancel();
            }
        }
    }

    public MixinLivingEntity(EntityType<?> type, Level level) {
        super(type, level);
    }
}
