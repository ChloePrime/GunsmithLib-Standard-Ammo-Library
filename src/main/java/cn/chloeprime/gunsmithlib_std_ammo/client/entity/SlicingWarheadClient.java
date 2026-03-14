package cn.chloeprime.gunsmithlib_std_ammo.client.entity;

import cn.chloeprime.gunsmithlib_std_ammo.common.entity.SlicingWarhead;
import cn.chloeprime.gunsmithlib_std_ammo.common.particle.GSAParticleTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.random.RandomGenerator;

public final class SlicingWarheadClient {
    private static final boolean TESTING_ARMORED_PARTICLE = false;
    private static final Minecraft MC = Minecraft.getInstance();
    private static final RandomGenerator RNG = new Random();

    public static void onClientTick(SlicingWarhead warhead, Vec3 hitNormal) {
        var level = MC.level;
        if (level != null) {
            spawnSliceParticle(level, warhead, hitNormal);
            spawnSmokeParticle(level, warhead, hitNormal);
        }
    }

    private static void spawnSliceParticle(Level level, SlicingWarhead warhead, Vec3 hitNormal) {
        var hasArmor = TESTING_ARMORED_PARTICLE || isWearingArmor(warhead.getSlicingTarget().orElse(null));
        var particle = hasArmor
                ? GSAParticleTypes.SPARK.get()
                : GSAParticleTypes.BLOOD.get();
        var speed = hasArmor ? 0.75 : 0.5;
        var count = hasArmor ? 16 : 4;
        var hitPos = warhead.position().add(warhead.getHitNormal().scale(-warhead.getAmmoLength() / 2));
        for (int i = 0; i < count; i++) {
            var x = hitPos.x() + RNG.nextDouble(0.125) - RNG.nextDouble(0.125);
            var y = hitPos.y() + RNG.nextDouble(0.125) - RNG.nextDouble(0.125);
            var z = hitPos.z() + RNG.nextDouble(0.125) - RNG.nextDouble(0.125);
            var dx = speed * (hitNormal.x() + RNG.nextDouble(0.1) - RNG.nextDouble(0.1));
            var dy = speed * (hitNormal.y() + RNG.nextDouble(0.1) - RNG.nextDouble(0.1));
            var dz = speed * (hitNormal.z() + RNG.nextDouble(0.1) - RNG.nextDouble(0.1));
            level.addParticle(particle, x, y, z, dx, dy, dz);
        }
    }

    private static void spawnSmokeParticle(Level level, SlicingWarhead warhead, Vec3 hitNormal) {
        var particle = ParticleTypes.SMALL_FLAME;
        var speed = 0.5;
        var count = 4;
        var tailPos = warhead.position().add(warhead.getHitNormal().scale(warhead.getAmmoLength() / 2));
        for (int i = 0; i < count; i++) {
            var x = tailPos.x() + RNG.nextDouble(0.01) - RNG.nextDouble(0.01);
            var y = tailPos.y() + RNG.nextDouble(0.01) - RNG.nextDouble(0.01) + 0.1;
            var z = tailPos.z() + RNG.nextDouble(0.01) - RNG.nextDouble(0.01);
            var dx = speed * (hitNormal.x() + RNG.nextDouble(0.125) - RNG.nextDouble(0.125));
            var dy = speed * (hitNormal.y() + RNG.nextDouble(0.125) - RNG.nextDouble(0.125));
            var dz = speed * (hitNormal.z() + RNG.nextDouble(0.125) - RNG.nextDouble(0.125));
            level.addParticle(particle, x, y, z, dx, dy, dz);
        }
    }

    private static boolean isWearingArmor(@Nullable Entity entity) {
        if (!(entity instanceof LivingEntity living)) {
            return false;
        }
        var armor = living.getAttribute(Attributes.ARMOR);
        if (armor == null){
            return false;
        }
        return armor.getBaseValue() >= 9.9999 || armor.getValue() - armor.getBaseValue() >= 5.9999;
    }

    private SlicingWarheadClient() {
    }
}
