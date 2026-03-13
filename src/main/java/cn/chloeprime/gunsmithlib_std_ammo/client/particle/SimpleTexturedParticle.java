package cn.chloeprime.gunsmithlib_std_ammo.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;

import javax.annotation.Nonnull;

/**
 * Copied From HitFeedback project.
 * MIT licenced ©2024 ChloePrime
 */
public class SimpleTexturedParticle extends TextureSheetParticle {
    public SimpleTexturedParticle(SpriteSet sprite, ClientLevel clientLevel, double d, double e, double f, double xv, double yv, double zv) {
        super(clientLevel, d, e, f, xv, yv, zv);
        this.sprite = sprite;
        this.gravity = 1;
        setSpriteFromAge(sprite);

        var oldVelocity = Math.sqrt(xd * xd + yd * yd + zd * zd);
        var newVelocity = Math.sqrt(xv * xv + yv * yv + zv * zv);
        var velScale = newVelocity / oldVelocity;
        this.xd *= velScale;
        this.yd *= velScale;
        this.zd *= velScale;
    }

    @Override
    public @Nonnull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        super.tick();
        setSpriteFromAge(sprite);
    }

    private final SpriteSet sprite;
}
