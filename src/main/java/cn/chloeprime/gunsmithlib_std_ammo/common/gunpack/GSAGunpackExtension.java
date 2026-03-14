package cn.chloeprime.gunsmithlib_std_ammo.common.gunpack;

import cn.chloeprime.gunsmithlib_std_ammo.common.entity.SlicingWarhead;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import mod.chloeprime.gunsmithlib.common.util.GunpackProperty;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class GSAGunpackExtension {
    @GunpackProperty
    private @Nullable SlicingWarhead.Properties slicing_shot;

    @GunpackProperty
    private @Nullable ResourceLocation slicing_shot_model_override;

    public Optional<SlicingWarhead.Properties> getSlicingShotProperties() {
        return Optional.ofNullable(slicing_shot);
    }

    public Optional<ResourceLocation> getSlicingShotModelOverride() {
        return Optional.ofNullable(slicing_shot_model_override);
    }

    public static Optional<GSAGunpackExtension> of(GunData data) {
        return Optional.ofNullable(((EnhancedGunData) data).extension());
    }
}
