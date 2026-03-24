package cn.chloeprime.gunsmithlib_std_ammo.common.effect;

import cn.chloeprime.gunsmithlib_std_ammo.common.GSADamageTypes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

@Mod.EventBusSubscriber
public class CancerEffect extends MobEffectBaseUtility {
    public static final UUID MAX_HEALTH_MODIFIER_ID = UUID.fromString("87ae430a-df76-4968-99a9-1aedc60a4f75");

    public CancerEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 40 == 20;
    }

    @Override
    public void applyEffectTick(@Nonnull LivingEntity victim, int amplifier) {
        var damage = amplifier + 1;
        var source = createCancerDamageSource(victim.level().registryAccess(), victim.damageSources().magic());
        var oldHealth = victim.getHealth();
        var success = victim.hurt(source, damage);
        if (!success) {
            return;
        }
        var actualDamage = oldHealth - victim.getHealth();
        if (actualDamage <= 0) {
            return;
        }
        reduceMaxHealth(victim, actualDamage);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void clearModifierOnPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }
        if (!(event.getEntity() instanceof Player victim)) {
            return;
        }
        Optional.ofNullable(victim.getAttribute(Attributes.MAX_HEALTH)).ifPresent(inst -> inst.removeModifier(MAX_HEALTH_MODIFIER_ID));
    }

    private static DamageSource createCancerDamageSource(RegistryAccess registry, DamageSource fallback) {
        return registry.registry(Registries.DAMAGE_TYPE)
                .flatMap(reg -> reg.getHolder(GSADamageTypes.CANCER))
                .map(DamageSource::new)
                .orElse(fallback);
    }

    private static void reduceMaxHealth(LivingEntity victim, double amount) {
        var instance = victim.getAttribute(Attributes.MAX_HEALTH);
        if (instance == null) {
            return;
        }
        var oldAmount = -Optional.ofNullable(instance.getModifier(MAX_HEALTH_MODIFIER_ID))
                .filter(modifier -> modifier.getOperation() == AttributeModifier.Operation.ADDITION)
                .map(AttributeModifier::getAmount)
                .orElse(0.0);
        var newAmount = Math.min(instance.getBaseValue() - 1, oldAmount + amount);
        var modifier = new AttributeModifier(MAX_HEALTH_MODIFIER_ID, "Cancer", -newAmount, AttributeModifier.Operation.ADDITION);
        instance.removeModifier(MAX_HEALTH_MODIFIER_ID);
        instance.addPermanentModifier(modifier);
    }

    public static MobEffect bootstrap() {
        return new CancerEffect(MobEffectCategory.HARMFUL, 0x00_40_00);
    }
}
