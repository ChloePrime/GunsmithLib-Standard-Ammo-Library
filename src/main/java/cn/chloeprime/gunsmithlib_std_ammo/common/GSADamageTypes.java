package cn.chloeprime.gunsmithlib_std_ammo.common;

import cn.chloeprime.gunsmithlib_std_ammo.GunsmithLibStdAmmoMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public class GSADamageTypes {
    public static final ResourceKey<DamageType> CANCER = ResourceKey.create(Registries.DAMAGE_TYPE, GunsmithLibStdAmmoMod.loc("cancer"));
}
