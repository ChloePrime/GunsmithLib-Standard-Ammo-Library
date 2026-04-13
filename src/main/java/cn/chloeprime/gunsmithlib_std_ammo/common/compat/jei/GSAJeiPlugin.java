package cn.chloeprime.gunsmithlib_std_ammo.common.compat.jei;

import cn.chloeprime.gunsmithlib_std_ammo.GunsmithLibStdAmmoMod;
import cn.chloeprime.gunsmithlib_std_ammo.common.item.GSAItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

@JeiPlugin
public class GSAJeiPlugin implements IModPlugin {
    private static final ResourceLocation UID = GunsmithLibStdAmmoMod.loc("jei");
    public static final Component TIB_SEED_INFO = Component.translatable("%s.jei.info.tib_seed".formatted(GunsmithLibStdAmmoMod.MOD_ID));

    @Override
    public @Nonnull ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addIngredientInfo(GSAItems.TIBERIUM_SEED.get(), TIB_SEED_INFO);
    }
}
