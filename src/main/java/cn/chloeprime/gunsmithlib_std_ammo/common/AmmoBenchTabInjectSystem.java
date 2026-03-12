package cn.chloeprime.gunsmithlib_std_ammo.common;

import cn.chloeprime.gunsmithlib_std_ammo.GunsmithLibStdAmmoMod;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.item.builder.AmmoItemBuilder;
import com.tacz.guns.resource.index.CommonBlockIndex;
import com.tacz.guns.resource.pojo.data.block.TabConfig;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class AmmoBenchTabInjectSystem {
    private static final Lazy<TabConfig> AMMO_VARIANT_TAB = Lazy.of(() -> new TabConfig(
            GunsmithLibStdAmmoMod.loc("ammo"),
            "%s.type.ammo.name".formatted(GunsmithLibStdAmmoMod.MOD_ID),
            AmmoItemBuilder.create().setId(GunsmithLibStdAmmoMod.loc("9mm_ap")).build()));

    @SubscribeEvent
    public static void onDatapackReloadCompleted(OnDatapackSyncEvent event) {
        injectTabsToDefaultAmmoBench(LogicalSide.SERVER);
    }

    public static void injectTabsToDefaultAmmoBench(LogicalSide side) {
        LogicalSidedProvider.WORKQUEUE.get(side).execute(AmmoBenchTabInjectSystem::injectTabsToDefaultAmmoBench0);
    }

    private static void injectTabsToDefaultAmmoBench0() {
        var data = TimelessAPI
                .getCommonBlockIndex(GunsmithLibStdAmmoMod.loc("tacz", "ammo_workbench"))
                .map(CommonBlockIndex::getData)
                .orElse(null);
        // 默认工作台可能被删
        if (data == null) {
            return;
        }
        data.getTabs().add(AMMO_VARIANT_TAB.get());
    }
}
