package cn.chloeprime.gunsmithlib_std_ammo.client.particle;

import cn.chloeprime.gunsmithlib_std_ammo.common.particle.TintedSparkParticleOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import org.joml.Vector3f;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class TintedSparkParticle extends FireworkParticles.SparkParticle {
    public TintedSparkParticle(
            ClientLevel pLevel, double x, double y, double z,
            double dx, double dy, double dz,
            Vector3f color, float alpha, float scale,
            int life, boolean hasTrail, boolean isFlickering,
            ParticleEngine engine, SpriteSet sprites
    ) {
        super(pLevel, x, y, z, dx, dy, dz, engine, sprites);
        setColor(color.x(), color.y(), color.z());
        setAlpha(alpha);
        setTrail(hasTrail);
        setFlicker(isFlickering);
        this.quadSize *= scale;
        this.lifetime = (int) (life * (1 + 0.25 * random.nextDouble())) + random.nextInt(10);
    }

    public record Provider(
          SpriteSet sprites
    ) implements ParticleProvider<TintedSparkParticleOption> {
        private static final ParticleEngine ENGINE = Minecraft.getInstance().particleEngine;

        @Override
        @ParametersAreNonnullByDefault
        public @Nonnull Particle createParticle(
                TintedSparkParticleOption options,
                ClientLevel level, double x, double y, double z,
                double dx, double dy, double dz
        ) {
            return new TintedSparkParticle(
                    level, x, y, z, dx, dy, dz,
                    options.getColor(), options.getAlpha(), options.getScale(),
                    options.getLife(), options.hasTrail(), options.isFlickering(),
                    ENGINE, sprites
            );
        }
    }
}
