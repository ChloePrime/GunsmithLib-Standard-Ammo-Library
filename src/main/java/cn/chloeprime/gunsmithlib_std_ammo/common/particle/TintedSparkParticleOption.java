package cn.chloeprime.gunsmithlib_std_ammo.common.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.particles.DustParticleOptionsBase;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraftforge.registries.DeferredRegister;
import org.joml.Vector3f;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

public class TintedSparkParticleOption extends TintedParticleOptionBase {
    public static class Factory {
        public final Codec<TintedSparkParticleOption> codec = RecordCodecBuilder.create(builder -> builder.group(
                ExtraCodecs.VECTOR3F.fieldOf("color").forGetter(opt -> opt.color),
                Codec.FLOAT.fieldOf("alpha").forGetter(opt -> opt.alpha),
                Codec.FLOAT.fieldOf("scale").forGetter(opt -> opt.scale),
                Codec.INT.fieldOf("life").forGetter(opt -> opt.life),
                Codec.BOOL.fieldOf("has_trail").forGetter(opt -> opt.hasTrail),
                Codec.BOOL.fieldOf("is_flickering").forGetter(opt -> opt.isFlickering)
        ).apply(builder, this::create));

        @SuppressWarnings("deprecation")
        @ParametersAreNonnullByDefault
        @MethodsReturnNonnullByDefault
        public final ParticleOptions.Deserializer<TintedSparkParticleOption> deserializer = new ParticleOptions.Deserializer<>() {
            public TintedSparkParticleOption fromCommand(ParticleType<TintedSparkParticleOption> type, StringReader command) throws CommandSyntaxException {
                Vector3f vector3f = DustParticleOptionsBase.readVector3f(command);
                command.expect(' ');
                var alpha = command.readFloat();
                command.expect(' ');
                var scale = command.readFloat();
                command.expect(' ');
                var life = command.readInt();
                command.expect(' ');
                var hasTrail = command.readBoolean();
                command.expect(' ');
                var isFlickering = command.readBoolean();
                return create(vector3f, alpha, scale, life, hasTrail, isFlickering);
            }

            public TintedSparkParticleOption fromNetwork(ParticleType<TintedSparkParticleOption> type, FriendlyByteBuf buf) {
                var albedo = DustParticleOptionsBase.readVector3f(buf);
                var scale = buf.readFloat();
                var alpha = buf.readFloat();
                return create(
                        albedo, alpha, scale,
                        buf.readVarInt(), buf.readBoolean(), buf.readBoolean()
                );
            }
        };

        public ParticleType<TintedSparkParticleOption> type = new ParticleType<>(false, deserializer) {
            @Override
            public @Nonnull Codec<TintedSparkParticleOption> codec() {
                return codec;
            }
        };

        public TintedSparkParticleOption create(
                Vector3f color, float alpha, float scale,
                int life, boolean hasTrail, boolean isFlickering
        ) {
            return new TintedSparkParticleOption(type, color, alpha, scale, life, hasTrail, isFlickering);
        }

        public
        Supplier<ParticleType<TintedSparkParticleOption>> registerTo(
                DeferredRegister<ParticleType<?>> dfr,
                String path
        ) {
            return dfr.register(path, () -> this.type);
        }
    }

    private final float alpha;
    private final boolean hasTrail;
    private final boolean isFlickering;
    private final int life;

    public TintedSparkParticleOption(
            ParticleType<? extends TintedSparkParticleOption> type,
            Vector3f color, float alpha, float scale,
            int life, boolean hasTrail, boolean isFlickering
    ) {
        super(type, color, scale);
        this.alpha = alpha;
        this.life = life;
        this.hasTrail = hasTrail;
        this.isFlickering = isFlickering;
    }

    public float getAlpha() {
        return alpha;
    }

    public boolean hasTrail() {
        return hasTrail;
    }

    public boolean isFlickering() {
        return isFlickering;
    }

    public int getLife() {
        return life;
    }

    @Override
    public void writeToNetwork(@Nonnull FriendlyByteBuf buf) {
        super.writeToNetwork(buf);
        buf.writeFloat(alpha);
        buf.writeVarInt(life);
        buf.writeBoolean(hasTrail);
        buf.writeBoolean(isFlickering);
    }
}
