package cn.chloeprime.gunsmithlib_std_ammo.client.particle;

import cn.chloeprime.gunsmithlib_std_ammo.common.particle.GSAParticleTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GSAParticleInitializer {
    @SubscribeEvent
    public static void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(GSAParticleTypes.TINTED_FIREWORK_SPARK.get(), TintedSparkParticle.Provider::new);
        event.registerSpriteSet(GSAParticleTypes.TINTED_ELECTRIC_SPARK.get(), TintedSparkParticle.Provider::new);
        event.registerSpriteSet(GSAParticleTypes.TINTED_FLASH.get(), TintedFlashParticle.Provider::new);
        event.registerSpriteSet(GSAParticleTypes.BLOOD.get(), BloodParticle.Provider::new);
        event.registerSpriteSet(GSAParticleTypes.SPARK.get(), BloodParticle.Provider::new);
    }
}
