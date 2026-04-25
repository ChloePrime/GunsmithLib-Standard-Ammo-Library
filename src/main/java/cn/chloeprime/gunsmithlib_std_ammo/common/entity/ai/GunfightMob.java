package cn.chloeprime.gunsmithlib_std_ammo.common.entity.ai;

import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.api.entity.ShootResult;
import mod.chloeprime.gunsmithlib.api.util.Gunsmith;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

import static java.lang.Math.max;
import static java.lang.Math.round;

public interface GunfightMob {
    record ShootReport(ShootResult result, int delay) { }

    private LivingEntity self() {
        return (LivingEntity) this;
    }

    default boolean isAiming() {
        return self().isUsingItem();
    }

    default void startAiming() {
        var self = self();
        self.startUsingItem(InteractionHand.MAIN_HAND);
        var operator = IGunOperator.fromLivingEntity(self);
        operator.aim(true);
    }

    default void stopAiming() {
        var self = self();
        self.stopUsingItem();
        var operator = IGunOperator.fromLivingEntity(self);
        operator.aim(false);
    }

    default Optional<ShootReport> shoot() {
        var self = self();
        var gun = Gunsmith.getGunInfo(self.getMainHandItem()).orElse(null);
        if (gun == null) {
            return Optional.empty();
        }
        var result = IGunOperator.fromLivingEntity(self).shoot(self::getXRot, self::getYRot);
        var delay = round(max(1, 1 / (gun.gunItem().getRPM(gun.gunStack()) / 1200F)));
        return Optional.of(new ShootReport(result, delay));
    }

    @Mod.EventBusSubscriber
    class UseTimeFix {
        @SubscribeEvent
        public static void onItemUseStart(LivingEntityUseItemEvent.Start event) {
            if (!(event.getEntity() instanceof GunfightMob)) {
                return;
            }
            event.setDuration(3600);
        }
    }
}
