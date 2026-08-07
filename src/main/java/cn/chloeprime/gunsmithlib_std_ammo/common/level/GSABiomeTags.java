package cn.chloeprime.gunsmithlib_std_ammo.common.level;

import cn.chloeprime.gunsmithlib_std_ammo.GunsmithLibStdAmmoMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public final class GSABiomeTags {
    public static final TagKey<Biome> WITHOUT_BULLET_MERCHANT_SPAWNS = TagKey.create(Registries.BIOME, GunsmithLibStdAmmoMod.loc("without_bullet_merchant_spawns"));

    private GSABiomeTags() {
    }
}
