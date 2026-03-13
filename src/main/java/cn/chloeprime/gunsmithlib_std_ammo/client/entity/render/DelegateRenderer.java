package cn.chloeprime.gunsmithlib_std_ammo.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Supplier;

public class DelegateRenderer<T extends Entity> extends EntityRenderer<T> {
    private final Supplier<EntityType<?>> delegateType;
    private final Map<T, Entity> delegateMap = new WeakHashMap<>();
    private static final EntityRenderDispatcher DISPATCHER = Minecraft.getInstance().getEntityRenderDispatcher();

    public DelegateRenderer(
            EntityRendererProvider.Context context,
            Supplier<EntityType<?>> delegateType
    ) {
        super(context);
        this.delegateType = delegateType;
    }

    @Override
    public @Nonnull ResourceLocation getTextureLocation(@Nonnull T self) {
        var delegate = getDelegate(self);
        return DISPATCHER.getRenderer(delegate).getTextureLocation(delegate);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(T self, float yaw, float partial, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        var delegate = getDelegate(self);
        delegate.setPos(self.position());
        delegate.xOld = self.xOld;
        delegate.yOld = self.yOld;
        delegate.zOld = self.zOld;
        delegate.setXRot(self.getXRot());
        delegate.setYRot(self.getYRot());
        delegate.xRotO = self.xRotO;
        delegate.yRotO = self.yRotO;
        delegate.setDeltaMovement(self.getDeltaMovement());
        setupDelegateInfo(self, delegate);
        DISPATCHER.render(delegate, 0, 0, 0, yaw, partial, poseStack, buffer, packedLight);
    }

    protected void setupDelegateInfo(T self, Entity delegate) {

    }

    private Entity getDelegate(T self) {
        return delegateMap.computeIfAbsent(self, this::createDelegate);
    }

    private Entity createDelegate(T self) {
        return delegateType.get().create(self.level());
    }
}
