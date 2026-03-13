package cn.chloeprime.gunsmithlib_std_ammo.common.entity;

import cn.chloeprime.gunsmithlib_std_ammo.GunsmithLibStdAmmoMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class GSAEntities {
    private static final DeferredRegister<EntityType<?>> DFR = DeferredRegister.create(Registries.ENTITY_TYPE, GunsmithLibStdAmmoMod.MOD_ID);
    public static final Supplier<EntityType<SlicingWarhead>> SLICING_WARHEAD = DFR.register("slicing_warhead", () -> SlicingWarhead.TYPE);

    public static void init(IEventBus bus) {
        DFR.register(bus);
    }

    private GSAEntities() {
    }

}
