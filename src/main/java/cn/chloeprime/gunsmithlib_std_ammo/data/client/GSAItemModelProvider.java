package cn.chloeprime.gunsmithlib_std_ammo.data.client;

import cn.chloeprime.gunsmithlib_std_ammo.GunsmithLibStdAmmoMod;
import cn.chloeprime.gunsmithlib_std_ammo.common.item.GSAItems;
import cn.chloeprime.gunsmithlib_std_ammo.common.util.DatagenRegistryHelper;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class GSAItemModelProvider extends ItemModelProvider implements DatagenRegistryHelper {
    public GSAItemModelProvider(
            PackOutput output,
            String modid,
            ExistingFileHelper existingFileHelper
    ) {
        super(output, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(GSAItems.STEEL_INGOT.get());

        getBuilder(getKey(GSAItems.NITRATION_MIXTURE.get()).toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", GunsmithLibStdAmmoMod.loc("item/nit_mix_overlay"))
                .texture("layer1", GunsmithLibStdAmmoMod.loc("minecraft", "item/potion"));
        basicItem(GSAItems.RAW_TUNGSTEN.get());
        basicItem(GSAItems.TUNGSTEN_INGOT.get());
        basicItem(GSAItems.TUNGSTEN_BLADE.get());

        basicItem(GSAItems.TIBERIUM_SEED.get());
        basicItem(GSAItems.GREEN_TIBERIUM_CRYSTAL.get());
        basicItem(GSAItems.BLUE_TIBERIUM_CRYSTAL.get());
        basicItem(GSAItems.N2_DYNAMITE.get());
        basicItem(GSAItems.TIBERIUM_COMPOUND.get());
        basicItem(GSAItems.TIBERIUM_ALLOY_INGOT.get());
    }
}
