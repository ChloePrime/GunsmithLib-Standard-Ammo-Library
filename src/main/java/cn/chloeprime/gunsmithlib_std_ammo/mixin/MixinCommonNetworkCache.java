package cn.chloeprime.gunsmithlib_std_ammo.mixin;

import cn.chloeprime.gunsmithlib_std_ammo.common.AmmoBenchTabInjectSystem;
import com.tacz.guns.resource.network.CommonNetworkCache;
import com.tacz.guns.resource.network.DataType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.LogicalSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(value = CommonNetworkCache.class, remap = false)
public class MixinCommonNetworkCache {
    @Inject(method = "fromNetwork(Ljava/util/Map;)V", at = @At("TAIL"))
    private void onClientReceiveGunpackCacheFinished(
            Map<DataType, Map<ResourceLocation, String>> cache, CallbackInfo ci
    ) {
        AmmoBenchTabInjectSystem.injectTabsToDefaultAmmoBench(LogicalSide.CLIENT);
    }
}
