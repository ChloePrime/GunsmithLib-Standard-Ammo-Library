package cn.chloeprime.gunsmithlib_std_ammo.common.gunpack;

import cn.chloeprime.gunsmithlib_std_ammo.common.entity.SlicingWarhead;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import mod.chloeprime.gunsmithlib.common.util.GunpackProperty;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class GSAGunpackExtension {
    @GunpackProperty
    private @Nullable SlicingWarhead.Properties slicing_shot;

    public Optional<SlicingWarhead.Properties> getSlicingShotProperties() {
        return Optional.ofNullable(slicing_shot);
    }

    public static Optional<GSAGunpackExtension> of(GunData data) {
        return Optional.ofNullable(((EnhancedGunData) data).extension());
    }
}
