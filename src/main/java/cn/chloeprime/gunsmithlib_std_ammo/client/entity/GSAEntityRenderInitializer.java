package cn.chloeprime.gunsmithlib_std_ammo.client.entity;

import cn.chloeprime.gunsmithlib_std_ammo.client.entity.render.AmmoEntityDelegateRenderer;
import cn.chloeprime.gunsmithlib_std_ammo.common.entity.GSAEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GSAEntityRenderInitializer {
    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(GSAEntities.SLICING_WARHEAD.get(), AmmoEntityDelegateRenderer::new);
    }
}
