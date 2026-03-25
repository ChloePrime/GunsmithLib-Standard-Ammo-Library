package cn.chloeprime.gunsmithlib_std_ammo.common.effect;

import cn.chloeprime.gunsmithlib_std_ammo.GunsmithLibStdAmmoMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

@Mod.EventBusSubscriber
@SuppressWarnings("unused")
public final class GSAMobEffects {
    private static final DeferredRegister<MobEffect> DFR = DeferredRegister.create(Registries.MOB_EFFECT, GunsmithLibStdAmmoMod.MOD_ID);
    public static final Supplier<MobEffect> ARMOR_MELTDOWN = DFR.register("armor_meltdown", ArmorMeltdownEffect::bootstrap);
    public static final Supplier<MobEffect> SUFFOCATING = DFR.register("suffocating", SuffocatingEffect::bootstrap);
    public static final Supplier<MobEffect> BURNING = DFR.register("burning", BurningEffect::bootstrap);
    public static final Supplier<MobEffect> CANCER = DFR.register("cancer", CancerEffect::bootstrap);
    public static final Supplier<MobEffect> DUM_HURT = DFR.register("dum_hurt", DumHurtEffect::bootstrap);

    public static void init(IEventBus bus) {
        DFR.register(bus);
    }

    private GSAMobEffects() {
    }
}
