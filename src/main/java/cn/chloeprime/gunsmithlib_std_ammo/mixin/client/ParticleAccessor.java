package cn.chloeprime.gunsmithlib_std_ammo.mixin.client;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Particle.class)
public interface ParticleAccessor {
    @Accessor boolean isStoppedByCollision();
    @Accessor void setStoppedByCollision(boolean value);
}
