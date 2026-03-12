package cn.chloeprime.gunsmithlib_std_ammo.client.particle;

import cn.chloeprime.gunsmithlib_std_ammo.common.particle.TintedSparkParticleOption;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import org.joml.Vector3f;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class TintedFlashParticle extends FireworkParticles.OverlayParticle {
    protected final int maxLife;
    protected final float scale;

    public TintedFlashParticle(
            ClientLevel pLevel, double x, double y, double z,
            Vector3f color, float alpha, float scale,
            int life,
            SpriteSet sprites
    ) {
        super(pLevel, x, y, z);
        setColor(color.x(), color.y(), color.z());
        super.setAlpha(alpha);
        this.pickSprite(sprites);
        this.scale = scale;
        this.quadSize *= scale;
        this.lifetime = this.maxLife = life;
    }

    @Override
    protected void setAlpha(float alpha) {
        // Disable call in render()
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        var normAge = (age + partialTicks - 1) / maxLife;
        var alpha = 2 * (0.5F - Math.abs(0.5F - normAge));
        super.setAlpha(alpha * alpha);
        super.render(buffer, camera, partialTicks);
    }

    @Override
    public float getQuadSize(float partialTicks) {
        var normAge = (age + partialTicks - 1) / maxLife;
        var alpha = 2 * (0.5F - Math.abs(0.5F - normAge));
        return 7.1F * alpha * alpha * scale;
    }

    public record Provider(
          SpriteSet sprites
    ) implements ParticleProvider<TintedSparkParticleOption> {
        @Override
        @ParametersAreNonnullByDefault
        public @Nonnull Particle createParticle(
                TintedSparkParticleOption options,
                ClientLevel level, double x, double y, double z,
                double dx, double dy, double dz
        ) {
            return new TintedFlashParticle(
                    level, x, y, z,
                    options.getColor(), options.getAlpha(), options.getScale(),
                    options.getLife(),
                    sprites
            );
        }
    }
}
