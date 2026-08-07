package cn.chloeprime.gunsmithlib_std_ammo.common;

import cn.chloeprime.gunsmithlib_std_ammo.GunsmithLibStdAmmoMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

public class GSASoundEvents {
    private static final DeferredRegister<SoundEvent> DFR = DeferredRegister.create(Registries.SOUND_EVENT, GunsmithLibStdAmmoMod.MOD_ID);
    public static final Supplier<SoundEvent> AUTOCANNON_FLAK_FIRE = register("autocannon/autocannon_flak_fire");
    public static final Supplier<SoundEvent> AUTOCANNON_SHOOT = register("autocannon/autocannon_shoot");
    public static final Supplier<SoundEvent> BULLET_MERCHANT_AMBIENT = register("bullet_merchant_ambient");
    public static final Supplier<SoundEvent> BULLET_MERCHANT_DODGE = register("bullet_merchant_dodge");
    public static final Supplier<SoundEvent> BULLET_MERCHANT_TRADE = register("bullet_merchant_trade");
    public static final Supplier<SoundEvent> BULLET_MERCHANT_HURT = register("bullet_merchant_hurt");
    public static final Supplier<SoundEvent> BULLET_MERCHANT_DEATH = register("bullet_merchant_death");
    public static final Supplier<SoundEvent> BULLET_MERCHANT_YES = register("bullet_merchant_yes");
    public static final Supplier<SoundEvent> BULLET_MERCHANT_NO = register("bullet_merchant_no");

    @ApiStatus.Internal
    public static void init(IEventBus bus) {
        DFR.register(bus);
    }

    private static Supplier<SoundEvent> register(String id) {
        return DFR.register(id, () -> SoundEvent.createVariableRangeEvent(GunsmithLibStdAmmoMod.loc(id)));
    }
}
