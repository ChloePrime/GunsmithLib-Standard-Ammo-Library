package cn.chloeprime.gunsmithlib_std_ammo.common.effect;

import cn.chloeprime.gunsmithlib_std_ammo.common.GSADamageTypes;
import cn.chloeprime.gunsmithlib_std_ammo.common.util.DamageSourceUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nonnull;

public class StrongPoisonEffect extends MobEffectBaseUtility {
    public StrongPoisonEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public static MobEffect bootstrap() {
        return new StrongPoisonEffect(MobEffectCategory.HARMFUL, MobEffects.POISON.getColor());
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }

    @Override
    public void applyEffectTick(@Nonnull LivingEntity victim, int amplifier) {
        if (victim.level().isClientSide()) {
            return;
        }
        var source = DamageSourceUtil.source(victim.level().registryAccess(), GSADamageTypes.POISON, victim.damageSources()::magic);
        var amount = getDamageFor(victim, amplifier + 1);
        var backup = victim.invulnerableTime;
        try {
            victim.invulnerableTime = 0;
            victim.hurt(source, amount);
        } finally {
            victim.invulnerableTime = backup;
        }
    }

    private static float getDamageFor(LivingEntity victim, int level) {
        return level * (float) (2 + 8 * Math.log(Math.max(victim.getMaxHealth(), 20) / 20) / Math.log(15));
    }
}
