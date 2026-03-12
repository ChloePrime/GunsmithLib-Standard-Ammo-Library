package cn.chloeprime.gunsmithlib_std_ammo.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public final class ArmorMeltdownEffect extends MobEffect {
    public static final String MODIFIER_ID = "6e76f69d-ecd0-47cd-828d-0b949b19557f";

    public ArmorMeltdownEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public static MobEffect bootstrap() {
        return new ArmorMeltdownEffect(MobEffectCategory.HARMFUL, 0xFFC700)
                .addAttributeModifier(Attributes.ARMOR, MODIFIER_ID, -1, AttributeModifier.Operation.ADDITION)
                .addAttributeModifier(Attributes.ARMOR_TOUGHNESS, MODIFIER_ID, -0.5, AttributeModifier.Operation.ADDITION);
    }
}
