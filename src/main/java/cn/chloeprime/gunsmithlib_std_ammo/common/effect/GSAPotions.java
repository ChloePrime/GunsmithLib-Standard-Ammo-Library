package cn.chloeprime.gunsmithlib_std_ammo.common.effect;

import cn.chloeprime.gunsmithlib_std_ammo.common.item.GSAItems;
import cn.chloeprime.gunsmithlib_std_ammo.mixin.PotionBrewingAccessor;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

import static cn.chloeprime.gunsmithlib_std_ammo.GunsmithLibStdAmmoMod.MOD_ID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class GSAPotions {
    private static final DeferredRegister<Potion> DFR = DeferredRegister.create(Registries.POTION, MOD_ID);
    public static final String VX_NAME = "%s.vx".formatted(MOD_ID);

    public static final Supplier<Potion> VX = DFR.register("vx", () -> new Potion(VX_NAME, new MobEffectInstance(GSAMobEffects.STRONG_POISON.get(), 3600)));
    public static final Supplier<Potion> LONG_VX = DFR.register("long_vx", () -> new Potion(VX_NAME, new MobEffectInstance(GSAMobEffects.STRONG_POISON.get(), 9600)));

    @ApiStatus.Internal
    public static void init(IEventBus bus) {
        DFR.register(bus);
    }

    @SubscribeEvent
    @ApiStatus.Internal
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(GSAPotions::registerBrewRecipes);
    }

    private static void registerBrewRecipes() {
        PotionBrewingAccessor.invokeAddMix(Potions.WATER, GSAItems.GREEN_TIBERIUM_CRYSTAL.get(), VX.get());
        PotionBrewingAccessor.invokeAddMix(Potions.WATER, GSAItems.VX_PLANT.get(), VX.get());
        PotionBrewingAccessor.invokeAddMix(Potions.AWKWARD, GSAItems.VX_PLANT.get(), VX.get());
        PotionBrewingAccessor.invokeAddMix(VX.get(), Items.REDSTONE, LONG_VX.get());
    }
}
