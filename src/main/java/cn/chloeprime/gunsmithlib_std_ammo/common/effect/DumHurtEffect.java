package cn.chloeprime.gunsmithlib_std_ammo.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import javax.annotation.Nonnull;

public class DumHurtEffect extends MobEffectBaseUtility {

    public static final String MODIFIER_ID = "0be83417-383c-4892-84b7-b1aa46180ce6";

    public DumHurtEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public static MobEffect bootstrap() {
        return new DumHurtEffect(MobEffectCategory.HARMFUL, 0x2e3a46)
                .addAttributeModifier(Attributes.MAX_HEALTH, MODIFIER_ID, -0.05, AttributeModifier.Operation.MULTIPLY_TOTAL)
                .addAttributeModifier(Attributes.MOVEMENT_SPEED, MODIFIER_ID, -0.05, AttributeModifier.Operation.MULTIPLY_TOTAL)
                .addAttributeModifier(Attributes.FLYING_SPEED, MODIFIER_ID, -0.05, AttributeModifier.Operation.MULTIPLY_TOTAL)
                .addAttributeModifier(Attributes.ATTACK_SPEED, MODIFIER_ID, -0.05, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public double getAttributeModifierValue(int amplifier, @Nonnull AttributeModifier modifier) {
        var base = super.getAttributeModifierValue(amplifier, modifier);
        return Math.max(base + 1, 0.1) - 1;
    }
}
