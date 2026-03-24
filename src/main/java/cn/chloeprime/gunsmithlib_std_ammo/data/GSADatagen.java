package cn.chloeprime.gunsmithlib_std_ammo.data;

import cn.chloeprime.gunsmithlib_std_ammo.data.client.GSABlockStateProvider;
import cn.chloeprime.gunsmithlib_std_ammo.data.client.GSAItemModelProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static cn.chloeprime.gunsmithlib_std_ammo.GunsmithLibStdAmmoMod.MOD_ID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class GSADatagen {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var packOutput = generator.getPackOutput();
        var lookupProvider = event.getLookupProvider();
        var existingFileHelper = event.getExistingFileHelper();
        if (event.includeServer()) {
            var bp = new GSABlockTagProvider(packOutput, lookupProvider, MOD_ID, existingFileHelper);
            generator.addProvider(true, bp);
            generator.addProvider(true, new GSAItemTagProvider(packOutput, lookupProvider, bp.contentsGetter(), MOD_ID, existingFileHelper));
            generator.addProvider(true, new GSALootProvider(packOutput));
            generator.addProvider(true, new GSARecipeProvider(packOutput));
            generator.addProvider(true, new GSADamageTypeTagsProvider(packOutput, lookupProvider, existingFileHelper));
        }
        if (event.includeClient()) {
            generator.addProvider(true, new GSABlockStateProvider(packOutput, MOD_ID, existingFileHelper));
            generator.addProvider(true, new GSAItemModelProvider(packOutput, MOD_ID, existingFileHelper));
        }
    }

    private GSADatagen() {
    }
}
