package cn.chloeprime.gunsmithlib_std_ammo.common.entity;

import cn.chloeprime.gunsmithlib_std_ammo.GunsmithLibStdAmmoMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class GSAEntities {
    private static final DeferredRegister<EntityType<?>> DFR = DeferredRegister.create(Registries.ENTITY_TYPE, GunsmithLibStdAmmoMod.MOD_ID);
    public static final Supplier<EntityType<SlicingWarhead>> SLICING_WARHEAD = DFR.register("slicing_warhead", () -> SlicingWarhead.TYPE);
    public static final Supplier<EntityType<BulletMerchant>> BULLET_MERCHANT = DFR.register("bullet_merchant", () -> BulletMerchant.TYPE);

    public static void init(IEventBus bus) {
        DFR.register(bus);
    }

    @SubscribeEvent
    public static void initAttributes(EntityAttributeCreationEvent event) {
        event.put(BULLET_MERCHANT.get(), BulletMerchant.createBulletMerchantAttributes().build());
    }

    private GSAEntities() {
    }
}
