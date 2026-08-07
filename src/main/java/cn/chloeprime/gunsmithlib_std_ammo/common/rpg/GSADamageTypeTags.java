package cn.chloeprime.gunsmithlib_std_ammo.common.rpg;

import cn.chloeprime.gunsmithlib_std_ammo.GunsmithLibStdAmmoMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public final class GSADamageTypeTags {
    public static final TagKey<DamageType> TACZ_BULLETS = TagKey.create(Registries.DAMAGE_TYPE, GunsmithLibStdAmmoMod.loc("tacz", "bullet"));

    private GSADamageTypeTags() {
    }
}
