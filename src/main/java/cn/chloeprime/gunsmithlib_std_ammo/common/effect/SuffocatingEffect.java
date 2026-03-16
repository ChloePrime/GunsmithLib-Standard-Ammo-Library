package cn.chloeprime.gunsmithlib_std_ammo.common.effect;

import mod.chloeprime.gunsmithlib.api.common.GunAttributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.UUID;
import java.util.random.RandomGenerator;

public class SuffocatingEffect extends MobEffectBaseUtility {
    public static final String SPEED_MODIFIER_ID = "481863e2-7abd-4388-a141-80248bb67f3b";
    public static final String RECOIL_MODIFIER_ID = "60845e32-938d-429a-8700-1e7bdded4d57";
    public static final String RELOAD_MODIFIER_ID = "99c839dd-9582-47f9-9d95-45409e83add8";
    public static final UUID RECOIL_MODIFIER_UID = UUID.fromString(RECOIL_MODIFIER_ID);
    public static final UUID RELOAD_MODIFIER_UID = UUID.fromString(RELOAD_MODIFIER_ID);
    private static final RandomGenerator RNG = new Random();

    public SuffocatingEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public static MobEffect bootstrap() {
        return new SuffocatingEffect(MobEffectCategory.HARMFUL, 0x233B6C)
                .addAttributeModifier(Attributes.MOVEMENT_SPEED, SPEED_MODIFIER_ID, 1, AttributeModifier.Operation.MULTIPLY_TOTAL)
                .addAttributeModifier(Attributes.FLYING_SPEED, SPEED_MODIFIER_ID, 1, AttributeModifier.Operation.MULTIPLY_TOTAL)
                .addAttributeModifier(Attributes.ATTACK_SPEED, SPEED_MODIFIER_ID, 1, AttributeModifier.Operation.MULTIPLY_TOTAL)
                .addAttributeModifier(GunAttributes.H_RECOIL.get(), RECOIL_MODIFIER_ID, 1, AttributeModifier.Operation.MULTIPLY_TOTAL)
                .addAttributeModifier(GunAttributes.V_RECOIL.get(), RECOIL_MODIFIER_ID, 1, AttributeModifier.Operation.MULTIPLY_TOTAL)
                .addAttributeModifier(GunAttributes.RELOAD_SPEED.get(), RELOAD_MODIFIER_ID, 1, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public double getAttributeModifierValue(int amplifier, @Nonnull AttributeModifier modifier) {
        int level = amplifier + 1;
        if (RECOIL_MODIFIER_UID.equals(modifier.getId())) {
            return 0.5 * level * modifier.getAmount();
        }
        if (RELOAD_MODIFIER_UID.equals(modifier.getId())) {
            return Math.pow(0.8, level) * modifier.getAmount() - 1;
        }
        return Math.pow(BASE_SPEED_DECREASE, level) - 1;
    }

    private static final double BASE_SPEED_DECREASE = Math.pow(0.75, 1.0 / 5);

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 2 == 0;
    }

    @Override
    public void applyEffectTick(@Nonnull LivingEntity victim, int pAmplifier) {
        if (victim.level().isClientSide()) {
            return;
        }
        // 1/3 概率使怪物失去目标
        if (victim instanceof Mob mob) {
            if (RNG.nextInt(3) == 0) {
                mob.setTarget(null);
                mob.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
            }
        }
        // 降低目标的氧气含量
        if (victim.getAirSupply() >= 10) {
            victim.setAirSupply(Math.min(5, victim.getMaxAirSupply()));
        }
    }
}
