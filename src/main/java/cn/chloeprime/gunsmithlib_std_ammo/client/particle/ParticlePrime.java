package cn.chloeprime.gunsmithlib_std_ammo.client.particle;

import cn.chloeprime.gunsmithlib_std_ammo.mixin.client.ParticleAccessor;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Copied From HitFeedback project.
 * MIT licenced ©2024 ChloePrime
 */
public class ParticlePrime extends SimpleTexturedParticle {
    public ParticlePrime(SpriteSet sprite, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
        super(sprite, clientLevel, d, e, f, g, h, i);
        this.lifetime += random.nextInt(40, 100);
    }

    @Override
    public void move(double dx, double dy, double dz) {
        super.move(dx, dy, dz);
        if (this instanceof ParticleAccessor accessor) {
            if (accessor.isStoppedByCollision()) {
                accessor.setStoppedByCollision(false);
                this.xd /= 4;
                this.zd /= 4;
                this.yd = 0;
            }
        }
    }

    @ParametersAreNonnullByDefault
    @MethodsReturnNonnullByDefault
    public record Provider(
            SpriteSet sprites
    ) implements ParticleProvider<SimpleParticleType> {
        public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            return new ParticlePrime(sprites, level, x, y, z, dx, dy, dz);
        }
    }
}
