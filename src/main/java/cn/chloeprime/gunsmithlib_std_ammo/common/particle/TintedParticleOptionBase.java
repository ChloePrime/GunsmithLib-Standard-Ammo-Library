package cn.chloeprime.gunsmithlib_std_ammo.common.particle;

import net.minecraft.core.particles.DustParticleOptionsBase;
import net.minecraft.core.particles.ParticleType;
import org.joml.Vector3f;

import javax.annotation.Nonnull;
import java.util.Objects;

public class TintedParticleOptionBase extends DustParticleOptionsBase {
    private final ParticleType<? extends TintedParticleOptionBase> type;

    public TintedParticleOptionBase(
            ParticleType<? extends TintedParticleOptionBase> type,
            Vector3f color,
            float scale
    ) {
        super(color, scale);
        this.type = Objects.requireNonNull(type);
    }

    @Override
    public @Nonnull ParticleType<?> getType() {
        return type;
    }
}
