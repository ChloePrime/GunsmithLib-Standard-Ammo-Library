package cn.chloeprime.gunsmithlib_std_ammo;

import cn.chloeprime.gunsmithlib_std_ammo.common.block.GSABlocks;
import cn.chloeprime.gunsmithlib_std_ammo.common.effect.GSAMobEffects;
import cn.chloeprime.gunsmithlib_std_ammo.common.item.GSAItems;
import cn.chloeprime.gunsmithlib_std_ammo.common.particle.GSAParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(GunsmithLibStdAmmoMod.MOD_ID)
public class GunsmithLibStdAmmoMod {
    public static final String MOD_ID = "gunsmithlib_std_ammo";

    @SuppressWarnings("removal")
    public GunsmithLibStdAmmoMod() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        GSABlocks.init(bus);
        GSAItems.init(bus);
        GSAMobEffects.init(bus);
        GSAParticleTypes.init(bus);
    }

    public static ResourceLocation loc(String path) {
        return loc(MOD_ID, path);
    }

    public static ResourceLocation loc(String name, String path) {
        return new ResourceLocation(name, path);
    }
}
