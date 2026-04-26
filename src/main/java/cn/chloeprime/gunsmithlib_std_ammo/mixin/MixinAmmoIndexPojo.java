package cn.chloeprime.gunsmithlib_std_ammo.mixin;

import cn.chloeprime.gunsmithlib_std_ammo.GunsmithLibStdAmmoMod;
import cn.chloeprime.gunsmithlib_std_ammo.common.gunpack.EnhancedAmmoData;
import cn.chloeprime.gunsmithlib_std_ammo.common.gunpack.GSAGunpackExtension;
import com.google.gson.annotations.SerializedName;
import com.tacz.guns.resource.pojo.AmmoIndexPOJO;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

@Mixin(AmmoIndexPOJO.class)
public class MixinAmmoIndexPojo implements EnhancedAmmoData {
    @SerializedName(GunsmithLibStdAmmoMod.MOD_ID + "_extension")
    private @Unique @Nullable GSAGunpackExtension gunsmithlib_std_ammo$extension;

    @Override
    @SuppressWarnings("AddedMixinMembersNamePattern")
    public @Nullable GSAGunpackExtension extension() {
        return gunsmithlib_std_ammo$extension;
    }
}
