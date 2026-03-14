package cn.chloeprime.gunsmithlib_std_ammo.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class DarkeningParticle extends ParticlePrime {
    protected final float rColMax;
    protected final float gColMax;
    protected final float bColMax;

    public DarkeningParticle(SpriteSet sprite, ClientLevel clientLevel, double x, double y, double z, double dx, double dy, double dz) {
        super(sprite, clientLevel, x, y, z, dx, dy, dz);
        rColMax = rCol;
        gColMax = gCol;
        bColMax = bCol;
        lifetime /= 4;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        float normalizedAge = (age + partialTicks - 1) / lifetime;
        float brightness = (float) Math.pow(1 - normalizedAge, 2);
        rCol = rColMax * brightness;
        gCol = gColMax * brightness;
        bCol = bColMax * brightness;
        super.render(buffer, camera, partialTicks);
    }

    @ParametersAreNonnullByDefault
    @MethodsReturnNonnullByDefault
    public record Provider(
            SpriteSet sprites
    ) implements ParticleProvider<SimpleParticleType> {
        public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            return new DarkeningParticle(sprites, level, x, y, z, dx, dy, dz);
        }
    }
}
