package cn.chloeprime.gunsmithlib_std_ammo;

import cn.chloeprime.gunsmithlib_std_ammo.common.GSASoundEvents;
import cn.chloeprime.gunsmithlib_std_ammo.common.block.GSABlocks;
import cn.chloeprime.gunsmithlib_std_ammo.common.effect.GSAMobEffects;
import cn.chloeprime.gunsmithlib_std_ammo.common.effect.GSAPotions;
import cn.chloeprime.gunsmithlib_std_ammo.common.entity.GSAEntities;
import cn.chloeprime.gunsmithlib_std_ammo.common.item.GSAItems;
import cn.chloeprime.gunsmithlib_std_ammo.common.particle.GSAParticleTypes;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(GunsmithLibStdAmmoMod.MOD_ID)
public class GunsmithLibStdAmmoMod {
    public static final String MOD_ID = "gunsmithlib_std_ammo";
    public static final Logger LOGGER = LogUtils.getLogger();

    @SuppressWarnings("removal")
    public GunsmithLibStdAmmoMod() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        GSABlocks.init(bus);
        GSAItems.init(bus);
        GSAEntities.init(bus);
        GSAMobEffects.init(bus);
        GSAPotions.init(bus);
        GSAParticleTypes.init(bus);
        GSASoundEvents.init(bus);
    }

    public static ResourceLocation loc(String path) {
        return loc(MOD_ID, path);
    }

    public static ResourceLocation loc(String name, String path) {
        return new ResourceLocation(name, path);
    }
}
