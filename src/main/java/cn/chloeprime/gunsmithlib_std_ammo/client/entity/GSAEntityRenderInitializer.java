package cn.chloeprime.gunsmithlib_std_ammo.client.entity;

import cn.chloeprime.gunsmithlib_std_ammo.client.entity.render.AmmoEntityDelegateRenderer;
import cn.chloeprime.gunsmithlib_std_ammo.client.entity.render.GSATntRenderer;
import cn.chloeprime.gunsmithlib_std_ammo.client.entity.render.MannequinRenderer;
import cn.chloeprime.gunsmithlib_std_ammo.common.block.GSABlocks;
import cn.chloeprime.gunsmithlib_std_ammo.common.entity.BulletMerchant;
import cn.chloeprime.gunsmithlib_std_ammo.common.entity.GSAEntities;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GSAEntityRenderInitializer {
    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(GSAEntities.SLICING_WARHEAD.get(), AmmoEntityDelegateRenderer::new);
        event.registerEntityRenderer(GSAEntities.BULLET_MERCHANT.get(), ctx -> new MannequinRenderer<>(BulletMerchant.SKIN_NORMAL_VERSION, ctx, true) {
            @Override
            public @Nonnull ResourceLocation getTextureLocation(@Nonnull BulletMerchant entity) {
                return entity.getSkinLocation();
            }
        });
        event.registerEntityRenderer(GSAEntities.N2_BOMB.get(), context -> new GSATntRenderer(context, () -> GSABlocks.N2_BOMB.get().defaultBlockState()));
    }
}
