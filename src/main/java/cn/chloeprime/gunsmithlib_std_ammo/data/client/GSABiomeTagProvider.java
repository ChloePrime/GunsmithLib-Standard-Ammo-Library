package cn.chloeprime.gunsmithlib_std_ammo.data.client;

import cn.chloeprime.gunsmithlib_std_ammo.GunsmithLibStdAmmoMod;
import cn.chloeprime.gunsmithlib_std_ammo.common.level.GSABiomeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class GSABiomeTagProvider extends BiomeTagsProvider {
    public GSABiomeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registryAccess, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, registryAccess, GunsmithLibStdAmmoMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@Nonnull HolderLookup.Provider registryAccess) {
        this.tag(GSABiomeTags.WITHOUT_BULLET_MERCHANT_SPAWNS).add(Biomes.THE_VOID);
    }
}
