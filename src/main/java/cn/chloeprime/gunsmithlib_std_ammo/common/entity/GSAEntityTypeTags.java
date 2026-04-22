package cn.chloeprime.gunsmithlib_std_ammo.common.entity;

import cn.chloeprime.gunsmithlib_std_ammo.GunsmithLibStdAmmoMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class GSAEntityTypeTags {
    public static final TagKey<EntityType<?>> INSTANT_DEATH_IMMUNE = TagKey.create(Registries.ENTITY_TYPE, GunsmithLibStdAmmoMod.loc("instant_death_immune"));
}
