package cn.chloeprime.gunsmithlib_std_ammo.common.particle;

import cn.chloeprime.gunsmithlib_std_ammo.GunsmithLibStdAmmoMod;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class GSAParticleTypes {
    private static final DeferredRegister<ParticleType<?>> DFR = DeferredRegister.create(Registries.PARTICLE_TYPE, GunsmithLibStdAmmoMod.MOD_ID);
    public static final Supplier<ParticleType<TintedSparkParticleOption>> TINTED_FIREWORK_SPARK = new TintedSparkParticleOption.Factory().registerTo(DFR, "tinted_firework_spark");
    public static final Supplier<ParticleType<TintedSparkParticleOption>> TINTED_ELECTRIC_SPARK = new TintedSparkParticleOption.Factory().registerTo(DFR, "tinted_electric_spark");
    public static final Supplier<ParticleType<TintedSparkParticleOption>> TINTED_FLASH = new TintedSparkParticleOption.Factory().registerTo(DFR, "tinted_flash");
    public static final Supplier<SimpleParticleType> BLOOD = DFR.register("blood", () -> new SimpleParticleType(true));
    public static final Supplier<SimpleParticleType> SPARK = DFR.register("spark", () -> new SimpleParticleType(true));

    public static void init(IEventBus bus) {
        DFR.register(bus);
    }

    private GSAParticleTypes() {
    }
}
