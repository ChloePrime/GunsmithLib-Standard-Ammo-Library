package cn.chloeprime.gunsmithlib_std_ammo.data;

import cn.chloeprime.gunsmithlib_std_ammo.GunsmithLibStdAmmoMod;
import cn.chloeprime.gunsmithlib_std_ammo.common.GSADamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class GSADamageTypeTagsProvider extends DamageTypeTagsProvider {
    public GSADamageTypeTagsProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            @Nullable ExistingFileHelper existingFileHelper
    ) {
        super(output, lookupProvider, GunsmithLibStdAmmoMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@Nonnull HolderLookup.Provider pProvider) {
        tag(DamageTypeTags.BYPASSES_ARMOR).add(GSADamageTypes.CANCER);
        tag(DamageTypeTags.BYPASSES_COOLDOWN).add(GSADamageTypes.CANCER);
        tag(DamageTypeTags.WITCH_RESISTANT_TO).add(GSADamageTypes.CANCER);
    }
}
