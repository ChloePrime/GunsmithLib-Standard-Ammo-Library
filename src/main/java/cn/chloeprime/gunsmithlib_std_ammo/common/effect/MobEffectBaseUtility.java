package cn.chloeprime.gunsmithlib_std_ammo.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

public class MobEffectBaseUtility extends MobEffect {
    protected MobEffectBaseUtility(MobEffectCategory category, int color) {
        super(category, color);
    }

    public int getLevelFor(LivingEntity owner) {
        return Optional.ofNullable(owner.getEffect(this))
                .map(MobEffectInstance::getAmplifier)
                .orElse(-1) + 1;
    }
}
