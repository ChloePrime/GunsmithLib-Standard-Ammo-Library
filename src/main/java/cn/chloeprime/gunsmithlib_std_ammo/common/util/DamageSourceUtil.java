package cn.chloeprime.gunsmithlib_std_ammo.common.util;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;

import java.util.function.Supplier;

public class DamageSourceUtil {
    public static DamageSource source(
            RegistryAccess registry,
            ResourceKey<DamageType> type,
            Supplier<DamageSource> fallback
    ) {
        return registry.registry(Registries.DAMAGE_TYPE)
                .flatMap(reg -> reg.getHolder(type))
                .map(DamageSource::new)
                .orElseGet(fallback);
    }
}
