package cn.chloeprime.gunsmithlib_std_ammo.client.entity;

import cn.chloeprime.gunsmithlib_std_ammo.common.entity.SlicingWarhead;
import cn.chloeprime.gunsmithlib_std_ammo.common.particle.GSAParticleTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
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
            var hasArmor = TESTING_ARMORED_PARTICLE || isWearingArmor(warhead.getSlicingTarget().orElse(null));
            var particle = hasArmor
                    ? GSAParticleTypes.SPARK.get()
                    : GSAParticleTypes.BLOOD.get();
            var speed = hasArmor
                    ? 0.75
                    : 0.5;
            var count = 4;
            for (int i = 0; i < count; i++) {
                var pos = new Vec3(
                        RNG.nextDouble(0.125) - RNG.nextDouble(0.125),
                        RNG.nextDouble(0.125) - RNG.nextDouble(0.125),
                        RNG.nextDouble(0.125) - RNG.nextDouble(0.125)
                ).add(warhead.position());
                var velocity = new Vec3(
                        hitNormal.x() + RNG.nextDouble(0.1) - RNG.nextDouble(0.1),
                        hitNormal.y() + RNG.nextDouble(0.1) - RNG.nextDouble(0.1),
                        hitNormal.z() + RNG.nextDouble(0.1) - RNG.nextDouble(0.1)
                ).scale(speed);
                level.addParticle(
                        particle,
                        pos.x(), pos.y(), pos.z(),
                        velocity.x(), velocity.y(), velocity.z());
            }
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
