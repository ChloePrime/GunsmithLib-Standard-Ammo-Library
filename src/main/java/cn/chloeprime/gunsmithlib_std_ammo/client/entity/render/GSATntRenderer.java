package cn.chloeprime.gunsmithlib_std_ammo.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class GSATntRenderer extends EntityRenderer<PrimedTnt> {
    private final BlockRenderDispatcher blockRenderer;
    private final Supplier<BlockState> block;

    public GSATntRenderer(EntityRendererProvider.Context context, Supplier<BlockState> block) {
        super(context);
        this.shadowRadius = 0.5F;
        this.blockRenderer = context.getBlockRenderDispatcher();
        this.block = block;
    }

    public void render(PrimedTnt tnt, float yaw, float partial, PoseStack pose, @Nonnull MultiBufferSource buffer, int packedLight) {
        pose.pushPose();
        {
            pose.translate(0, 0.5F, 0);
            int fuse = tnt.getFuse();
            if ((float) fuse - partial + 1 < 10) {
                float alpha = 1 - ((float) fuse - partial + 1) / 10;
                alpha = Mth.clamp(alpha, 0, 1);
                alpha *= alpha;
                alpha *= alpha;
                float scale = 1 + alpha * 0.3F;
                pose.scale(scale, scale, scale);
            }

            pose.mulPose(Axis.YP.rotationDegrees(-90));
            pose.translate(-0.5F, -0.5F, 0.5F);
            pose.mulPose(Axis.YP.rotationDegrees(90));
            TntMinecartRenderer.renderWhiteSolidBlock(this.blockRenderer, block.get(), pose, buffer, packedLight, fuse / 5 % 2 == 0);
        }
        pose.popPose();
        super.render(tnt, yaw, partial, pose, buffer, packedLight);
    }

    @SuppressWarnings("deprecation")
    public @Nonnull ResourceLocation getTextureLocation(@Nonnull PrimedTnt entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
