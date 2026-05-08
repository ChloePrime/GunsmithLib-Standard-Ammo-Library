package cn.chloeprime.gunsmithlib_std_ammo.common.gunpack;

import cn.chloeprime.gunsmithlib_std_ammo.common.entity.SlicingWarhead;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import mod.chloeprime.gunsmithlib.api.util.GunInfo;
import mod.chloeprime.gunsmithlib.common.util.GunpackProperty;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;

public class GSAGunpackExtension {
    @GunpackProperty
    private @Nullable SlicingWarhead.Properties slicing_shot;

    @GunpackProperty
    private @Nullable ResourceLocation slicing_shot_model_override;

    @GunpackProperty
    private @Nullable PriceData price;

    @GunpackProperty
    private @Nullable RedstoneData redstone;

    public Optional<SlicingWarhead.Properties> getSlicingShotProperties() {
        return Optional.ofNullable(slicing_shot);
    }

    public Optional<ResourceLocation> getSlicingShotModelOverride() {
        return Optional.ofNullable(slicing_shot_model_override);
    }

    public Optional<PriceData> getPrice() {
        return Optional.ofNullable(price);
    }

    public Optional<RedstoneData> getRedstoneData() {
        return Optional.ofNullable(redstone);
    }

    public static Optional<GSAGunpackExtension> of(GunData data) {
        return Optional.ofNullable(((EnhancedGunData) data).extension());
    }

    @SuppressWarnings("OptionalIsPresent")
    public static <T> Optional<T> forGunOrAmmo(
            GunInfo gunInfo,
            Function<GSAGunpackExtension, T> field
    ) {
        var onGun = GSAGunpackExtension
                .of(gunInfo.index().getGunData())
                .map(field);
        if (onGun.isPresent()) {
            return onGun;
        }
        var onAmmo = TimelessAPI
                .getCommonAmmoIndex(gunInfo.index().getGunData().getAmmoId())
                .map(index -> (EnhancedAmmoData) index.getPojo())
                .map(EnhancedAmmoData::extension)
                .map(field);
        if (onAmmo.isPresent()) {
            return onAmmo;
        }
        return Optional.empty();
    }
}
